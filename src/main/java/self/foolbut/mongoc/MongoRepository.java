package self.foolbut.mongoc;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.bson.BsonDocument;
import org.bson.BsonDocumentWrapper;
import org.bson.BsonString;
import org.bson.Document;
import org.bson.codecs.BsonValueCodecProvider;
import org.bson.codecs.DocumentCodecProvider;
import org.bson.codecs.IterableCodecProvider;
import org.bson.codecs.MapCodecProvider;
import org.bson.codecs.ValueCodecProvider;
import org.bson.codecs.configuration.CodecRegistries;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;
import org.bson.codecs.pojo.annotations.BsonId;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;
import org.springframework.util.Assert;

import com.mongodb.DBObjectCodecProvider;
import com.mongodb.DBRefCodecProvider;
import com.mongodb.DocumentToDBRefTransformer;
import com.mongodb.MongoClient;
import com.mongodb.MongoCredential;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.gridfs.codecs.GridFSFileCodecProvider;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.geojson.codecs.GeoJsonCodecProvider;
import self.foolbut.mongoc.annotation.Collection;
import self.foolbut.mongoc.codec.WithIdStrCodecProvider;
import self.foolbut.mongoc.ex.CollectionNameNotFoundException;

class MongoRepository implements IMongoRepository{

    private static final ConcurrentHashMap<Class<?>,PojoMeta> metaMap = new ConcurrentHashMap<Class<?>,PojoMeta>();

    private MongoDatabase _database;
    private CodecRegistry _withIdCodec = CodecRegistries.fromProviders(new WithIdStrCodecProvider(),new ValueCodecProvider(),
            new BsonValueCodecProvider(),
            new DBRefCodecProvider(),
            new DBObjectCodecProvider(),
            new DocumentCodecProvider(new DocumentToDBRefTransformer()),
            new IterableCodecProvider(new DocumentToDBRefTransformer()),
            new MapCodecProvider(new DocumentToDBRefTransformer()),
            new GeoJsonCodecProvider(),
            new GridFSFileCodecProvider(),
            PojoCodecProvider.builder().automatic(true).build());

    public MongoRepository(MongoClient client, String dbName){
        Assert.notNull(client);
        Assert.notNull(dbName);
        List<MongoCredential> credentialList = client.getCredentialsList();
        Assert.notNull(credentialList);
        Assert.notEmpty(credentialList);
        boolean dbAuthed = false;
        for(MongoCredential credential: credentialList){
            if(dbName.equals(credential.getSource())){
                dbAuthed = true;
                break;
            }
        }
        if(!dbAuthed){
            throw new RuntimeException("db <"+dbName+"> not authenticated.");
        }

        this._database = client.getDatabase(dbName);
    }

    @Override
    public void insertOne(Object obj) {
        String collectionName = this.getCollectionName(obj.getClass());

        Document doc = this.buildDocument(obj, false);

        doc.append("gmtCreated", new Date());
        doc.put("isDeleted", false);
        this._database.getCollection(collectionName).insertOne(doc);
        this.parseObjectId(doc.getObjectId("_id"),obj);
    }

    @Override
    public <T> List<T> findPage(Map<String,Object> params, Class<T> claz,int limit,int offset){
        List<T> finalList = new ArrayList<T>();
        Bson condition = this.buildQuery(params);
        FindIterable<T> ite = this.getCollection(claz).find(condition);
        ite.limit(limit).skip(offset);
        MongoCursor<T> cursor = ite.iterator();
        while(cursor.hasNext()){
            T obj = cursor.next();
            finalList.add(obj);
        }
        return finalList;
    }

    @Override
    public <T> List<T> findAll(Map<String, Object> params, Class<T> claz) {
        List<T> finalList = new ArrayList<T>();
        Bson condition = this.buildQuery(params);
        
        FindIterable<T> ite = this.getCollection(claz).find(condition);
        MongoCursor<T> cursor = ite.iterator();
        while(cursor.hasNext()){
            T obj = cursor.next();
            finalList.add(obj);
        }
        return finalList;
    }

    @Override
    public long findCnt(Map<String, Object> params, Class<?> T) {
        String collectionName = this.getCollectionName(T);
        Bson condition = this.buildQuery(params);
        return this._database.getCollection(collectionName).count(condition);
    }

    @Override
    public <T> T findById(String objId, Class<T> claz) {
        FindIterable<T> ite = this.getCollection(claz).find(Filters.eq(new ObjectId()));
        return ite.first();
    }

    @Override
    public <T> T findOne(Map<String, Object> params, Class<T> claz) {
        Bson condition = this.buildQuery(params);
        
        FindIterable<T> ite = this.getCollection(claz).find(condition);
        return ite.first();
    }

    @Override
    public boolean updateOne(String objectId, Object obj) {
        String collectionName = this.getCollectionName(obj.getClass());
        Bson update = MongoUtils.buildUpdate(obj);
        com.mongodb.client.result.UpdateResult result = this._database.getCollection(collectionName).updateOne(Filters.eq("_id", new ObjectId(objectId)), update);
        return result.getModifiedCount() > 0;
    }

    @Override
    public <T> void deleteOne(String objectId,  Class<T> claz){
        String collectionName = this.getCollectionName(claz);
        Map<String,Object> deleteMap = new HashMap<String,Object>();
        deleteMap.put("isDeleted", true);
        Bson update = MongoUtils.buildUpdate(deleteMap);
        this._database.getCollection(collectionName).updateOne(Filters.eq(new ObjectId(objectId)), update);
    }

    private String getCollectionName(Class<?> claz){
        if(!metaMap.containsKey(claz)){
            this.parsePojoMeta(claz);
        }
        return metaMap.get(claz).getCollectionName();
    }

    private Field getIdField(Class<?> claz){
        if(!metaMap.containsKey(claz)){
            this.parsePojoMeta(claz);
        }
        return metaMap.get(claz).getIdField();
    }

    private void parseObjectId(ObjectId objId, Object obj){
        Class<?> claz = obj.getClass();
        Field idField = this.getIdField(claz);
        if(null != idField){
            boolean accessible = idField.isAccessible();
            if(!accessible){
                idField.setAccessible(true);
            }
            try {
                idField.set(obj, objId.toHexString());
            } catch (IllegalArgumentException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                // do nothing,never happen
            }finally{
                if(!accessible){
                    idField.setAccessible(false);
                }
            }
        }
    }

    private void parsePojoMeta(Class<?> claz){
        // parse collectionName
        Collection c = claz.getAnnotation(Collection.class);
        if(null == c){
            throw new CollectionNameNotFoundException( claz);
        }
        String collectionName = c.value();
        if(null == collectionName || 0 == collectionName.trim().length()){
            throw new CollectionNameNotFoundException( claz);
        }

        Field idField = null;
        Field[] fields = claz.getDeclaredFields();
        for(Field field :fields){
            if("id".equals(field.getName()) && String.class.equals(field.getType())){idField = field; break;};
            BsonId anno = field.getAnnotation(BsonId.class);
            if(null != anno && String.class.equals(field.getType())){idField = field; break;}
        }
        
        PojoMeta meta = new PojoMeta(collectionName,idField);
        metaMap.putIfAbsent(claz, meta);
    }

    private Bson buildQuery(Map<String, Object> params){
        List<Bson> condList = new ArrayList<Bson>();
        condList.add(Filters.eq("isDeleted",false));
        //Set<String> keys = params.keySet();
        for(Entry<String, Object> entry: params.entrySet()){
            if(null != entry.getValue()){
                condList.add(MongoUtils.buildCriteria(entry.getKey(), entry.getValue()));
            }
        }
        return Filters.and(condList);
    }

    private Document buildDocument(Object obj,boolean withId){
        Document result = new Document();
        BsonDocument document = null;
        ObjectId id = null;
        document = BsonDocumentWrapper.asBsonDocument(obj, MongoUtils._defaultCodec);
        if(null != this.getIdField(obj.getClass())){
            document = BsonDocumentWrapper.asBsonDocument(obj, MongoUtils._defaultCodec);
            if(document.containsKey("_id")){
                BsonString idStr = document.getString("_id");
                if(null != idStr && null != idStr.getValue() && withId){
                    id = new ObjectId(idStr.getValue());
                }else if(!withId){
                    document.remove("_id");
                }
            }
        }

        Set<String> keys = document.keySet();
        for(String key: keys){
            result.append(key, document.get(key));
        }
        if(null != id){
            result.put("_id", id);
        }
        return result;
    }

    private <T> MongoCollection<T> getCollection(Class<T> T){
        String collectionName = this.getCollectionName(T);
        MongoCollection<T> result = this._database.getCollection(collectionName,T);
        if(getIdField(T) != null){
            result = result.withCodecRegistry(_withIdCodec);
        }else{
            result = result.withCodecRegistry(MongoUtils._defaultCodec);
        }
        return result;
    }
}

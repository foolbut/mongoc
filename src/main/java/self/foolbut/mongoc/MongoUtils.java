package self.foolbut.mongoc;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;

import org.bson.BsonDocument;
import org.bson.BsonDocumentWrapper;
import org.bson.codecs.BsonValueCodecProvider;
import org.bson.codecs.DocumentCodecProvider;
import org.bson.codecs.IterableCodecProvider;
import org.bson.codecs.MapCodecProvider;
import org.bson.codecs.ValueCodecProvider;
import org.bson.codecs.configuration.CodecRegistries;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;

import com.mongodb.DBObjectCodecProvider;
import com.mongodb.DBRefCodecProvider;
import com.mongodb.DocumentToDBRefTransformer;
import com.mongodb.client.gridfs.codecs.GridFSFileCodecProvider;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import com.mongodb.client.model.geojson.codecs.GeoJsonCodecProvider;

class MongoUtils {

    static final CodecRegistry _defaultCodec = CodecRegistries.fromProviders(new ValueCodecProvider(),
            new BsonValueCodecProvider(),
            new DBRefCodecProvider(),
            new DBObjectCodecProvider(),
            new DocumentCodecProvider(new DocumentToDBRefTransformer()),
            new IterableCodecProvider(new DocumentToDBRefTransformer()),
            new MapCodecProvider(new DocumentToDBRefTransformer()),
            new GeoJsonCodecProvider(),
            new GridFSFileCodecProvider(),
            PojoCodecProvider.builder().automatic(true).build());
    static Bson buildUpdate(Object obj){
        BsonDocument update = BsonDocumentWrapper.asBsonDocument(obj, _defaultCodec);
        List<Bson> bsonList =new ArrayList<Bson>();
        bsonList.add(Updates.currentDate("lastModified"));
        Set<String> keys = update.keySet();
        for(String key: keys){
            bsonList.add(Updates.set(key, update.get(key)));
        }
        return Updates.combine(bsonList);
    }

    static Bson buildCriteria(String key,Object val) throws RuntimeException{
        char c = key.charAt(0);
        String field = null;
        if("_id".equals(key)){
            ObjectId objVal = null;
            if(val instanceof String){
                objVal = new ObjectId((String)val);
            }else if(val instanceof ObjectId){
                objVal = (ObjectId)val;
            }else{
                
            }
            return Filters.eq(objVal);
        }
        switch(c){
            case '>':
                if(key.startsWith(">=")){
                    field = key.substring(2);
                    return Filters.gte(field, val);
                }else{
                    field = key.substring(1);
                    return Filters.gt(field, val);
                }
            case '<':
                if(key.startsWith("<=")){
                    field = key.substring(2);
                    return  Filters.lte(field, val);
                }else{
                    field = key.substring(1);
                    return  Filters.lt(field, val);
                }
            case '^':
                field = key.substring(1);
                return  Filters.regex(field, (Pattern)val);
            case '!':
                field = key.substring(1);
                return  Filters.ne(field, val);
            case '*':
                return Filters.in(field, val);
            default:
                field = key;
                return  Filters.eq(field, val);
        }
    }
}

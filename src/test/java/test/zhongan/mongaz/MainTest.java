package test.zhongan.mongaz;

import java.util.Arrays;
import java.util.Date;

import org.bson.codecs.Codec;
import org.bson.codecs.configuration.CodecProvider;
import org.bson.codecs.configuration.CodecRegistries;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;

import com.mongodb.MongoClient;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import self.foolbut.mongoc.MongoRepositoryFactory;
import self.foolbut.mongoc.codec.ObjectIdStrCodec;

public class MainTest {

    private static CodecRegistry _defaultCodec =CodecRegistries.fromRegistries(MongoClient.getDefaultCodecRegistry(),
            CodecRegistries.fromProviders(PojoCodecProvider.builder().automatic(true).build()));
    public static void main(String[] args) throws Exception{
//        Builder builder = PojoCodecProvider.builder().automatic(true);
//        
//        CodecRegistry pojoCodecRegistry = CodecRegistries.fromProviders(new WithIdStrCodecProvider(),new ValueCodecProvider(),
//                new BsonValueCodecProvider(),
//                new DBRefCodecProvider(),
//                new DBObjectCodecProvider(),
//                new DocumentCodecProvider(new DocumentToDBRefTransformer()),
//                new IterableCodecProvider(new DocumentToDBRefTransformer()),
//                new MapCodecProvider(new DocumentToDBRefTransformer()),
//                new GeoJsonCodecProvider(),
//                new GridFSFileCodecProvider(),
//                PojoCodecProvider.builder().automatic(true).build());
        MongoCredential c = MongoCredential.createCredential("prism_alimini_dev", "prism_alimini", "prism_alimini_dev_700d6e".toCharArray());
        MongoClient client = new MongoClient(new ServerAddress("10.253.104.208",30001),Arrays.asList(new MongoCredential[]{c}));
////        MongoCollection<OrderPushLog> collectionClient = client.getDatabase("prism_alimini").getCollection("order_pay_push_log",OrderPushLog.class).withCodecRegistry(pojoCodecRegistry);
////        Document doc = new Document();
////        doc.append("orderNo", "201801021558006690497");
////        
////        FindIterable<OrderPushLog> fi = collectionClient.find(doc);
////        OrderPushLog ol = fi.first();
////        client.close();
////        System.out.println(ol.getId());
////        System.out.println(new ObjectId(ol.getId()));
//        
//        OrderPushLog log = new OrderPushLog();
//        log.setExeTime(new Date());
//        log.setId("5a4b403ae4b00bb55e856eac");
//        BsonDocument doc =  BsonDocumentWrapper.asBsonDocument(log, pojoCodecRegistry);
//        System.out.println(doc.get("id"));
//        System.out.println(doc.get("_id"));
//        System.out.println(doc.get("exeTime"));
        
        MongoRepositoryFactory mongoRepo = new MongoRepositoryFactory();
        mongoRepo.setClient(client);
        mongoRepo.setDbName("prism_alimini");
        mongoRepo.afterPropertiesSet();
//        Map<String,Object> params = new HashMap<String,Object>();
//        params.put("orderNo", "201801021558006690497");
//        List<OrderPushLog> list = mongoRepo.findAll(params, OrderPushLog.class);
//
//        System.out.println(list.size());
//        System.out.println(list.get(0).getId());
//        System.out.println(mongoRepo.findCnt(params, OrderPushLog.class));
        
        OrderPushLog newRecord = new OrderPushLog();
        newRecord.setExeTime(new Date());
        newRecord.setJobId(1232222344l);
        newRecord.setOrderNo("201801021558006690497");
        newRecord.setSuccess(true);
        newRecord.setErrorMsg("abc");
        mongoRepo.insertOne(newRecord);
        System.out.print(newRecord.getId());
    }
    
    private static class WithIdStrCodecProvider implements  CodecProvider {
        private Codec<String> objectIdCodec = new ObjectIdStrCodec();
        @Override
        public <T> Codec<T> get(Class<T> clazz, CodecRegistry registry) {
            if(String.class.equals(clazz)){
                return (Codec<T>)(objectIdCodec);
            }
            return null;
        }
    } 
}

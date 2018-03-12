package self.foolbut.mongoc;

import java.util.List;
import java.util.Map;

import com.mongodb.client.model.FindOptions;

public interface IMongoRepository {

    void insertOne(Object obj);

    <T> List<T> findPage(Map<String,Object> params, Class<T> claz,int limit,int offset);

    <T> List<T> findAll(Map<String,Object> params, Class<T> claz);

    long findCnt(Map<String,Object> params, Class<?> T);

    <T> T findById(String ObjectId,Class<T> claz);

    <T> T findOne(Map<String,Object> params, Class<T> claz);

    boolean  updateOne(String objectId, Object obj);

    <T> void deleteOne(String objectId,  Class<T> claz);
}

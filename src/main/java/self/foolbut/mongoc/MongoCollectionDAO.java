package self.foolbut.mongoc;

import java.util.List;
import java.util.Map;

public interface MongoCollectionDAO<T> {

    public void insert(T object);    

    T findOne(Map<String,Object> params);    

    List<T> findAll(Map<String,Object> params);
    
    int findCnt(Map<String,Object> params);

    List<T> findPage(Map<String,Object> params,int skip, int offset);

    void updateById(String objectId,Map<String,Object> params);
}

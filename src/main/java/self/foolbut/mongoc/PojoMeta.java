package self.foolbut.mongoc;

import java.lang.reflect.Field;

class PojoMeta {

    private Field idField;
    private String collectionName;
    
    public PojoMeta(String collectionName,Field idField){
        this.collectionName = collectionName;
        this.idField = idField;
    }
    public Field getIdField() {
        return idField;
    }
    public String getCollectionName() {
        return collectionName;
    }
    
}

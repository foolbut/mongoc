package self.foolbut.mongoc.ex;

public class CollectionNameNotFoundException extends RuntimeException{

    private Class<?> claz;
    public CollectionNameNotFoundException(Class<?> claz){
        this.claz = claz;
    }
    
    public String getMessage(){
        return "No collection name found in pojo class<"+claz+">";
    }
}

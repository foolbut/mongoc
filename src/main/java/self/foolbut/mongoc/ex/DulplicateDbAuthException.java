package self.foolbut.mongoc.ex;

public class DulplicateDbAuthException extends RuntimeException{

    private String msg;
    /**
     * 
     */
    private static final long serialVersionUID = -7099383658715188538L;
    
    public DulplicateDbAuthException(String dbName){
        this.msg = "Dulplicate db user auth of "+ dbName;
    }

    public String getMessage(){
        return this.msg;
    }
}

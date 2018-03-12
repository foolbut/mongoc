package self.foolbut.mongoc.ex;

public class DatabaseNotAuthorizedException extends RuntimeException{

    private static final long serialVersionUID = 6241923223890115717L;
    
    private String msg;
    public DatabaseNotAuthorizedException(String targetDatabase){
        this.msg = "Database "+targetDatabase+" not authorized.";
    }

    public String getMessge(){
        return this.msg;
    }

}

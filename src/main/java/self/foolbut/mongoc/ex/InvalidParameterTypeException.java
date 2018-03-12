package self.foolbut.mongoc.ex;

import java.lang.reflect.Type;

public class InvalidParameterTypeException extends RuntimeException{

    private String message;
    private static final long serialVersionUID = 6210448699416927499L;

    public InvalidParameterTypeException(Class<?> claz, String parameterName,Type type){
        this.message = "Parameter "+ parameterName+" of "+ claz+ " must be of type "+ type;
    }
    
    public String getMessage(){
        return this.message;
    }
}

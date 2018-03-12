package self.foolbut.mongoc;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PojoClassRegistry {

    private static final Map<String,List<Field>> fieldMap = new HashMap<String,List<Field>>();
    
    protected static void register(Class claz){
        
    }
    
    public static Field getField(Class<?> claz,String fieldName){
        if(fieldMap.containsKey(claz)){
            for(Field field: fieldMap.get(claz)){
                if(field.getName().equals(fieldName)) return field;
            }
        }
        return null;
    }
}

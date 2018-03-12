package self.foolbut.mongoc.interceptor.impl;

import java.lang.reflect.Field;
import java.util.Date;

import self.foolbut.mongoc.PojoClassRegistry;
import self.foolbut.mongoc.interceptor.PojoCheckInterceptor;

public class CommonStandardPojoInterceptor implements PojoCheckInterceptor{

    @Override
    public void check(Class<?> claz) {
        Field gmtCreated = PojoClassRegistry.getField(claz, "gmtCreated"),
            gmtModified = PojoClassRegistry.getField(claz, "gmtModified"),
            isDeleted = PojoClassRegistry.getField(claz, "isDeleted");
        if(null != gmtCreated && Date.class.equals(gmtCreated.getType())){
            
        }
    }
}

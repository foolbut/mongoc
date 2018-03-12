package self.foolbut.mongoc.interceptor;

import java.util.ArrayList;
import java.util.List;

public class InterceptorRegistry {

    private static List<PojoCheckInterceptor> pojoInterceptorList = new ArrayList<PojoCheckInterceptor>();
    public static void pojoCheck(Class<?> claz){
        for(PojoCheckInterceptor interceptor:pojoInterceptorList){
            interceptor.check(claz);
        }
    }
    public static void register(PojoCheckInterceptor interceptor){
        pojoInterceptorList.add(interceptor);
    }
}

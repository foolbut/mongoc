package self.foolbut.mongoc.interceptor;

import org.bson.conversions.Bson;

public interface QueryInterceptor {

    void checkCondition(Bson bson,Class<?> claz);
}

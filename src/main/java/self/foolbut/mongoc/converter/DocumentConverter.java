package self.foolbut.mongoc.converter;

import org.bson.Document;
import org.bson.conversions.Bson;

public interface DocumentConverter {

    Document serialize(Object obj);

    <T> T parse(Document doc, Class<?> T);
}

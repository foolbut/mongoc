package self.foolbut.mongoc.converter;

import org.bson.Document;
import org.bson.codecs.configuration.CodecRegistry;

public class DefaultDocumentConverter implements DocumentConverter{

    private CodecRegistry codeRegistry;
    @Override
    public Document serialize(Object obj) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public <T> T parse(Document doc, Class<?> T) {
        // TODO Auto-generated method stub
//        BsonReader reader = new BsonReader();
//        codeRegistry.get(T).decode(reader, decoderContext)
        return null;
    }

}

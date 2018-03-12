package self.foolbut.mongoc.codec;

import org.bson.codecs.Codec;
import org.bson.codecs.configuration.CodecProvider;
import org.bson.codecs.configuration.CodecRegistry;

public class WithIdStrCodecProvider implements  CodecProvider {
    private Codec<String> objectIdCodec = new ObjectIdStrCodec();
    @Override
    public <T> Codec<T> get(Class<T> clazz, CodecRegistry registry) {
        if(String.class.equals(clazz)){
            return (Codec<T>)(objectIdCodec);
        }
        return null;
    }
} 

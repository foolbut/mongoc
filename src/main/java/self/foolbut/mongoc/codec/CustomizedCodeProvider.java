package self.foolbut.mongoc.codec;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bson.codecs.Codec;
import org.bson.codecs.configuration.CodecProvider;
import org.bson.codecs.configuration.CodecRegistry;

public class CustomizedCodeProvider implements CodecProvider{

    public CustomizedCodeProvider(List<? extends Codec> codecList) {
    }

    @Override
    public <T> Codec<T> get(Class<T> clazz, CodecRegistry registry) {
        return null;//return (Codec<T>)codecMap.get(clazz);
    }

}

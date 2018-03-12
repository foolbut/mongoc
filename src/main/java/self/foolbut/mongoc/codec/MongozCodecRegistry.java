package self.foolbut.mongoc.codec;

import static java.util.Arrays.asList;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.bson.codecs.BsonValueCodecProvider;
import org.bson.codecs.Codec;
import org.bson.codecs.DocumentCodecProvider;
import org.bson.codecs.IterableCodecProvider;
import org.bson.codecs.MapCodecProvider;
import org.bson.codecs.ValueCodecProvider;
import org.bson.codecs.configuration.CodecProvider;
import org.bson.codecs.configuration.CodecRegistry;

import com.mongodb.DBObjectCodecProvider;
import com.mongodb.DBRefCodecProvider;
import com.mongodb.DocumentToDBRefTransformer;
import com.mongodb.client.gridfs.codecs.GridFSFileCodecProvider;
import com.mongodb.client.model.geojson.codecs.GeoJsonCodecProvider;
public class MongozCodecRegistry implements CodecRegistry, CodecProvider{
    private final List<CodecProvider> codecProviders;

    private static final Set<Class<? extends CodecProvider>> defaultClassSet = new HashSet<Class<? extends CodecProvider>>();
    static{
        defaultClassSet.add(ValueCodecProvider.class);
        defaultClassSet.add(BsonValueCodecProvider.class);
        defaultClassSet.add(DBRefCodecProvider.class);
        defaultClassSet.add(DBObjectCodecProvider.class);
        defaultClassSet.add(DocumentCodecProvider.class);
        defaultClassSet.add(IterableCodecProvider.class);
        defaultClassSet.add(MapCodecProvider.class);
        defaultClassSet.add(GeoJsonCodecProvider.class);
        defaultClassSet.add(GridFSFileCodecProvider.class);
    }
    public MongozCodecRegistry(final List<? extends Codec> codecList) {
        ArrayList<CodecProvider> defaultProviderList = new ArrayList<CodecProvider>();
        if(null != codecList && 0 == codecList.size()){
            CustomizedCodeProvider  cProvider = new CustomizedCodeProvider(codecList);
            defaultProviderList.add(cProvider);
        }
        defaultProviderList.addAll(asList(new ValueCodecProvider(),
                new BsonValueCodecProvider(),
                new DBRefCodecProvider(),
                new DBObjectCodecProvider(),
                new DocumentCodecProvider(new DocumentToDBRefTransformer()),
                new IterableCodecProvider(new DocumentToDBRefTransformer()),
                new MapCodecProvider(new DocumentToDBRefTransformer()),
                new GeoJsonCodecProvider(),
                new GridFSFileCodecProvider()));
        this.codecProviders = defaultProviderList;
    }

    @Override
    public <T> Codec<T> get(Class<T> clazz, CodecRegistry registry) {
        for (CodecProvider provider : codecProviders) {
            if(provider.getClass().equals(this.getClass())){
                return null;
            }
            Codec<T> codec = provider.get(clazz, registry);
            if (codec != null) {
                return codec;
            }
        }
        return null;
    }

    @Override
    public <T> Codec<T> get(Class<T> clazz) {
        // TODO Auto-generated method stub
        return null;
    }
}

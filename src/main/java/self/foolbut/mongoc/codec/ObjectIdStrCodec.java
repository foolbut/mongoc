package self.foolbut.mongoc.codec;

import org.bson.BsonReader;
import org.bson.BsonType;
import org.bson.codecs.DecoderContext;
import org.bson.codecs.StringCodec;

public class ObjectIdStrCodec extends StringCodec {

    @Override
    public String decode(final BsonReader reader, final DecoderContext decoderContext) {
        if (reader.getCurrentBsonType() == BsonType.SYMBOL) {
            return reader.readSymbol();
        }else if(reader.getCurrentBsonType() == BsonType.OBJECT_ID){
            return reader.readObjectId().toHexString();
        } else {
            return reader.readString();
        }
    }
}

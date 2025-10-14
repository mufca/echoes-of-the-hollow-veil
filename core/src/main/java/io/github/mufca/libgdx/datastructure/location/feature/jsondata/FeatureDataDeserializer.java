package io.github.mufca.libgdx.datastructure.location.feature.jsondata;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import io.github.mufca.libgdx.datastructure.location.feature.FeatureType;
import java.io.IOException;

public class FeatureDataDeserializer extends JsonDeserializer<FeatureData> {

    private static final String FIELD_NAME = "type";

    @Override
    public FeatureData deserialize(JsonParser parser, DeserializationContext context)
        throws IOException {
        ObjectCodec codec = parser.getCodec();
        JsonNode node = codec.readTree(parser);
        String typeText = node.get(FIELD_NAME).asText();
        FeatureType type = FeatureType.valueOf(typeText);
        return codec.treeToValue(node, type.getData());
    }
}

package net.locortes.box.java.unirest.metadata.serializers;

import com.google.gson.*;
import net.locortes.box.java.unirest.metadata.Metadata;

import java.lang.reflect.Type;

/**
 * Created by vicenc.cortesolea on 20/02/2017.
 *
 * This class implements a specific serialization to perform searches from the available json
 */
public class MetadataSerializer4Search implements JsonSerializer<Metadata> {

    @Override
    public JsonElement serialize(Metadata src, Type typeOfSrc, JsonSerializationContext context) {
        final JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("templateKey", src.getTemplateKey());
        jsonObject.addProperty("scope", src.getScope());

        final JsonElement jsonFields = context.serialize(src.getFields());
        jsonObject.add("filters", jsonFields);

        return jsonObject;
    }
}

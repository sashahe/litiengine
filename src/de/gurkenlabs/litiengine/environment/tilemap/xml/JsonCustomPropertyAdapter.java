package de.gurkenlabs.litiengine.environment.tilemap.xml;

import java.io.IOException;
import java.util.Hashtable;
import java.util.Map;

import de.gurkenlabs.litiengine.environment.tilemap.ICustomProperty;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

public class JsonCustomPropertyAdapter extends TypeAdapter<Map<String, ICustomProperty>> {

  @Override
  public void write(JsonWriter out, Map<String, ICustomProperty> value) throws IOException {
    out.beginArray();
    for (Map.Entry<String, ICustomProperty> property : value.entrySet()) {
      out.beginObject();
      out.name("name");
      out.value(property.getKey());
      out.name("type");
      out.value(property.getValue().getType());
      out.name("value");
      out.value(property.getValue().getAsString());
      out.endObject();
    }
    out.endArray();
  }

  @Override
  public Map<String, ICustomProperty> read(JsonReader in) throws IOException {
    in.beginArray();
    Map<String, ICustomProperty> table = new Hashtable<>();
    while (in.hasNext()) {
      String name = null;
      String type = null;
      String value = null;
      in.beginObject();
      while (in.hasNext()) {
        String prop = in.nextName();
        if (prop.equals("name")) {
          name = in.nextString();
        } else if (prop.equals("type")) {
          type = in.nextString();
        } else if (prop.equals("value")) {
          value = in.nextString();
        } else {
          in.skipValue();
        }
      }
      in.endObject();
      table.put(name, new CustomProperty(type, value));
    }
    in.endArray();
    return null;
  }

}

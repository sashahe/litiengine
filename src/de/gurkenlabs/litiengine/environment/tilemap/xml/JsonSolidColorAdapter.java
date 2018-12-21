package de.gurkenlabs.litiengine.environment.tilemap.xml;

import java.awt.Color;
import java.io.IOException;

import de.gurkenlabs.litiengine.util.ColorHelper;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

public class JsonSolidColorAdapter extends TypeAdapter<Color> {

  @Override
  public void write(JsonWriter out, Color value) throws IOException {
    out.value(ColorHelper.encode(value));
  }

  @Override
  public Color read(JsonReader in) throws IOException {
    if (in.peek() == JsonToken.NULL) {
      return null;
    }
    return ColorHelper.decode(in.nextString(), true);
  }
}

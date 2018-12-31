package de.gurkenlabs.litiengine.environment.tilemap.xml;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.TypeAdapter;
import com.google.gson.annotations.JsonAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

@JsonAdapter(JsonTileDataGroup.Adapter.class)
public class JsonTileDataGroup {
  private static class Adapter extends TypeAdapter<JsonTileDataGroup> {

    @Override
    public void write(JsonWriter out, JsonTileDataGroup value) throws IOException {
      throw new UnsupportedOperationException(); // TODO
    }

    @Override
    public JsonTileDataGroup read(JsonReader in) throws IOException {
      JsonTileDataGroup group = new JsonTileDataGroup();
      if (in.peek() == JsonToken.BEGIN_ARRAY) {
        group.tiles = new ArrayList<>();
        in.beginArray();
        while (in.hasNext()) {
          group.tiles.add(new Tile(in.nextInt()));
        }
        in.endArray();
      } else {
        group.base64Data = in.nextString();
      }
      return group;
    }
  }

  private List<Tile> tiles;
  private String base64Data;

  public List<Tile> parse(String compression) throws InvalidTileLayerException {
    if (this.tiles == null) {
      this.tiles = TileData.parseBase64Data(this.base64Data, compression);
    }
    return this.tiles;
  }
}

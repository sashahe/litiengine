package de.gurkenlabs.litiengine.environment.tilemap.xml;

import java.util.Arrays;
import java.util.List;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;

public class JsonTileData extends TileData {

  private JsonTileDataGroup finiteData;
  private TileChunk[] chunkData;

  public JsonTileData(JsonObject json, JsonDeserializationContext context) {
    this.setEncoding(json.get("encoding").getAsString());
    if (json.has("compression")) {
      this.setCompression(json.get("compression").getAsString());
    }
    if (json.has("data")) {
      this.finiteData = context.deserialize(json.get("data"), JsonTileDataGroup.class);
    } else {
      this.chunkData = context.deserialize(json.get("chunks"), TileChunk[].class);
    }
  }

  @Override
  protected boolean isInfinite() {
    return this.chunkData != null;
  }

  @Override
  protected List<TileChunk> getChunks() {
    return Arrays.asList(chunkData);
  }

  @Override
  protected List<Tile> parseData() throws InvalidTileLayerException {
    return this.finiteData.parse(this.getCompression());
  }

  @Override
  protected List<Tile> parseChunk(TileChunk tiles) throws InvalidTileLayerException {
    return tiles.getData().parse(this.getCompression());
  }
}

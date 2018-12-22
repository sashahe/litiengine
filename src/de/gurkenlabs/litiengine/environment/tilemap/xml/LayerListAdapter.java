package de.gurkenlabs.litiengine.environment.tilemap.xml;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import de.gurkenlabs.litiengine.environment.tilemap.IImageLayer;
import de.gurkenlabs.litiengine.environment.tilemap.ILayer;
import de.gurkenlabs.litiengine.environment.tilemap.IMapObjectLayer;
import de.gurkenlabs.litiengine.environment.tilemap.ITileLayer;

import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

public class LayerListAdapter implements JsonSerializer<List<ILayer>>, JsonDeserializer<List<ILayer>> {
  private static final Logger log = Logger.getLogger(LayerListAdapter.class.getName());

  @Override
  public List<ILayer> deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
    System.out.println(typeOfT.getTypeName());
    JsonArray rawLayers = json.getAsJsonArray();
    List<ILayer> layers = new ArrayList<>(rawLayers.size());
    for (JsonElement rawLayer : rawLayers) {
      JsonObject layer = rawLayer.getAsJsonObject();
      Class<? extends ILayer> type;
      String layerType = layer.get("type").getAsString();
      if (layerType.equals("tilelayer")) {
        type = TileLayer.class;
      } else if (layerType.equals("objectgroup")) {
        type = MapObjectLayer.class;
      } else if (layerType.equals("imagelayer")) {
        type = ImageLayer.class;
      } else {
        // JSON is intended to be extensible, so don't throw an exception for an unrecognized layer
        log.log(Level.WARNING, "Unrecognized layer type \"" + layerType + '"');
        continue;
      }
      layers.add(context.deserialize(layer, type));
    }
    return layers;
  }

  @Override
  public JsonElement serialize(List<ILayer> src, Type typeOfSrc, JsonSerializationContext context) {
    JsonArray serialized = new JsonArray(src.size());
    for (ILayer layer : src) {
      JsonElement json = context.serialize(layer);
      if (json.isJsonObject()) {
        JsonObject object = json.getAsJsonObject();
        if (layer instanceof ITileLayer) {
          object.addProperty("type", "tilelayer");
        } else if (layer instanceof IMapObjectLayer) {
          object.addProperty("type", "objectgroup");
        } else if (layer instanceof IImageLayer) {
          object.addProperty("type", "imagelayer");
        } else {
          log.log(Level.WARNING, "A layer was missing a specific ILayer subinterface");
        }
      } else {
        log.log(Level.WARNING, "A layer may have been serialized incorrectly; it should be serialized as an object");
      }
    }
    return serialized;
  }
}

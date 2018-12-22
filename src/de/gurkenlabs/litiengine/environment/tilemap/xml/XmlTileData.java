package de.gurkenlabs.litiengine.environment.tilemap.xml;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlMixed;
import javax.xml.bind.annotation.XmlTransient;

public class XmlTileData extends TileData {

  @XmlMixed
  @XmlElementRef(type = TileChunk.class, name = "chunk")
  private List<Object> rawValue;

  @XmlTransient
  private List<TileChunk> chunks;

  @XmlTransient
  private String value;

  @Override
  protected boolean isInfinite()  {
    return this.chunks != null && !this.chunks.isEmpty();
  }

  @XmlTransient
  public String getValue() {
    return value;
  }

  public void setValue(String value) {
    this.value = value;
  }

  @Override
  protected List<? extends TileChunk> getChunks() {
    return this.chunks;
  }

  @Override
  protected List<Tile> parseData() throws InvalidTileLayerException {
    return parseTiles(this.value);
  }

  @Override
  protected List<Tile> parseTiles(String tiles) throws InvalidTileLayerException {
    if (this.getEncoding().equals("base64")) {
      return parseBase64Data(tiles, this.getCompression());
    } else if (this.getEncoding().equals("csv")) {
      return parseCsvData(tiles);
    } else {
      throw new InvalidTileLayerException("unsupported tile layer encoding \"" + this.getEncoding() + '"');
    }
  }

  /**
   * This method processes the {@link XmlMixed} contents that were unmarshalled and extract either the string value containing the information
   * about the layer of a set of {@link TileChunk}s if the map is infinite.
   */
  private void processMixedData() {
    if (this.rawValue == null || this.rawValue.isEmpty()) {
      return;
    }

    List<TileChunk> rawChunks = new ArrayList<>();
    String v = null;
    for (Object val : this.rawValue) {
      if (val instanceof String) {
        String trimmedValue = ((String) val).trim();
        if (!trimmedValue.isEmpty()) {
          v = trimmedValue;
        }
      }

      if (val instanceof TileChunk) {
        rawChunks.add((TileChunk) val);
      }
    }

    if (rawChunks.isEmpty()) {
      this.value = v;
      return;
    }

    this.chunks = rawChunks;
  }

  @SuppressWarnings("unused")
  private void afterUnmarshal(Unmarshaller u, Object parent) {
    this.processMixedData();

    if (this.isInfinite()) {
      // make sure that the chunks are organized top-left to bottom right
      // this is important for their data to be parsed in the right order
      Collections.sort(this.chunks);

      this.updateDimensionsByTileData();
    }
  }
}

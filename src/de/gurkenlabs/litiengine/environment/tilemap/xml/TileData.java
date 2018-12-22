package de.gurkenlabs.litiengine.environment.tilemap.xml;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.GZIPInputStream;
import java.util.zip.InflaterInputStream;

import javax.xml.bind.DatatypeConverter;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlTransient;

import de.gurkenlabs.litiengine.util.ArrayUtilities;

public abstract class TileData {

  @XmlAttribute
  private String encoding;

  @XmlAttribute
  private String compression;

  @XmlTransient
  private List<Tile> parsedTiles;

  @XmlTransient
  private int width;

  @XmlTransient
  private int height;

  @XmlTransient
  private int offsetX;

  @XmlTransient
  private int offsetY;

  @XmlTransient
  private int minChunkOffsetXMap;

  @XmlTransient
  private int minChunkOffsetYMap;

  @XmlTransient
  public String getEncoding() {
    return encoding;
  }

  @XmlTransient
  public String getCompression() {
    return compression;
  }

  public void setEncoding(String encoding) {
    this.encoding = encoding;
  }

  public void setCompression(String compression) {
    this.compression = compression;
  }

  protected void setMinChunkOffsets(int x, int y) {
    this.minChunkOffsetXMap = x;
    this.minChunkOffsetYMap = y;
  }

  protected abstract boolean isInfinite();

  protected int getWidth() {
    if (this.isInfinite() && this.minChunkOffsetXMap != 0) {
      return this.width + (this.offsetX - this.minChunkOffsetXMap);
    }

    return this.width;
  }

  protected int getHeight() {
    if (this.isInfinite() && this.minChunkOffsetYMap != 0) {
      return this.height + (this.offsetY - this.minChunkOffsetYMap);
    }
    
    return this.height;
  }

  protected int getOffsetX() {
    return this.offsetX;
  }

  protected int getOffsetY() {
    return this.offsetY;
  }

  protected List<Tile> parseTiles() throws InvalidTileLayerException {
    if (this.parsedTiles != null) {
      return this.parsedTiles;
    }

    if (this.getEncoding() == null || this.getEncoding().isEmpty()) {
      return new ArrayList<>();
    }

    if (this.isInfinite()) {
      this.parsedTiles = this.parseChunkData();
    } else {
      this.parsedTiles = this.parseData();
    }

    return this.parsedTiles;
  }

  protected static List<Tile> parseBase64Data(String value, String compression) throws InvalidTileLayerException {
    List<Tile> parsed = new ArrayList<>();

    String enc = value.trim();
    byte[] dec;
    try {
      dec = DatatypeConverter.parseBase64Binary(enc);
    } catch (IllegalArgumentException e) {
      throw new InvalidTileLayerException("invalid base64 string", e);
    }
    try (ByteArrayInputStream bais = new ByteArrayInputStream(dec)) {
      InputStream is;

      if (compression == null || compression.isEmpty()) {
        is = bais;
      } else if (compression.equals("gzip")) {
        is = new GZIPInputStream(bais, dec.length);
      } else if (compression.equals("zlib")) {
        is = new InflaterInputStream(bais);
      } else {
        throw new IllegalArgumentException("Unsupported tile layer compression method " + compression);
      }

      int read;

      while ((read = is.read()) != -1) {
        int tileId = 0;
        tileId |= read;

        read = is.read();
        int flags = read << Byte.SIZE;

        read = is.read();
        flags |= read << Byte.SIZE * 2;

        read = is.read();
        flags |= read << Byte.SIZE * 3;
        tileId |= flags;

        if (tileId == Tile.NONE) {
          parsed.add(Tile.EMPTY);
        } else {
          parsed.add(new Tile(tileId));
        }
      }

    } catch (IOException e) {
      throw new InvalidTileLayerException(e);
    }

    return parsed;
  }

  protected static List<Tile> parseCsvData(String value) throws InvalidTileLayerException {

    List<Tile> parsed = new ArrayList<>();

    // trim 'space', 'tab', 'newline'. pay attention to additional unicode chars
    // like \u2028, \u2029, \u0085 if necessary
    String[] csvTileIds = value.trim().split("[\\s]*,[\\s]*");

    for (String gid : csvTileIds) {
      int tileId;
      try {
        tileId = Integer.parseUnsignedInt(gid);
      } catch (NumberFormatException e) {
        throw new InvalidTileLayerException(e);
      }

      if (tileId > Integer.MAX_VALUE) {
        parsed.add(new Tile(tileId));
      } else {
        if (tileId == Tile.NONE) {
          parsed.add(Tile.EMPTY);
        } else {
          parsed.add(new Tile((int) tileId));
        }
      }
    }

    return parsed;
  }

  protected abstract List<? extends TileChunk> getChunks();

  /**
   * For infinite maps, the size of a tile layer depends on the <code>TileChunks</code> it contains.
   */
  protected void updateDimensionsByTileData() {
    int minX = 0;
    int maxX = 0;
    int minY = 0;
    int maxY = 0;
    int maxChunkWidth = 0;
    int maxChunkHeight = 0;

    for (TileChunk chunk : this.getChunks()) {
      if (chunk.getX() < minX) {
        minX = chunk.getX();
      }

      if (chunk.getY() < minY) {
        minY = chunk.getY();
      }

      if (chunk.getX() + chunk.getWidth() > maxX) {
        maxX = chunk.getX();
        maxChunkWidth = chunk.getWidth();
      }

      if (chunk.getY() + chunk.getHeight() > maxY) {
        maxY = chunk.getY();
        maxChunkHeight = chunk.getHeight();
      }
    }

    this.width = (maxX + maxChunkWidth) - minX;
    this.height = (maxY + maxChunkHeight) - minY;

    this.offsetX = minX;
    this.offsetY = minY;
  }

  private List<Tile> parseChunkData() throws InvalidTileLayerException {
    // first fill a two-dimensional array with all the information of the chunks
    Tile[][] tileArr = new Tile[this.getHeight()][this.getWidth()];

    for (TileChunk chunk : this.getChunks()) {
      List<Tile> chunkTiles = this.parseTiles(chunk.getValue());//parseBase64Data(chunk.getValue(), this.compression);
      this.addTiles(tileArr, chunk, chunkTiles);
    }

    // fill up the rest of the map with Tile.EMPTY
    for (int y = 0; y < tileArr.length; y++) {
      for (int x = 0; x < tileArr[y].length; x++) {
        if (tileArr[y][x] == null) {
          tileArr[y][x] = Tile.EMPTY;
        }
      }
    }

    return ArrayUtilities.toList(tileArr);
  }

  private void addTiles(Tile[][] tileArr, TileChunk chunk, List<Tile> chunkTiles) {
    int startX = chunk.getX() - this.minChunkOffsetXMap;
    int startY = chunk.getY() - this.minChunkOffsetYMap;

    int index = 0;

    for (int y = startY; y < startY + chunk.getHeight(); y++) {
      for (int x = startX; x < startX + chunk.getWidth(); x++) {
        tileArr[y][x] = chunkTiles.get(index);
        index++;
      }
    }
  }

  protected abstract List<Tile> parseData() throws InvalidTileLayerException;
  protected abstract List<Tile> parseTiles(String tiles) throws InvalidTileLayerException; // for chunks
}
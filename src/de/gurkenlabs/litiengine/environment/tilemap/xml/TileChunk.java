package de.gurkenlabs.litiengine.environment.tilemap.xml;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlValue;

import com.google.gson.annotations.Expose;

/**
 * This class represents a chunk of tiles in an infinite map.
 */
@XmlRootElement(name = "chunk")
public class TileChunk implements Comparable<TileChunk> {
  @Expose
  @XmlAttribute
  private int x;

  @Expose
  @XmlAttribute
  private int y;

  @Expose
  @XmlAttribute
  private int width;

  @Expose
  @XmlAttribute
  private int height;

  @XmlValue
  private String value;

  @Expose
  private JsonTileDataGroup data;

  @XmlTransient
  public String getValue() {
    return this.value;
  }

  public JsonTileDataGroup getData() {
    return this.data;
  }

  public int getX() {
    return this.x;
  }

  public int getY() {
    return this.y;
  }

  public int getWidth() {
    return this.width;
  }

  public int getHeight() {
    return this.height;
  }

  @Override
  public int compareTo(TileChunk o) {
    if (this.getY() != o.getY()) {
      return Integer.compare(this.getY(), o.getY());
    }

    if (this.getX() != o.getX()) {
      return Integer.compare(this.getX(), o.getX());
    }

    return 0;
  }
}

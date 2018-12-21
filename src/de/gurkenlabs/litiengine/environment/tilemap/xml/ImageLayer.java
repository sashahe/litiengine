package de.gurkenlabs.litiengine.environment.tilemap.xml;

import java.awt.Color;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import de.gurkenlabs.litiengine.environment.tilemap.IImageLayer;
import de.gurkenlabs.litiengine.environment.tilemap.IMapImage;

public class ImageLayer extends Layer implements IImageLayer {
  private static final long serialVersionUID = 3233918712579479523L;

  @XmlElement(name = "image")
  private MapImage image;

  @XmlJavaTypeAdapter(ColorAdapter.class)
  @XmlAttribute(name = "trans")
  private Color transparentcolor;

  @Override
  public IMapImage getImage() {
    return this.image;
  }

  @Override
  public Color getTransparentColor() {
    return this.transparentcolor;
  }

  @Override
  public int getOffsetX() {
    if (this.isInfiniteMap()) {
      Map map = (Map) this.getMap();
      return super.getOffsetX() - map.getChunkOffsetX() * map.getTileWidth();
    }

    return super.getOffsetX();
  }

  @Override
  public int getOffsetY() {
    if (this.isInfiniteMap()) {
      Map map = (Map) this.getMap();
      return super.getOffsetX() - map.getChunkOffsetY() * map.getTileHeight();
    }

    return super.getOffsetY();
  }

  public void setMapPath(final String path) {
    this.image.setAbsolutePath(path);
  }

  private boolean isInfiniteMap() {
    return this.getMap() != null && this.getMap().isInfinite() && this.getMap() instanceof Map;
  }
}

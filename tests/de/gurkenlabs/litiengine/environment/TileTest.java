package de.gurkenlabs.litiengine.environment;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.awt.Dimension;
import java.awt.Point;
import java.util.ArrayList;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

import de.gurkenlabs.litiengine.environment.tilemap.MapUtilities;
import de.gurkenlabs.litiengine.environment.tilemap.IMap;
import de.gurkenlabs.litiengine.environment.tilemap.MapOrientation;


 public class TileTest {
  private Environment testEnvironment;
  private IMap map;

  @BeforeEach
  public void setUp() {
    IMap map = mock(IMap.class);
    when(map.getSizeInPixels()).thenReturn(new Dimension(100, 100));
    when(map.getSizeInTiles()).thenReturn(new Dimension(10, 10));
    when(map.getOrientation()).thenReturn(MapOrientation.ORTHOGONAL);
    when(map.getRenderLayers()).thenReturn(new ArrayList<>());
    this.map = map;
  }

  @Test
  public void testGetTileOrthogonalMapValid() {
    when(map.getTileWidth()).thenReturn(1);
    when(map.getTileHeight()).thenReturn(1);
    Point p = MapUtilities.getTile(this.map, 1, 1);
    assertEquals(p.getX(), 1);
    assertEquals(p.getY(), 1);
  }

  @Test
  public void testGetTileOrthogonalMapInvalid() {
    when(map.getTileWidth()).thenReturn(2);
    when(map.getTileHeight()).thenReturn(2);
    Point p = MapUtilities.getTile(this.map, -1, -1);
    assertEquals(p.getX(), -1);
    assertEquals(p.getY(), -1);
  }
   
} 
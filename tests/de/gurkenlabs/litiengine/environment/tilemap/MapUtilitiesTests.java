package de.gurkenlabs.litiengine.environment.tilemap;

import java.awt.Point;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

import de.gurkenlabs.litiengine.environment.tilemap.MapUtilities;

public class MapUtilitiesTests {

  @Test
  public void testHexContainsMouse() {
    Point p;

    StaggerAxis staggerAxis = StaggerAxis.X;
    StaggerIndex staggerIndex = StaggerIndex.EVEN;
    Point tileLocation = new Point(0, 0);

    int s = 1;
    int t = 1;

    int r = 1;

    int jumpWidth = 1;
    int jumpHeight = 1;

    double mouseX = 1.5;
    double mouseY = 1.5;

    p = MapUtilities.callAccessHexStaggering(staggerAxis, staggerIndex, tileLocation, s, t, r, jumpWidth, jumpHeight, mouseX, mouseY);

    // If mouse is contained then the original coordinates should be used
    assertEquals(tileLocation.getX(), p.getX());
    assertEquals(tileLocation.getY(), p.getY());

    staggerAxis = StaggerAxis.Y;
    p = MapUtilities.callAccessHexStaggering(staggerAxis, staggerIndex, tileLocation, s, t, r, jumpWidth, jumpHeight, mouseX, mouseY);

    // Should be the same for Y-direction 
    assertEquals(tileLocation.getX(), p.getX());
    assertEquals(tileLocation.getY(), p.getY());
  }

  @Test
  public void testMouseIsOutsideHex() {
    Point p;

    StaggerAxis staggerAxis = StaggerAxis.X;
    StaggerIndex staggerIndex = StaggerIndex.EVEN;
    Point tileLocation = new Point(0, 0);

    int s = 1;
    int t = 1;

    int r = 1;

    int jumpWidth = 1;
    int jumpHeight = 1;

    double mouseX = 0.0;
    double mouseY = 1.0;

    // Mouse is now outside hex on the upper left triangle outside
    p = MapUtilities.callAccessHexStaggering(staggerAxis, staggerIndex, tileLocation, s, t, r, jumpWidth, jumpHeight, mouseX, mouseY);
    assertEquals(-1, p.getX());
    assertEquals(tileLocation.getY(), p.getY());

    staggerAxis = StaggerAxis.Y;

    p = MapUtilities.callAccessHexStaggering(staggerAxis, staggerIndex, tileLocation, s, t, r, jumpWidth, jumpHeight, mouseX, mouseY);
    assertEquals(tileLocation.getX(), p.getX());
    assertEquals(-1, p.getY());

    // Mouse is now outside hex on the lower left triangle outside
    staggerAxis = StaggerAxis.X;
    mouseY = 2.5;

    p = MapUtilities.callAccessHexStaggering(staggerAxis, staggerIndex, tileLocation, s, t, r, jumpWidth, jumpHeight, mouseX, mouseY);
    assertEquals(-1, p.getX());
    assertEquals(tileLocation.getY() + 1, p.getY());

    staggerAxis = StaggerAxis.Y;
    p = MapUtilities.callAccessHexStaggering(staggerAxis, staggerIndex, tileLocation, s, t, r, jumpWidth, jumpHeight, mouseX, mouseY);
    assertEquals(tileLocation.getX() + 1, p.getX());
    assertEquals(-1, p.getY());
  }
}
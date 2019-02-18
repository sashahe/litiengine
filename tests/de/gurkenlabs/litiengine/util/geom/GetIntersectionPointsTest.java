package de.gurkenlabs.litiengine.util.geom;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.awt.geom.Dimension2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.geom.Line2D;

import java.util.List;

import org.junit.jupiter.api.Test;


public class GetIntersectionPointsTest {
  final Rectangle2D rect = new Rectangle2D.Double(0, 0, 1, 1);

  final Point2D aboveTop = new Point2D.Double(0.5, 1.2);
  final Point2D belowTop = new Point2D.Double(0.5, 0.8);

  final Point2D aboveBottom = new Point2D.Double(0.5, 0.2);
  final Point2D belowBottom = new Point2D.Double(0.5, -0.2);

  final Point2D leftOfLeft = new Point2D.Double(-0.2, 0.5);
  final Point2D rightOfLeft = new Point2D.Double(0.2, 0.5);

  final Point2D leftOfRight = new Point2D.Double(0.8, 0.5);
  final Point2D rightOfRight = new Point2D.Double(1.2, 0.5);

  // Test intersection with no points

  @Test
  public void testGetIntersectionPointsNone() {
    final Line2D line = new Line2D.Double(this.aboveTop, this.aboveTop);
    List<Point2D> points = GeometricUtilities.getIntersectionPoints(line, this.rect);
    assertEquals(points.size(), 0);
  }  

  // Test intersection with one point

  @Test
  public void testGetIntersectionPointsOnlyTop() {
    final Line2D line = new Line2D.Double(this.aboveTop, this.belowTop);
    List<Point2D> points = GeometricUtilities.getIntersectionPoints(line, this.rect);
    assertEquals(points.size(), 1);
    assertEquals(points.get(0).getX(), 0.5);
    assertEquals(points.get(0).getY(), 1);
  }

  @Test
  public void testGetIntersectionPointsOnlyBottom() {
    final Line2D line = new Line2D.Double(this.belowBottom, this.aboveBottom);
    List<Point2D> points = GeometricUtilities.getIntersectionPoints(line, this.rect);
    assertEquals(points.size(), 1);
    assertEquals(points.get(0).getX(), 0.5);
    assertEquals(points.get(0).getY(), 0);
  }

  @Test
  public void testGetIntersectionPointsOnlyLeft() {
    final Line2D line = new Line2D.Double(this.leftOfLeft, this.rightOfLeft);
    List<Point2D> points = GeometricUtilities.getIntersectionPoints(line, this.rect);
    assertEquals(points.size(), 1);
    assertEquals(points.get(0).getX(), 0);
    assertEquals(points.get(0).getY(), 0.5);
  }

  @Test
  public void testGetIntersectionPointsOnlyRight() {
    final Line2D line = new Line2D.Double(this.leftOfRight, this.rightOfRight);
    List<Point2D> points = GeometricUtilities.getIntersectionPoints(line, this.rect);
    assertEquals(points.size(), 1);
    assertEquals(points.get(0).getX(), 1);
    assertEquals(points.get(0).getY(), 0.5);
    }

  // Test intersection with two points

  @Test
  public void testGetIntersectionPointsTopRight() {
    final Line2D line = new Line2D.Double(aboveTop, rightOfRight);
    List<Point2D> points = GeometricUtilities.getIntersectionPoints(line, this.rect);
    assertEquals(points.size(), 2);
    }
    
  @Test
  public void testGetIntersectionPointsTopBottom() {
    final Line2D line = new Line2D.Double(aboveTop, belowBottom);
    List<Point2D> points = GeometricUtilities.getIntersectionPoints(line, this.rect);
    assertEquals(points.size(), 2);
    }

  @Test
  public void testGetIntersectionPointsTopLeft() {
    final Line2D line = new Line2D.Double(aboveTop, leftOfLeft);
    List<Point2D> points = GeometricUtilities.getIntersectionPoints(line, this.rect);
    assertEquals(points.size(), 2);
    }

  @Test
  public void testGetIntersectionPointsRightBottom() {
    final Line2D line = new Line2D.Double(belowBottom, rightOfRight);
    List<Point2D> points = GeometricUtilities.getIntersectionPoints(line, this.rect);
    assertEquals(points.size(), 2);
    }
    
  @Test
  public void testGetIntersectionPointsRightLeft() {
    final Line2D line = new Line2D.Double(leftOfLeft, rightOfRight);
    List<Point2D> points = GeometricUtilities.getIntersectionPoints(line, this.rect);
    assertEquals(points.size(), 2);
    }

  @Test
  public void testGetIntersectionBottomLeft() {
    final Line2D line = new Line2D.Double(leftOfLeft, belowBottom);
    List<Point2D> points = GeometricUtilities.getIntersectionPoints(line, this.rect);
    assertEquals(points.size(), 2);
    }

}
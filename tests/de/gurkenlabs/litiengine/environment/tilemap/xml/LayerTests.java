package de.gurkenlabs.litiengine.environment.tilemap.xml;

import javax.xml.bind.Unmarshaller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;

import de.gurkenlabs.litiengine.environment.tilemap.xml.ConcreteLayer;

public class LayerTests {
  private ConcreteLayer layer;
  private Object object;
  private Unmarshaller unmarshaller;

  @BeforeEach
  public void setUp() {
    this.layer = new ConcreteLayer();
    this.object = new Object();
    this.unmarshaller = mock(Unmarshaller.class);
  }

  /** 
   * Test the edge case when the object passed in is of type Map
  */
  @Test
  public void testParentMapisMapWhenObjectisMap() {
    Map map = new Map();

    this.layer.callAfterUnmarshal(this.unmarshaller, map);
    assertTrue(this.layer.getParentMap() instanceof Map);
  }

  /**
   * Tests for the 0 -> null functionallity, i.e. that it stores a value of null instead of zero.
   */
  @Test
  public void testOffsetXBecomesNullWhenZero() {
    this.layer.setOffsetX(0);
    this.layer.callAfterUnmarshal(this.unmarshaller, this.object);

    assertEquals(null, this.layer.getOffsetXRaw());
  }

  @Test
  public void testOffsetYBecomesNullWhenZero() {
    this.layer.setOffsetY(0);
    this.layer.callAfterUnmarshal(this.unmarshaller, this.object);

    assertEquals(null, this.layer.getOffsetYRaw());
  }

  @Test
  public void testWidthBecomesNullWhenZero() {
    this.layer.setWidth(0);
    this.layer.callAfterUnmarshal(this.unmarshaller, this.object);

    assertEquals(null, this.layer.getWidthRaw());
  }

  @Test
  public void testHeightBecomesNullWhenZero() {
    this.layer.setHeight(0);
    this.layer.callAfterUnmarshal(this.unmarshaller, object);

    assertEquals(null, this.layer.getHeightRaw());
  }

  /**
   * Test for the 1 -> null functionallity, i.e. that it stores a value of null instead of one.
   */

  @Test
  public void testOpacityBecomesNullWhenOne() {
    this.layer.setOpacity(1.0f);
    this.layer.callAfterUnmarshal(this.unmarshaller, object);

    assertEquals(null, this.layer.getOpacityRaw());
  }

  /**
   * Test for the true -> null functionallity, i.e. that it stores a value of null instead of true.
   */
  @Test
  public void testVisibleBecomesNullWhenTrue() {
    this.layer.setVisible(true);
    this.layer.callAfterUnmarshal(this.unmarshaller, object);

    assertEquals(null, this.layer.getIsVisibleRaw());
  }
}
package de.gurkenlabs.litiengine.abilities;

import static org.junit.jupiter.api.Assertions.*;

import de.gurkenlabs.litiengine.Align;
import org.junit.jupiter.api.Test;

import de.gurkenlabs.litiengine.abilities.effects.Effect;
import de.gurkenlabs.litiengine.abilities.effects.EffectTarget;
import de.gurkenlabs.litiengine.annotation.AbilityInfo;
import de.gurkenlabs.litiengine.entities.Creature;

import java.awt.*;
import java.awt.geom.Point2D;

public class AbilityTests {

  @Test
  public void testInitialization() {
    TestAbility ability = new TestAbility(new Creature());

    assertEquals("I do somethin", ability.getName());
    assertEquals("does somethin", ability.getDescription());
    assertEquals(CastType.ONCONFIRM, ability.getCastType());
    assertEquals(true, ability.isMultiTarget());

    assertEquals(333, ability.getAttributes().getCooldown().getCurrentValue().intValue());
    assertEquals(222, ability.getAttributes().getDuration().getCurrentValue().intValue());
    assertEquals(111, ability.getAttributes().getImpact().getCurrentValue().intValue());
    assertEquals(99, ability.getAttributes().getImpactAngle().getCurrentValue().intValue());
    assertEquals(444, ability.getAttributes().getRange().getCurrentValue().intValue());
    assertEquals(999, ability.getAttributes().getValue().getCurrentValue().intValue());
  }

  @Test
  public void testEffectInitialization() {
    Creature entity = new Creature();
    TestAbility ability = new TestAbility(new Creature());

    Effect effect = new TestEffect(ability, EffectTarget.ENEMY);
    assertEquals(ability.getAttributes().getDuration().getCurrentValue().intValue(), effect.getDuration());
    assertEquals(ability, effect.getAbility());
    assertEquals(0, effect.getFollowUpEffects().size());
    assertFalse(effect.isActive(entity));
    assertArrayEquals(new EffectTarget[] { EffectTarget.ENEMY }, effect.getEffectTargets());
  }

  /*
   * Test the function getOrigin() with both default and valid inputs.
   */
  @Test
  public void testGetOrigin() {
    Creature entity1 = new Creature();
    Creature entity2 = new Creature();
    Creature entity3 = new Creature();
    Creature entity4 = new Creature();
    Point2D point1 = new Point2D.Double(0,0);
    Point2D point2 = new Point2D.Double(1,1);
    Point2D point3 = new Point2D.Double(2,2);
    Point2D point4 = new Point2D.Double(26,26);
    Point2D point5 = new Point2D.Double(16, 16);
    Point2D point6 = new Point2D.Double(16,25.6);
    Point2D point7 = new Point2D.Double(42, 42);

    /*
     * If AbilityOrigin = LOCATION; (default) mapLocation = Point2D.Double(0,0)
     *    --> return Point2D.Double(0,0)
     */
    TestOriginLocation abilityLocation = new TestOriginLocation(entity1);
    assertEquals(point1, abilityLocation.getOrigin());

    /*
     * If AbilityOrigin = LOCATION; mapLocation = Point2D.Double(1,1)
     *    --> return Point2D.Double(1,1)
     */
    entity1.setLocation(point2);
    assertEquals(point2, entity1.getLocation());
    assertEquals(point2, abilityLocation.getOrigin());

    /*
     * If AbilityOrigin = CUSTOM; origin = null; (default) mapLocation =  Point2D.Double(0,0)
     *    --> return Point2D.Double(0,0)
     */
    TestOriginCustom abilityCustom = new TestOriginCustom(entity2);
    abilityCustom.setOrigin(null);
    assertEquals(point1, abilityCustom.getOrigin());

    /*
     * If AbilityOrigin = CUSTOM; origin = Point2D.Double(1,1); mapLocation =  Point2D.Double(1,1)
     *    --> return Point2D.Double(2,2).
     */
    entity2.setLocation(point2);
    assertEquals(point2, entity2.getLocation());

    abilityCustom.setOrigin(point2);
    assertEquals(point3, abilityCustom.getOrigin());

    /*
     * If AbilityOrigin = DIMENSION_CENTER; (default) mapLocation =  Point2D.Double(0,0); (default) height = 32; (default) width = 32
     *    --> return Point2D.Double(16,16)
     */
    TestOriginDimension abilityDimension = new TestOriginDimension(entity3);
    assertEquals(point5, abilityDimension.getOrigin());

    /*
     * If AbilityOrigin = DIMENSION_CENTER; mapLocation = Point2D.Double(1,1); height = 50; width = 50
     *    --> return Point2D.Double(26, 26)
     */
    entity3.setLocation(point2);
    assertEquals(point2, entity3.getLocation());

    entity3.setHeight(50);
    assertEquals(50, entity3.getHeight());

    entity3.setWidth(50);
    assertEquals(50, entity3.getWidth());

    assertEquals(point4, abilityDimension.getOrigin());

    /*
     * Test with default values:
     * If AbilityOrigin = COLLISIONBOX_CENTER; mapLocation = (default) Point2D.Double(0,0); (default) height = 32; (default) width = 32;
     * (default) Valign = DOWN; (default) Align = CENTER; (default) collisionBoxHeight = -1; (default) collisionBoxwidth = -1
     *    --> return Point2D(16, 25.6)
     */
    TestOriginCollisionBox abilityCollision = new TestOriginCollisionBox(entity4);
    assertEquals(point6, abilityCollision.getOrigin());

    /*
     * If AbilityOrigin = COLLISIONBOX_CENTER; mapLocation = Point2D.Double(2,2); height = 50; width = 50;
     * Valign = DOWN; Align = RIGHT; collisionBoxHeight = -1; collisionBoxwidth = -1
     *    --> return Point2D(42, 42)
     */
    entity4.setWidth(50);
    assertEquals(50, entity4.getWidth());

    entity4.setHeight(50);
    assertEquals(50, entity4.getHeight());

    entity4.setLocation(point3);
    assertEquals(point3, entity4.getLocation());

    entity4.setCollisionBoxAlign(Align.RIGHT);
    assertEquals(entity4.getCollisionBoxAlign(), Align.RIGHT);

    assertEquals(point7, abilityCollision.getOrigin());

  }

  @AbilityInfo(castType = CastType.ONCONFIRM, name = "I do somethin", description = "does somethin", cooldown = 333, duration = 222, impact = 111, impactAngle = 99, multiTarget = true, origin = AbilityOrigin.COLLISIONBOX_CENTER, range = 444, value = 999)
  private class TestAbility extends Ability {

    protected TestAbility(Creature executor) {
      super(executor);
    }
  }

  private class TestEffect extends Effect {
    protected TestEffect(Ability ability, EffectTarget... targets) {
      super(ability, targets);
    }
  }

  @AbilityInfo(origin = AbilityOrigin.LOCATION)
  private class TestOriginLocation extends Ability {
    protected TestOriginLocation(Creature executor) {
      super(executor);
    }
  }

  @AbilityInfo(origin = AbilityOrigin.CUSTOM)
  private class TestOriginCustom extends Ability {
    protected TestOriginCustom(Creature executor) {
      super(executor);
    }
  }

  @AbilityInfo(origin = AbilityOrigin.COLLISIONBOX_CENTER)
  private class TestOriginCollisionBox extends Ability {
    protected TestOriginCollisionBox(Creature executor) {
      super(executor);
    }
  }

  @AbilityInfo(origin = AbilityOrigin.DIMENSION_CENTER)
  private class TestOriginDimension extends  Ability {
    protected TestOriginDimension(Creature executor) {
      super(executor);
    }
  }

}

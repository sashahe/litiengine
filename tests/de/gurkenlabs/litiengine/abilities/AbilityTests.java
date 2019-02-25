package de.gurkenlabs.litiengine.abilities;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.Test;

import de.gurkenlabs.litiengine.abilities.effects.Effect;
import de.gurkenlabs.litiengine.abilities.effects.EffectTarget;
import de.gurkenlabs.litiengine.annotation.AbilityInfo;
import de.gurkenlabs.litiengine.entities.Creature;
import org.mockito.Mock;

import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

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
    Point2D point1 = new Point2D.Double(0,0);
    Point2D point2 = new Point2D.Double(1,1);
    Point2D point3 = new Point2D.Double(16, 16);
    Point2D point4 = new Point2D.Double(16,25.6);
    Point2D point5 = new Point2D.Double(2,2);
    Rectangle2D shape1 = new Rectangle2D.Double(9.6, 19.2, 12.8, 12.8);

    /*
     * If AbilityOrigin = LOCATION; (default) mapLocation = Point2D.Double(0,0)
     *    --> return Point2D.Double(0,0)
     */

    Creature entity1 = mock(Creature.class);
    when(entity1.getLocation()).thenReturn(point1);

    TestOriginLocation abilityLocation = new TestOriginLocation(entity1);
    assertEquals(point1, abilityLocation.getOrigin());

    /*
     * If AbilityOrigin = LOCATION; mapLocation = Point2D.Double(1,1)
     *    --> return Point2D.Double(1,1)
     */
    when(entity1.getLocation()).thenReturn(point2);
    assertEquals(point2, abilityLocation.getOrigin());

    /*
     * If AbilityOrigin = CUSTOM; origin = null; (default) mapLocation = Point2D.Double(0,0)
     *    --> return Point2D.Double(0,0)
     */
    Creature entity2 = mock(Creature.class);
    when(entity2.getLocation()).thenReturn(point1);
    when(entity2.getX()).thenReturn((double) 1);
    when(entity2.getY()).thenReturn((double) 1);

    TestOriginCustom abilityCustom = new TestOriginCustom(entity2);
    assertEquals(point1, abilityCustom.getOrigin());

    /*
     * If AbilityOrigin = CUSTOM; origin = Point2D.Double(1,1); mapLocation = Point2D.Double(1,1)
     *    --> return Point2D.Double(2,2).
     */
    abilityCustom.setOrigin(point2);
    assertEquals(point5, abilityCustom.getOrigin());

    /*
     * If AbilityOrigin = DIMENSION_CENTER; (default) mapLocation =  Point2D.Double(0,0); (default) height = 32; (default) width = 32
     *    --> return Point2D.Double(16,16)
     */

    Creature entity3 = mock(Creature.class);
    when(entity3.getCenter()).thenReturn(point3);

    TestOriginDimension abilityDimension = new TestOriginDimension(entity3);
    assertEquals(point3, abilityDimension.getOrigin());

    /*
     * If AbilityOrigin = COLLISIONBOX_CENTER; (default) mapLocation = Point2D.Double(0,0); (default) height = 32; (default) width = 32;
     * (default) Valign = DOWN; (default) Align = CENTER; (default) collisionBoxHeight = -1; (default) collisionBoxwidth = -1
     *    --> return Point2D(16, 25.6)
     */

    Creature entity4 = mock(Creature.class);
    when(entity4.getCollisionBox()).thenReturn(shape1);

    TestOriginCollisionBox abilityCollision = new TestOriginCollisionBox(entity4);
    assertEquals(point4, abilityCollision.getOrigin());

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

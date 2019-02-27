package de.gurkenlabs.litiengine.abilities;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import java.awt.Shape;
import java.awt.geom.Arc2D;
import java.awt.geom.Ellipse2D;

import de.gurkenlabs.litiengine.abilities.effects.Effect;
import de.gurkenlabs.litiengine.abilities.effects.EffectTarget;
import de.gurkenlabs.litiengine.annotation.AbilityInfo;
import de.gurkenlabs.litiengine.entities.Creature;

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

  @AbilityInfo(impact = 150, impactAngle = 360, origin = AbilityOrigin.DIMENSION_CENTER, range = 100)
  private class TestAbility360 extends Ability {

    protected TestAbility360(Creature executor) {
      super(executor);
    }
  }
  /*
   * Impact Angle = 360
   * Range 100
   * Executor Angle = 90 (EAST)
   *
   * Expect Impact Area to be a circle with diameter 150 and origin at (16+range/2, 16) = (66, 16)
   * i.e. the bounding rectangle should have its upper left corner at (-9.0, -59.0) and sides of length 150
   */
  @Test
  public void testInternalCalculateImpactArea360() {
    TestAbility360 ability = new TestAbility360(new Creature());
    Shape s = ability.internalCalculateImpactArea(90);
    Ellipse2D e = (Ellipse2D) s;
    assertTrue(e instanceof Ellipse2D);
    assertEquals(-9.0, e.getX(), 0.001);
    assertEquals(-59.0, e.getY(), 0.001);
    assertEquals(150, e.getHeight(), 0.001);
    assertEquals(150, e.getWidth(), 0.001);

  }

  @AbilityInfo(impact = 111, impactAngle = 180, origin = AbilityOrigin.DIMENSION_CENTER, range = 150)
  private class TestAbility180 extends Ability {

    protected TestAbility180(Creature executor) {
      super(executor);
    }
  }
  /*
   * Impact Angle = 180
   * Range = 150
   * Executor Angle = 0 (NORTH)
   *
   * Expect Impact Area to be a 180 deg arc with its origin at (16, 16+range/2) = (16, 91)
   * i.e. the bounding rectangle should have its upper left corner at (-39.5, 35.5) and sides of length 111
   */
  @Test
  public void testInternalCalculateImpactArea180() {
    TestAbility180 ability = new TestAbility180(new Creature());
    Shape s = ability.internalCalculateImpactArea(0);
    Arc2D a = (Arc2D) s;
    assertTrue(a instanceof Arc2D);
    assertEquals(-39.5, a.getX(), 0.001);
    assertEquals(35.5, a.getY(), 0.001);
    assertEquals(111, a.getHeight(), 0.001);
    assertEquals(111, a.getWidth(), 0.001);
    assertEquals(-90, a.getAngleStart(), 0.001);
    assertEquals(180, a.getAngleExtent(), 0.001);
  }

  /*
   * Executor angle = 0
   * Expect same shape from the public and the internal method
   */
  @Test
  public void testCalculateImpactArea() {
    Creature c = new Creature();
    TestAbility360 ability = new TestAbility360(c);
    Ellipse2D ePub = (Ellipse2D) ability.calculateImpactArea();
    Ellipse2D eInt = (Ellipse2D) ability.internalCalculateImpactArea(0);
    assertEquals(ePub.getX(), eInt.getX(), 0.001);
    assertEquals(ePub.getY(), eInt.getY(), 0.001);
    assertEquals(ePub.getWidth(), eInt.getWidth(), 0.001);
    assertEquals(ePub.getHeight(), eInt.getHeight(), 0.001);
  }

  /*
   * Executor angle = 45
   * Expect same shape from the public and the internal method
   */
  @Test
  public void testCalculateImpactArea45() {
    Creature c = new Creature();
    c.setAngle(45);
    TestAbility360 ability = new TestAbility360(c);
    Ellipse2D ePub = (Ellipse2D) ability.calculateImpactArea();
    Ellipse2D eInt = (Ellipse2D) ability.internalCalculateImpactArea(45);
    assertEquals(ePub.getX(), eInt.getX(), 0.001);
    assertEquals(ePub.getY(), eInt.getY(), 0.001);
    assertEquals(ePub.getWidth(), eInt.getWidth(), 0.001);
    assertEquals(ePub.getHeight(), eInt.getHeight(), 0.001);
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
}

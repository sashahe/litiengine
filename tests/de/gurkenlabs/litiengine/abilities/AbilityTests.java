package de.gurkenlabs.litiengine.abilities;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

import static org.mockito.Mockito.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;

import org.junit.jupiter.api.Test;

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

  @AbilityInfo(castType = CastType.ONCONFIRM, name = "I do somethin", description = "does somethin", cooldown = 333, duration = 222, impact = 111, impactAngle = 99, multiTarget = true, origin = AbilityOrigin.COLLISIONBOX_CENTER, range = 444, value = 999)
  private class TestAbility extends Ability {

    protected TestAbility(Creature executor) {
      super(executor);
    }
  }

  @AbilityInfo(castType = CastType.ONCONFIRM, name = "I do somethin", description = "does somethin", cooldown = 333, duration = 222, impact = 0, impactAngle = 99, multiTarget = true, origin = AbilityOrigin.COLLISIONBOX_CENTER, range = 444, value = 999)
  private class TestAbilityNoImpact extends Ability {

    protected TestAbilityNoImpact(Creature executor) {
      super(executor);
    }
  }

  private class TestEffect extends Effect {
    protected TestEffect(Ability ability, EffectTarget... targets) {
      super(ability, targets);
    }
  }

  /**
  * Test getPotentialCollisionBox when the collision box of the entity is the zero rectangle
  * and the impact of the of the ability is zero.
  * 
  * Expected potential impact area is an ellipse with a center in the origin 
  * and a zero width and height.
  */
  @Test
  public void testGetPotentialCollisionZeroBoxZeroImpact() {
    Rectangle2D collisonBox = new Rectangle2D.Double(0,0,0,0);
    Ellipse2D ellipse = new Ellipse2D.Double(0,0,0,0);

    Creature entity = mock(Creature.class);
    when(entity.getCollisionBox()).thenReturn(collisonBox);

    TestAbilityNoImpact ability = new TestAbilityNoImpact(entity);
    assertEquals(ellipse, ability.calculatePotentialImpactArea());
  }

  /**
  * Test getPotentialCollisionBox when the collision box of the entity is non-zero
  * and the impact of the of the ability is zero.
  * 
  * Expected potential impact area is an ellipse with a center 
  * corresponding to the collisionbox and a zero width and height.
  */
  @Test
  public void testGetPotentialCollisionBoxNonZeroBoxZeroImpact() {
    Rectangle2D collisonBox = new Rectangle2D.Double(1,1,1,1);
    Ellipse2D ellipse = new Ellipse2D.Double(collisonBox.getCenterX(),collisonBox.getCenterY(),0,0);

    Creature entity = mock(Creature.class);
    when(entity.getCollisionBox()).thenReturn(collisonBox);

    TestAbilityNoImpact ability = new TestAbilityNoImpact(entity);
    assertEquals(ellipse, ability.calculatePotentialImpactArea());
  }

  /**
  * Test getPotentialCollisionBox when the collision box of the entity is the zero rectangle,
  * and the impact of the of the ability is non-zero.
  * 
  * Expected potential impact area is an ellipse with a center 
  * shifted by half of the negative impact from the origin,
  * and a width and height corresponding to the impact.
  */
  @Test
  public void testGetPotentialCollisionBoxZeroBoxNonZeroImpact() {
    Rectangle2D collisonBox = new Rectangle2D.Double(0,0,0,0);
    Ellipse2D ellipse = new Ellipse2D.Double(0-111*0.5, 0-111*0.5, 111, 111);

    Creature entity = mock(Creature.class);
    when(entity.getCollisionBox()).thenReturn(collisonBox);

    TestAbility ability = new TestAbility(entity);
    assertEquals(ellipse, ability.calculatePotentialImpactArea());
  }

  /**
  * Test getPotentialCollisionBox when the collision box of the entity is non-zero,
  * and the impact of the of the ability is non-zero.
  * 
  * Expected potential impact area is an ellipse with a center 
  * corresponding to the collisionbox shifted by half of the negative impact,
  * and a width and height corresponding to the impact.
  */
  @Test
  public void testGetPotentialCollisionBoxNonZeroBoxNonZeroImpact() {
    Rectangle2D collisonBox = new Rectangle2D.Double(1,1,0,0);
    Ellipse2D ellipse = new Ellipse2D.Double(1-111*0.5, 1-111*0.5, 111, 111);

    Creature entity = mock(Creature.class);
    when(entity.getCollisionBox()).thenReturn(collisonBox);

    TestAbility ability = new TestAbility(entity);
    assertEquals(ellipse, ability.calculatePotentialImpactArea());
  }
}


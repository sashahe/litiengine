package de.gurkenlabs.litiengine.abilities;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

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

  private class TestEffect extends Effect {
    protected TestEffect(Ability ability, EffectTarget... targets) {
      super(ability, targets);
    }
  }

  /**
  * If the executor is dead it is not possible to cast
  */
  @Test
  public void testCanCastWhenDead() {
    Creature entity = mock(Creature.class);
    when(entity.isDead()).thenReturn(true);

    TestAbility a = new TestAbility(entity);
    assertEquals(a.getExecutor(), entity);
    assertFalse(!a.getExecutor().isDead());
    assertFalse(a.canCast());
  }

  /**
  * If the executor is alive, and
  * If the ability has no current execution it is possible to cast
  */
  @Test
  public void testCanCastWhenNoExecution() {
    Creature entity = mock(Creature.class);
    when(entity.isDead()).thenReturn(false);

    TestAbility a = new TestAbility(entity);
    a.setCurrentExecution(null);

    assertEquals(a.getExecutor(), entity);
    assertTrue(!a.getExecutor().isDead());
    assertTrue(a.getCurrentExecution() == null);
    assertTrue(a.canCast());
  }

  /**
  * If the executor is alive, and
  * If the execution has no execution ticks left it is possible to cast
  */
  @Test
  public void testCanCastWhenNoExecutionticks() {
    Creature entity = mock(Creature.class);
    when(entity.isDead()).thenReturn(false);

    AbilityExecution ae = mock(AbilityExecution.class);
    when(ae.getExecutionTicks()).thenReturn(0l);

    TestAbility a = new TestAbility(entity);
    a.setCurrentExecution(ae);

    assertEquals(a.getExecutor(), entity);
    assertTrue(!a.getExecutor().isDead());
    assertFalse(a.getCurrentExecution() == null);
    assertTrue(a.getCurrentExecution().getExecutionTicks() == 0);
    assertTrue(a.canCast());
  }
}
;
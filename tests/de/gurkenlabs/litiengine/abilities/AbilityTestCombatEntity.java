package de.gurkenlabs.litiengine.abilities;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.*;

import java.awt.geom.Ellipse2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

import de.gurkenlabs.litiengine.Game;
import de.gurkenlabs.litiengine.abilities.effects.Effect;
import de.gurkenlabs.litiengine.abilities.effects.EffectTarget;
import de.gurkenlabs.litiengine.annotation.AbilityInfo;
import de.gurkenlabs.litiengine.entities.CombatEntity;

public class AbilityTestCombatEntity {
  @Test
  public void testGetRemainingCooldownInSeconds() {
    Game.init();
    CombatEntity CombatEntity = mock(CombatEntity.class);
    TestAbility ability = new TestAbility(CombatEntity);
    float actual = ability.getRemainingCooldownInSeconds(Game.loop());
    assertEquals(0, actual);
  }

  @Test
  public void testGetRemainingCooldownInSecondsNoCast() {
    Game.init();
    CombatEntity CombatEntity = mock(CombatEntity.class);
    TestAbility ability = new TestAbility(CombatEntity);
    float actual = ability.getRemainingCooldownInSeconds(Game.loop());
    assertEquals(0, actual);
  }

  @Test
  public void testGetRemainingCooldownInSecondsCombatEntityIsDead() {
    Game.init();
    CombatEntity CombatEntity = mock(CombatEntity.class);
    when(CombatEntity.isDead()).thenReturn(true);
    TestAbility ability = new TestAbility(CombatEntity);
    ability.cast();
    float actual = ability.getRemainingCooldownInSeconds(Game.loop());
    assertEquals(0, actual);
  }

  @Test
  public void testInitialization() {
    TestAbility ability = new TestAbility(new CombatEntity());

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
    CombatEntity entity = new CombatEntity();
    TestAbility ability = new TestAbility(new CombatEntity());

    Effect effect = new TestEffect(ability, EffectTarget.ENEMY);
    assertEquals(ability.getAttributes().getDuration().getCurrentValue().intValue(), effect.getDuration());
    assertEquals(ability, effect.getAbility());
    assertEquals(0, effect.getFollowUpEffects().size());
    assertFalse(effect.isActive(entity));
    assertArrayEquals(new EffectTarget[] { EffectTarget.ENEMY }, effect.getEffectTargets());
  }

  /*
   * Test the function getOrigin() with valid inputs when AbilityOrigin = LOCATION.
   */
  @Test
  public void testGetOriginLocation() {
    Point2D point1 = new Point2D.Double(0, 0);
    Point2D point2 = new Point2D.Double(1, 1);

    /*
     * If AbilityOrigin = LOCATION; (default) mapLocation = Point2D.Double(0,0)
     * --> return Point2D.Double(0,0)
     */
    CombatEntity entity = mock(CombatEntity.class);
    when(entity.getLocation()).thenReturn(point1);

    TestOriginLocation abilityLocation = new TestOriginLocation(entity);
    assertEquals(point1, abilityLocation.getOrigin());

    /*
     * If AbilityOrigin = LOCATION; mapLocation = Point2D.Double(1,1)
     * --> return Point2D.Double(1,1)
     */
    when(entity.getLocation()).thenReturn(point2);
    assertEquals(point2, abilityLocation.getOrigin());
  }

  /*
   * Test the function getOrigin() with valid inputs when AbilityOrigin = CUSTOM.
   */
  @Test
  public void testGetOriginCustom() {
    Point2D point1 = new Point2D.Double(0, 0);
    Point2D point2 = new Point2D.Double(1, 1);
    Point2D point3 = new Point2D.Double(2, 2);

    /*
     * If AbilityOrigin = CUSTOM; origin = null; (default) mapLocation = Point2D.Double(0,0)
     * --> return Point2D.Double(0,0)
     */
    CombatEntity entity = mock(CombatEntity.class);
    when(entity.getLocation()).thenReturn(point1);
    when(entity.getX()).thenReturn((double) 1);
    when(entity.getY()).thenReturn((double) 1);

    TestOriginCustom abilityCustom = new TestOriginCustom(entity);
    assertEquals(point1, abilityCustom.getOrigin());

    /*
     * If AbilityOrigin = CUSTOM; origin = Point2D.Double(1,1); mapLocation = Point2D.Double(1,1)
     * --> return Point2D.Double(2,2).
     */
    abilityCustom.setOrigin(point2);
    assertEquals(point3, abilityCustom.getOrigin());
  }

  /*
   * Test the function getOrigin() with valid inputs when AbilityOrigin = DIMENSION_CENTER.
   */
  @Test
  public void testGetOriginDimension() {
    Point2D point1 = new Point2D.Double(16, 16);

    /*
     * If AbilityOrigin = DIMENSION_CENTER; (default) mapLocation = Point2D.Double(0,0); (default) height = 32; (default) width = 32
     * --> return Point2D.Double(16,16)
     */

    CombatEntity entity = mock(CombatEntity.class);
    when(entity.getCenter()).thenReturn(point1);

    TestOriginDimension abilityDimension = new TestOriginDimension(entity);
    assertEquals(point1, abilityDimension.getOrigin());
  }

  /*
   * Test the function getOrigin() with valid inputs when AbilityOrigin = COLLISIONBOX_CENTER.
   */

  @Test
  public void testGetOriginCollisionBox() {
    Point2D point1 = new Point2D.Double(16, 25.6);
    Rectangle2D shape1 = new Rectangle2D.Double(9.6, 19.2, 12.8, 12.8);

    /*
     * If AbilityOrigin = COLLISIONBOX_CENTER; (default) mapLocation = Point2D.Double(0,0); (default) height = 32; (default) width = 32;
     * (default) Valign = DOWN; (default) Align = CENTER; (default) collisionBoxHeight = -1; (default) collisionBoxwidth = -1
     * --> return Point2D(16, 25.6)
     */

    CombatEntity entity = mock(CombatEntity.class);
    when(entity.getCollisionBox()).thenReturn(shape1);

    TestOriginCollisionBox abilityCollision = new TestOriginCollisionBox(entity);
    assertEquals(point1, abilityCollision.getOrigin());

  }

  @AbilityInfo(castType = CastType.ONCONFIRM, name = "I do somethin", description = "does somethin", cooldown = 333, duration = 222, impact = 111, impactAngle = 99, multiTarget = true, origin = AbilityOrigin.COLLISIONBOX_CENTER, range = 444, value = 999)
  private class TestAbility extends Ability {

    protected TestAbility(CombatEntity executor) {
      super(executor);
    }
  }

  @AbilityInfo(impact = 0)
  private class TestAbilityNoImpact extends Ability {

    protected TestAbilityNoImpact(CombatEntity executor) {
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
    protected TestOriginLocation(CombatEntity executor) {
      super(executor);
    }
  }

  @AbilityInfo(origin = AbilityOrigin.CUSTOM)
  private class TestOriginCustom extends Ability {
    protected TestOriginCustom(CombatEntity executor) {
      super(executor);
    }
  }

  @AbilityInfo(origin = AbilityOrigin.COLLISIONBOX_CENTER)
  private class TestOriginCollisionBox extends Ability {
    protected TestOriginCollisionBox(CombatEntity executor) {
      super(executor);
    }
  }

  @AbilityInfo(origin = AbilityOrigin.DIMENSION_CENTER)
  private class TestOriginDimension extends Ability {
    protected TestOriginDimension(CombatEntity executor) {
      super(executor);
    }
  }

  /**
   * Test getPotentialCollisionBox when the collision box of the entity is the zero rectangle
   * and the impact of the of the ability is zero.
   * 
   * Expected: potentialImpactArea() is an ellipse with a center in the origin
   * and a zero width and height.
   */
  @Test
  public void testGetPotentialCollisionZeroBoxZeroImpact() {
    Rectangle2D collisonBox = new Rectangle2D.Double(0, 0, 0, 0);
    Ellipse2D ellipse = new Ellipse2D.Double(0, 0, 0, 0);

    CombatEntity entity = mock(CombatEntity.class);
    when(entity.getCollisionBox()).thenReturn(collisonBox);

    TestAbilityNoImpact ability = new TestAbilityNoImpact(entity);
    assertEquals(ellipse, ability.calculatePotentialImpactArea());
  }

  /**
   * Test getPotentialCollisionBox when the collision box of the entity is non-zero
   * and the impact of the of the ability is zero.
   * 
   * Expected: potentialImpactArea() is an ellipse with a center
   * corresponding to the collisionbox and a zero width and height.
   */
  @Test
  public void testGetPotentialCollisionBoxNonZeroBoxZeroImpact() {
    Rectangle2D collisonBox = new Rectangle2D.Double(1, 1, 1, 1);
    Ellipse2D ellipse = new Ellipse2D.Double(collisonBox.getCenterX(), collisonBox.getCenterY(), 0, 0);

    CombatEntity entity = mock(CombatEntity.class);
    when(entity.getCollisionBox()).thenReturn(collisonBox);

    TestAbilityNoImpact ability = new TestAbilityNoImpact(entity);
    assertEquals(ellipse, ability.calculatePotentialImpactArea());
  }

  /**
   * Test getPotentialCollisionBox when the collision box of the entity is the zero rectangle,
   * and the impact of the of the ability is non-zero.
   * 
   * Expected: potentialImpactArea() is an ellipse with a center
   * shifted by half of the negative impact from the origin,
   * and a width and height corresponding to the impact.
   */
  @Test
  public void testGetPotentialCollisionBoxZeroBoxNonZeroImpact() {
    Rectangle2D collisonBox = new Rectangle2D.Double(0, 0, 0, 0);
    Ellipse2D ellipse = new Ellipse2D.Double(0 - 111 * 0.5, 0 - 111 * 0.5, 111, 111);

    CombatEntity entity = mock(CombatEntity.class);
    when(entity.getCollisionBox()).thenReturn(collisonBox);

    TestAbility ability = new TestAbility(entity);
    assertEquals(ellipse, ability.calculatePotentialImpactArea());
  }

  /**
   * Test getPotentialCollisionBox when the collision box of the entity is non-zero,
   * and the impact of the of the ability is non-zero.
   * 
   * Expected: potentialImpactArea() is an ellipse with a center
   * corresponding to the collisionbox shifted by half of the negative impact,
   * and a width and height corresponding to the impact.
   */
  @Test
  public void testGetPotentialCollisionBoxNonZeroBoxNonZeroImpact() {
    Rectangle2D collisonBox = new Rectangle2D.Double(1, 1, 0, 0);
    Ellipse2D ellipse = new Ellipse2D.Double(1 - 111 * 0.5, 1 - 111 * 0.5, 111, 111);

    CombatEntity entity = mock(CombatEntity.class);
    when(entity.getCollisionBox()).thenReturn(collisonBox);

    TestAbility ability = new TestAbility(entity);
    assertEquals(ellipse, ability.calculatePotentialImpactArea());
  }

  /**
   * If the executor is dead it is not possible to cast
   * Expected: canCast() is false.
   */
  @Test
  public void testCanCastWhenDead() {
    CombatEntity entity = mock(CombatEntity.class);
    when(entity.isDead()).thenReturn(true);

    TestAbility a = new TestAbility(entity);
    assertEquals(a.getExecutor(), entity);
    assertFalse(!a.getExecutor().isDead());
    assertFalse(a.canCast());
  }

  /**
   * If the executor is alive and the ability has no current execution,
   * it is possible to cast.
   * Expected: canCast() is true.
   */
  @Test
  public void testCanCastWhenNoExecution() {
    CombatEntity entity = mock(CombatEntity.class);
    when(entity.isDead()).thenReturn(false);

    TestAbility a = new TestAbility(entity);
    a.setCurrentExecution(null);

    assertEquals(a.getExecutor(), entity);
    assertTrue(!a.getExecutor().isDead());
    assertTrue(a.getCurrentExecution() == null);
    assertTrue(a.canCast());
  }

  /**
   * If the executor is alive and the execution has no execution ticks left,
   * it is possible to cast.
   * Expected: canCast() is true.
   */
  @Test
  public void testCanCastWhenNoExecutionticks() {
    CombatEntity entity = mock(CombatEntity.class);
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
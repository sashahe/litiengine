package de.gurkenlabs.litiengine.environment;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.Collection;
import de.gurkenlabs.litiengine.util.io.CSV;

import de.gurkenlabs.litiengine.entities.ICollisionEntity;
import de.gurkenlabs.litiengine.entities.IEntity;
import de.gurkenlabs.litiengine.environment.tilemap.IMapObject;

public final class CustomMapObjectLoader extends MapObjectLoader {
  private final ConstructorInvocation invoke;

  @FunctionalInterface
  private interface ConstructorInvocation {
    IEntity invoke(Environment environment, IMapObject mapObject) throws InvocationTargetException, IllegalAccessException, InstantiationException;
  }

  protected CustomMapObjectLoader(String mapObjectType, Class<? extends IEntity> entityType) {
    super(mapObjectType);
    int numberOfBranches = 19;
    int branches[] = new int [numberOfBranches];
    branches[0] = 1;
    if (entityType.isInterface() || Modifier.isAbstract(entityType.getModifiers())) {
      branches[1] = 1;
      throw new IllegalArgumentException("cannot create loader for interface or abstract class");
    } else {
      branches[2] = 1;
    }

    ConstructorInvocation inv = null;
    Constructor<?>[] constructors = entityType.getConstructors();
    int priority = 0; // env+mo, mo+env, mo, env, nullary
    for (int i = 0; i < constructors.length; i++) {
      branches[3] = 1;
      final Constructor<?> constructor = constructors[i];
      Class<?>[] classes = constructor.getParameterTypes();
      if (classes.length == 2) {
        branches[4] = 1;
        if (classes[0] == Environment.class && classes[1] == IMapObject.class) {
          branches[5] = 1;
          inv = (e, o) -> (IEntity) constructor.newInstance(e, o);
          break; // exit early because we've already found the highest priority constructor
        } else if (classes[0] == IMapObject.class && classes[1] == Environment.class) {
          branches[6] = 1;
          inv = (e, o) -> (IEntity) constructor.newInstance(o, e);
          priority = 3;
        } else {
          branches[7] = 1;
        }
      } else if (classes.length == 1) {
        branches[8] = 1;
        if (priority < 3) {
          branches[9] = 1;
          if (classes[0] == IMapObject.class) {
            branches[10] = 1;
            inv = (e, o) -> (IEntity) constructor.newInstance(o);
            priority = 2;
          } else if (priority < 2 && classes[0] == Environment.class) {
            branches[11] = 1;
            inv = (e, o) -> (IEntity) constructor.newInstance(e);
            priority = 1;
          } else {
            branches[12] = 1;
          }
        } else {
          branches[13] = 1;
        }
      } else if (classes.length == 0 && priority < 1) {
        branches[14] = 1;
        inv = (e, o) -> (IEntity) constructor.newInstance();
        // priority is already 0
      } else {
        branches[15] = 1;
      }
    }
    branches[16] = 1;

    if (inv == null) {
      branches[17] = 1;
      throw new IllegalArgumentException("could not find suitable constructor");
    } else {
      branches[18] = 1;
    }

    this.invoke = inv;
    try {
      CSV.write(branches, 6);
    } catch (Exception e) {
      System.err.println("Error: " + e);
    }
  }

  @Override
  public Collection<IEntity> load(Environment environment, IMapObject mapObject) throws MapObjectException {
    IEntity entity;
    try {
      entity = invoke.invoke(environment, mapObject);
    } catch (ReflectiveOperationException e) {
      throw new MapObjectException(e);
    }

    loadDefaultProperties(entity, mapObject);
    if (entity instanceof ICollisionEntity)
      loadCollisionProperties((ICollisionEntity) entity, mapObject);
    return Arrays.asList(entity);
  }
}

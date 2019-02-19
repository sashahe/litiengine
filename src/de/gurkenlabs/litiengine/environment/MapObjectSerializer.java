package de.gurkenlabs.litiengine.environment;

import java.lang.reflect.Field;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import de.gurkenlabs.litiengine.entities.IEntity;
import de.gurkenlabs.litiengine.environment.tilemap.IMapObject;
import de.gurkenlabs.litiengine.environment.tilemap.TmxProperty;
import de.gurkenlabs.litiengine.environment.tilemap.xml.DecimalFloatAdapter;
import de.gurkenlabs.litiengine.environment.tilemap.xml.MapObject;
import de.gurkenlabs.litiengine.util.ArrayUtilities;
import de.gurkenlabs.litiengine.util.io.CSV;

public final class MapObjectSerializer {
  private static final Logger log = Logger.getLogger(MapObjectSerializer.class.getName());

  private MapObjectSerializer() {
  }

  public static MapObject serialize(IEntity entity) {
    MapObject obj = new MapObject();
    obj.setId(entity.getMapId());
    obj.setX((float) entity.getX());
    obj.setY((float) entity.getY());
    obj.setWidth(entity.getWidth());
    obj.setHeight(entity.getHeight());
    obj.setName(entity.getName());

    serialize(entity.getClass(), entity, obj);
    return obj;
  }

  private static <T extends IEntity> void serialize(Class<?> clz, T entity, MapObject mapObject) {

    for (final Field field : clz.getDeclaredFields()) {
      serialize(field, entity, mapObject);
    }

    // recursively call all parent classes and serialize annotated fields
    Class<?> parentClass = clz.getSuperclass();
    if (parentClass != null) {
      serialize(parentClass, entity, mapObject);
    }
  }

  private static void serialize(Field field, Object entity, IMapObject mapObject) {
    TmxProperty property = field.getAnnotation(TmxProperty.class);
    if (property == null) {
      return;
    }

    Object value;
    try {
      if (!field.isAccessible()) {
        field.setAccessible(true);
      }

      value = field.get(entity);
      if (value == null) {
        return;
      }

      mapObject.setValue(property.name(), getPropertyValue(field, value));
    } catch (IllegalAccessException e) {
      log.log(Level.SEVERE, e.getMessage(), e);
    }
  }

  /**
   * Returns String representation of an object. Handles array of Objects as well
   * 
   * @param field
   *                for determining what kind of primitive or ojective type the Object is.
   * @param value
   *                Object, can be primitive or Objecte types.
   * 
   */
  public static String getPropertyValue(Field field, Object value) {
    int numberOfBranches = 21;
    int branches[] = new int[numberOfBranches];

    branches[0] = 1;

    if (field.getType().equals(float.class) || field.getType().equals(double.class)) {
      branches[1] = 1;
      try {
        branches[2] = 1;
        writeBranchesToCSV(branches);
        return new DecimalFloatAdapter().marshal((Float) value);
      } catch (Exception e) {
        branches[3] = 1;
        writeBranchesToCSV(branches);
        log.log(Level.SEVERE, e.getMessage(), e);
      }
    } else if (field.getType().equals(int.class)) {
      branches[4] = 1;
      writeBranchesToCSV(branches);
      return Integer.toString((int) value);
    } else if (field.getType().equals(short.class)) {
      branches[5] = 1;
      writeBranchesToCSV(branches);
      return Short.toString((short) value);
    } else if (field.getType().equals(byte.class)) {
      branches[6] = 1;
      writeBranchesToCSV(branches);
      return Byte.toString((byte) value);
    } else if (field.getType().equals(long.class)) {
      branches[7] = 1;
      writeBranchesToCSV(branches);
      return Long.toString((long) value);
    } else {
      branches[8] = 1;
    }

    if (value instanceof List<?>) {
      branches[8] = 1;
      writeBranchesToCSV(branches);
      return ArrayUtilities.join((List<?>) value);
      // special handling
    } else {
      branches[9] = 1;
    }

    if (value.getClass().isArray()) {
      branches[10] = 1;
      if (field.getType().getComponentType() == int.class) {
        branches[11] = 1;
        writeBranchesToCSV(branches);
        return ArrayUtilities.join((int[]) value);
      } else if (field.getType().getComponentType() == double.class) {
        branches[12] = 1;
        writeBranchesToCSV(branches);
        return ArrayUtilities.join((double[]) value);
      } else if (field.getType().getComponentType() == float.class) {
        branches[13] = 1;
        writeBranchesToCSV(branches);
        return ArrayUtilities.join((float[]) value);
      } else if (field.getType().getComponentType() == short.class) {
        branches[14] = 1;
        writeBranchesToCSV(branches);
        return ArrayUtilities.join((short[]) value);
      } else if (field.getType().getComponentType() == byte.class) {
        branches[15] = 1;
        writeBranchesToCSV(branches);
        return ArrayUtilities.join((byte[]) value);
      } else if (field.getType().getComponentType() == long.class) {
        branches[16] = 1;
        writeBranchesToCSV(branches);
        return ArrayUtilities.join((long[]) value);
      } else if (field.getType().getComponentType() == String.class) {
        branches[17] = 1;
        writeBranchesToCSV(branches);
        return ArrayUtilities.join((String[]) value);
      } else if (field.getType().getComponentType() == boolean.class) {
        branches[18] = 1;
        writeBranchesToCSV(branches);
        return ArrayUtilities.join((boolean[]) value);
      } else {
        branches[19] = 1;
        writeBranchesToCSV(branches);
        return ArrayUtilities.join((Object[]) value);
      }
    } else {
      branches[20] = 1;
    }

    writeBranchesToCSV(branches);
    return value.toString();
  }

  private static void writeBranchesToCSV(int[] branches) {
    try {
      CSV.write(branches, 4);
    } catch (Exception e) {
      System.err.println("Error: " + e);
    }
  }
}

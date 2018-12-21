package de.gurkenlabs.litiengine.util;

import java.awt.Color;

public final class ColorHelper {
  private ColorHelper() {
    throw new UnsupportedOperationException();
  }

  /**
   * Encodes the specified color to a hexadecimal string representation.
   * The output format is:
   * <ul>
   * <li>#RRGGBB - For colors without alpha</li>
   * <li>#AARRGGBB - For colors with alpha</li>
   * </ul>
   * Examples: <br>
   * <code>Color.RED</code> = "#ff0000"<br>
   * <code>new Color(255, 0, 0, 200)</code> = "#c8ff0000"
   * 
   * @param color
   *          The color that is encoded.
   * @return An hexadecimal string representation of the specified color.
   * 
   * @see ColorHelper#decode(String)
   * @see Color
   * @see Color#getRGB()
   * @see Integer#toHexString(int)
   */
  public static String encode(Color color) {
    if (color == null) {
      return null;
    }

    String colorString = String.format("%08x", color.getRGB());
    if (color.getAlpha() == 255) {
      return "#" + colorString.substring(2);
    } else {
      return "#" + colorString;
    }
  }

  /**
   * Decodes the specified color string to an actual <code>Color</code> instance.
   * The accepted format is:
   * <ul>
   * <li>#RRGGBB - For colors without alpha</li>
   * <li>#AARRGGBB - For colors with alpha</li>
   * </ul>
   * Examples: <br>
   * "#ff0000" = <code>Color.RED</code><br>
   * "#c8ff0000" = <code>new Color(255, 0, 0, 200)</code>
   * 
   * @param colorHexString
   *          The hexadecimal encodes color string representation.
   * @return The decoded color.
   * 
   * @see ColorHelper#encode(Color)
   * @see Color
   * @see Color#decode(String)
   * @see Integer#decode(String)
   */
  public static Color decode(String colorHexString) {
    return decode(colorHexString, false);
  }

  public static Color decode(String colorHexString, boolean solid) {
    if (colorHexString == null || colorHexString.isEmpty()) {
      return null;
    }

    if (colorHexString.charAt(0) != '#') {
      colorHexString = '#' + colorHexString;
    }

    if (colorHexString.length() != 7 && colorHexString.length() != 9) {
      throw new IllegalArgumentException("invalid color string");
    }

    Color cl = new Color(Integer.parseUnsignedInt(colorHexString.substring(1), 16), colorHexString.length() == 9);
    if (solid) {
      float alphaRatio = cl.getAlpha() / (float) 255f;
      cl = new Color(Math.round(alphaRatio * cl.getRed()),
          Math.round(alphaRatio * cl.getGreen()),
          Math.round(alphaRatio * cl.getBlue()));
    }
    return cl;
  }

  /**
   * Ensures that the specified value lies within the accepted range for Color values (0-255).
   * Smaller values will be forced to be 0 and larger values will result in 255.
   * 
   * @param value
   *          The value to check for.
   * @return An integer value that fits the color value restrictions.
   */
  public static int ensureColorValueRange(float value) {
    return ensureColorValueRange(Math.round(value));
  }

  /**
   * Ensures that the specified value lies within the accepted range for Color values (0-255).
   * Smaller values will be forced to be 0 and larger values will result in 255.
   * 
   * @param value
   *          The value to check for.
   * @return An integer value that fits the color value restrictions.
   */
  public static int ensureColorValueRange(int value) {
    return Math.min(Math.max(value, 0), 255);
  }
}

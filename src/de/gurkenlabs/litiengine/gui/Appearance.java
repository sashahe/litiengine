package de.gurkenlabs.litiengine.gui;

import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Paint;
import java.awt.RenderingHints;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Consumer;

public class Appearance {
  private final List<Consumer<Appearance>> changedConsumer;

  private Color foreColor;
  private Color backgroundColor1;
  private Color backgroundColor2;
  private boolean horizontalBackgroundGradient;
  private boolean transparentBackground;
  private Color borderColor = new Color(0, 0, 0);
  private int borderThickness = 1;
  private Boolean borderRounded = false;

  private Object textAntialiasing;

  public Appearance() {
    this.changedConsumer = new CopyOnWriteArrayList<>();
    this.textAntialiasing = RenderingHints.VALUE_TEXT_ANTIALIAS_OFF;
  }

  public Appearance(Color foreColor) {
    this();
    this.foreColor = foreColor;
    this.setTransparentBackground(true);
  }

  public Appearance(Color foreColor, Color backColor) {
    this();
    this.foreColor = foreColor;
    this.backgroundColor1 = backColor;
  }

  @Override
  public boolean equals(Object obj) {
    if (obj instanceof Appearance) {
      return this.hashCode() == obj.hashCode();
    }

    return super.equals(obj);
  }

  @Override
  public int hashCode() {
    return Objects.hash(this.getForeColor(), this.getBackgroundColor1(), this.getBackgroundColor2(), this.isHorizontalBackgroundGradient(), this.isTransparentBackground());
  }

  public Color getForeColor() {
    return this.foreColor;
  }

  public Color getBackgroundColor1() {
    return this.backgroundColor1;
  }

  public Color getBackgroundColor2() {
    return this.backgroundColor2;
  }

  public Paint getBackgroundPaint(double width, double height) {
    if (this.isTransparentBackground()) {
      return null;
    }
    if (this.backgroundColor1 == null) {
      return this.backgroundColor2;
    } else if (this.backgroundColor2 == null) {
      return this.backgroundColor1;
    }

    if (this.horizontalBackgroundGradient) {
      return new GradientPaint(0, 0, this.backgroundColor1, (float) (width / 2.0), 0, this.backgroundColor2);
    } else {
      return new GradientPaint(0, 0, this.backgroundColor1, 0, (float) (height / 2.0), this.backgroundColor2);
    }
  }

  public Object getTextAntialiasing() {
    return this.textAntialiasing;
  }

  public boolean isHorizontalBackgroundGradient() {
    return this.horizontalBackgroundGradient;
  }

  public boolean isTransparentBackground() {
    return transparentBackground;
  }

  public void setForeColor(Color foreColor) {
    this.foreColor = foreColor;
    this.fireOnChangeEvent();
  }

  public void setBackgroundColor1(Color backColor1) {
    this.backgroundColor1 = backColor1;
    this.fireOnChangeEvent();
  }

  public void setBackgroundColor2(Color backColor2) {
    this.backgroundColor2 = backColor2;
    this.fireOnChangeEvent();
  }

  public void setHorizontalBackgroundGradient(boolean horizontal) {
    this.horizontalBackgroundGradient = horizontal;
    this.fireOnChangeEvent();
  }

  public void setTransparentBackground(boolean transparentBackground) {
    this.transparentBackground = transparentBackground;
    this.fireOnChangeEvent();
  }

  public void setBorderColor(Color color) {
    this.borderColor = color;
  }

  public void setBorderThickness(int thickness) {
    this.borderThickness = thickness;
  }

  public void setBorderRounded(Boolean rounded) {
    this.borderRounded = rounded;
  }

  public Color getBorderColor() {
    return this.borderColor;
  }

  public int getBorderThickness() {
    return this.borderThickness;
  }

  public Boolean getBorderRounded() {
    return this.borderRounded;
  }

  /**
   * Sets the {@link RenderingHints#KEY_TEXT_ANTIALIASING} settings for the rendered text.
   * 
   * @param antialiasing
   *                       Either {@link RenderingHints#VALUE_TEXT_ANTIALIAS_ON} or {@link RenderingHints#VALUE_TEXT_ANTIALIAS_OFF}
   */
  public void setTextAntialiasing(Object antialiasing) {
    this.textAntialiasing = antialiasing;
  }

  public void onChange(Consumer<Appearance> cons) {
    this.changedConsumer.add(cons);
  }

  public void update(Appearance updateAppearance) {
    this.setBackgroundColor1(updateAppearance.getBackgroundColor1());
    this.setBackgroundColor2(updateAppearance.getBackgroundColor2());
    this.setForeColor(updateAppearance.getForeColor());
    this.setHorizontalBackgroundGradient(updateAppearance.isHorizontalBackgroundGradient());
    this.setTransparentBackground(updateAppearance.isTransparentBackground());
    this.setTextAntialiasing(updateAppearance.getTextAntialiasing());
  }

  protected void fireOnChangeEvent() {
    for (Consumer<Appearance> cons : this.changedConsumer) {
      cons.accept(this);
    }
  }
}
package de.gurkenlabs.litiengine.gui;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.awt.Color;

import org.junit.jupiter.api.Test;

public class AppearanceTest {

    @Test
    public void testBorderColor() {
        Appearance a = new Appearance();
        Color c = new Color(0, 0, 0);
        a.setBorderColor(c);
        assertEquals(c, a.getBorderColor());
    }

    @Test
    public void testBorderThickness() {
        Appearance a = new Appearance();
        a.setBorderThickness(1);
        assertEquals(1, a.getBorderThickness());
    }

    @Test
    public void testBorderRounded() {
        Appearance a = new Appearance();
        a.setBorderRounded(true);
        assertEquals(true, a.getBorderRounded());
    }
}
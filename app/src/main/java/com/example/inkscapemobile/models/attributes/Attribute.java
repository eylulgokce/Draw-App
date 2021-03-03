package com.example.inkscapemobile.models.attributes;

import android.graphics.Color;

/**
 * Different attributes are created using the static factory methods.
 * A attribute holds it's type and value for making attribute modification transportable
 * Attributes objects are immutable so they can easily reused to use it in multiple sketch objects
 * The reason for using this class instead of android Paint objects are the attributes for width and font size
 * <p>
 * Defensive programming is applied with substitution to the nearest value, to increase robustness
 *
 * @param <T> type of the attribute value
 */
public class Attribute<T> {
    private final AttributeType type;
    private final T value;

    private Attribute(AttributeType type, T value) {
        this.type = type;
        this.value = value;
    }

    public static Attribute<Integer> createFontSizeAttribute(int fontSize) {
        if (fontSize < 0) {
            fontSize = 0;
        }
        return new Attribute<Integer>(AttributeType.fontSize, fontSize);
    }

    /**
     * @param color rgb color values stored in an array
     * @return color attribute object
     */
    public static Attribute<Integer> createColorAttribute(int color) {
        if (color < Color.BLACK) {
            color = Color.BLACK;
        } else if (color > Color.WHITE) {
            color = Color.WHITE;
        }

        return new Attribute<Integer>(AttributeType.color, color);
    }


    public static Attribute<Float> createStrokeAttribute(float strokeWidth) {
        if (strokeWidth < 0) {
            strokeWidth = 0;
        }
        return new Attribute<Float>(AttributeType.stroke, strokeWidth);
    }

    public static Attribute<Float> createWidthAttribute(float width) {
        if (width < 0) {
            width = 0;
        }
        return new Attribute<Float>(AttributeType.width, width);
    }

    public AttributeType getType() {
        return type;
    }

    public T getValue() {
        return value;
    }
}

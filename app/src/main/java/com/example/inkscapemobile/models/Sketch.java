package com.example.inkscapemobile.models;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;

import com.example.inkscapemobile.Observable;
import com.example.inkscapemobile.models.attributes.Attribute;
import com.example.inkscapemobile.models.attributes.AttributePaintWrapper;
import com.example.inkscapemobile.models.attributes.AttributeType;
import com.example.inkscapemobile.models.attributes.PaintFactory;

import java.util.EnumMap;

/**
 * Abstraction of a Graphical element that can be drawn. Serves as an interface to all possible
 * graphical elements (shapes, text, drawing...). Provides re-usability for attributes, highlighting
 */
public abstract class Sketch extends Observable implements GraphicalElement {
    //map was chosen over a list to avoid repeating attribute type checking
    public AttributePaintWrapper attributeWrapper;
    private boolean highlighted = false;
    private static final Paint highlightingPaint = PaintFactory.createSketchHighlightPaint();
    public Rect footprint;

    public EnumMap<AttributeType, Attribute<?>> getAttributes() {
        return attributeWrapper.getAttributes();
    }

    /**
     * Footprint is a rectangle around the sketch which is drawn when the sketch is selected.
     * Can be assembled using:
     * + the left most point of the sketch
     * + the most upper point of the sketch
     * + the right most point
     * + the lowest point
     *
     * @return region a round the sketch
     */
    public Rect getFootprint() {
        return footprint;
    }

    /**
     * Turn the selection highlighting on or of
     *
     * @param toggle highlighting on or off
     */
    @Override
    public void toggleHighlighting(boolean toggle) {
        highlighted = toggle;
        notifyObservers();
    }

    @Override
    public void draw(Canvas canvas) {
        if (highlighted) {
            canvas.drawRect(footprint, highlightingPaint);
        }
    }

    /**
     * @return a deep copy of this sketch
     */
    public abstract Sketch deepCopy();
}

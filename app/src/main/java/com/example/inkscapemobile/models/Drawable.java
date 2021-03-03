package com.example.inkscapemobile.models;

import android.graphics.Canvas;

public interface Drawable {
    /**
     * Draws/displays, this element to the canvas/screen
     *
     * @param canvas canvas to draw on
     */
    void draw(Canvas canvas);
}

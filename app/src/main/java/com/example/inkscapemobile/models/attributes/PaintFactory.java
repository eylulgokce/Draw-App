package com.example.inkscapemobile.models.attributes;

import android.graphics.Color;
import android.graphics.Paint;

import java.util.EnumMap;

public class PaintFactory {

    private static Paint basicPaintConfig() {
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setStrokeCap(Paint.Cap.BUTT);
        paint.setStrokeJoin(Paint.Join.MITER);
        return paint;
    }

    public static Paint createSketchHighlightPaint() {
        Paint sketchHighlightPaint = basicPaintConfig();
        sketchHighlightPaint.setStyle(Paint.Style.STROKE);
        sketchHighlightPaint.setColor(Color.rgb(32, 106, 173));
        sketchHighlightPaint.setStrokeWidth(6);

        return sketchHighlightPaint;
    }

    public static Paint createGroupHighlightPaint() {
        Paint paint = basicPaintConfig();
        paint.setStyle(Paint.Style.FILL_AND_STROKE);
        paint.setColor(Color.argb(40, 32, 106, 173));
        return paint;
    }

    public static Paint createGroupVisibilityPaint() {
        Paint paint = basicPaintConfig();
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(5);
        paint.setColor(Color.argb(60, 32, 106, 173));
        return paint;
    }

    /**
     * Converts a attribute map to the android paint object for use on the canvas
     *
     * @param attributes attributes to convert
     * @return android paint
     */
    public static Paint createPaintFromAttributes(EnumMap<AttributeType, Attribute<?>> attributes) {
        Paint paint = basicPaintConfig();

        for (AttributeType type : attributes.keySet()) {
            switch (type) {
                case color:
                    setColorAttributeToPaint(paint, attributes.get(type));
                    break;
                case stroke:
                    setStrokeAttributeToPaint(paint, attributes.get(type));
                    break;
                case fontSize:
                    setFontSizeAttributeToPaint(paint, attributes.get(type));
                    break;
            }
        }

        return paint;
    }

    private static void setColorAttributeToPaint(Paint paint, Attribute<?> color) {
        paint.setColor((Integer) color.getValue());
    }

    private static void setStrokeAttributeToPaint(Paint paint, Attribute<?> stroke) {
        float strokeWidth = (Float) (stroke.getValue());
        if (strokeWidth > 0) {
            paint.setStyle(Paint.Style.STROKE);
        } else {
            paint.setStyle(Paint.Style.FILL);
        }
        paint.setStrokeWidth(strokeWidth);
    }

    private static void setFontSizeAttributeToPaint(Paint paint, Attribute<?> fontSize) {
        paint.setTextSize((Integer) (fontSize.getValue()));
    }


}

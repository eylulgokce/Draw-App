package com.example.inkscapemobile.models.sketches;

import android.graphics.Canvas;
import android.graphics.Rect;
import android.util.Log;

import com.example.inkscapemobile.models.Sketch;
import com.example.inkscapemobile.models.attributes.Attribute;
import com.example.inkscapemobile.models.attributes.AttributeFactory;
import com.example.inkscapemobile.models.attributes.AttributeType;

import java.util.EnumMap;

/**
 * Creates a Text where we get X and Y coordinates from the Canvas
 * View and override draw method from Shape Class to draw a Text in the
 * Canvas View.
 */
public class Text extends Sketch {
    private float xStartingPoint;
    private float yBaseLine;
    private String textContent;
    private static final int HIGHLIGHTING_OFFSET = 10;

    public Text(EnumMap<AttributeType, Attribute<?>> attributes, float xStartingPoint, float yBaseLine, String textContent) {
        attributeWrapper = AttributeFactory.createTextAttributes(attributes);
        this.xStartingPoint = xStartingPoint;
        this.yBaseLine = yBaseLine;
        this.textContent = textContent;
        setFootprint();
        Log.d("Graphical Element", "Text: created");
    }

    /**
     *
     */
    private void setFootprint() {
        footprint = new Rect();
        attributeWrapper.getPaint().getTextBounds(textContent, 0, textContent.length(), footprint);
        int textHeight = footprint.height();
        footprint.offsetTo((int) xStartingPoint, (int) yBaseLine - textHeight);
        footprint.inset(-HIGHLIGHTING_OFFSET, -HIGHLIGHTING_OFFSET);
    }

    @Override
    public void applyAttributeChanges(Attribute<?> changedAttribute) {
        attributeWrapper.setAttribute(changedAttribute);
        if (changedAttribute.getType() == AttributeType.fontSize) {
            setFootprint();
        }
        notifyObservers();
    }


    /**
     * Unlike other graphical elements the selection is done via the footprint
     */
    @Override
    public boolean isSelected(float x, float y) {
        return footprint.contains((int) x, (int) y);
    }

    @Override
    public void moveBy(float x, float y) {
        xStartingPoint += x;
        yBaseLine += y;
        setFootprint();
        notifyObservers();
    }

    @Override
    public void moveTo(float x, float y) {
        xStartingPoint = x;
        yBaseLine = y;
        setFootprint();
        notifyObservers();
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        canvas.drawText(textContent, xStartingPoint, yBaseLine, attributeWrapper.getPaint());
    }

    public String getTextContent() {
        return textContent;
    }

    public void setTextContent(String textContent) {
        this.textContent = textContent;
        setFootprint();
        notifyObservers();
    }

    @Override
    public Sketch deepCopy() {
        EnumMap<AttributeType, Attribute<?>> attributes = new EnumMap<>(AttributeType.class);
        attributes.putAll(attributeWrapper.getAttributes());
        return new Text(attributes, xStartingPoint, yBaseLine, textContent);
    }

    @Override
    public String toJson() {
        return "{\"type\":\"Text\", \"attributes\":" + attributesToJson(attributeWrapper.getAttributes()) +
                ",\"xStartingPoint\":" + xStartingPoint +
                ",\"yBaseLine\":" + yBaseLine +
                ",\"textContent\":\"" + textContent +"\"" +
                "}";
    }
}

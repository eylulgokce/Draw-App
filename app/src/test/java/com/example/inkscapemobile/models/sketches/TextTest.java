package com.example.inkscapemobile.models.sketches;

import android.graphics.Color;
import android.graphics.Rect;

import com.example.inkscapemobile.models.attributes.Attribute;
import com.example.inkscapemobile.models.attributes.AttributeFactory;
import com.example.inkscapemobile.models.attributes.AttributeType;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.EnumMap;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

class TextTest {
    private final float StartingPoint = 200;
    private final float Baseline = 20;
    private final String textContext = "0";
    private static EnumMap<AttributeType, Attribute<?>> attributeEnumMap;
    private Text text;

    @BeforeEach
    void instantiate() {
        attributeEnumMap = AttributeFactory.createDefaultToolbarAttributes();
        text = new Text(attributeEnumMap, StartingPoint, Baseline, textContext);
    }

    @Test
    void colorAttribute_applyAttributeChanges_attributeChanged() {
        Attribute<?> before = attributeEnumMap.get(AttributeType.color);

        Attribute<?> changing = Attribute.createColorAttribute(Color.WHITE);
        text.applyAttributeChanges(changing);

        assertNotEquals(text.getAttributes().get(AttributeType.color), before);
        assertEquals(text.getAttributes().get(AttributeType.color), changing);
    }

    @Test
    void moveText_positionChanged() {
        //before
        float internalX = (float) ReflectionTestUtils.getField(text, "xStartingPoint");
        float internalY = (float) ReflectionTestUtils.getField(text, "yBaseLine");
        assertEquals(StartingPoint, internalX);
        assertEquals(Baseline, internalY);

        //action
        text.moveBy(10, 10);

        //after
        internalX = (float) ReflectionTestUtils.getField(text, "xStartingPoint");
        internalY = (float) ReflectionTestUtils.getField(text, "yBaseLine");
        assertEquals(StartingPoint + 10, internalX);
        assertEquals(Baseline + 10, internalY);

    }

    @Test
    void moveTextTo_positionChanged() {
        float x = 200;
        float y = 200;
        text.moveTo(x, y);

        Rect rect = text.getFootprint();
        int newStartingPoint = rect.left;
        int newBaseLine = rect.top;

        assertNotEquals(newStartingPoint, StartingPoint);
        assertNotEquals(newBaseLine, Baseline);
    }

    @Test
    void getTextContent() {
        String newContent = "testing";
        text.setTextContent(newContent);
        assertNotEquals(text.getTextContent(), textContext);
        assertEquals(text.getTextContent(), newContent);
    }

    @Test
    void setTextContent() {
        String newContent = "testing";
        text.setTextContent(newContent);
        assertNotEquals(text.getTextContent(), textContext);
        assertEquals(text.getTextContent(), newContent);
    }

    @Test
    void deepCopy_differentHashCode() {
        Text deepCopy = (Text) text.deepCopy();
        Text shallowCopy = text;

        assertNotEquals(text.toString(), deepCopy.toString());
        assertEquals(text.toString(), shallowCopy.toString());
    }
}
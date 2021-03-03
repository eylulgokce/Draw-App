package com.example.inkscapemobile.models;

import com.example.inkscapemobile.models.attributes.Attribute;
import com.example.inkscapemobile.models.attributes.AttributeType;

import java.util.EnumMap;

/**
 * A graphical element is a sketch, or a group of sketches,
 * which also can be handled as simple sketches, by utilizing the composite pattern.
 * The GraphicalElement interface is the component interface of the pattern,
 * for the sketches(leaves) and the composites(groups)
 */
public interface GraphicalElement extends Drawable {
    /**
     * A element is selected when the given coordinates are
     * located within the graphical element.
     * (Within the area that is covered by the visible parts of this element and not the footprint,
     * except the text element)
     *
     * @param x coordinate
     * @param y coordinate
     * @return true if this element is selected
     */
    boolean isSelected(float x, float y);

    /**
     * Moves this element (it's center/anchor point) BY the given vector
     * essentially adds the given vector to the center/anchor point
     *
     * @param x vector
     * @param y vector
     */
    void moveBy(float x, float y);

    /**
     * Moves this Element (its center/anchor point) to the given point
     *
     * @param x destination point
     * @param y destination point
     */
    void moveTo(float x, float y);

    /**
     * replace the currently set attribute with the given one
     *
     * @param changedAttribute new attributes which should be applied
     */
    void applyAttributeChanges(Attribute<?> changedAttribute);

    /**
     * Turn the highlighting on or off, when a element is
     *
     * @param toggle
     */
    void toggleHighlighting(boolean toggle);

    String toJson();

    default String attributesToJson(EnumMap<AttributeType, Attribute<?>> attributes) {
        StringBuilder sb = new StringBuilder("{");
        for (AttributeType type : attributes.keySet()) {
            sb.append('"').append(type.name()).append("\":");
            sb.append(attributes.get(type).getValue().toString()).append(',');
        }

        //remove the last ,
        if (sb.charAt(sb.length() - 1) == ',') {
            sb.deleteCharAt(sb.length() - 1);
        }
        return sb.append("}").toString();
    }

}

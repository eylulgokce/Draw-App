package com.example.inkscapemobile.application.storage;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import androidx.room.TypeConverter;

import com.example.inkscapemobile.models.GraphicalElement;
import com.example.inkscapemobile.models.Group;
import com.example.inkscapemobile.models.Layer;
import com.example.inkscapemobile.models.Sketch;
import com.example.inkscapemobile.models.attributes.Attribute;
import com.example.inkscapemobile.models.attributes.AttributeType;
import com.example.inkscapemobile.models.sketches.Circle;
import com.example.inkscapemobile.models.sketches.Drawing;
import com.example.inkscapemobile.models.sketches.Line;
import com.example.inkscapemobile.models.sketches.Rectangle;
import com.example.inkscapemobile.models.sketches.Text;
import com.example.inkscapemobile.models.sketches.Triangle;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.LinkedList;

/**
 * Adapter class from the adapter pattern, to adapt our model classes to the json representation,
 * which is stored in the database.
 *
 * Adapter, to adapt the Json Objects to our Java Objects
 *
 */
public class ModelsFromJsonAdapter {
    @TypeConverter
    public static Layer[] layersFromJson(String json) {
        Layer[] layers = new Layer[3];
        JsonElement layerArrayJsonElement = JsonParser.parseString(json);

        if (!layerArrayJsonElement.isJsonArray()) {
            throw new RuntimeException("json not valid, should be array of layers but was no array");
        }

        JsonArray layerJsonArray = layerArrayJsonElement.getAsJsonArray();
        for (int i = 0; i < 3; i++) {
            layers[i] = parseLayer(layerJsonArray.get(i));
        }

        return layers;
    }

    private static Layer parseLayer(JsonElement layerJsonElement) {
        if (!layerJsonElement.isJsonArray()) {
            throw new RuntimeException("json not valid: Layer should be a array but was not");
        }
        JsonArray layerContentJson = layerJsonElement.getAsJsonArray();
        LinkedList<GraphicalElement> content = new LinkedList<>();

        for (int i = 0; i < layerContentJson.size(); i++) {
            content.add(parseGraphicalElement(layerContentJson.get(i)));
        }

        return new Layer(content);
    }

    private static GraphicalElement parseGraphicalElement(JsonElement graphicalElementJson) {
        if (!graphicalElementJson.isJsonObject()) {
            throw new RuntimeException("json not valid: graphical element was no json object");
        }
        JsonObject elementJsonObject = graphicalElementJson.getAsJsonObject();
        String type = elementJsonObject.get("type").getAsString();

        if (type.equals("Group")) {
            return parseJsonGroup(elementJsonObject);
        } else {
            return parseSketchWithType(type, elementJsonObject);
        }
    }

    private static Group parseJsonGroup(JsonObject groupAsJson) {
        JsonArray sketchesJsonArray = groupAsJson.getAsJsonArray("sketches");
        LinkedList<Sketch> groupSketches = new LinkedList<>();

        for (int i = 0; i < sketchesJsonArray.size(); i++) {
            JsonObject elementAsJson = sketchesJsonArray.get(i).getAsJsonObject();
            String type = elementAsJson.get("type").getAsString();
            groupSketches.add(parseSketchWithType(type, elementAsJson));

        }
        return new Group(groupSketches);
    }

    private static Sketch parseSketchWithType(String type, JsonObject elementJson) {
        if (!elementJson.has("attributes")) {
            throw new RuntimeException("json not valid: sketch has no attribute object");
        }

        switch (type) {
            case "Circle":
                return getCircleFromJson(elementJson);
            case "Drawing":
                return getDrawingFromJson(elementJson);
            case "Line":
                return getLineFromJson(elementJson);
            case "Rectangle":
                return getRectangleFromJson(elementJson);
            case "Text":
                return getTextFromJson(elementJson);
            case "Triangle":
                return getTriangleFromJson(elementJson);
        }

        throw new RuntimeException("json not valid: graphical element type not found: " + type);
    }


    private static EnumMap<AttributeType, Attribute<?>> parseAttributes(JsonObject attributeJson) {
        EnumMap<AttributeType, Attribute<?>> attributes = new EnumMap<>(AttributeType.class);

        if (attributeJson.has(AttributeType.color.name())) {
            int color = attributeJson.get(AttributeType.color.name()).getAsInt();
            attributes.put(AttributeType.color, Attribute.createColorAttribute(color));
        }

        if (attributeJson.has(AttributeType.stroke.name())) {
            float stroke = attributeJson.get(AttributeType.stroke.name()).getAsFloat();
            attributes.put(AttributeType.stroke, Attribute.createStrokeAttribute(stroke));
        }

        if (attributeJson.has(AttributeType.fontSize.name())) {
            int fontSize = attributeJson.get(AttributeType.fontSize.name()).getAsInt();
            attributes.put(AttributeType.fontSize, Attribute.createFontSizeAttribute(fontSize));
        }

        if (attributeJson.has(AttributeType.width.name())) {
            float width = attributeJson.get(AttributeType.width.name()).getAsFloat();
            attributes.put(AttributeType.width, Attribute.createWidthAttribute(width));
        }

        return attributes;
    }


    private static Circle getCircleFromJson(JsonObject circleJsonObject) {
        JsonObject attributeJsonObject = circleJsonObject.getAsJsonObject("attributes");
        float centerX = circleJsonObject.get("centerX").getAsFloat();
        float centerY = circleJsonObject.get("centerY").getAsFloat();

        return new Circle(parseAttributes(attributeJsonObject), centerX, centerY);

    }

    private static Drawing getDrawingFromJson(JsonObject drawingJsonObject) {
        JsonObject attributeJsonObject = drawingJsonObject.getAsJsonObject("attributes");

        ArrayList<float[]> pathPoints = new ArrayList<>();
        for ( JsonElement outerElement : drawingJsonObject.get("drawnPath").getAsJsonArray()) {
            JsonArray innerElement = outerElement.getAsJsonArray();
            float[] point = new float[]{innerElement.get(0).getAsFloat(), innerElement.get(1).getAsFloat()};
            pathPoints.add(point);
        }

        return new Drawing(parseAttributes(attributeJsonObject), pathPoints);
    }

    private static Line getLineFromJson(JsonObject lineJsonObject) {
        JsonObject attributeJsonObject = lineJsonObject.getAsJsonObject("attributes");
        JsonArray startPointJson = lineJsonObject.getAsJsonArray("startPoint");
        JsonArray endPointJson = lineJsonObject.getAsJsonArray("endPoint");
        float[] startPoint = {startPointJson.get(0).getAsFloat(), startPointJson.get(1).getAsFloat()};
        float[] endPoint = {endPointJson.get(0).getAsFloat(), endPointJson.get(1).getAsFloat()};


        return new Line(parseAttributes(attributeJsonObject), startPoint, endPoint);
    }

    private static Rectangle getRectangleFromJson(JsonObject rectangleJsonObject) {
        JsonObject attributeJsonObject = rectangleJsonObject.getAsJsonObject("attributes");
        float centerX = rectangleJsonObject.get("centerX").getAsFloat();
        float centerY = rectangleJsonObject.get("centerY").getAsFloat();
        return new Rectangle(parseAttributes(attributeJsonObject), centerX, centerY);
    }

    private static Text getTextFromJson(JsonObject textJsonObject) {
        JsonObject attributeJsonObject = textJsonObject.getAsJsonObject("attributes");
        float xStartingPoint = textJsonObject.get("xStartingPoint").getAsFloat();
        float yBaseLine = textJsonObject.get("yBaseLine").getAsFloat();
        String textContent = textJsonObject.get("textContent").getAsString();
        return new Text(parseAttributes(attributeJsonObject), xStartingPoint, yBaseLine, textContent);
    }

    private static Triangle getTriangleFromJson(JsonObject triangleJsonObject) {
        JsonObject attributeJsonObject = triangleJsonObject.getAsJsonObject("attributes");
        JsonArray topPointJson = triangleJsonObject.getAsJsonArray("topPoint");
        float[] topPoint = {topPointJson.get(0).getAsFloat(), topPointJson.get(1).getAsFloat()};
        return new Triangle(parseAttributes(attributeJsonObject), topPoint);
    }
}

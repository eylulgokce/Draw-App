package com.example.inkscapemobile.application.storage;

import android.graphics.Bitmap;

import androidx.room.TypeConverter;

import com.example.inkscapemobile.models.GraphicalElement;
import com.example.inkscapemobile.models.Layer;
import com.google.gson.Gson;

import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;

/**
 * Adapter class from the adapter pattern, to adapt our model classes to the json representation,
 * which is stored in the database.
 *
 * Adapter, to adapt our Java Objects to the Json Object representation
 *
 */
public class ModelsToJsonAdapter {
    @TypeConverter
    public static String layersToJson(Layer[] layers) {
        if (layers.length == 0) {
            return "[]";
        }

        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < layers.length - 1; i++) {
            sb.append(layerToJson(layers[i])).append(',');
        }
        sb.append(layerToJson(layers[layers.length - 1]));
        return sb.append(']').toString();
    }

    private static String layerToJson(Layer layer) {
        StringBuilder sb = new StringBuilder("[");

        Iterator<GraphicalElement> elementIterator = layer.getContent().iterator();
        if (elementIterator.hasNext()) {
            sb.append(elementIterator.next().toJson());
        }
        while (elementIterator.hasNext()) {
            sb.append(',').append(elementIterator.next().toJson());
        }

        return sb.append("]").toString();
    }
}

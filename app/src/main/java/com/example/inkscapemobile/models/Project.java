package com.example.inkscapemobile.models;

import android.graphics.Bitmap;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import com.example.inkscapemobile.application.storage.ModelsFromJsonAdapter;
import com.example.inkscapemobile.application.storage.ModelsToJsonAdapter;

import java.util.LinkedList;

/**
 * A project holds all layers with all graphical elements.
 * It is the root model, by which all other models(graphical elements)
 * can be accessed.
 */
@Entity
public class Project {
    @PrimaryKey
    @NonNull
    private final String projectId;
    private String projectName;

    @TypeConverters({ModelsFromJsonAdapter.class, ModelsToJsonAdapter.class})
    private Layer[] layers;

    public Project(String projectId, String projectName, Layer[] layers) {
        this.projectName = projectName;
        this.projectId = projectId;
        this.layers = layers;
    }

    @Ignore
    public Project(String projectId, String projectName) {
        this.projectName = projectName;
        this.projectId = projectId;
        this.layers = new Layer[3];
        for (int i = 0; i < this.layers.length; i++) {
            this.layers[i] = new Layer(new LinkedList<>());
        }
    }

    public String getProjectName() {
        return projectName;
    }

    public String getProjectId() {
        return projectId;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public Layer[] getLayers() {
        return layers;
    }

    public void setLayers(Layer[] layers) {
        this.layers = layers;
    }

    /**
     * log all contents of the project
     */
    public void logProject() {
        Log.d("Project", "Project Content:");
        for (int i = 0; i < layers.length; i++) {
            for (GraphicalElement element : layers[i].getContent()) {
                Log.d("Project", "Layer " + i + " > " + element.getClass().getSimpleName());
                if (element instanceof Group) {
                    for (Sketch sketch : ((Group) element).getSketches()) {
                        Log.d("Project", "Layer " + i + " >>> " + sketch.getClass().getSimpleName());
                    }
                }
            }
        }
    }
}

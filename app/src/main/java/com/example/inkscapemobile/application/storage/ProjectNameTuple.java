package com.example.inkscapemobile.application.storage;

import androidx.room.ColumnInfo;

/**
 * A small helper class to hold projectId and name only.
 * Used in the list of projects of the HomeScreenActivity to not load the entire project data for
 * every project, when we just need the names and ids
 */
public class ProjectNameTuple {
    public ProjectNameTuple(String projectId, String projectName) {
        this.projectId = projectId;
        this.projectName = projectName;
    }

    @ColumnInfo(name = "projectId")
    public String projectId;

    @ColumnInfo(name = "projectName")
    public String projectName;
}

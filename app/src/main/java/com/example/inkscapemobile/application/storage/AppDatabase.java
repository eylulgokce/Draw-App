package com.example.inkscapemobile.application.storage;


import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.example.inkscapemobile.models.Project;

/**
 * Database object of the Android Room database.
 *
 * The entity stored is the Project class. Due to the lack of proper research
 * on the storage technology at architecture planning phase, we had to implement a solution,
 * which is not ideal. We store the Project in the database, where non-primitive members,
 * like the Layer class and each used Object (which is actually the total models package) are
 * stored as string in a json representation. Therefore we created adapter classes, utilizing the
 * adapter pattern, to convert our model object to and from a json representation.
 *
 */
@Database(entities = {Project.class}, version = 17, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {

    public abstract ProjectDao projectDao();
}

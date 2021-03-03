package com.example.inkscapemobile.application.storage;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.inkscapemobile.models.Project;
import com.google.common.util.concurrent.ListenableFuture;

import java.util.List;

/**
 * Data Access Object as part of the Android Room library.
 * Calls are made async, as one could see (the code is documentation enough ;))
 */
@Dao
public interface ProjectDao {
    @Query("Select projectId, projectName from Project")
    ListenableFuture<List<ProjectNameTuple>> getAll();

    @Query("SELECT * FROM Project WHERE projectId = :projectId")
    ListenableFuture<Project> loadProjectById(String projectId);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    ListenableFuture<Long> insert(Project project);

    @Delete
    ListenableFuture<Integer> delete(Project project);

    @Update
    ListenableFuture<Integer> update(Project project);
}

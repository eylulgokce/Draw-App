package com.example.inkscapemobile.application.storage;

import com.google.common.util.concurrent.ListenableFuture;

/**
 * Database CRUD methods are async, (actually just the Update function), where
 * the update function need special treatment in a special case.
 * This object represents a currently ongoing saving process, where data is written to the database
 * asynchronously.
 */
public class StorageSavingProcess {
    private ListenableFuture<Integer> process;
    private String projectId;

    public StorageSavingProcess(ListenableFuture<Integer> process, String projectId) {
        this.process = process;
        this.projectId = projectId;
    }

    public ListenableFuture<Integer> getProcess() {
        return process;
    }

    public String getProjectId() {
        return projectId;
    }
}

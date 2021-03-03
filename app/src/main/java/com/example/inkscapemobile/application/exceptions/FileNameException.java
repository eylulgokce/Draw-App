package com.example.inkscapemobile.application.exceptions;

/**
 * This exception should be thrown when the user tries to export a file with a name,
 * but there already exists a file with this name.
 */
public class FileNameException extends Exception {
    private final String filename;

    public FileNameException(String message, String filename) {
        super(message);
        this.filename = filename;
    }

    public String getFilename() {
        return filename;
    }
}

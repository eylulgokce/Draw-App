package com.example.inkscapemobile.application.filetypes;

/**
 * Enum for a file type which can be selected by the user for exporting a project.
 * When a file type is selected, the corresponding converter can be taken from this enum, to export the project.
 * <p>
 * This encompasses following aspect of the strategy pattern:
 * <p>
 * "a class defines many behaviors, and these appear as multiple conditional
 * statements in its operations. Instead of many conditionals, move related
 * conditional branches into their own Strategy class."
 */
public enum FileType {
    JPEG(new JPEGFileConverter()),
    PDF(new PDFFileConverter()),
    PNG(new PNGFileConverter());

    private FileTypeConverter converter;

    FileType(FileTypeConverter converter) {
        this.converter = converter;
    }

    public FileTypeConverter getConverter() {
        return converter;
    }
}

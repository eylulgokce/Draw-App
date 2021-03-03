package com.example.inkscapemobile.application.filetypes;

import android.content.ContentResolver;
import android.graphics.Bitmap;
import android.graphics.Paint;
import android.graphics.pdf.PdfDocument;
import android.os.Environment;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * ConcreteStrategy-class of the Strategy-pattern implementing the strategy for exporting in PDF-format.
 * Is of course implementing the Strategy-interface.
 */
public class PDFFileConverter implements FileTypeConverter {

    private final static String FILE_ENDING = ".pdf";

    private final static float FLOAT_LEFT = 0;
    private final static float FLOAT_RIGHT = 0;

    private final static int PAGE_NUMBER = 1;
    private final static Paint NEW_PAINT = null; //is allowed when using this method

    /**
     * The method which has to be implemented based on the implemented strategy-interface.
     * @param bitmap The Bitmap of the project to be exported
     * @param contentResolver The actual ContentResolver for this project
     * @param fileName The file-name which the exported image should have
     * @throws IOException
     */
    @Override
    public void convert(Bitmap bitmap, ContentResolver contentResolver, String fileName) throws IOException {

        //creating the new PDF-File using the Library-class PdfDocument
        PdfDocument pdf = new PdfDocument();
        //Creating the Document in size of the given bitmap
        PdfDocument.PageInfo info = new PdfDocument.PageInfo.Builder(bitmap.getWidth(), bitmap.getHeight(), PAGE_NUMBER).create();
        PdfDocument.Page page = pdf.startPage(info);

        //Getting the Canvas of PdfDocument and drawing our bitmap into it
        page.getCanvas().drawBitmap(bitmap, FLOAT_LEFT, FLOAT_RIGHT, NEW_PAINT);
        pdf.finishPage(page);

        String pdfFilePath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "/" + fileName + ".pdf";
        File pdfFile = new File(pdfFilePath);

        //Storing the image into the PDF-File
        try (FileOutputStream outputStream = new FileOutputStream(pdfFile)) {
            pdf.writeTo(outputStream);
            pdf.close();
        }
    }
}
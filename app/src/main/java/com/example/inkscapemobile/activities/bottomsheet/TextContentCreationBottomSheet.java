package com.example.inkscapemobile.activities.bottomsheet;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.inkscapemobile.R;
import com.example.inkscapemobile.models.sketches.Text;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

/**
 * Android bottom sheet ui element for changing the text content of an text sketch element
 */
public class TextContentCreationBottomSheet extends BottomSheetDialogFragment {
    private Text textSketch;
    private EditText textContentBox;
    private Button confirmButton;

    public TextContentCreationBottomSheet(Text textSketch) {
        this.textSketch = textSketch;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View mView = inflater.inflate(R.layout.text_content_creation_bottom_sheet, container, false);
        textContentBox = mView.findViewById(R.id.text_content_creation_text);
        confirmButton = mView.findViewById(R.id.text_content_menu_btn);
        setToStoredValue();
        setOnClickListener();
        return mView;
    }

    private void setToStoredValue() {
        textContentBox.setText(textSketch.getTextContent());
    }

    private void setOnClickListener() {
        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textSketch.setTextContent(textContentBox.getText().toString());
                dismiss();
            }
        });
    }
}

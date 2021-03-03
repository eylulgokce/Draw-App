package com.example.inkscapemobile.activities.bottomsheet;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.inkscapemobile.R;
import com.example.inkscapemobile.application.ToolbarStatus;
import com.example.inkscapemobile.models.attributes.Attribute;
import com.example.inkscapemobile.models.attributes.AttributeType;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

/**
 * Android bottom sheet ui element for changing the font size attribute stored in the toolbar
 */
public class FontSizeMenuBottomSheet extends BottomSheetDialogFragment {
    private ToolbarStatus toolbarStatus;
    private EditText fontSizeInputField;
    private Button confirmButton;

    public FontSizeMenuBottomSheet(ToolbarStatus toolbarStatus) {
        this.toolbarStatus = toolbarStatus;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View mView = inflater.inflate(R.layout.fontsize_menu_bottom_sheet, container, false);
        fontSizeInputField = mView.findViewById(R.id.font_size_input_field);
        confirmButton = mView.findViewById(R.id.font_size_menu_btn);
        setToStoredValue();
        setOnClickListener();
        return mView;
    }

    /**
     * set text to current values
     */
    private void setToStoredValue() {
        int storedFontSize = (Integer) toolbarStatus.getSelectedAttributes().get(AttributeType.fontSize).getValue();
        fontSizeInputField.setText(("" + storedFontSize));
    }

    /**
     * when the input contains legal values, store them in the toolbar and close the Bottomsheet
     */
    private void setOnClickListener() {
        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String insertedText = fontSizeInputField.getText().toString();
                try {
                    int insertedFontSize = Integer.parseInt(insertedText);
                    if (insertedFontSize >= 5 && insertedFontSize <= 500) {
                        toolbarStatus.selectAttribute(Attribute.createFontSizeAttribute(insertedFontSize));
                        dismiss();
                    }
                } catch (NumberFormatException e) {
                    Log.d("fontsize", "no valid fontsize given");
                }
            }
        });
    }
}

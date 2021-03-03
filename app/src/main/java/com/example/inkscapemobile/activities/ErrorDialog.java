package com.example.inkscapemobile.activities;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

import com.example.inkscapemobile.R;

/**
 * Used as the callback for the ErrorDialog.
 * The action method will be executed when the user presses the AlertDialog Button.
 */

interface ErrorDialogAction {
    public void action();
}

/**
 * Simple error messages dispatcher.
 * Calling the dispatchError method will display an error message to the user.
 */

public class ErrorDialog {

    private AlertDialog.Builder errorDialog;

    public ErrorDialog(Context context) {
        this.errorDialog = new AlertDialog.Builder(context).setIcon(R.drawable.ic_baseline_error_outline_24)
                .setTitle("something went wrong!");
    }

    /**
     * Dispatch an error message which will be displayed to the user and specify a callback which will be executed
     * once the user presses the DialogButton.
     * Use this method when you have to take further actions inorder to recover a safe state again, like dismissing
     * the current Activity.
     *
     * @param message the error message which will be displayed to the user
     * @param buttonText the text of the button
     * @param action the callback to be executed when the user presses the button
     */

    public void dispatchError(String message, String buttonText, ErrorDialogAction action) {
        this.errorDialog.setMessage(message).setNeutralButton(buttonText, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
                action.action();
            }
        }).show();
    }

    /**
     * Dispatch a simple error message to the user which can only be dismissed.
     * Use this method when you only want to inform the user about an error and don't want to take
     * further actions to recover a safe state.
     *
     * @param message the error message which will be displayed to the user
     */

    public void dispatchError(String message) {
        this.dispatchError(message, "dismiss", new ErrorDialogAction() {
            @Override
            public void action() {
                // action should be empty
            }
        });
    }

}



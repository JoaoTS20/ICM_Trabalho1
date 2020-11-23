package com.example.passadicosspot;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

public class DialogDescriptionFragment extends DialogFragment {
    public EditText desc;
    public interface OnDialogDismissListener {
        public void onDialogDismissListener(Bitmap bitmap, String description);
    }
    OnDialogDismissListener mFragment;
    Bitmap bitmap;
    public DialogDescriptionFragment(Bitmap b,OnDialogDismissListener mFragment){
        bitmap = b;
        this.mFragment = mFragment;
    }
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.description_getter, null);
        desc = view.findViewById(R.id.editTxtDesc);
        builder.setView(view)
                .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        mFragment.onDialogDismissListener(bitmap,desc.getText().toString());
                    }
                })
                .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User cancelled the dialog
                    }
                });
        // Create the AlertDialog object and return it
        return builder.create();
    }

}

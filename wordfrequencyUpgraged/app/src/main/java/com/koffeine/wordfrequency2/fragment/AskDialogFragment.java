package com.koffeine.wordfrequency2.fragment;


import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;

import com.koffeine.wordfrequency2.WordsFreqApplication;

/**
 * Created by mKoffeine on 11.04.2016.
 */
public class AskDialogFragment extends DialogFragment {
    public static String WORD = "word";


    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        final String word = getArguments().getString(WORD);
        builder.setMessage("Do u want delete '" + word + "'")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        if (word != null) {
                            ((WordsFreqApplication) getActivity().getApplicationContext()).
                                    getSqlHolder().deleteWord(word);

                        }
                    }
                })
                .setNegativeButton("cancel", null);
        return builder.create();

    }
}

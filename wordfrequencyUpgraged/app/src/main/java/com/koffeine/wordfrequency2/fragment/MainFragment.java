package com.koffeine.wordfrequency2.fragment;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.koffeine.wordfrequency2.Logger;
import com.koffeine.wordfrequency2.R;
import com.koffeine.wordfrequency2.WordsFreqApplication;
import com.koffeine.wordfrequency2.model.IWordsModel;
import com.koffeine.wordfrequency2.model.WordsModelByArray;
import com.koffeine.wordfrequency2.provider.WordSQLHolder;


public class MainFragment extends Fragment {
    private Logger logger = Logger.getLogger(MainFragment.class.getSimpleName());
    private String id = "";//Double.toString(Math.random());//"";
    private EditText inText;
    private TextView outText;
    private String TEXT = "text";
    private DownloadDataTask downloadDataTask;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View inflate = inflater.inflate(R.layout.fragment_main, container, false);
        inText = (EditText) getActivity().findViewById(R.id.editTextInput);
        if (savedInstanceState != null) {
            String text = savedInstanceState.getString(TEXT);

            if (text != null && inText != null) {
                inText.setText(text);
            }
        }
        return inflate;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        inText = (EditText) getActivity().findViewById(R.id.editTextInput);
        super.onSaveInstanceState(outState);
        if (outState != null && inText != null) {
            outState.putString(TEXT, inText.getText().toString());
        }
        logger.debug("123123 main fragment onSaveInstanceState " + outState);
    }

    @Override
    public void onStart() {
        super.onStart();
        logger.debug("onStart " + id);
        inText = (EditText) getActivity().findViewById(R.id.editTextInput);
        inText.addTextChangedListener(new OnValueChanged());
        outText = (TextView) getActivity().findViewById(R.id.editTextResult);
        if (getWordsModel() == null) {
            downloadDataTask = new DownloadDataTask();
            downloadDataTask.execute();
        }
        logger.debug("Model: " + getWordsModel());


        Button btnClipboard = (Button) getActivity().findViewById(R.id.button_copy);
        btnClipboard.setOnClickListener(new ButtonBufferClick());
        Button btnClear = (Button) getActivity().findViewById(R.id.button_clear);
        btnClear.setOnClickListener(new ButtonClearClick());
        Button btnPast = (Button) getActivity().findViewById(R.id.button_past);
        btnPast.setOnClickListener(new ButtonPastClick());
        Button btnAdd = (Button) getActivity().findViewById(R.id.button_add);
        btnAdd.setOnClickListener(new ButtonAddClick());
    }

    @Override
    public void onStop() {
        inText.setOnClickListener(null);
        outText.setOnClickListener(null);
        outText = null;
        logger.debug("onStop " + id);
        super.onStop();
    }

    @Override
    public void onDestroy() {
        logger.debug("onDestroy " + id);
        logger = null;
        if (downloadDataTask != null) {
            downloadDataTask.cancel(true);
        }
        super.onDestroy();
    }

    private IWordsModel getWordsModel() {
        return ((WordsFreqApplication) getActivity().getApplicationContext()).getWordsModel();
    }


    private void updateStatus() {
        String s = inText.getText().toString();
        logger.debug("onTextChanged " + s);
        String status = "";
        if (getWordsModel() != null) {
            status = getWordsModel().getStatus(s.toLowerCase().trim());
        }
        outText.setText(status);
    }


    private class OnValueChanged implements TextWatcher {

        public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {

        }

        public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            updateStatus();

        }

        public void afterTextChanged(Editable editable) {

        }
    }

    private class ButtonBufferClick implements View.OnClickListener {

        public void onClick(View view) {
            if (Build.VERSION.SDK_INT < 11) {
                android.text.ClipboardManager clipboard = (android.text.ClipboardManager)
                        getActivity().getSystemService(Context.CLIPBOARD_SERVICE);
                clipboard.setText(inText.getText().toString());
            } else {
                ClipboardManager clipboard = (ClipboardManager)
                        getActivity().getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("", inText.getText().toString());
                clipboard.setPrimaryClip(clip);
            }
        }
    }

    private class ButtonPastClick implements View.OnClickListener {

        public void onClick(View view) {
            if (Build.VERSION.SDK_INT < 11) {
                android.text.ClipboardManager clipboard = (android.text.ClipboardManager)
                        getActivity().getSystemService(Context.CLIPBOARD_SERVICE);
                inText.setText(clipboard.getText());
            } else {
                ClipboardManager clipboard = (ClipboardManager) getActivity().getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData primaryClip = clipboard.getPrimaryClip();
                if (primaryClip != null && primaryClip.getItemCount() > 0) {
                    ClipData.Item item = primaryClip.getItemAt(0);
                    if (item != null) {
                        inText.setText(item.getText());
                    }
                }
            }
        }
    }

    private class ButtonAddClick implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            EditText editText = (EditText) getActivity().findViewById(R.id.editTextInput);
            String w = editText.getText().toString();
            if (w.length() > 1) {
                WordSQLHolder sqlHolder = ((WordsFreqApplication) getActivity().getApplication()).getSqlHolder();
                sqlHolder.insertInDB(w);
            }
        }
    }


    private class ButtonClearClick implements View.OnClickListener {

        public void onClick(View view) {
            inText.setText("");
            inText.requestFocus();
        }
    }

    private class DownloadDataTask extends AsyncTask<String, Void, IWordsModel> {

        @Override
        protected IWordsModel doInBackground(String... strings) {
            IWordsModel wordsModel = new WordsModelByArray();
            wordsModel.initLogic(getActivity().getApplicationContext());
            return wordsModel;
        }

        @Override
        protected void onPostExecute(IWordsModel wordsModel) {
            FragmentActivity activity = getActivity();
            if (activity != null && activity.getApplicationContext() != null &&
                    !activity.isFinishing() && downloadDataTask != null && !downloadDataTask.isCancelled()) {
                ((WordsFreqApplication) activity.getApplicationContext()).setWordsModel(wordsModel);
                updateStatus();
                String message = "onPostExecute. model is ready";
                Toast toast = Toast.makeText(getActivity().getApplicationContext(), message, Toast.LENGTH_SHORT);
                toast.show();
            } else {
                logger.debug("123123 onPostExecute activity:  " + activity);
            }

        }
    }
}

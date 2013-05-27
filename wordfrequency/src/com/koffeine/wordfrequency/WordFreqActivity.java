package com.koffeine.wordfrequency;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


public class WordFreqActivity extends Activity {
    private EditText inText;
    private EditText outText;
    private Logger logger = Logger.getLogger(WordFreqActivity.class.getSimpleName());

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        inText = (EditText) findViewById(R.id.editTextInput);
        inText.addTextChangedListener(new OnValueChanged());
        outText = (EditText) findViewById(R.id.editTextResult);
        if (getWordsModel() == null) {
            new DownloadDataTask().execute();
        }
        logger.debug("Model: " + getWordsModel());


        Button btnClipboard = (Button) findViewById(R.id.button_copy);
        btnClipboard.setOnClickListener(new ButtonBufferClick());
        Button btnClear = (Button) findViewById(R.id.button_clear);
        btnClear.setOnClickListener(new ButtonClearClick());

    }

    @Override
    protected void onStop() {
        logger.debug("onStop ");
        logger = null;
        inText = null;
        outText = null;
        super.onStop();
    }

    private WordsModel getWordsModel() {
        return ((WordsFreqApplication) getApplicationContext()).getWordsModel();
    }


    private void updateStatus() {
        String s = inText.getText().toString();
        logger.debug("onTextChanged " + s);
        String status = "";
        if (getWordsModel() != null) {
            status = getWordsModel().getStatus(s);
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
            ClipboardManager clipboard = (ClipboardManager)
                    getSystemService(Context.CLIPBOARD_SERVICE);

            ClipData clip = ClipData.newPlainText("", inText.getText().toString());
            clipboard.setPrimaryClip(clip);
        }
    }

    private class ButtonClearClick implements View.OnClickListener {

        public void onClick(View view) {
            inText.setText("");
            inText.requestFocus();
        }
    }

    private class DownloadDataTask extends AsyncTask<String, Void, WordsModel> {

        @Override
        protected WordsModel doInBackground(String... strings) {
            WordsModel wordsModel = new WordsModel();
            wordsModel.initLogic();
            return wordsModel;
        }

        @Override
        protected void onPostExecute(WordsModel wordsModel) {
            ((WordsFreqApplication) getApplicationContext()).setWordsModel(wordsModel);
            updateStatus();
            String message = "onPostExecute. model is ready";
            Toast toast = Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT);
            toast.show();
        }
    }
}
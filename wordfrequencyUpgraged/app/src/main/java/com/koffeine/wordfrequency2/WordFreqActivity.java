package com.koffeine.wordfrequency2;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.koffeine.wordfrequency2.model.IWordsModel;
import com.koffeine.wordfrequency2.model.WordsModelFactory;


public class WordFreqActivity extends Activity {
    private String id = "";//Double.toString(Math.random());//"";
    private EditText inText;
    private EditText outText;
    private Logger logger = Logger.getLogger(WordFreqActivity.class.getSimpleName());

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        logger.debug("onsCreate");
        setContentView(R.layout.main);
    }

    @Override
    protected void onStart() {
        logger.debug("onStart " + id);
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
        super.onStart();
    }

    @Override
    protected void onStop() {
        inText.setOnClickListener(null);
        inText = null;
        outText.setOnClickListener(null);
        outText = null;
        logger.debug("onStop " + id);
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        logger.debug("onDestroy " + id);
        logger = null;
        super.onDestroy();
    }

    private IWordsModel getWordsModel() {
        return ((WordsFreqApplication) getApplicationContext()).getWordsModel();
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
                        getSystemService(Context.CLIPBOARD_SERVICE);
                clipboard.setText(inText.getText().toString());
            } else {
                ClipboardManager clipboard = (ClipboardManager)
                        getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("", inText.getText().toString());
                clipboard.setPrimaryClip(clip);
            }
        }
    }

    public void onPastClick(View view) {
        if (Build.VERSION.SDK_INT < 11) {
            android.text.ClipboardManager clipboard = (android.text.ClipboardManager)
                    getSystemService(Context.CLIPBOARD_SERVICE);
            inText.setText(clipboard.getText());
        } else {
            ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
            ClipData primaryClip = clipboard.getPrimaryClip();
            if (primaryClip != null && primaryClip.getItemCount() > 0) {
                ClipData.Item item = primaryClip.getItemAt(0);
                if (item != null) {
                    inText.setText(item.getText());
                }
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
            IWordsModel wordsModel = new WordsModelFactory().createWordsModel();
            wordsModel.initLogic(getApplicationContext());
            return wordsModel;
        }

        @Override
        protected void onPostExecute(IWordsModel wordsModel) {
            ((WordsFreqApplication) getApplicationContext()).setWordsModel(wordsModel);
            updateStatus();
            String message = "onPostExecute. model is ready";
            Toast toast = Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT);
            toast.show();
        }
    }
}
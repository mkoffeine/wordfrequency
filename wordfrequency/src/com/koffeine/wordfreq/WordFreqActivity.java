package com.koffeine.wordfreq;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.IOException;

public class WordFreqActivity extends Activity {
    private EditText inText;
    private EditText outText;
    private Logger logger = Logger.getLogger(WordFreqActivity.class.getSimpleName());
    WordsModel model;

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
        try {
            long time = System.currentTimeMillis();
            model = new WordsModel();
            long delta = System.currentTimeMillis() - time;
            Toast toast = Toast.makeText(getApplicationContext(), "initLogic done. Delta: " + delta, Toast.LENGTH_LONG);
            toast.show();
            logger.debug("initLogic done. Delta: " + delta);
        } catch (IOException e) {
            Toast toast = Toast.makeText(getApplicationContext(), "IOException during initLogic()", Toast.LENGTH_LONG);
            toast.show();
            logger.debug(e.toString());
        }
        Button btnClipboard = (Button) findViewById(R.id.button_copy);
        btnClipboard.setOnClickListener(new ButtonBufferClick());
        Button btnClear = (Button) findViewById(R.id.button_clear);
        btnClear.setOnClickListener(new ButtonClearClick());

    }


    private void updateStatus() {
        String s = inText.getText().toString();
        Log.d("123123", "123123 onTextChanged " + s);
        String status = model.getStatus(s);
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
}

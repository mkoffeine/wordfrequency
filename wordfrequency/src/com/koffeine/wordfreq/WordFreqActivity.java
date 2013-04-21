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

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;

public class WordFreqActivity extends Activity {
    private EditText inText;
    private EditText outText;
    private Logger logger = Logger.getLogger(WordFreqActivity.class.getSimpleName());
    private WordInfo[] wordsArray;

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
            initLogic();
        } catch (IOException e) {
            logger.debug(e.toString());
        }
        Button btnClipboard =  (Button) findViewById(R.id.button_copy);
        btnClipboard.setOnClickListener(new ButtonBufferClick());
        Button btnClear =  (Button) findViewById(R.id.button_clear);
        btnClear.setOnClickListener(new ButtonClearClick());

    }

    private void initLogic() throws IOException {

        InputStream is = null;
        String dictionaryFile = "en_full_raw.dic";

        is = getApplicationContext().getAssets().open(dictionaryFile);

        BufferedReader br = new BufferedReader(new InputStreamReader(is));

        String line;
        int i = 0;
        ArrayList<WordInfo> words = new ArrayList<WordInfo>();
        while ((line = br.readLine()) != null) {
            i++;
            int separatorIndex1 = line.indexOf(" ");
            int separatorIndex2 = line.indexOf(" ", separatorIndex1 + 1);
            String word = line.substring(0, separatorIndex1);
            int freq = Integer.parseInt(line.substring(separatorIndex1 + 1, separatorIndex2));
            int lineNumber = Integer.parseInt(line.substring(separatorIndex2 + 1));
            words.add(new WordInfo(word, lineNumber, freq));
            if (i % 11000 == 0) {
                logger.debug("" + word + " " + i + "  # " + lineNumber + "  " + freq + "  ==== " + line);
            }
        }
        wordsArray = words.toArray(new WordInfo[]{});
        logger.debug("Start to sort");
        long time = System.currentTimeMillis();
//		Arrays.sort(wordsArray);
        long delta = System.currentTimeMillis() - time;
        logger.debug("Sorting is finished. Time: " + delta);

        for (WordInfo wordInfo : wordsArray) {
            if (i % 11000 == 0)
                logger.debug(wordInfo.toString());
        }
    }

    private void updateStatus() {
        String s = inText.getText().toString();
        Log.d("123123", "123123 onTextChanged " + s);
        String status = "";
        if (s.length() > 2) {
//		int index = Arrays.binarySearch(wordsArray, new WordInfoEmulate(s));
            int index = Arrays.binarySearch(wordsArray, new WordInfo(s, 0, 0));
            index = index > -1 ? index : -index - 1;
            WordInfo info = wordsArray[index];
            status = "words:   " + info.toString() + "\n\n";
            String[] suffixes = {"s", "ing", "es", "ly", "er", "ar", "ir", "y", "able", "e",
                    "t", "ed", "en", "ist", "ful", "fy", "ian", "ize", "ible",
                    "ness", "less", "ism", "hood", "ment"
                    , "al", "ent", "nt", "sm", "m", "st", "an"
                    , "ss", "ng", "n", "ble", "le", "r", "l"
                    , "t", "ul", "sm", "n", "ood", "od", "d"};
            for (String suffix : suffixes) {
                String word = s + suffix;
                index = Arrays.binarySearch(wordsArray, new WordInfo(word, 0, 0));
                if (index > -1) {
                    status += "   " + wordsArray[index].toString() + "\n";
                }
                else {
                    //status += "   --no-- " + word + "\n";
                }
            }
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
        }
    }
}

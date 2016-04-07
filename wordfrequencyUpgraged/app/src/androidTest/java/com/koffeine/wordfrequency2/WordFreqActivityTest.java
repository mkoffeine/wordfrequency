package com.koffeine.wordfrequency2;

import android.app.Activity;
import android.test.ActivityInstrumentationTestCase2;
import android.test.TouchUtils;
import android.view.KeyEvent;
import android.widget.EditText;

/**
 * This is a simple framework for a test of an Application.  See
 * {@link android.test.ApplicationTestCase ApplicationTestCase} for more information on
 * how to write and extend Application tests.
 * <p/>
 * To run this test, you can type:
 * adb shell am instrument -w \
 * -e class com.koffeine.wordfrequency.WordFreqActivityTest \
 * com.koffeine.wordfrequency.tests/android.test.InstrumentationTestRunner
 */
public class WordFreqActivityTest extends ActivityInstrumentationTestCase2<WordFreqActivity> {

    private Activity activity;
    private EditText etInput;
    private EditText etResult;
    public WordFreqActivityTest() {
        super("com.koffeine.wordfrequency", WordFreqActivity.class);
    }

    @Override
    protected void setUp() throws Exception {
        // TODO Auto-generated method stub
        super.setUp();
        activity = getActivity();
        etInput = (EditText) activity.findViewById(R.id.editTextInput);
        etResult = (EditText) activity.findViewById(R.id.editTextResult);
    }
    public void testControlsCreated() {
        assertNotNull(activity);
        assertNotNull(etInput);
        assertNotNull(etResult);
    }

    public void testApp() {
        TouchUtils.tapView(this, etInput);
        sendKeys(KeyEvent.KEYCODE_A);
        sleep();
        sendKeys(KeyEvent.KEYCODE_B);
        sleep();
        sendKeys(KeyEvent.KEYCODE_A);
        sleep();
        sendKeys(KeyEvent.KEYCODE_N);
        sleep();
        sleep();
        sendKeys(KeyEvent.KEYCODE_D);
        sleep();
        sleep();
        sleep();
        sendKeys(KeyEvent.KEYCODE_O);
        sleep();
        sleep();
        sleep();
        sleep();
        assertTrue(etResult.getText().toString().contains("abandon"));
        sendKeys(KeyEvent.KEYCODE_N);
        assertTrue(etResult.getText().toString().contains("abandon"));

    }
    private void sleep() {
        WordsFreqApplication wordsFreqApplication = (WordsFreqApplication)activity.getApplicationContext();
        try {
            if (wordsFreqApplication.getWordsModel() == null) {
                Thread.sleep(500);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


}

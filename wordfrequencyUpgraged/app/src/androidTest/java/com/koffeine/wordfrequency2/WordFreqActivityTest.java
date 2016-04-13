package com.koffeine.wordfrequency2;

import android.app.Activity;
import android.test.ActivityInstrumentationTestCase2;
import android.test.TouchUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;


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
    private TextView etResult;
    private View btnClear;
    private View btnCopy;
    private View btnPast;

    //    @TargetApi(8)
    public WordFreqActivityTest() {
        super("com.koffeine.wordfrequency2", WordFreqActivity.class);
    }

    @Override
    protected void setUp() throws Exception {
        // TODO Auto-generated method stub
        super.setUp();
        activity = getActivity();
        etInput = (EditText) activity.findViewById(R.id.editTextInput);
        etResult = (TextView) activity.findViewById(R.id.editTextResult);
        btnClear = activity.findViewById(R.id.button_clear);
        btnCopy = activity.findViewById(R.id.button_copy);
        btnPast = activity.findViewById(R.id.button_past);
    }
    public void testControlsCreated() {
        assertNotNull(activity);
        assertNotNull(etInput);
        assertNotNull(etResult);
    }

    public void testClearBtn() {
        TouchUtils.tapView(this, etInput);
        sendKeys(KeyEvent.KEYCODE_A);
        sleep();
        sendKeys(KeyEvent.KEYCODE_B);
        sleep();
        assertTrue(etInput.getText().toString().contains("ab"));
        sleep();
        TouchUtils.clickView(this, btnClear);
        sleep();
        assertTrue(etInput.getText().toString().equals(""));
    }

    public void testCopyPastBtn() {
        TouchUtils.tapView(this, etInput);
        sendKeys(KeyEvent.KEYCODE_A);
        sleep();
        sendKeys(KeyEvent.KEYCODE_B);
        sleep();
        TouchUtils.clickView(this, btnCopy);
        sleep();
        sendKeys(KeyEvent.KEYCODE_B);
        sleep();
        TouchUtils.clickView(this, btnPast);
        assertTrue(etInput.getText().toString().equals("ab"));
    }

    private void sleep() {
        WordsFreqApplication wordsFreqApplication = (WordsFreqApplication)activity.getApplicationContext();
        try {
            if (wordsFreqApplication.getWordsModel() == null) {
                Thread.sleep(300);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

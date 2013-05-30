package com.koffeine.wordfrequency;

import android.test.ActivityInstrumentationTestCase2;

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

    public WordFreqActivityTest() {
        super("com.koffeine.wordfrequency", WordFreqActivity.class);
    }

}

package com.koffeine.wordfrequency2.model;

import org.junit.Assert;
import org.junit.Test;

/**
 * Created by mKoffeine on 13.04.2016.
 */
public class WordInfoTest {
    @Test
    public void test() {
        WordInfo wordInfo = new WordInfo("qw", 3, 7777);
        WordInfo wordInfo2 = new WordInfo("qw", 3, 7777);
        Assert.assertEquals("Object should be equals", wordInfo, wordInfo2);
    }

}

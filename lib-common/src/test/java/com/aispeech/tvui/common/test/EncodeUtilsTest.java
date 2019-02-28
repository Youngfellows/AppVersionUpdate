package com.aispeech.tvui.common.test;

import com.aispeech.tvui.common.util.EncodeUtils;

import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Created by huijieZ on 2018/8/23.
 */

public class EncodeUtilsTest {
    @Test
    public void addition_isCorrect() {
        assertEquals(4, 2 + 2);
    }

    @Test
    public void urlEncodeTest(){
        Assert.assertEquals("%E4%BD%A0%E5%A5%BD",EncodeUtils.urlEncode("你好"));
        Assert.assertEquals("hello%20%E4%BD%A0%E5%A5%BD", EncodeUtils.urlEncode("hello 你好"));
        Assert.assertEquals("hello%20%C4%E3%BA%C3",EncodeUtils.urlEncode("hello 你好", "gb2312"));
        System.out.print(EncodeUtils.urlEncode("hello 你好"));
    }

    @Test
    public void urlDecode(){
        Assert.assertEquals("hello 你好", EncodeUtils.urlDecode("hello%20%E4%BD%A0%E5%A5%BD"));
        Assert.assertEquals("hello 你好", EncodeUtils.urlDecode("hello%20%C4%E3%BA%C3", "gb2312"));
    }


}

package com.aispeech.tvui.common.test;

import com.aispeech.tvui.common.util.FileIOUtils;

import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

/**
 * Created by huijieZ on 2018/8/23.
 */

public class FileIOUtilsTest {
    @Test
    public void writeFileFromISTest(){
        InputStream is = null;
        try {
            is = new FileInputStream(new File("E:\\testPath\\test1\\test.txt"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        FileIOUtils.writeFileFromIS("E:\\LOG\\test.txt", is);
    }

    @Test
    public void writeFileFromISappendTest(){
        InputStream is = null;
        try {
            is = new FileInputStream(new File("E:\\testPath\\test1\\test.txt"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        FileIOUtils.writeFileFromIS("E:\\LOG\\test.txt", is, true);
    }

    @Test
    public void writeFileFromBytesByStreamTesst(){
        String str = "bytesByStream 你好测试";
        FileIOUtils.writeFileFromBytesByStream("E:\\LOG\\test.txt", str.getBytes());
    }

    @Test
    public void writeFileFromBytesByStreamTest(){
        String str = "bytesByStream 追加测试";
        FileIOUtils.writeFileFromBytesByStream("E:\\LOG\\test.txt", str.getBytes(), true);
    }

    @Test
    public void writeFileFromBytesByChannel(){
        String str = "bytesByStream channel方式 追加测试";
        FileIOUtils.writeFileFromBytesByChannel("E:\\LOG\\test.txt", str.getBytes(), true, true);
    }

    @Test
    public void writeFileFromBytesByChannelNotAppend(){
        String str = "bytesByStream channel方式 追加测试";
        FileIOUtils.writeFileFromBytesByChannel("E:\\LOG\\test.txt", str.getBytes(), false, true);
    }

    @Test
    public void inputStream2StringTest(){
        InputStream is = null;
        try {
            is = new FileInputStream(new File("E:\\LOG\\test.txt"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        Assert.assertEquals("bytesByStream channel方式 追加测试",FileIOUtils.inputStream2String(is));
    }

}

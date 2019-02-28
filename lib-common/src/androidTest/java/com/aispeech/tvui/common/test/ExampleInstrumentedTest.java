package com.aispeech.tvui.common.test;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import android.util.Log;

import com.aispeech.tvui.common.util.AssetsUtil;
import com.aispeech.tvui.common.util.EncodeUtils;
import com.aispeech.tvui.common.util.FileUtil;
import com.aispeech.tvui.common.util.TLog;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {
    Context appContext;

    @Before
    public void useAppContext() {
        // Context of the app under test.
        appContext = InstrumentationRegistry.getTargetContext();
        assertEquals("com.aispeech.tvui.common.test", appContext.getPackageName());
    }

    @Test
    public void base64EncodeTest(){
        String str = "base64EncodeTest";
        String strResult = EncodeUtils.base64Encode2String(str.getBytes());
        Assert.assertEquals("YmFzZTY0RW5jb2RlVGVzdA==",strResult);
    }


    @Test
    public void base64DecodeTest(){
        String str = "YmFzZTY0RW5jb2RlVGVzdA==";
        String print = EncodeUtils.base64Decode(str);
        Assert.assertEquals("base64EncodeTest", print);
    }

    @Test
    public void base64DecodeByteTest(){
        String str = "YmFzZTY0RW5jb2RlVGVzdA==";
        byte [] bytes = EncodeUtils.base64Decode(str.getBytes());
        Assert.assertEquals("base64EncodeTest", new String(bytes));
    }

    @Test
    public void saveLogTest(){
        String logSaveFilePath = FileUtil.getCacheAbsolutePath(appContext);
        Log.i("TLog","logSaveFilePath is:"+logSaveFilePath);
//        FileUtil.deleteOldFile(logSaveFilePath+"log.txt");
        FileUtil.createOrExistsFile(logSaveFilePath+"log.txt");
        Assert.assertEquals(true, FileUtil.isFileExists(logSaveFilePath+"log.txt"));
//        TLog.setLogFilePath(logSaveFilePath);
        String resultContent = FileUtil.readFileContent(logSaveFilePath+"log.txt");
        Log.i("TLog", "resultContent is:"+resultContent);
    }

    @Test
    public void getAssetsFile(){
        String productId = AssetsUtil.readProp(appContext, "K_PRODUCT_ID");
        Assert.assertEquals("278574510", productId);
    }

    @Test
    public void printJSon(){
        TLog.setLogLevel(3);
        String jsonTest1 = "{\"name\":\"huijieZ\",\"action\":\"testJson\"}";
        String jsonTest2 = "{\"widget\":[{\"name\":\"huijieZ\"},{\"name\":\"taoge\"},{\"name\":\"rongge\"}]}";
        TLog.json(jsonTest1);
        TLog.json("printJSon Test", jsonTest2);
    }

    @Test
    public void testAssets(){
        TLog.setLogLevel(3);
        List<String> stringList;
        try {
            stringList = AssetsUtil.getJsonAssets(appContext, "D:/huijieZ/AndroidStudioProjects/tvui-v3/lib-common/src/main/assets/jsonfile/test.json");
            TLog.i("TEST ASSETS", stringList);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}

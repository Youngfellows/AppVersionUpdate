package com.aispeech.tvui.common.test;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() {
        assertEquals(4, 2 + 2);
    }

    int logLevel = 0b0000;
    @Before
//    public void init(){
//        TLog.init(logLevel);
//        Assert.assertEquals(0b0000, TLog.getLogLevel());
//        System.out.print("init test:"+TLog.getLogLevel());
//    }

    @Test
    public void isValidTest(){
//        System.out.print("print: "+TLog.isValid("TYPE_LOG_PRINT")+
//                "\nsave: "+TLog.isValid("TYPE_LOG_SAVE")+
//                "\nimport save: "+TLog.isValid("TYPE_LOG_IMPORTANTLOG")+
//                "\ndot save: "+TLog.isValid("TYPE_LOG_DOT"));
    }

    /**
     * 测试文件上传
     */

    @Test
    public void testFileUpload(){
//        post类型 表单类型 字段名称file
//        http://localhost:8080/testUploadFiles post
//        File file = FileUtil.getFileByPath("E:\\LOG\\test.txt");
//        RetrofitClient.getInstance("http://localhost:8080/testUploadFiles").upLoadFile("这是一个文件上传", file);

    }

}
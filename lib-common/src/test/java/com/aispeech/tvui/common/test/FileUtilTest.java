package com.aispeech.tvui.common.test;

import com.aispeech.tvui.common.util.FileUtil;

import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.junit.Assert.assertEquals;

/**
 * Created by huijieZ on 2018/8/23.
 */

public class FileUtilTest {
    @Test
    public void addition_isCorrect() {
        assertEquals(4, 2 + 2);
    }


    @Test
    public void spaceStringTest(){
        Assert.assertEquals(false, FileUtil.isSpace("E:\\LOG"));
        Assert.assertEquals(true, FileUtil.isSpace(null));

        try {
            FileUtil.isSpace("E:\\TEST PATH");
        } catch (Exception e) {
            Assert.assertEquals(e.getMessage(), "路径名包含空白字符");
        }
    }

    @Test
    public void createOrExistsDirTest(){
        FileUtil.createOrExistsDir("E:\\testPath\\test1");
    }

    @Test
    public void isFileExistsTest(){
        Assert.assertEquals(true, FileUtil.isFileExists("E:\\testPath\\test.txt"));
    }

    @Test
    public void deleteOldFileTest(){
        FileUtil.deleteOldFile("E:\\testPath\\test.txt");
        Assert.assertEquals(false, FileUtil.isFileExists("E:\\testPath\\test.txt"));
    }

//    @Test
//    public void saveStringToFileAppendTest() {
//        ExecutorService cachedThreadPool = Executors.newCachedThreadPool();
//        for (int i = 0; i < 10; i++) {
//            final int index = i;
//            try {
//                Thread.sleep(10);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//            cachedThreadPool.execute(new Runnable() {
//                @Override
//                public void run() {
//                    FileUtil.saveStringToFile("E:\\LOG\\log.txt", "testStringToFiletesttesttesttesttest" + index+"\n", true);
//                }
//            });
//        }
//        Assert.assertNotEquals(0, FileUtil.getFileByPath("E:\\LOG\\log.txt").length());
//    }

    @Test
    public void saveStringToFileTest() {
        ExecutorService cachedThreadPool = Executors.newCachedThreadPool();
        for (int i = 0; i < 10; i++) {
            final int index = i;
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            cachedThreadPool.execute(new Runnable() {
                @Override
                public void run() {
                    FileUtil.saveStringToFile("E:\\LOG\\log.txt", "testStringToFiletesttesttesttesttest" + index+"\n");
                }
            });
        }
        Assert.assertNotEquals(0, FileUtil.getFileByPath("E:\\LOG\\log.txt").length());
    }

    @Test
    public void readFileContentTest(){
        File file =new File("E:\\LOG\\log.txt");
        System.err.println(file.exists());
        System.err.println(file.length());
        System.err.println(file.lastModified());

        String str = FileUtil.readFileContent("E:\\LOG\\log.txt");

        Assert.assertNotEquals(0,str.length());
        System.out.print("test str is:"+str);
    }

    @Test
    public void deleteFileTest(){
        Assert.assertEquals(true, FileUtil.isFileExists("E:\\LOG\\log.txt"));
        FileUtil.deleteFile("E:\\LOG\\log.txt");
        Assert.assertEquals(false, FileUtil.isFileExists("E:\\LOG\\log.txt"));
    }

    @Test
    public void copyFileToDirTest(){
        Assert.assertEquals(false, FileUtil.isFileExists("E:\\LOG\\test.txt"));
        FileUtil.copyFileToDir("E:\\testPath\\test1\\test.txt","E:\\LOG");
        Assert.assertEquals(true, FileUtil.isFileExists("E:\\LOG\\test.txt"));
    }

    @Test
    public void getFileSizeTest(){
        Assert.assertEquals(19, FileUtil.getFileSize("E:\\LOG\\test.txt"));
    }

}

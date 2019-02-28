package com.aispeech.tvui.common.test;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import android.util.Log;

import com.aispeech.tvui.common.Contributor;
import com.aispeech.tvui.common.net.RetrofitClient;
import com.aispeech.tvui.common.util.FileIOUtils;
import com.aispeech.tvui.common.util.FileUtil;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static org.junit.Assert.assertEquals;

/**
 * Created by huijieZ on 2018/8/27.
 */
@RunWith(AndroidJUnit4.class)
public class RetrofitClientTest {

    Context context;

    @Before
    public void useAppContext(){
        context = InstrumentationRegistry.getTargetContext();
        assertEquals("com.aispeech.tvui.common.test", context.getPackageName());
    }

    /**
     *Retrofit get方法单元测试 测试通过
     */
    @Test
    public void testBaseGet(){
        Call<List<Contributor>> call = RetrofitClient.getInstance("https://api.github.com").getContributors("square", "retrofit");
        List<Contributor> contributors = null;
        try {
            contributors = call.execute().body();
            for (Contributor contributor : contributors) {
                Log.i("TEST", contributor.login + " (" + contributor.contributions + ")");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * 测试带参数的Get请求
     */
    @Test
    public void testGetCommon(){

        Map<String, String> maps = new HashMap<String, String>();
        maps.put("ip", "21.22.11.33");

        //"http://172.16.153.28:8080/getIpInfo?ip=21.22.11.33";
        Call<ResponseBody> responseBodyCall = RetrofitClient.getInstance("http://172.16.153.28:8080").get("getIpInfo", maps);
        responseBodyCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                String str = FileIOUtils.inputStream2String(response.body().byteStream());
                Log.i("RetrofitClientTest", "get请求success" + str);
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable throwable) {
                Log.i("RetrofitClientTest", "get请求fail" + throwable.getMessage());
            }
        });
        try {
            Thread.currentThread().sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


    /**
     * 测试post请求
     */

    @Test
    public void testPostCommon() {
        //"http://172.16.153.28:8080/getIpInfo?ip=21.22.11.33";
        Map<String, String> maps = new HashMap<String, String>();
        maps.put("ip", "21.22.11.33");
        Call<ResponseBody> call = RetrofitClient.getInstance("http://172.16.153.28:8080").post("getIpInfo", maps);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                String str = FileIOUtils.inputStream2String(response.body().byteStream());
                Log.i("RetrofitClientTest", "post请求success" + str);
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable throwable) {
                Log.i("RetrofitClientTest", "post请求fail" + throwable.getMessage());
            }
        });

    }

    /**
     * 测试文件上传 单元测试通过
     */

    @Test
    public void testFileUpload(){
//        post类型 表单类型 字段名称file
//        http://localhost:8080/testUploadFiles post
        String path = FileUtil.getCacheAbsolutePath(context);
        File file = FileUtil.getFileByPath(path+"log.txt");
        if(file.exists()){
            Log.v("Upload", "file is exists");
        }
//        Map<String ,File> map = new HashMap<String, File>();
//        map.put("file1", file);
        Call<ResponseBody> call = RetrofitClient.getInstance("http://172.16.153.28:8080/").upLoadFile(file);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Log.v("Upload", "success");
            }
            @Override public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.e("Upload error:", t.getMessage()); }
        });

        try {
            Thread.currentThread().sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * 下载功能
     */
    //http://172.16.153.28:8080/fileDownload
    @Test
    public void testFileDownLoad(){
        String downUrl = "fileDownload";
        Call<ResponseBody> call = RetrofitClient.getInstance("http://172.16.153.28:8080/").downLoadFile(downUrl);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, final Response<ResponseBody> response) {
//                String str = FileIOUtils.inputStream2String(response.body().byteStream());
                Log.i("RetrofitClintTest","下载成功：" + response.message()+"length: "+response.body().contentLength());
                //下载到盒子,下载文件名字为：downLoadTest.txt
                final String fileName = "downLoadTest.txt";
                final String path = FileUtil.getCacheAbsolutePath(context);
                if(FileUtil.createOrExistsFile(path+fileName)){
                    final File file = FileUtil.getFileByPath(path+fileName);
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            FileIOUtils.writeFileFromIS(file, response.body().byteStream());
                            String fileStr = FileUtil.readFileContent(path+fileName);
                            Log.i("RetrofitClientTest", "下载文件的内容是：" + fileStr);
                        }
                    }).start();
                }

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable throwable) {
                Log.i("RetrofitClintTest","下载失败：" + throwable.getMessage());
            }
        });
    }

    /**
     * 多文件上传测试
     */
    @Test
    public void filesUploadTest(){
        String path = FileUtil.getCacheAbsolutePath(context);
        File file1 = FileUtil.getFileByPath(path+"log.txt");
        File file2 = FileUtil.getFileByPath(path+"downLoadTest.txt");
        Map<String ,File> map = new HashMap<String, File>();
        map.put("file1", file1);
        map.put("file2", file2);
        Call<ResponseBody> responseBodyCall = RetrofitClient.getInstance("http://172.16.153.28:8080/").upLoadFiles(map);
        responseBodyCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Log.v("Upload", "success");
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable throwable) {
                Log.e("Upload error:", throwable.getMessage());
            }
        });
    }


}

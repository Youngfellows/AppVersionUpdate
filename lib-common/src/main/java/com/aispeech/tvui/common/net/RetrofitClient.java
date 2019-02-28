package com.aispeech.tvui.common.net;

import android.text.TextUtils;
import android.util.Log;

import com.aispeech.tvui.common.Contributor;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by huijieZ on 2018/8/27.
 *
 * @author huijieZ
 */

public class RetrofitClient {
    private static String TAG = "RetrofitClient";
    private static final int DEFAULT_TIMEOUT = 5;
    /**
     * 接口类
     */
    private IApiService iApiService;
    private OkHttpClient okHttpClient;
    private static String baseUrl = "https://api.github.com";

    private static Retrofit.Builder builder;

    /**
     * 带参构造方法
     *
     * @param url 要访问地址的baseUrl
     */
    private RetrofitClient(String url) {
        Log.e(TAG, "555555555 ----->>>>> 66666666 url = " + url);
        if (TextUtils.isEmpty(url)) {
            url = baseUrl;
        }
        Log.e(TAG, "before Object obj=new Object();");
        Object obj=new Object();
        Log.e(TAG, "after Object obj=new Object();");
        okHttpClient = new OkHttpClient.Builder()
                .addNetworkInterceptor(
                        new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
                .connectTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
                .build();

        Log.e(TAG, "7777777 ----->>>>> 88888888");
        Retrofit.Builder rtfBuilder = new Retrofit.Builder();
        Log.e(TAG, "rtfBuilder -->> bubububu");
        rtfBuilder.baseUrl(url);
        Log.e(TAG, "111111 -->> bubububu");
        rtfBuilder.addConverterFactory(GsonConverterFactory.create());
        Log.e(TAG, "22222 -->> bubububu");
        rtfBuilder.client(okHttpClient);
        Log.e(TAG, "22222 -->> bubububu");

        Retrofit retrofit = rtfBuilder.build();
//        Retrofit retrofit = null;

//                Retrofit retrofit = new Retrofit.Builder()
//                .client(okHttpClient)
//                .baseUrl(url)
//                .addConverterFactory(GsonConverterFactory.create())
//                .build();
//        Retrofit retrofit = getRetrofit(url);

        Log.e(TAG, "123456789 ----->>>>> 987654321 retrofit = " + retrofit);

        iApiService = retrofit.create(IApiService.class);
        Log.e(TAG, "88888888 ----->>>>> 99999999");
//
//                OkHttpClient.Builder client = new OkHttpClient.Builder();
//                client.connectTimeout(DEFAULT_TIME_OUT, TimeUnit.SECONDS);//连接超时时间
//                client.writeTimeout(DEFAULT_READ_TIME_OUT,TimeUnit.SECONDS);//读操作超时时间
//                client.addNetworkInterceptor(new HttpLoggingInterceptor()
//                        .setLevel(HttpLoggingInterceptor.Level.BODY));//添加log打印
//
//                return new Retrofit.Builder()
//                        .baseUrl(getBaseUrl())
//                        .client(client.build())
//                        .addConverterFactory(GsonConverterFactory.create())//Gson
//                        //                .addConverterFactory(ScalarsConverterFactory.create())//String
//                        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
//                        .build();
    }

    private Retrofit getRetrofit(String url) {
        OkHttpClient.Builder client = new OkHttpClient.Builder();
        client.connectTimeout(10, TimeUnit.SECONDS);//连接超时时间
        client.writeTimeout(5, TimeUnit.SECONDS);//读操作超时时间
        client.addNetworkInterceptor(new HttpLoggingInterceptor()
                .setLevel(HttpLoggingInterceptor.Level.BODY));//添加log打印
        return new Retrofit.Builder()
                .baseUrl(url)
                .client(client.build())
                .addConverterFactory(GsonConverterFactory.create())//Gson
                //.addConverterFactory(ScalarsConverterFactory.create())//String
                // .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
    }


    public IApiService getiApiService() {
        return iApiService;
    }

    /**
     * 带参实例化方法
     *
     * @param url baseUrl
     * @return 返回RetrofitClient对象(Retrofit已经初始化完毕)
     */
    public static RetrofitClient getInstance(String url) {
        Log.e(TAG, "init 102023323222 ---->>> 798s9a99as99999 ");
        return new RetrofitClient(url);
    }

    /**
     * 普通Get方法 无参
     *
     * @param owner 请求的完整url的 组成部分
     * @param repo  请求的完整url的 组成部分
     * @return 请求返回的数据，以List数组的形式
     */
    public Call<List<Contributor>> getContributors(String owner, String repo) {
        return iApiService.contributors(owner, repo);
    }

    /**
     * 多参数 Get方法
     *
     * @param url        请求网址
     * @param parameters 请求参数
     * @return 返回请求对象BaseResponse
     */
    public Call<ResponseBody> get(String url, Map<String, String> parameters) {
        return iApiService.executeGet(url, parameters);
    }

    /**
     * 多参数post方法
     *
     * @param url        请求网址
     * @param parameters 请求参数
     * @return 返回ResponseBody对象
     */
    public Call<ResponseBody> post(String url, Map<String, String> parameters) {
        return iApiService.executePost(url, parameters);
    }

    /**
     * 文件上传
     *
     * @param file 要上传的文件
     * @return 返回上传结果
     */
    public Call<ResponseBody> upLoadFile(File file) {
        // create RequestBody instance from file
        RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
        // MultipartBody.Part is used to send also the actual file name
        MultipartBody.Part body = MultipartBody.Part.createFormData("file", file.getName(), requestFile);

        return iApiService.upload(body);
    }

    /**
     * 多文件上传
     *
     * @param maps 文件存储列表
     * @return 返回上传结果
     */
    public Call<ResponseBody> upLoadFiles(Map<String, File> maps) {
        File file1 = maps.get("file1");
        File file2 = maps.get("file2");
        RequestBody requestBody1 = RequestBody.create(MediaType.parse("multipart/form-data"), file1);
        RequestBody requestBody2 = RequestBody.create(MediaType.parse("multipart/form-data"), file2);
        Map<String, RequestBody> params = new HashMap<>();
        params.put("file\"; filename=\"" + file1.getName(), requestBody1);
        params.put("file\"; filename=\"" + file2.getName(), requestBody2);

        return iApiService.uploadFiles(params);
    }

    /**
     * 文件下载
     *
     * @param fileUrl 下载地址
     * @return 返回下载结果
     */
    public Call<ResponseBody> downLoadFile(String fileUrl) {
        return iApiService.downLoadFile(fileUrl);
    }

}

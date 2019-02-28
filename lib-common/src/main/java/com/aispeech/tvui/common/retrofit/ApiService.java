package com.aispeech.tvui.common.retrofit;

import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.FieldMap;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.PartMap;
import retrofit2.http.Path;
import retrofit2.http.QueryMap;
import retrofit2.http.Streaming;
import retrofit2.http.Url;

/**
 *  自定义添加header功能未添加
 * Created by Alex on 2018/10/24.
 */

public interface ApiService {

    /**
     *  get请求,
     * @param url   请求地址
     * @param maps  参数集合
     * @return
     */
    @GET
    Call<ResponseBody> executeGet(
            @Url String url,
            @QueryMap Map<String, String> maps);

    /**
     *get请求
     * @param url   请求地址
     * @return
     */
    @GET
    Call<ResponseBody> executeGet(
            @Url String url);

    /**
     *  post请求
     * @param url 请求地址
     * @param maps  参数集合
     * @return
     */
    @POST
    Call<ResponseBody> executePost(
            @Url String url,
            @FieldMap Map<String, String> maps);

    /**
     *  单文件上传，表单形式
     * @param url   上传地址
     * @param args  参数
     * @param file  文件
     * @return
     */
    @Multipart
    @POST
    Call<ResponseBody> upLoadFile(
            @Url String url,
            @PartMap Map<String, RequestBody> args, @Part MultipartBody.Part file);

    /**
     *  单文件上传
     * @param url   上传地址
     * @param body  内容
     * @return
     */
    @POST
    Call<ResponseBody> upLoadJson(
            @Url String url,
            @Body RequestBody body
    );

    /**
     *  单文件下载
     * @param fileUrl 下载地址
     * @return
     */
    @Streaming
    @GET
    Call<ResponseBody> downloadFile(@Url String fileUrl);
}

package com.aispeech.tvui.common.retrofit;

import android.util.Log;

import com.aispeech.tvui.common.interfaces.RequestCallback;
import com.aispeech.tvui.common.interfaces.RetrofitDownCallback;
import com.aispeech.tvui.common.util.TLog;
import com.aispeech.tvui.common.util.URLUtils;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * 统一处理Retrofit的Post、Get请求(同步、异步)
 * 统一处理Retrofit的文件下载请求
 */
public class RetrofitClient extends BaseRetrofit {
    private String TAG = getClass().getSimpleName();
    private volatile static RetrofitClient instance;
    private ApiService mCommonApiService;

    /**
     * 构造方法，初始化retrofit和apiService
     */
    private RetrofitClient() {
        mCommonApiService = getRetrofit();
    }

    /**
     * 获取RetrofitClient单例
     *
     * @return
     */
    public static RetrofitClient getInstance() {
        if (instance == null) {
            synchronized (RetrofitClient.class) {
                if (instance == null) {
                    instance = new RetrofitClient();
                }
            }
        }
        return instance;
    }

    /**
     * 同步get请求
     *
     * @param url 地址
     * @return
     */
    public String syncGet(String url) {
        Call<ResponseBody> call = mCommonApiService.executeGet(url);
        String result = "";
        try {
            result = call.execute().body().string();
        } catch (IOException e) {
            e.printStackTrace();
        }
        TLog.d(TAG, "result: " + result);
        return result;
    }

    /**
     * 同步带参数get请求
     *
     * @param url
     * @param map
     */
    public String syncGet(String url, Map<String, String> map) {
        Call<ResponseBody> call = mCommonApiService.executeGet(url, map);
        String result = "";
        try {
            result = call.execute().body().string();
        } catch (IOException e) {
            e.printStackTrace();
        }
        TLog.d(TAG, "result: " + result);
        return result;
    }

    /**
     * 同步post
     *
     * @param url 地址
     * @param map 参数集合
     * @return
     */
    public String syncPost(String url, Map<String, String> map) {
        Call<ResponseBody> call = mCommonApiService.executePost(url, map);
        String result = "";
        try {
            result = call.execute().body().string();
        } catch (IOException e) {
            e.printStackTrace();
        }
        TLog.d(TAG, "result: " + result);
        return result;
    }

    /**
     * 异步get请求
     *
     * @param url 地址
     */
    public void asyncGet(String url, final RequestCallback callback) {
        Call<ResponseBody> call = mCommonApiService.executeGet(url);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                TLog.v(TAG, "onResponse: " + response.body().toString());
                if (response.isSuccessful() && callback != null) {
                    try {
                        callback.onSuccess(response.body().string());
                    } catch (IOException e) {
                        callback.onFailure(e.getMessage());
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                TLog.v(TAG, "onFailure" + t.toString());
                if (callback != null) {
                    callback.onFailure(t.getMessage());
                }
            }
        });
    }

    /**
     * 异步带参数get请求
     *
     * @param url 地址
     * @param map 参数集合
     */
    public void asyncGet(String url, Map<String, String> map, final RequestCallback callback) {
        Call<ResponseBody> call = mCommonApiService.executeGet(url, map);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                TLog.v(TAG, "onResponse: " + response.body().toString());
                if (response.isSuccessful() && callback != null) {
                    try {
                        callback.onSuccess(response.body().string());
                    } catch (IOException e) {
                        callback.onFailure(e.getMessage());
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                TLog.v(TAG, "onFailure" + t.toString());
                if (callback != null) {
                    callback.onFailure(t.getMessage());
                }
            }
        });
    }

    /**
     * post请求
     *
     * @param url 地址
     * @param map 参数集合
     */
    public void asyncPost(String url, Map<String, String> map, final RequestCallback callback) {
        Call<ResponseBody> call = mCommonApiService.executePost(url, map);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                TLog.v(TAG, "onResponse: " + response.body().toString());
                if (response.isSuccessful() && callback != null) {
                    try {
                        callback.onSuccess(response.body().string());
                    } catch (IOException e) {
                        callback.onFailure(e.getMessage());
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                TLog.v(TAG, "onFailure");
                if (callback != null) {
                    callback.onFailure(t.getMessage());
                }
            }
        });
    }

    /**
     * 1.使用默认请求体
     * 2.同步文件保存的进度
     *
     * @param fileUrl      下载路径
     * @param downCallback 回调
     */
    public void download2(final String fileUrl, final RetrofitDownCallback<ResponseBody> downCallback) {
        Call<ResponseBody> call = mCommonApiService.download2(fileUrl);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, final Response<ResponseBody> response) {
                int code = response.code();
                boolean isSuccessful = response.isSuccessful();
                Log.d(TAG, "onResponse: code = " + code + " ,isSuccessful = " + isSuccessful);
                if (downCallback != null) {
                    downCallback.onSuccess(call, response);
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                TLog.e(TAG, "onFailure: " + t.toString());
                t.printStackTrace();
                if (downCallback != null) {
                    downCallback.onFailure(call, t);
                }
            }
        });
    }


    /**
     * 1.使用自定义请求体
     * 2.可以监听Retrofit的实时下载进度
     *
     * @param fileUrl      下载路径
     * @param downCallback 回调
     */
    public void download(final String fileUrl, final RetrofitDownCallback<ResponseBody> downCallback) {
        String url = URLUtils.getUrl(fileUrl);
        String baseUrl = URLUtils.getHost(fileUrl);
        Log.i(TAG, baseUrl);
        Log.i(TAG, url);

        RetrofitCallback<ResponseBody> retrofitCallback = new RetrofitCallback<ResponseBody>() {
            @Override
            public void onSuccess(Call<ResponseBody> call, Response<ResponseBody> response) {
                Log.i(TAG, "onSuccess: code = " + response.code());
                if (response.isSuccessful() && downCallback != null) {
                    downCallback.onSuccess(call, response);
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.i(TAG, "onFailure " + t.getMessage());
                if (downCallback != null) {
                    downCallback.onFailure(call, t);
                }
            }

            @Override
            public void onLoading(long total, long progress, boolean done) {
//                super.onLoading(total, progress, done);
                Log.i(TAG, "onLoading " + (float) (progress * 1.0 / total) * 100 + "% , " + (done ? "下载完成" : "未下载完成"));
                if (downCallback != null) {
                    downCallback.onLoading(total, progress, done);
                }
            }
        };

        ApiService apiService = getRetrofit(baseUrl, retrofitCallback);
        Call<ResponseBody> call = apiService.download(url);
        call.enqueue(retrofitCallback);
    }


    /**
     * 上送数据
     *
     * @param url      上送地址
     * @param json     JSO弄数据,默认传入数据为json格式，无需再次进行object to json转换
     * @param callback 上送回调
     */
    public void upLoadJson(String url, String json, final com.aispeech.tvui.common.interfaces.RequestCallback callback) {
        TLog.d(TAG, "json: " + json + "\n");
        final RequestBody requestBody = RequestBody.create(MediaType.parse(
                "application/json; charset=utf-8"), json);
        Call<ResponseBody> call = mCommonApiService.upLoadJson(url, requestBody);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    TLog.v(TAG, "isSuccessful onResponse: " + response.body().toString());
                    try {
                        callback.onSuccess(response.body().string());
                    } catch (IOException e) {
                        callback.onFailure(e.getMessage());
                        e.printStackTrace();
                    }
                } else {
                    TLog.v(TAG, "is not Success");
                    callback.onFailure("request is failure");
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                TLog.v(TAG, "onFailure");
                callback.onFailure(t.getMessage());
            }
        });
    }

    /**
     * 单文件上传，表单形式
     *
     * @param url      服务器地址
     * @param file     文件地址
     * @param callback 回调
     * @param map      参数列表,key为参数名，value为参数值
     */

    public void upLoadFile(String url, Map<String, String> map, File file, final com.aispeech.tvui.common.interfaces.RequestCallback callback) {
        Map<String, RequestBody> mapRequestBody = new HashMap<>();
        MediaType textType = MediaType.parse("text/plain");
        Iterator<Map.Entry<String, String>> iterator = map.entrySet().iterator();
        while (iterator.hasNext()) {//遍历插入map插入参数集合
            Map.Entry<String, String> entry = iterator.next();
            mapRequestBody.put(entry.getKey(), RequestBody.create(textType, entry.getValue()));
        }

        //文件处理
        RequestBody filebody = RequestBody.create(MediaType.parse("application/octet-stream"), file);
        MultipartBody.Part filePart = MultipartBody.Part.createFormData("file", file.getName(), filebody);
        Call<ResponseBody> call = mCommonApiService.upLoadFile(url, mapRequestBody, filePart);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    TLog.v(TAG, "isSuccessful onResponse: " + response.body().toString());
                    try {
                        callback.onSuccess(response.body().string());
                    } catch (IOException e) {
                        callback.onFailure(e.getMessage());
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                TLog.v(TAG, "onFailure");
                callback.onFailure(t.getMessage());
            }
        });
    }
}

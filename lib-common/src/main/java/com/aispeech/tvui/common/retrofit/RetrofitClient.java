package com.aispeech.tvui.common.retrofit;

import android.util.Log;

import com.aispeech.tvui.common.util.TLog;

import java.io.BufferedInputStream;
import java.io.Closeable;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
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
import retrofit2.Retrofit;

public class RetrofitClient extends BaseRetrofit implements ApiFuction {
    private String TAG = getClass().getSimpleName();

    private volatile static RetrofitClient retrofitClient;
    private Retrofit retrofit;
    private ApiService apiService;

    /**
     * 构造方法，初始化retrofit和apiService
     */
    public RetrofitClient() {
        retrofit = getRetrofit();
        apiService = retrofit.create(ApiService.class);
    }

    @Override
    protected String getBaseUrl() {
        return "https://api.github.com";
    }

    /**
     * 获取RetrofitClient单例
     *
     * @return
     */
    public static RetrofitClient getRetrofitClient() {
        if (retrofitClient == null) {
            synchronized (RetrofitClient.class) {
                if (retrofitClient == null) {
                    return new RetrofitClient();
                }
            }
        }
        return retrofitClient;
    }

    /**
     * 同步get请求
     *
     * @param url 地址
     * @return
     */
    public String syncGet(String url) {
        Call<ResponseBody> call = apiService.executeGet(url);
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
        Call<ResponseBody> call = apiService.executeGet(url, map);
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
        Call<ResponseBody> call = apiService.executePost(url, map);
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
    public void asyncGet(String url, final RequestCallback requestCallback) {
        Call<ResponseBody> call = apiService.executeGet(url);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                TLog.v(TAG, "onResponse: " + response.body().toString());
                if (response.isSuccessful()) {
                    try {
                        requestCallback.onSuccess(response.body().string());
                    } catch (IOException e) {
                        requestCallback.onFaliue(e.getMessage());
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                TLog.v(TAG, "onFailure" + t.toString());
                requestCallback.onFaliue(t.getMessage());
            }
        });
    }

    /**
     * 异步带参数get请求
     *
     * @param url 地址
     * @param map 参数集合
     */
    public void asyncGet(String url, Map<String, String> map, final RequestCallback requestCallback) {
        Call<ResponseBody> call = apiService.executeGet(url, map);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                TLog.v(TAG, "onResponse: " + response.body().toString());
                if (response.isSuccessful()) {
                    try {
                        requestCallback.onSuccess(response.body().string());
                    } catch (IOException e) {
                        requestCallback.onFaliue(e.getMessage());
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                TLog.v(TAG, "onFailure" + t.toString());
                requestCallback.onFaliue(t.getMessage());
            }
        });
    }

    /**
     * post请求
     *
     * @param url 地址
     * @param map 参数集合
     */
    public void asyncPost(String url, Map<String, String> map, final RequestCallback requestCallback) {
        Call<ResponseBody> call = apiService.executePost(url, map);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                TLog.v(TAG, "onResponse: " + response.body().toString());
                if (response.isSuccessful()) {
                    try {
                        requestCallback.onSuccess(response.body().string());
                    } catch (IOException e) {
                        requestCallback.onFaliue(e.getMessage());
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                TLog.v(TAG, "onFailure");
                requestCallback.onFaliue(t.getMessage());
            }
        });
    }


    /**
     * 请求版本更新
     *
     * @param baseURL  url
     * @param map      参数集合
     * @param callback 回调
     */
    @Override
    public void upgradeVersion(String baseURL, Map<String, String> map, final UpgradeRequestCallBack callback) {
        Call<ResponseBody> call = apiService.executeGet(baseURL, map);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                TLog.d(TAG, "onResponse： " + response.body().toString());
                if (response.isSuccessful()) {
                    try {
                        callback.requestSuccess(response.body().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                        callback.requestError(e.getMessage());
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.e(TAG, "onFailure " + t.getMessage());
                callback.requestError(t.getMessage());
            }
        });
    }

    /**
     * 获取更新配置单
     *
     * @param configUrl rul
     * @param callback  回调
     */
    @Override
    public void upgradeConfig(String configUrl, final UpgradeRequestCallBack callback) {
        Call<ResponseBody> call = apiService.executeGet(configUrl);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                TLog.d(TAG, "onResponse: " + response.body().toString());
                if (response.isSuccessful()) {
                    try {
                        callback.requestSuccess(response.body().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                        callback.requestError(e.getMessage());
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                TLog.d(TAG, "onFailure" + t.toString());
                t.printStackTrace();
                callback.requestError(t.toString());
            }
        });
    }

    /**
     * 文件下载
     *
     * @param fileUrl  下载地址
     * @param dirPath  本地保存文件夹名
     * @param fileName 本地保存文件名
     * @param callback 下载回调
     */
    @Override
    public void download(final String fileUrl, final String dirPath, final String fileName, final DownloadListener callback) {
        Call<ResponseBody> call = apiService.downloadFile(fileUrl);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, final Response<ResponseBody> response) {
                Log.e(TAG, "onResponse: code = " + response.code());
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        InputStream is = null;
                        FileOutputStream fos = null;
                        BufferedInputStream bis = null;
                        try {
                            is = response.body().byteStream();
                            //获取文件总长度
                            long totalLength = response.body().contentLength();
                            long currentLength = 0;
                            boolean done = false;
                            int progress = 0;

                            //创建文件夹
                            File f = new File(dirPath);
                            if (!f.exists()) {
                                f.mkdirs();
                            }

                            //创建文件
                            File file = new File(f, fileName);
                            if (file.exists()) {
                                Log.i(TAG, "文件已经纯在了，删除");
                                file.delete();
                            }

                            fos = new FileOutputStream(file);
                            bis = new BufferedInputStream(is);
                            byte[] buffer = new byte[1024];
                            int len;
                            while ((len = bis.read(buffer)) != -1) {
                                fos.write(buffer, 0, len);
                                //此处进行更新操作
                                //len即可理解为已下载的字节数
                                //onLoading(len, totalLength);
                                currentLength += len;
                                progress = (int) (100 * currentLength / totalLength);
                                if (progress == 100) {
                                    done = true;
                                }
                                callback.onLoading(totalLength, currentLength, done);
                                Log.w(TAG, "totalLength: " + totalLength + ",currentLength: " + currentLength + " ,progress: " + progress);
                            }
                            fos.flush();
                            TLog.d(TAG, "文件保存成功： " + file.getPath());
                            callback.onSuccess(file);
                        } catch (IOException e) {
                            e.printStackTrace();
                            callback.onFailure(e);
                        } finally {
                            closeStream(is);
                            closeStream(fos);
                            closeStream(bis);
                        }
                    }
                }).start();
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                TLog.d(TAG, "onFailure: " + t.toString());
                t.printStackTrace();
                callback.onFailure(t);
            }
        });
    }

    /**
     * 关闭IO流
     */
    private void closeStream(Closeable stream) {
        if (stream != null) {
            try {
                stream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 上送数据
     *
     * @param url             上送地址
     * @param json            JSO弄数据,默认传入数据为json格式，无需再次进行object to json转换
     * @param requestCallback 上送回调
     */
    public void upLoadJson(String url, String json, final RequestCallback requestCallback) {
        TLog.d(TAG, "json: " + json + "\n");
        final RequestBody requestBody = RequestBody.create(MediaType.parse(
                "application/json; charset=utf-8"), json);
        Call<ResponseBody> call = apiService.upLoadJson(url, requestBody);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    TLog.v(TAG, "isSuccessful onResponse: " + response.body().toString());
                    try {
                        requestCallback.onSuccess(response.body().string());
                    } catch (IOException e) {
                        requestCallback.onFaliue(e.getMessage());
                        e.printStackTrace();
                    }
                } else {
                    TLog.v(TAG, "is not Success");
                    requestCallback.onFaliue("request is failure");
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                TLog.v(TAG, "onFailure");
                requestCallback.onFaliue(t.getMessage());
            }
        });
    }

    /**
     * 单文件上传，表单形式
     *
     * @param url             服务器地址
     * @param file            文件地址
     * @param requestCallback 回调
     * @param map             参数列表,key为参数名，value为参数值
     */
    public void upLoadFile(String url, Map<String, String> map, File file, final RequestCallback requestCallback) {
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
        Call<ResponseBody> call = apiService.upLoadFile(url, mapRequestBody, filePart);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    TLog.v(TAG, "isSuccessful onResponse: " + response.body().toString());
                    try {
                        requestCallback.onSuccess(response.body().string());
                    } catch (IOException e) {
                        requestCallback.onFaliue(e.getMessage());
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                TLog.v(TAG, "onFailure");
                requestCallback.onFaliue(t.getMessage());
            }
        });
    }
}
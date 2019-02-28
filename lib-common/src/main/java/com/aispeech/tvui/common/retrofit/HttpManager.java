package com.aispeech.tvui.common.retrofit;


import android.util.Log;

import com.aispeech.tvui.common.interfaces.DownloadCallback;
import com.aispeech.tvui.common.interfaces.RequestCallback;
import com.aispeech.tvui.common.interfaces.RetrofitDownCallback;
import com.aispeech.tvui.common.util.TLog;

import java.io.BufferedInputStream;
import java.io.Closeable;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;

/**
 * HTTP网络请求入口
 */
public class HttpManager {
    private String TAG = "HttpManager";
    private static volatile HttpManager instance;

    /**
     * 是否保存下载的文件
     */
    private boolean isSave = true;

    private HttpManager() {

    }

    public static HttpManager getInstance() {
        if (instance == null) {
            synchronized (HttpManager.class) {
                if (instance == null) {
                    instance = new HttpManager();
                }
            }
        }
        return instance;
    }

    /**
     * 普通Get请求
     *
     * @param url
     */
    public void get(String url, final RequestCallback callback) {
        RetrofitClient.getInstance().asyncGet(url, callback);
    }

    /**
     * 带参数get请求
     *
     * @param url
     * @param map
     */
    public void get(String url, Map<String, String> map, final RequestCallback callback) {
        RetrofitClient.getInstance().asyncGet(url, map, callback);
    }

    /**
     * 1.下载并文件
     * 2.跟踪下载文件的进度
     *
     * @param fileUrl  下载文件url
     * @param dirPath  保存的目录名称
     * @param fileName 保存的文件名
     * @param callback 下载回调
     */
    public void download(String fileUrl, final String dirPath, final String fileName, final DownloadCallback callback) {
        //保存文件
        RetrofitClient.getInstance().download(fileUrl, new RetrofitDownCallback<ResponseBody>() {
            @Override
            public void onSuccess(Call<ResponseBody> call, Response<ResponseBody> response) {
                boolean isSuccessful = response.isSuccessful();
                int code = response.code();
                Log.i(TAG, "onSuccess: code = " + code + " ,isSuccessful = " + isSuccessful);
                if (isSave && isSuccessful) {
                    saveFile(response, dirPath, fileName, callback); //保存文件
                }
            }

            @Override
            public void onLoading(long total, long progress, boolean done) {
                Log.i(TAG, "onLoading " + (float) (progress * 1.0 / total) * 100 + "% , " + (done ? "下载完成" : "未下载完成"));
                if (callback != null) {
                    callback.onLoading(total, progress, done);
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.i(TAG, "onFailure " + t.getMessage());
                if (callback != null) {
                    callback.onFailure(t);
                }
            }
        });
    }

    /**
     * 1.下载并保存文件
     * 2.跟踪保存文件的进度
     *
     * @param fileUrl  下载文件url
     * @param dirPath  保存的目录名称
     * @param fileName 保存的文件名
     * @param callback 下载回调
     */
    public void download2(String fileUrl, final String dirPath, final String fileName, final DownloadCallback callback) {
        RetrofitClient.getInstance().download2(fileUrl, new RetrofitDownCallback<ResponseBody>() {
            @Override
            public void onSuccess(Call<ResponseBody> call, final Response<ResponseBody> response) {
                int code = response.code();
                boolean isSuccessful = response.isSuccessful();
                Log.i(TAG, "onSuccess: code = " + code + " ,isSuccessful = " + isSuccessful);
                if (isSave) {
                    saveFile2(response, dirPath, fileName, callback);
                }
            }

            @Override
            public void onLoading(long total, long progress, boolean done) {

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                TLog.e(TAG, "onFailure " + t.getMessage());
                if (callback != null) {
                    callback.onFailure(t);
                }
            }
        });

    }

    /**
     * 1.保存文件到指定目录
     * 2.并回传文件保存进度
     *
     * @param response
     * @param dirPath  保存目录
     * @param fileName 文件名
     * @param callback 回调
     */
    private void saveFile2(final Response<ResponseBody> response, final String dirPath, final String fileName, final DownloadCallback callback) {
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

    /**
     * 保存文件到指定目录
     *
     * @param response
     * @param dirPath  保存目录
     * @param fileName 文件名
     * @param callback 回调
     */
    private void saveFile(Response<ResponseBody> response, String dirPath, String fileName, DownloadCallback callback) {
        InputStream is = null;
        FileOutputStream fos = null;
        BufferedInputStream bis = null;
        try {
            is = response.body().byteStream();
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
            }
            fos.flush();
            Log.i(TAG, "文件保存成功");
            if (callback != null) {
                callback.onSuccess(file);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            closeStream(is);
            closeStream(fos);
            closeStream(bis);
        }
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
}

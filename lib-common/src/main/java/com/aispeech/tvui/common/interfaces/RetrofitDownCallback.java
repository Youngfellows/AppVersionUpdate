package com.aispeech.tvui.common.interfaces;


import retrofit2.Call;
import retrofit2.Response;

/**
 * 下载回调
 */
public interface RetrofitDownCallback<T> {

    public void onSuccess(Call<T> call, Response<T> response);

    public void onLoading(long total, long progress, boolean done);

    public void onFailure(Call<T> call, Throwable t);
}

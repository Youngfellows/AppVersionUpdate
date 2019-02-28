package com.aispeech.upgrade.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.ColorRes;
import android.support.annotation.DrawableRes;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.aispeech.upgrade.R;
import com.aispeech.upgrade.receiver.HomeEventReceiver;
import com.aispeech.upgrade.utils.DialogUtils;


/**
 * Created by jie.chen on 2018/4/21.
 * 1.安装更新对话框
 * 2.提供是否有进度条，用于处理用户下载进度
 * 3.提供是否强制升级的设置
 */

public class UpgradeDialog {
    public static String TAG = "UpgradeDialog";

    public static class Builder {
        private Context mContext;
        private Dialog mDialog;
        private View mView;
        private ViewHolder mViewHolder;
        private int mWidth;//屏幕宽
        private int mHeight;//屏幕高
        private boolean mForcedUpdate;//设置是否强制更新
        HomeEventReceiver mHomeEventReceiver;
        private Handler mHandler;

        public Builder(Context context) {
            mContext = context;
            initView();
        }

        /**
         * 初始化对话框
         */
        private void initView() {
            mDialog = new Dialog(mContext, DialogUtils.getStyle());
            mHandler = new Handler(Looper.getMainLooper());
            //            mDialog = new Dialog(mContext);
//            mView = LayoutInflater.from(mContext).inflate(R.layout.update_app_dialog, null);
            mView = LayoutInflater.from(mContext).inflate(R.layout.layout_dialog, null);
            mViewHolder = new ViewHolder(mView);
            mDialog.setContentView(mView);//加载布局

            //根据屏幕大小设置对话框大小
            setDialogSize();
            mHomeEventReceiver = new HomeEventReceiver();// 注册监听HOME键的广播
            HomeEventReceiver.setHomeKeyeventListener(homeKeyeventListener);
        }

        private HomeEventReceiver.HomeKeyeventListener homeKeyeventListener = new HomeEventReceiver.HomeKeyeventListener() {
            @Override
            public void homePress() {
                dismiss();
            }
        };

        /**
         * 根据屏幕大小  设置整体屏幕 标题，按钮的所占位置
         */
        private void setDialogSize() {
            //设置可以在服务或者广播中开启弹框
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){//6.0+
                mDialog.getWindow().setType(WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY);
            }else {
                mDialog.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
            }
            WindowManager windowManager = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
            mWidth = windowManager.getDefaultDisplay().getWidth();
            mHeight = windowManager.getDefaultDisplay().getHeight();
            Log.i(TAG, "屏幕宽: " + mWidth + " 屏幕高： " + mHeight);
            //            mWidth *= 0.390625;//宽度的40%
            //            mHeight *= 0.37;
            mWidth = mWidth / 2;
            mHeight = mHeight / 2;
            Log.i(TAG, "宽: " + mWidth + " ,高: " + mHeight);

            //设置对话框的大小
            FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(mWidth, mHeight);
            mViewHolder.llDialog.setLayoutParams(layoutParams);

            //设置Title
            mViewHolder.tvTitle.measure(0, 0);
            LinearLayout.LayoutParams titleParams = new LinearLayout.LayoutParams(mWidth, mViewHolder.tvTitle.getMeasuredHeight());
            titleParams.topMargin = dip2px(28);
            mViewHolder.tvTitle.setLayoutParams(titleParams);


            //新特性
            mViewHolder.tvFeature.measure(0, 0);
            LinearLayout.LayoutParams featureParams = new LinearLayout.LayoutParams(mWidth, mViewHolder.tvFeature.getMeasuredHeight());
            featureParams.setMarginStart(dip2px(70));
            featureParams.topMargin = dip2px(29);
            mViewHolder.tvFeature.setLayoutParams(featureParams);

            //更新日志
            mViewHolder.svUpdateInfo.measure(0, 0);
            LinearLayout.LayoutParams svLayoutParams = new LinearLayout.LayoutParams(mWidth, mViewHolder.svUpdateInfo.getMeasuredHeight());
            svLayoutParams.setMarginStart(dip2px(70));
            mViewHolder.svUpdateInfo.setLayoutParams(svLayoutParams);

            //底部按钮
            mViewHolder.llBottom.measure(0, 0);
            LinearLayout.LayoutParams llBotomParams = new LinearLayout.LayoutParams(mWidth, dip2px(40));
            llBotomParams.topMargin = dip2px(28);
            llBotomParams.bottomMargin = dip2px(22);
            mViewHolder.llBottom.setLayoutParams(llBotomParams);
        }


        /**
         * 设置是否强制更新
         */
        public Builder setForcedUpdate() {
            mForcedUpdate = true;
            Log.i(TAG, "是否强制升级: mForcedUpdate = " + mForcedUpdate);
            if (mForcedUpdate) {
                //底部按钮
                mViewHolder.llBottom.measure(0, 0);
                LinearLayout.LayoutParams llBotomParams = new LinearLayout.LayoutParams(mWidth, dip2px(40));
                llBotomParams.topMargin = dip2px(30);
                llBotomParams.bottomMargin = dip2px(10);
                mViewHolder.llBottom.setLayoutParams(llBotomParams);

                mViewHolder.llOk.measure(0, 0);
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(mWidth, dip2px(40));
                mViewHolder.llOk.setLayoutParams(layoutParams);
            }
            return this;
        }


        /**
         * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
         */
        private int dip2px(float dpValue) {
            final float scale = mContext.getResources().getDisplayMetrics().density;
            return (int) (dpValue * scale + 0.5f);
        }

        /**
         * 设置字体颜色
         *
         * @param colorId
         */
        public Builder setTextColor(@ColorRes int colorId) {
            mViewHolder.tvTitle.setTextColor(mContext.getResources().getColor(colorId));
            mViewHolder.tvFeature.setTextColor(mContext.getResources().getColor(colorId));
            mViewHolder.tvUpdateInfo.setTextColor(mContext.getResources().getColor(colorId));
            return this;
        }

        /**
         * 设置按钮的选择框
         *
         * @return
         */
        public Builder setButtonSelector(@DrawableRes int selectorId) {
            mViewHolder.btnOk.setBackground(mContext.getResources().getDrawable(selectorId));
            mViewHolder.btnCancle.setBackground(mContext.getResources().getDrawable(selectorId));
            return this;
        }

        /**
         * 设置背景色
         *
         * @param drawableId
         * @return
         */
        public Builder setBackground(@DrawableRes int drawableId) {
            mViewHolder.llDialog.setBackgroundResource(drawableId);
            return this;
        }

        /**
         * 设置Title
         *
         * @param title
         */
        public Builder setTitle(String title) {
            mViewHolder.tvTitle.setText(title);
            return this;
        }

        /**
         * 设置更新特性
         *
         * @param feature
         */
        public Builder setFeature(String feature) {
            mViewHolder.tvFeature.setText(feature);
            return this;
        }

        /**
         * 设置更新日志
         *
         * @param updateInfo
         */
        public Builder setUpdateInfo(String updateInfo) {
            mViewHolder.tvUpdateInfo.setText(updateInfo);
            return this;
        }


        /**
         * 设置默认选中的按钮
         *
         * @return
         */
        public Builder setDefaultSelect() {
            //不是强制更新默认选中确认
            mViewHolder.tvTitle.setFocusable(false);
            mViewHolder.tvFeature.setFocusable(false);
            mViewHolder.tvUpdateInfo.setFocusable(false);
            mViewHolder.btnOk.requestFocus();//默认选中立即升级
            return this;
        }

        /**
         * 设置确认按钮
         *
         * @param positive 确认的文本信息
         * @param listener 点击事件
         */
        public Builder setPositiveButton(String positive, final View.OnClickListener listener) {
            mViewHolder.btnOk.setText(positive);
            mViewHolder.btnOk.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dismiss();//取消对话框
                    if (listener != null) {
                        listener.onClick(v);
                    }
                }
            });
            mViewHolder.btnOk.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    Log.i(TAG, "立即升级是否获取到焦点: " + hasFocus);
                }
            });
            return this;
        }

        /**
         * 设置取消按钮
         *
         * @param negative 取消的文本信息
         * @param listener 点击事件
         */
        public Builder setNegativeButton(String negative, final View.OnClickListener listener) {
            mViewHolder.btnCancle.setText(negative);
            mViewHolder.btnCancle.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dismiss();//消失对话框
                    if (listener != null) {
                        listener.onClick(v);
                    }
                }
            });
            mViewHolder.btnCancle.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    Log.i(TAG, "下载再说是否获取到焦点: " + hasFocus);
                }
            });
            return this;
        }

        /**
         * 显示对话框
         */
        public void show() {
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    if (mDialog != null) {
                        //强制更新
                        if (mForcedUpdate) {
                            mViewHolder.btnCancle.setVisibility(View.GONE);
                        }
                        mDialog.show();
                        mContext.registerReceiver(mHomeEventReceiver, new IntentFilter(Intent.ACTION_CLOSE_SYSTEM_DIALOGS));
                        Log.i(TAG, "show: mDialogStateListener = " + mDialogStateListener);
                        if (mDialogStateListener != null) {
                            mDialogStateListener.onShow();
                        }
                    }
                }
            });
        }

        /**
         * 消失对话框
         */
        public void dismiss() {
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    if (mDialog != null) {
                        if (mDialog.isShowing()) {
                            mDialog.dismiss();
                        }
                    }
                    mContext.unregisterReceiver(mHomeEventReceiver);
                    Log.i(TAG, "dismiss: mDialogStateListener = " + mDialogStateListener);
                    if (mDialogStateListener != null) {
                        mDialogStateListener.onDismiss();
                    }
                }
            });
        }


        class ViewHolder {
            LinearLayout llDialog;
            LinearLayout llOk;
            LinearLayout llCancel;
            TextView tvTitle;
            TextView tvFeature;
            TextView tvUpdateInfo;
            Button btnCancle;
            Button btnOk;
            ScrollView svUpdateInfo;
            LinearLayout llBottom;

            public ViewHolder(View view) {
                llDialog = (LinearLayout) view.findViewById(R.id.dialog_layout);
                llOk = (LinearLayout) view.findViewById(R.id.ll_ok);
                llCancel = (LinearLayout) view.findViewById(R.id.ll_cancel);
                tvTitle = (TextView) view.findViewById(R.id.tv_title);
                tvFeature = (TextView) view.findViewById(R.id.tv_feature);
                tvUpdateInfo = (TextView) view.findViewById(R.id.tv_update_info);
                btnCancle = (Button) view.findViewById(R.id.btn_cancle);
                btnOk = (Button) view.findViewById(R.id.btn_ok);
                svUpdateInfo = (ScrollView) view.findViewById(R.id.sv_update_info);
                llBottom = (LinearLayout) view.findViewById(R.id.ll_bottom);
            }
        }
    }

    public static DialogStateListener mDialogStateListener;

    public static void setDialogStateListener(DialogStateListener dialogStateListener) {
        mDialogStateListener = dialogStateListener;
    }
}

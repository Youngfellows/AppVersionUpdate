package com.aispeech.upgrade.utils;

import android.support.annotation.StyleRes;

import com.aispeech.upgrade.R;

/**
 * Created by Byron on 2018/4/22.
 */

public class DialogUtils {
    private static int mStyle = R.style.UpdateDialogStyle;

    public static void initStyle(@StyleRes int style) {
        mStyle = style;
    }

    public static int getStyle() {
        return mStyle;
    }

}

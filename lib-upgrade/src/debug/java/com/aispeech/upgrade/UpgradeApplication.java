package com.aispeech.upgrade;

import android.app.Application;


/**
 * @author henrychen
 * @version $Rev$
 * @email henrychen@aispeech.com
 * @time 2018/10/17 15:26
 * @des ${TODO}
 * @updateAuthor $Author$
 * @updateDate $Date$
 * @updateDes ${TODO}
 */

public class UpgradeApplication extends Application {
    private static Application mApplication;

    public static Application getApplication() {
        return mApplication;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        this.mApplication = this;
    }
}

package com.aispeech.upgradeaar;

import android.app.Application;


/**
 * @author henrychen
 * @version $Rev$
 * @email henrychen@aispeech.com
 * @time 2018/10/18 18:40
 * @des ${TODO}
 * @updateAuthor $Author$
 * @updateDate $Date$
 * @updateDes ${TODO}
 */

public class App extends Application {
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

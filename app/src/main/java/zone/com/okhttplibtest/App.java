package zone.com.okhttplibtest;


import android.app.Application;

import com.facebook.stetho.Stetho;

import zone.com.okhttplib.Config;

public class App extends Application {

    private static final String KEY_APP_ID = "appid";
    private static final String KEY_APK_SIGNATURE = "app_signature";

    private static App instance;

    @Override
    public void onCreate() {
        super.onCreate();
        Config.getInstance().setContext(this);
        instance = this;
        Stetho.initialize(Stetho
                .newInitializerBuilder(this)
//                        .enableDumpapp( Stetho.defaultDumperPluginsProvider(this))
                .enableWebKitInspector(Stetho.defaultInspectorModulesProvider(this))
                .build());
    }


    public static App getInstance() {
        return instance;
    }


    /**
     * 判断当前版本是否兼容目标版本的方法
     *
     * @param VersionCode
     * @return
     */
    public boolean isMethodsCompat(int VersionCode) {
        int currentVersion = android.os.Build.VERSION.SDK_INT;
        return currentVersion >= VersionCode;
    }

}

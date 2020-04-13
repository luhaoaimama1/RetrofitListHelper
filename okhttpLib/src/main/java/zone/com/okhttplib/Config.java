package zone.com.okhttplib;

import android.content.Context;

public class Config {
    //主要是为了  java测试 而不是Android平台
    public static boolean isAPP = true;

    private static volatile Config singleton;

    private Config() {
    }

    public static Config getInstance() {
        if (singleton == null) {
            synchronized (Config.class) {
                if (singleton == null) {
                    singleton = new Config();
                }
            }
        }
        return singleton;
    }

    private Context context;
    private boolean isDebug=false;

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public boolean isDebug() {
        return isDebug;
    }

    //BuildConfig.DEBUG
    public void setDebug(boolean debug) {
        isDebug = debug;
    }
}
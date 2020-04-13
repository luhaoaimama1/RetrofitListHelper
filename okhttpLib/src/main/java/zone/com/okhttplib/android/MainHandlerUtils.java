package zone.com.okhttplib.android;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import zone.com.okhttplib.Config;
import zone.com.okhttplib.java.callwrapper.core.ProgressCallback;
import zone.com.okhttplib.android.utils.HandlerUiUtil;

/**
 * Created by Zone on 2016/3/17.
 */
public class MainHandlerUtils {

    public static void onLoading(final ProgressCallback listener, final long total, final long current, final long networkSpeed, final boolean isDownloading) {
        if (Config.isAPP) {
            HandlerUiUtil.post(new Runnable() {
                @Override
                public void run() {
                    listener.onLoading(total, current, networkSpeed, isDownloading);
                }
            });
        } else
            listener.onLoading(total, current, networkSpeed, isDownloading);

    }

    public static void onFailure(final Callback<ResponseBody> callBack, final Call<ResponseBody> call, final Throwable t) {
        if (Config.isAPP) {
            HandlerUiUtil.post(new Runnable() {
                @Override
                public void run() {
                    callBack.onFailure(call, t);
                }
            });
        } else
            callBack.onFailure(call, t);
    }
}

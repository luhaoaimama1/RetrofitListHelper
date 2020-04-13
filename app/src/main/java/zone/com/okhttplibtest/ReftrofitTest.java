package zone.com.okhttplibtest;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import zone.com.okhttplib.Config;
import zone.com.okhttplib.java.callwrapper.core.ProgressCallback;
import zone.com.sdk.API.file.api.FileImpl;
import zone.com.okhttplib.java.callwrapper.RequestBodyHelper;
import zone.com.sdk.API.gank.api.GankImpl;
import zone.com.sdk.API.gank.bean.MeiZiData;

/**
 * [2017] by Zone
 * <p>
 * 测试各种泛型
 */

public class ReftrofitTest {

    public static void main(String[] args) {
        Config.isAPP = false;

//        rxjavaHttp();
//        upload();
        down();
    }

    private static void rxjavaHttp() {
        new GankImpl()
                .getPics("5", "5")
                .delayDismiss(5000)
                .enqueue(new Callback<MeiZiData>() {
                    @Override
                    public void onResponse(Call<MeiZiData> call, Response<MeiZiData> response) {
                        //UI线程
                        System.out.println("pop==>onResponse");
                    }

                    @Override
                    public void onFailure(Call<MeiZiData> call, Throwable t) {
                        System.out.println("pop==>onFailure");
                    }
                });
    }


    private static void down() {
        new FileImpl().downLoad().enqueue(new ProgressCallback<ResponseBody>() {
            @Override
            public void onLoading(long total, long current, long networkSpeed, boolean isDownloading) {
                System.out.println("下载:" + current * 1f / total);
            }

            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });
    }

    private static void upload() {
        File file2 = new File("/Users/fuzhipeng/Documents/psb (2).jpeg");
        RequestBodyHelper rp = new RequestBodyHelper();
        rp.put("mFile", file2);
        rp.put("String_uid", "love");

        RequestBody body = rp.createRequestBody(new ProgressCallback() {
            @Override
            public void onLoading(long total, long current, long networkSpeed, boolean isDownloading) {
                System.out.println("上传："+current * 1f / total);
            }

            @Override
            public void onResponse(Call call, Response response) {

            }

            @Override
            public void onFailure(Call call, Throwable t) {

            }
        });
        Map<String, RequestBody> maps = new HashMap<>();
        maps.put("part1", body);
        new FileImpl().uploadFiles(maps).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {

            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {

            }
        });
    }


}

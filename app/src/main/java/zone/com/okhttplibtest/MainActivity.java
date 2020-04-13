package zone.com.okhttplibtest;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import ezy.ui.layout.LoadingLayout;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import zone.com.okhttplib.java.callwrapper.DialogCall;
import zone.com.okhttplib.android.utils.HandlerUiUtil;
import zone.com.okhttplib.android.views.LoadingDialog;
import zone.com.okhttplib.android.views.LoadingPopWindow;
import zone.com.sdk.API.gank.bean.MeiZiData;
import zone.com.okhttplibtest.utils.GsonUtils;
import zone.com.sdk.Diycode;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {


    LinearLayout llRoot;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        llRoot = findViewById(R.id.ll_root);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_rxjavaSync:
                onRxjavaSyncClick();
                break;
            case R.id.bt_rxjava:
                onRxjavaClick();
                break;
            case R.id.bt_pop:
                onPopClick();
                break;
            case R.id.bt_Dlg:
                onDlgClick();
                break;
            case R.id.bt_DelayDialog:
                onDelayDialogClick();
                break;
            case R.id.bt_FristLoading:
                onFristLoadingClick();
                break;

        }
    }

    public void onRxjavaSyncClick() {
        Diycode.getInstance()
                .getPics("5", "2", 2)
                .popWindow(new LoadingPopWindow(this))
                .delayDismiss(5000)
                .enqueueObservable()
                .subscribe(o -> System.out.println("Sync 妹子==>：" + GsonUtils.toJson(o))
                        , throwable -> System.out.println("Sync 异常==>" + throwable)
                        , () -> {
                            toastSuccess();
                            System.out.println("Sync 成功==>");
                        });
    }

    public void onRxjavaClick() {
        Diycode.getInstance()
                .getPics("5", "2", 2)
                .executeObservable()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(o -> System.out.println("同步 妹子==>：" + GsonUtils.toJson(o))
                        , throwable -> System.out.println("同步  异常==>" + throwable)
                        , () -> {
                            toastSuccess();
                            System.out.println("同步  成功==>");
                        });
    }

    public void onPopClick() {
        Diycode.getInstance()
                .getPics("5", "5")
                .popWindow(new LoadingPopWindow(this))
                .delayDismiss(5000)
                .enqueue(new Callback<MeiZiData>() {
                    @Override
                    public void onResponse(Call<MeiZiData> call, Response<MeiZiData> response) {
                        //UI线程
                        toastSuccess();
                        System.out.println("pop==>onResponse");
                    }

                    @Override
                    public void onFailure(Call<MeiZiData> call, Throwable t) {
                        System.out.println("pop==>onFailure");
                    }
                });
    }

    private void toastSuccess() {
        Toast.makeText(MainActivity.this, "success!", Toast.LENGTH_SHORT).show();
    }

    public void onDlgClick() {
        Diycode.getInstance()
                .getPics("5", "5")
                .dialog(new LoadingDialog(this))
                .enqueue(new Callback<MeiZiData>() {
                    @Override
                    public void onResponse(Call<MeiZiData> call, Response<MeiZiData> response) {
                        //UI线程
                        toastSuccess();
                        System.out.println("dialog==>onResponse");
                    }

                    @Override
                    public void onFailure(Call<MeiZiData> call, Throwable t) {
                        System.out.println("dialog==>onFailure");
                    }
                });
    }

    public void onDelayDialogClick() {
        final LoadingDialog dialog = new LoadingDialog(this);
        Diycode.getInstance()
                .getPics("5", "5")
                .OnLoadingListener(new DialogCall.OnLoadingListener() {
                    @Override
                    public void onLoading(DialogCall.State state) {
                        switch (state) {
                            case Loading:
                                dialog.show();
                                break;
                            case Success:
                            case Error:
                                toastSuccess();
                                HandlerUiUtil.postDelay(new Runnable() {
                                    @Override
                                    public void run() {
                                        dialog.dismiss();
                                    }
                                }, 2000);
                                break;
                        }
                    }
                })
                .enqueue(new Callback<MeiZiData>() {
                    @Override
                    public void onResponse(Call<MeiZiData> call, Response<MeiZiData> response) {

                    }

                    @Override
                    public void onFailure(Call<MeiZiData> call, Throwable t) {

                    }
                });
    }

    public void onFristLoadingClick() {
        Diycode.getInstance()
                .getPics("5", "5")
                .loadingLayout(LoadingLayout.wrap(llRoot))
                .enqueue(new Callback<MeiZiData>() {
                    @Override
                    public void onResponse(Call<MeiZiData> call, Response<MeiZiData> response) {
                        toastSuccess();
                        System.out.println("onFristLoadingClick==>onResponse");
                    }

                    @Override
                    public void onFailure(Call<MeiZiData> call, Throwable t) {
                        System.out.println("onFristLoadingClick==>onFailure");
                    }
                });
    }

}

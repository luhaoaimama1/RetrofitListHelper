package zone.com.okhttplib.java.callwrapper;

import android.app.Dialog;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import android.view.View;

import java.io.IOException;

import ezy.ui.layout.LoadingLayout;
import io.reactivex.Observable;
import okhttp3.Request;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import zone.com.okhttplib.Config;
import zone.com.okhttplib.java.callwrapper.rxjavahelper.CallEnqueueObservable;
import zone.com.okhttplib.java.callwrapper.rxjavahelper.CallExecuteObservable;
import zone.com.okhttplib.android.utils.HandlerUiUtil;
import zone.com.okhttplib.android.views.BasePopWindow;

/**
 * [2017] by Zone
 * <p>
 * 主要是增加
 * <p>
 * 支持rxjava2
 * 支持 请求与dialog状态 的功能;
 * <p>
 * 只能绑定mLoadingLayout，mPopWindow，mDialog中的一个如果有多个会优先采取上面的一个
 *
 * @param <T>
 */
public class DialogCall<T> implements Call<T> {

    private Call<T> call;
    @Nullable
    private LoadingLayout mLoadingLayout;
    @Nullable
    private BasePopWindow mPopWindow;
    @Nullable
    private Dialog mDialog;
    private Callback<T> mCallback;
    private long delayMillis;

    //default 空实现  可以用来监视内部状态 自己去更改
    private OnLoadingListener mOnLoadingListener = new OnLoadingListenerDefault();


    public DialogCall(Call<T> call) {
        this.call = call;
    }


    public void setCall(Call<T> call) {
        this.call = call;
    }

    public Call<T> getCall() {
        return call;
    }

    // =======================================
    // ============ Call原生方法 ==============
    // =======================================
    @Override
    public Response execute() throws IOException {
        return call.execute();
    }

    @Override
    public boolean isExecuted() {
        return call.isExecuted();
    }

    @Override
    public void cancel() {
        call.cancel();
    }

    @Override
    public boolean isCanceled() {
        return call.isCanceled();
    }

    @Override
    public Call clone() {
        return call.clone();
    }

    @Override
    public Request request() {
        return call.request();
    }

    @Override
    public void enqueue(@NonNull Callback callback) {
        mCallback = callback;
        if (!(mCallback instanceof DialogCall.CallBackDelegate)) {
            mCallback = new CallBackDelegate(callback, new OnLoadingListenerInner(mOnLoadingListener));
        }
        ((CallBackDelegate) mCallback).onLoadingListenerInner.onLoading(State.Loading);
        call.enqueue(mCallback);
    }

    //这个是为了rxjava使用的
    public Observable<T> enqueueObservable() {
        return new CallEnqueueObservable<T>(this);
    }

    //这个是为了rxjava使用的
    public Observable<T> executeObservable() {
        return new CallExecuteObservable<T>(this);
    }


    // =======================================
    // ============ extra方法 ==============
    // =======================================


    public DialogCall<T> delayDismiss(long delayMillis) {
        this.delayMillis = delayMillis;
        return this;
    }

    public DialogCall<T> loadingLayout(@NonNull final LoadingLayout loadingLayout) {
        this.mLoadingLayout = loadingLayout;
        loadingLayout.setRetryListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                call = call.clone();//因为重试的话 所以需要clone以下
                enqueue(mCallback);
            }
        });
        return this;
    }

    public DialogCall<T> dialog(@NonNull final Dialog dialog) {
        this.mDialog = dialog;
        return this;
    }

    public DialogCall<T> popWindow(@NonNull final BasePopWindow popWindow) {
        this.mPopWindow = popWindow;
        return this;
    }

    public DialogCall<T> OnLoadingListener(@NonNull OnLoadingListener loadingListener) {
        mOnLoadingListener = loadingListener;
        return this;
    }

    public interface OnLoadingListener {
        void onLoading(State state);
    }

    public class OnLoadingListenerDefault implements OnLoadingListener {
        @Override
        public void onLoading(State state) {
            switch (state) {
                case Loading:
                    if (mLoadingLayout != null)
                        mLoadingLayout.showLoading();
                    else if (mPopWindow != null)
                        mPopWindow.show();
                    else if (mDialog != null)
                        mDialog.show();
                    break;
                case Success:
                    if (mLoadingLayout != null)
                        mLoadingLayout.showContent();
                    else if (mPopWindow != null)
                        mPopWindow.dismiss();
                    else if (mDialog != null)
                        mDialog.dismiss();
                    break;
                case Error:
                    if (mLoadingLayout != null)
                        mLoadingLayout.showError();
                    else if (mPopWindow != null)
                        mPopWindow.dismiss();
                    else if (mDialog != null)
                        mDialog.dismiss();
                    break;
            }
        }
    }

    public enum State {
        Error, Loading, Success;
    }


    /**
     * 真正的实现
     */
    private class CallBackDelegate<T> implements Callback<T> {

        final OnLoadingListenerInner onLoadingListenerInner;
        private Callback<T> callBack;

        public CallBackDelegate(Callback<T> callBack, @NonNull OnLoadingListenerInner onLoadingListenerInner) {
            this.callBack = callBack;
            this.onLoadingListenerInner = onLoadingListenerInner;
            onLoadingListenerInner.onLoading(State.Loading);
        }

        @Override
        public void onResponse(Call<T> call, Response<T> response) {
            if (callBack != null)
                callBack.onResponse(call, response);
            boolean successful = false;
            if (response.isSuccessful()) {
                successful = true;
            }
            doLast(successful, call, response, null);
        }

        private void doLast(boolean successful, Call<T> call, Response<T> response, Throwable t) {
            if (successful) {
                onLoadingListenerInner.onLoading(State.Success);
            } else
                onLoadingListenerInner.onLoading(State.Error);
        }

        @Override
        public void onFailure(Call<T> call, Throwable t) {
            if (callBack != null)
                callBack.onFailure(call, t);
            doLast(false, call, null, t);
        }
    }


    /**
     * 被委任的实现 这里处理 pop dialog等功能
     */
    private class OnLoadingListenerInner implements OnLoadingListener {
        private final OnLoadingListener onLoadingListener;

        OnLoadingListenerInner(@NonNull OnLoadingListener onLoadingListener) {
            this.onLoadingListener = onLoadingListener;
        }

        @Override
        public void onLoading(final State state) {

            if (!Config.isAPP)
                return;

            if (state != State.Loading)
                HandlerUiUtil.postDelay(new Runnable() {
                    @Override
                    public void run() {
                        onLoadingListener.onLoading(state);
                    }
                }, delayMillis);
            else
                HandlerUiUtil.post(new Runnable() {
                    @Override
                    public void run() {
                        onLoadingListener.onLoading(state);
                    }
                });

        }
    }

}
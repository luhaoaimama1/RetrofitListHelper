/*
 * Copyright 2017 GcsSloop
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * Last modified 2017-03-08 01:01:18
 *
 * GitHub:  https://github.com/GcsSloop
 * Website: http://www.gcssloop.com
 * Weibo:   http://weibo.com/GcsSloop
 */

package zone.com.okhttplib;

import android.content.Context;

import com.facebook.stetho.okhttp3.StethoInterceptor;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.ParameterizedType;

import okhttp3.Cache;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Retrofit;
import zone.com.okhttplib.java.callwrapper.DialogCall;
import zone.com.okhttplib.java.callwrapper.DownLoadCall;
import zone.com.okhttplib.java.cookie.store.CookieStore;

/**
 * 实现类，具体实现在此处
 *
 * @param <Service>
 */
public abstract class BaseOKHttpClient<Service> {

    protected Context context;
    private static Retrofit mRetrofit;
    protected Service mService;

    public BaseOKHttpClient() {
        if (Config.isAPP) {
            context = Config.getInstance().getContext();
        }
        onCreate();
        initRetrofitInner();
        this.mService = mRetrofit.create(getServiceClass());
    }

    private Class<Service> getServiceClass() {
        return (Class<Service>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
    }

    protected abstract void onCreate();

    private synchronized void initRetrofitInner() {
        if(mRetrofit==null)
            synchronized (BaseOKHttpClient.class){
                if (mRetrofit == null)
                    mRetrofit=initRetrofit();
            }
    }

    protected abstract Retrofit initRetrofit() ;

    protected <T> DialogCall<T> dialogWrapper(Call<T> call) {
        return new DialogCall(call);
    }

    protected DownLoadCall downLoadWrapper(Call call, File file) {
        return new DownLoadCall((Call<ResponseBody>) call, file);
    }

    public static Retrofit getRetrofit() {
        return mRetrofit;
    }



    public static CookieStore getCookieStore(OkHttpClient client) {
        return (CookieStore) client.cookieJar();
    }

    public static Cache getCache() {
        //缓存目录
        String path = Config.getInstance().getContext().getCacheDir().getAbsolutePath()
                + File.separator + "data" + File.separator + "net_cache";
        return new Cache(new File(path), 10 * 1024 * 1024);

    }

    public static Interceptor getHttpLoggingInterceptor(boolean isDebug) {
        if (isDebug) {
            HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
            loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            return loggingInterceptor;
        } else {
            return nullInterceptor;
        }
    }

    public static Interceptor getStethoInterceptor(boolean isDebug) {
        if (isDebug) {
            return new StethoInterceptor();
        } else {
            return nullInterceptor;
        }
    }

    private static Interceptor nullInterceptor = new Interceptor() {
        @Override
        public Response intercept(Interceptor.Chain chain) throws IOException {
            return chain.proceed(chain.request());
        }
    };

}




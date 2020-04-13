# ZDialogOKHttp
#### [中文版文档](./README-cn.md)

# Usage

### JicPack
Add it in your root build.gradle at the end of repositories:

	allprojects {
		repositories {
			...
			maven { url "https://jitpack.io" }
		}
	}
Step 2. Add the dependency

    * compile 'com.github.luhaoaimama1:ZDialogOKHttp:[Latest release](https://github.com/luhaoaimama1/ZDialogOKHttp/releases)'

# Function Description

> a little practice for apt to use for apisdk. The project is focused on okhttplib.

-[x] network request library supports uploading and downloading https cookies, etc.;

-[x] may be associated with requests such as dialog pop view.

-[x] can also be associated with the network state layout generally used when first loaded;

-[x] copy from retrofit when x] support rxjava2.

```
    Some common class presentations
         DownLoadCall for download
         DialogCall network requests bound to pop-up windows
         map for RequestBodyHelper transfer can be placed directly into the file
         BaseOKHttpClient base okhttp client
         BaseImpl is a basic setup client.
```


# Easy use:

Before using the configuration

```
 Config.getInstance().setContext(this);
```

1.pop 's Link

```
   Diycode.getInstance()
                 .getPics("5", "5")
                 .popWindow(new LoadingPopWindow(this))
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
```

2.rxjava2 support

```
  Diycode.getInstance()
                .getPics("5", "2",2)
                .popWindow(new LoadingPopWindow(this))
                .delayDismiss(5000)//想让进度条显示的久一点
                .enqueueObservable()
                .subscribe(o -> System.out.println("Sync 妹子==>：" + GsonUtils.toJson(o))
                        , throwable -> System.out.println("Sync 异常==>" + throwable)
                        , () -> System.out.println("Sync 成功==>"));
```

3.firstLoad 's State associated with the interface

```
    Diycode.getInstance()
                .getPics("5", "5")
                .firstLoading(LoadingLayout.wrap(llRoot))
                .enqueue(new Callback<MeiZiData>() {
                    @Override
                    public void onResponse(Call<MeiZiData> call, Response<MeiZiData> response) {
                        System.out.println("onFristLoadingClick==>onResponse");
                    }

                    @Override
                    public void onFailure(Call<MeiZiData> call, Throwable t) {
                        System.out.println("onFristLoadingClick==>onFailure");
                    }
                });
```


# Reference&Thanks：
https://github.com/GcsSloop/diycode
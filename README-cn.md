# 功能介绍

> 对于 apt 则是用来给 apisdk用的 算是个小实践把 此项目重点是okhttplib

-[x] 网络请求库 支持上传下载 https cookies等;

-[x] 支持rxjava2

-[x] 可以与dialog pop view等进行请求关联

-[x] firstLoad那种 网络状态关联;

DownLoadCall 下载用的
DialogCall 网络请求与弹窗绑定用的
RequestBodyHelper  传参用的map可以直接放入文件
BaseOKHttpClient 基类okhttp客户端
BaseImpl 是一个基础设置client


# Easy use:

使用之前配置

```
 Config.getInstance().setContext(this);
```

1.pop关联范例:

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

2.rxjava2支持

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

3.firstLoad状态与界面关联

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

# 项目实践


# Update log

>由于每个版本更新的东西较多，所以从现在开始每个版本都会贴上更新日志.

## 1.0.2

  * 1.初始完成


# Reference&Thanks：
https://github.com/GcsSloop/diycode
# 一个模仿Retrofit动态代理方案的缓存框架

### 使用示例

1. 创建缓存业务接口：

```java
public interface IMyCache {

    /**
     * RxJava获取缓存示例
     */
    @Cache(action = Cache.GET, key = "cache_key1")
    @Strategy(strategy = Strategy.TIMELY, timeUnit = TimeUnit.DAYS)
    Observable<List<String>> getCacheRxJava(Observable<List<String>> source);

    /**
     * 普通方式获取缓存示例
     */
    @Cache(action = Cache.GET, key = "cache_key2")
    @Strategy(strategy = Strategy.EXPIRE, expireTime = 10L, timeUnit = TimeUnit.SECONDS)
    List<String> getCacheNormal(Callable<List<String>> source);

    /**
     * 放置缓存示例
     */
    @Cache(action = Cache.PUT, key = "cache_kye1")
    void setCache(List<String> source);

}
```

2. 通过Cache.create(Class<T> cache)方法创建实例对象，这里推荐使用单例设计模式，示例代码如下：

```java
public final class MyCache {

    private IMyCache mCache;

    private MyCache() {
        mCache = Cache.create(IMyCache.class);
    }

    private static final class Holder {
        static final MyCache INSTANCE = new MyCache();
    }

    private static MyCache getInstance() {
        return Holder.INSTANCE;
    }

    public static IMyCache get() {
        return getInstance().mCache;
    }

}
```

3. 在你的业务代码中使用

```java
public void getCacheRxJava() {
    // 模拟Retrofit网络数据
    List<String> data = new ArrayList<>();
    for (int i = 0; i < 10; i++) {
        data.add(i + "");
    }
    Observable<List<String>> sourceFromNetwork = Observable.just(data);

    MyCache.get()
            .getCacheRxJava(sourceFromNetwork)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(new Observer<List<String>>() {
                @Override
                public void onSubscribe(Disposable d) {

                }

                @Override
                public void onNext(List<String> cache) {

                }

                @Override
                public void onError(Throwable e) {

                }

                @Override
                public void onComplete() {

                }
            });

}

public void getCacheNormal() {
    List<String> cache = MyCache.get().getCacheNormal(new Callable<List<String>>() {
        @Override
        public List<String> call() throws Exception {
            List<String> data = new ArrayList<>();
            for (int i = 0; i < 10; i++) {
                data.add(i + "");
            }
            return data;
        }
    });
}

public void setCache() {
    List<String> data = new ArrayList<>();
    for (int i = 0; i < 10; i++) {
        data.add(i + "");
    }

    MyCache.get().setCache(data);
}
```

### API详细文档

1. 通过@Cache定义一个缓存，其中action有Cache.GET和Cache.PUT两种，分别代表获取和放置缓存；key表示缓存的键
2. 通过@Strategy定义缓存策略，其中timeUnit表示时间单位；strategy有Strategy.TIMELY和Strategy.EXPIRE两种，Strategy.TIMELY表示同一个时间段内缓存有效，例如上述示例中表示“缓存当天有效”；Strategy.EXPIRE表示从缓存放置时间开始计算，expireTime内有效，即一段时间内有效，例如示例中表示“10秒内有效”
3. 缓存接口中需要传入source参数，表示缓存来源，即没有缓存或者缓存失效了，本框架会从source获取缓存。支持RxJava方式和Callable方式获取
4. 最后，通过Cache.create(Class<T> cache)方法创建缓存实例对象
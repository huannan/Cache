package com.nan.cache;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.nan.cache.cacheapi.MyCache;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void getCacheRxJava(View view) {
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

    public void getCacheNormal(View view) {
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

    public void setCache(View view) {
        List<String> data = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            data.add(i + "");
        }

        MyCache.get().setCache(data);
    }
}
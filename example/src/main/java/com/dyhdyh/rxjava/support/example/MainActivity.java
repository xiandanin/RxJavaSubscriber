package com.dyhdyh.rxjava.support.example;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.dyhdyh.rxjava.support.RxObserver;
import com.dyhdyh.rxjava.support.RxSchedulers;

import java.util.Random;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {
    View layoutContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        layoutContent = findViewById(R.id.layout_content);
    }


    public void test(View v) {
        Observable.just("一脸懵逼")
                .map(new Function<String, String>() {
                    @Override
                    public String apply(@NonNull String s) throws Exception {
                        Thread.sleep(new Random().nextInt(3000) + 1000);
                        Log.d(Thread.currentThread().getName() + "-------------->", "map----->" + s);
                        throw new RuntimeException();
                        //return "转换---->" + s;
                    }
                })
                .compose(RxSchedulers.io2main())
                //.compose(RxCompose.loadingDialog(this))
                /*.subscribe(RxObserver.loadingDialog(this, new Consumer() {
                    @Override
                    public void accept(@NonNull Object o) throws Exception {
                        Toast.makeText(MainActivity.this, o+"--->成功", Toast.LENGTH_SHORT).show();
                    }

                }))*/
                .subscribe(RxObserver.loadingDialog(this, new Consumer<Object>() {
                    @Override
                    public void accept(@NonNull Object o) throws Exception {
                        Log.d("------------>",o+"--------->onNext");
                    }
                }));
    }

    public void test1(View v) {
        Observable.just("一脸懵逼")
                .map(new Function<String, String>() {
                    @Override
                    public String apply(@NonNull String s) throws Exception {
                        Log.d(Thread.currentThread().getName() + "-------------->", "map----->" + s);
                        return "转换---->" + s;
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(@NonNull Disposable disposable) throws Exception {
                        Log.d(Thread.currentThread().getName() + "-------------->", "doOnSubscribe----->" + disposable);
                    }
                }).doOnNext(new Consumer<String>() {
            @Override
            public void accept(@NonNull String s) throws Exception {
                Log.d(Thread.currentThread().getName() + "-------------->", "doOnNext----->" + s);
            }
        }).doAfterNext(new Consumer<String>() {
            @Override
            public void accept(@NonNull String s) throws Exception {
                Log.d(Thread.currentThread().getName() + "-------------->", "doAfterNext----->" + s);
            }
        }).doOnError(new Consumer<Throwable>() {
            @Override
            public void accept(@NonNull Throwable throwable) throws Exception {
                Log.d(Thread.currentThread().getName() + "-------------->", "doOnError----->" + throwable);
            }
        }).doOnComplete(new Action() {
            @Override
            public void run() throws Exception {
                Log.d(Thread.currentThread().getName() + "-------------->", "doOnComplete----->");
            }
        }).doFinally(new Action() {
            @Override
            public void run() throws Exception {
                Log.d(Thread.currentThread().getName() + "-------------->", "doFinally----->");
            }
        }).subscribe(new Observer<String>() {
            @Override
            public void onSubscribe(@NonNull Disposable d) {
                Log.d(Thread.currentThread().getName() + "-------------->", "onSubscribe----->" + d);
            }

            @Override
            public void onNext(@NonNull String s) {

                Log.d(Thread.currentThread().getName() + "-------------->", "onNext----->" + s);
            }

            @Override
            public void onError(@NonNull Throwable e) {

                Log.d(Thread.currentThread().getName() + "-------------->", "onError----->" + e);
            }

            @Override
            public void onComplete() {
                Log.d(Thread.currentThread().getName() + "-------------->", "onComplete----->");
            }
        });
    }
}

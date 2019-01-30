package com.alog.netlibrary;

import android.util.Log;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.functions.Consumer;

/**
 * @author: yuanbing
 * @created time: 2019/1/30 16:44
 * @description:
 */
public class RxManager {

    public static RxManager getInstance() {
        return new RxManager();
    }

    public void getNet() {
        Observable.create(new ObservableOnSubscribe<Object>() {
            @Override
            public void subscribe(ObservableEmitter<Object> emitter) throws Exception {
                emitter.onNext(1);
            }
        }).subscribe(new Consumer<Object>() {
            @Override
            public void accept(Object o) throws Exception {
                Log.d("11", o + "");
            }
        });
    }
}

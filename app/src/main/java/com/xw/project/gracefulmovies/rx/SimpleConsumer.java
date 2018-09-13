package com.xw.project.gracefulmovies.rx;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

public abstract class SimpleConsumer<T> implements Observer<T> {

    @Override
    public void onSubscribe(Disposable d) {
    }

    @Override
    public void onNext(T it) {
        accept(it);
    }

    @Override
    public void onError(Throwable e) {
    }

    @Override
    public void onComplete() {
    }

    public abstract void accept(T it);
}
package com.xw.project.gracefulmovies.server;

import java.net.ConnectException;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.Locale;

import retrofit2.adapter.rxjava.HttpException;
import rx.Subscriber;

/**
 * <p/>
 * Created by woxingxiao on 2017-02-10.
 */
public abstract class ApiSubscriber<T> extends Subscriber<T> {

    //HTTP的状态码
    private static final int HTTP_BAD_REQUEST = 400;
    private static final int HTTP_FORBIDDEN = 403;
    private static final int HTTP_NOT_FOUND = 404;
    private static final int HTTP_TIMEOUT = 408;
    private static final int HTTP_INTERNAL_SERVER_ERROR = 500;
    //出错提示
    private static final String MSG_NETWORK_ERROR = "网络错误";
    private static final String MSG_NETWORK_CONNECTION_ERROR = "网络连接不可用，请检查或稍后重试";
    private static final String MSG_UNKNOWN_ERROR = "Ops，好像出错了~";
    private static final String MSG_TIME_OUT = "网络请求超时";
    private static final String MSG_SERVER_ERROR = "服务器错误";

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onNext(T t) {

    }

    @Override
    public void onCompleted() {

        onFinally();
    }

    @Override
    public void onError(Throwable e) {

        resolveException(e);

        onFinally();
    }

    public void onFinally() {

    }

    private void resolveException(Throwable e) {
        Throwable throwable = e;
        //获取最根源的异常
        while (throwable.getCause() != null) {
            e = throwable;
            throwable = throwable.getCause();
        }

        if (e instanceof ApiException) {
            String msg = ((ApiException) e).getMsg();
            if (msg == null || msg.isEmpty()) {
                msg = String.format(Locale.CHINA, "出错了！错误代码：%d", ((ApiException) e).getCode());
            }

            onError(msg);
        } else if (e instanceof HttpException) {
            HttpException httpException = (HttpException) e;
            switch (httpException.code()) {
                case HTTP_BAD_REQUEST:
                case HTTP_FORBIDDEN:
                case HTTP_NOT_FOUND:
                case HTTP_INTERNAL_SERVER_ERROR:
                    onError(MSG_SERVER_ERROR);
                    break;
                case HTTP_TIMEOUT:
                    onError(MSG_TIME_OUT);
                    break;
                default:
                    onError(MSG_NETWORK_ERROR);
                    break;
            }
        } else if (e instanceof SocketTimeoutException) {
            onError(MSG_TIME_OUT);
        } else if (e instanceof ConnectException) {
            onError(MSG_NETWORK_ERROR);
        } else if (e instanceof UnknownHostException) {
            onError(MSG_NETWORK_CONNECTION_ERROR);
        } else if (e instanceof SocketException) {
            onError(MSG_SERVER_ERROR);
        } else {
            onError(MSG_UNKNOWN_ERROR);
        }
    }

    protected abstract void onError(String msg);
}

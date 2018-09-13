package com.xw.project.gracefulmovies.data.api;

import com.google.gson.JsonSyntaxException;

import java.net.ConnectException;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.Locale;

import io.reactivex.observers.DisposableObserver;
import retrofit2.HttpException;

/**
 * <p>
 * Created by woxingxiao on 2018-08-18.
 */
public abstract class ApiObserver<T> extends DisposableObserver<T> {

    //HTTP的状态码
    private static final int HTTP_BAD_REQUEST = 400;
    private static final int HTTP_TOKEN_EXPIRED = 401; // token 过期
    private static final int HTTP_FORBIDDEN = 403;
    private static final int HTTP_NOT_FOUND = 404;
    private static final int HTTP_TIMEOUT = 408;
    private static final int HTTP_INTERNAL_SERVER_ERROR = 500;
    //出错提示
    private static final String MSG_TOKEN_EXPIRED = "token已过期，请重新登录";
    private static final String MSG_NETWORK_ERROR = "网络错误";
    private static final String MSG_NETWORK_CONNECTION_ERROR = "网络连接不可用，请检查或稍后重试";
    private static final String MSG_UNKNOWN_ERROR = "Ops，好像出错了~";
    private static final String MSG_TIME_OUT = "网络请求超时";
    private static final String MSG_SERVER_ERROR = "服务器错误";
    private static final String MSG_JSON_PARSE_ERROR = "数据解析错误";

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onNext(T t) {
    }

    @Override
    public void onComplete() {
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
        if (e instanceof ApiException) {
            String msg = ((ApiException) e).getMsg();
            if (msg == null || msg.isEmpty()) {
                msg = String.format(Locale.CHINA, "出错了！错误代码：%d", ((ApiException) e).getCode());
            }

            onErrorResolved(e, msg);
        } else if (e instanceof HttpException) {
            HttpException httpException = (HttpException) e;
            switch (httpException.code()) {
                case HTTP_BAD_REQUEST:
                case HTTP_FORBIDDEN:
                case HTTP_NOT_FOUND:
                case HTTP_INTERNAL_SERVER_ERROR:
                    onErrorResolved(e, MSG_SERVER_ERROR);
                    break;
                case HTTP_TOKEN_EXPIRED:
                    onErrorResolved(e, MSG_TOKEN_EXPIRED);
                    break;
                case HTTP_TIMEOUT:
                    onErrorResolved(e, MSG_TIME_OUT);
                    break;
                default:
                    onErrorResolved(e, MSG_NETWORK_ERROR);
                    break;
            }
        } else if (e instanceof SocketTimeoutException) {
            onErrorResolved(e, MSG_TIME_OUT);
        } else if (e instanceof ConnectException) {
            onErrorResolved(e, MSG_NETWORK_ERROR);
        } else if (e instanceof UnknownHostException) {
            onErrorResolved(e, MSG_NETWORK_CONNECTION_ERROR);
        } else if (e instanceof SocketException) {
            onErrorResolved(e, MSG_SERVER_ERROR);
        } else if (e instanceof JsonSyntaxException) {
            onErrorResolved(e, MSG_JSON_PARSE_ERROR);
        } else {
            onErrorResolved(e, MSG_UNKNOWN_ERROR);
        }
    }

    protected abstract void onErrorResolved(Throwable e, String msg);
}
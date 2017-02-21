package com.xw.project.gracefulmovies.server;

import android.util.SparseArray;

import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

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
    //业务错误码
    private static final int[] CODES = {
            10001,
            10002,
            10003,
            10004,
            10005,
            10007,
            10008,
            10009,
            10011,
            10012,
            10013,
            10014,
            10020,
            10021,
            209401,
            209402,
            209403,
            209404,
            209405
    };
    //业务出错提示
    private static final String[] CODE_MSG = {
            "错误的请求KEY",
            "该KEY无请求权限",
            "KEY过期",
            "错误的OPENID",
            "应用未审核超时，请提交认证",
            "未知的请求源",
            "被禁止的IP",
            "被禁止的KEY",
            "当前IP请求超过限制",
            "请求超过次数限制",
            "测试KEY超过请求限制",
            "系统内部异常",
            "接口维护",
            "接口停用",
            "影片名不能为空",
            "查询不到该影片相关信息",
            "网络错误，请重试",
            "城市名不能为空",
            "查询不到热映电影相关信息"
    };

    private SparseArray<String> mSparseArray;

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
                mapCodeAndMsg(); // 错误码与提示一一对应到map

                msg = mSparseArray.get(((ApiException) e).getCode(), MSG_SERVER_ERROR);
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
        } else {
            onError(MSG_UNKNOWN_ERROR);
        }
    }

    private void mapCodeAndMsg() {
        if (mSparseArray == null) {
            mSparseArray = new SparseArray<>();
            for (int i = 0; i < CODES.length; i++) {
                mSparseArray.put(CODES[i], CODE_MSG[i]);
            }
        }
    }

    protected abstract void onError(String msg);
}

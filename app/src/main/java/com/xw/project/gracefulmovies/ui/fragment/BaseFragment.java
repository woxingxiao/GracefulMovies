package com.xw.project.gracefulmovies.ui.fragment;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.xw.project.gracefulmovies.data.DataResource;
import com.xw.project.gracefulmovies.ui.activity.base.StatusView;
import com.xw.project.gracefulmovies.util.Util;

import java.io.Serializable;
import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

/**
 * BaseFragment
 * <p>
 * Created by woxingxiao on 2018-08-23.
 */

public abstract class BaseFragment<VDB extends ViewDataBinding> extends Fragment {

    protected static final String PARAM_1 = "param_1";
    protected static final String PARAM_2 = "param_2";
    protected static final String OBJ_1 = "obj_1";
    protected static final int REQUEST_CODE_1 = 0x66;
    protected static int MARGIN_TOP_DP;

    private static final String[] PARAMS = {PARAM_1, PARAM_2};
    private static final String[] OBJECTS = {OBJ_1};

    protected FragmentActivity mActivity;
    protected VDB mBinding;
    private StatusView mStatusView;
    private boolean isVisibleToUser;
    private boolean isViewPrepared;
    private boolean isDataHasLoaded;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        mActivity = (FragmentActivity) context;
        MARGIN_TOP_DP = Util.getStatusBarHeight();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, viewLayoutRes(), container, false);

        afterInflateView();

        return mBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        isViewPrepared = true;
        lazyLoad();
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);

        this.isVisibleToUser = isVisibleToUser;
        lazyLoad();
    }

    private void lazyLoad() {
        if (!isDataHasLoaded && isViewPrepared && isVisibleToUser) {
            isDataHasLoaded = true;
            onLazyLoad();
        }
    }

    protected void onLazyLoad() {
    }

    @LayoutRes
    protected abstract int viewLayoutRes();

    protected abstract void afterInflateView();

    @Override
    public void onDestroy() {
        isDataHasLoaded = false;
        isViewPrepared = false;
        super.onDestroy();
    }

    @Override
    public void onDestroyView() {
        isDataHasLoaded = false;
        isViewPrepared = false;
        super.onDestroyView();
    }

    public static Fragment newInstance(@NonNull Class<? extends Fragment> cls) {
        try {
            try {
                return cls.newInstance();
            } catch (java.lang.InstantiationException e) {
                e.printStackTrace();
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static Fragment newInstance(@NonNull Class<? extends Fragment> cls, @NonNull Object... params) {
        try {
            try {
                Fragment fragment = cls.newInstance();
                fragment.setArguments(assembleArgsWithParam(new Bundle(), params));
                return fragment;
            } catch (java.lang.InstantiationException e) {
                e.printStackTrace();
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        return null;
    }

    private static Bundle assembleArgsWithParam(Bundle bundle, @NonNull Object... params) {
        int p_i = 0;
        int o_i = 0;

        for (Object obj : params) {
            if (obj instanceof Integer) {
                bundle.putInt(PARAMS[p_i], (int) obj);
            } else if (obj instanceof Boolean) {
                bundle.putBoolean(PARAMS[p_i], (boolean) obj);
            } else if (obj instanceof Float) {
                bundle.putFloat(PARAMS[p_i], (float) obj);
            } else if (obj instanceof Double) {
                bundle.putDouble(PARAMS[p_i], (double) obj);
            } else if (obj instanceof String) {
                bundle.putString(PARAMS[p_i], (String) obj);
            } else if (obj instanceof Long) {
                bundle.putLong(PARAMS[p_i], (long) obj);
            } else if (obj instanceof Parcelable) {
                bundle.putParcelable(OBJECTS[o_i], (Parcelable) obj);
            } else if (obj instanceof Serializable) {
                bundle.putSerializable(OBJECTS[o_i], (Serializable) obj);
            }

            if (obj instanceof Integer || obj instanceof Boolean || obj instanceof Float
                    || obj instanceof Double || obj instanceof String || obj instanceof Long) {
                p_i++;
            } else if (obj instanceof Parcelable || obj instanceof Serializable) {
                o_i++;
            }
        }

        return bundle;
    }

    protected void navigate(Class toActivity) {
        startActivity(new Intent(getActivity(), toActivity));
    }

    protected void navigate(Class toActivity, @NonNull Object... params) {
        Intent intent = new Intent(getActivity(), toActivity);
        startActivity(assembleIntentWithParam(intent, params));
    }

    protected void navigateForResult(int requestCode, Class toActivity) {
        startActivityForResult(new Intent(getActivity(), toActivity), requestCode);
    }

    protected void navigateForResult(int requestCode, Class toActivity, @NonNull Object... params) {
        Intent intent = new Intent(getActivity(), toActivity);
        startActivityForResult(assembleIntentWithParam(intent, params), requestCode);
    }

    private Intent assembleIntentWithParam(Intent intent, @NonNull Object... params) {
        int p_i = 0;
        int o_i = 0;

        for (Object obj : params) {
            if (obj instanceof Integer) {
                intent.putExtra(PARAMS[p_i], (int) obj);
            } else if (obj instanceof Boolean) {
                intent.putExtra(PARAMS[p_i], (boolean) obj);
            } else if (obj instanceof Float) {
                intent.putExtra(PARAMS[p_i], (float) obj);
            } else if (obj instanceof Double) {
                intent.putExtra(PARAMS[p_i], (double) obj);
            } else if (obj instanceof String) {
                intent.putExtra(PARAMS[p_i], (String) obj);
            } else if (obj instanceof Long) {
                intent.putExtra(PARAMS[p_i], (long) obj);
            } else if (obj instanceof Parcelable) {
                intent.putExtra(OBJECTS[o_i], (Parcelable) obj);
            } else if (obj instanceof Serializable) {
                intent.putExtra(OBJECTS[o_i], (Serializable) obj);
            }

            if (obj instanceof Integer || obj instanceof Boolean || obj instanceof Float
                    || obj instanceof Double || obj instanceof String || obj instanceof Long) {
                p_i++;
            } else if (obj instanceof Parcelable || obj instanceof Serializable) {
                o_i++;
            }
        }

        return intent;
    }

    protected void toast(String msg) {
        Toast.makeText(mActivity, msg, Toast.LENGTH_SHORT).show();
    }

    protected void processStatusView(ViewGroup container, DataResource resource) {
        if (resource == null)
            return;

        if (mStatusView == null) {
            mStatusView = new StatusView(mActivity);
            mStatusView.setOnReloadListener(this::onReload);
        }
        switch (resource.getStatus()) {
            case LOADING:
                mStatusView.show(container, StatusView.LOADING, MARGIN_TOP_DP);
                break;
            case SUCCESS:
                mStatusView.dismiss();
                break;
            case ERROR:
                if (resource.getException().getCause() instanceof ConnectException ||
                        resource.getException().getCause() instanceof UnknownHostException) {
                    mStatusView.show(container, StatusView.CONNECTION_ERROR, MARGIN_TOP_DP);
                } else if (resource.getException().getCause() instanceof SocketTimeoutException) {
                    mStatusView.show(container, StatusView.CONNECTION_TIME_OUT, MARGIN_TOP_DP);
                } else {
                    toast(resource.getException().getMessage());
                    mStatusView.dismiss();
                }
                break;
            case EMPTY:
                mStatusView.show(container, StatusView.NO_DATA, MARGIN_TOP_DP);
                break;
        }
    }

    protected void onReload() {
    }
}

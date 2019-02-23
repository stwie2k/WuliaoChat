package android.zyz.WuLiaoChat.factory.data;

import android.support.annotation.StringRes;


public interface DataSource {


    interface Callback<T> extends SucceedCallback<T>, FailedCallback {

    }

    interface SucceedCallback<T> {
        // 数据加载成功, 网络请求成功
        void onDataLoaded(T t);

    }

    //只关注失败的接口
    interface FailedCallback {
        // 数据加载失败, 网络请求失败
        void onDataNotAvailable(@StringRes int strRes);
    }


    void dispose();

}

package android.zyz.WuLiaoChat.common.widget.convention;

import android.support.annotation.StringRes;

public interface PlaceHolderView {


    void triggerEmpty();

    /**
     * 网络错误
     * 显示一个网络错误的图标
     */
    void triggerNetError();

    /**
     * 加载错误，并显示错误信息
     *
     * @param strRes 错误信息
     */
    void triggerError(@StringRes int strRes);

    /**
     * 显示正在加载的状态
     */
    void triggerLoading();


    void triggerOk();


    void triggerOkOrEmpty(boolean isOk);
}

package android.zyz.WuLiaoChat.factory.presenter.user;

import android.zyz.WuLiaoChat.factory.presenter.BaseContract;

public interface UpdateInfoContract {
    interface Presenter extends BaseContract.Presenter {
        // 更新
        void update(String photoFilePath, String desc, boolean isMan);
    }

    interface View extends BaseContract.View<Presenter> {
        // 回调成功
        void updateSucceed();
    }
}

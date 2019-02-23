package android.zyz.WuLiaoChat.factory.presenter.contact;

import android.zyz.WuLiaoChat.factory.model.db.User;
import android.zyz.WuLiaoChat.factory.presenter.BaseContract;

public interface PersonalContract {
    interface Presenter extends BaseContract.Presenter {
        // 获取用户信息
        User getUserPersonal();
    }

    interface View extends BaseContract.View<Presenter> {
        String getUserId();

        // 加载数据完成
        void onLoadDone(User user);

        // 是否发起聊天
        void allowSayHello(boolean isAllow);

        // 设置关注状态
        void setFollowStatus(boolean isFollow);
    }
}

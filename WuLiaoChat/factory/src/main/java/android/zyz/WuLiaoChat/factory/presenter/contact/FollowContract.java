package android.zyz.WuLiaoChat.factory.presenter.contact;

import android.zyz.WuLiaoChat.factory.model.card.UserCard;
import android.zyz.WuLiaoChat.factory.presenter.BaseContract;


public interface FollowContract {
    // 任务调度者
    interface Presenter extends BaseContract.Presenter {
        // 关注一个人
        void follow(String id);
    }

    interface View extends BaseContract.View<Presenter> {
        // 成功的情况下返回一个用户的信息
        void onFollowSucceed(UserCard userCard);
    }
}
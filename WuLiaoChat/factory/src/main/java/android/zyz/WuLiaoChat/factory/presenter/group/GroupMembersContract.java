package android.zyz.WuLiaoChat.factory.presenter.group;

import android.zyz.WuLiaoChat.factory.model.db.view.MemberUserModel;

import android.zyz.WuLiaoChat.factory.model.db.view.MemberUserModel;
import android.zyz.WuLiaoChat.factory.presenter.BaseContract;


public interface GroupMembersContract {
    interface Presenter extends BaseContract.Presenter {
        // 具有一个刷新的方法
        void refresh();
    }

    // 界面
    interface View extends BaseContract.RecyclerView<Presenter, MemberUserModel> {
        // 获取群的ID
        String getGroupId();
    }
}

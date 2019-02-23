package android.zyz.WuLiaoChat.factory.presenter.group;

import android.zyz.WuLiaoChat.factory.data.helper.GroupHelper;
import android.zyz.WuLiaoChat.factory.model.db.view.MemberUserModel;

import android.zyz.WuLiaoChat.factory.Factory;
import android.zyz.WuLiaoChat.factory.data.helper.GroupHelper;
import android.zyz.WuLiaoChat.factory.model.db.view.MemberUserModel;
import android.zyz.WuLiaoChat.factory.presenter.BaseRecyclerPresenter;

import java.util.List;


public class GroupMembersPresenter extends BaseRecyclerPresenter<MemberUserModel, GroupMembersContract.View>
        implements GroupMembersContract.Presenter {

    public GroupMembersPresenter(GroupMembersContract.View view) {
        super(view);
    }

    @Override
    public void refresh() {
        // 显示Loading
        start();

        // 异步加载
        Factory.runOnAsync(loader);
    }

    private Runnable loader = new Runnable() {
        @Override
        public void run() {
            GroupMembersContract.View view = getView();
            if (view == null)
                return;

            String groupId = view.getGroupId();

            // 传递数量为-1 代表查询所有
            List<MemberUserModel> models = GroupHelper.getMemberUsers(groupId, -1);

            refreshData(models);
        }
    };
}

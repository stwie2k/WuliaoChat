package android.zyz.WuLiaoChat.factory.presenter.group;

import android.zyz.WuLiaoChat.factory.data.group.GroupsRepository;
import android.zyz.WuLiaoChat.factory.data.helper.GroupHelper;
import android.zyz.WuLiaoChat.factory.model.db.Group;
import android.zyz.WuLiaoChat.factory.presenter.BaseSourcePresenter;
import android.zyz.WuLiaoChat.factory.utils.DiffUiDataCallback;
import android.support.v7.util.DiffUtil;

import android.zyz.WuLiaoChat.factory.data.group.GroupsDataSource;
import android.zyz.WuLiaoChat.factory.data.group.GroupsRepository;
import android.zyz.WuLiaoChat.factory.data.helper.GroupHelper;
import android.zyz.WuLiaoChat.factory.data.helper.UserHelper;
import android.zyz.WuLiaoChat.factory.model.db.Group;
import android.zyz.WuLiaoChat.factory.presenter.BaseSourcePresenter;
import android.zyz.WuLiaoChat.factory.utils.DiffUiDataCallback;

import java.util.List;


public class GroupsPresenter extends BaseSourcePresenter<Group, Group,
        GroupsDataSource, GroupsContract.View> implements GroupsContract.Presenter {

    public GroupsPresenter(GroupsContract.View view) {
        super(new GroupsRepository(), view);
    }

    @Override
    public void start() {
        super.start();
        GroupHelper.refreshGroups();
    }

    @Override
    public void onDataLoaded(List<Group> groups) {
        final GroupsContract.View view = getView();
        if (view == null)
            return;

        // 对比差异
        List<Group> old = view.getRecyclerAdapter().getItems();
        DiffUiDataCallback<Group> callback = new DiffUiDataCallback<>(old, groups);
        DiffUtil.DiffResult result = DiffUtil.calculateDiff(callback);

        // 界面刷新
        refreshData(result, groups);
    }
}

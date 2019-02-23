package android.zyz.WuLiaoChat.factory.data.group;

import android.zyz.WuLiaoChat.factory.data.BaseDbRepository;
import android.text.TextUtils;

import com.raizlabs.android.dbflow.sql.language.SQLite;

import android.zyz.WuLiaoChat.factory.data.helper.GroupHelper;
import android.zyz.WuLiaoChat.factory.model.db.Group;
import android.zyz.WuLiaoChat.factory.model.db.Group_Table;
import android.zyz.WuLiaoChat.factory.model.db.view.MemberUserModel;

import java.util.List;


public class GroupsRepository extends BaseDbRepository<Group>
        implements GroupsDataSource {


    @Override
    public void load(SucceedCallback<List<Group>> callback) {
        super.load(callback);

        SQLite.select()
                .from(Group.class)
                .orderBy(Group_Table.name, true)
                .limit(100)
                .async()
                .queryListResultCallback(this)
                .execute();
    }

    @Override
    protected boolean isRequired(Group group) {

        if (group.getGroupMemberCount() > 0) {
            // 以及初始化了成员的信息
            group.holder = buildGroupHolder(group);
        } else {
            // 待初始化的群的信息
            group.holder = null;
            GroupHelper.refreshGroupMember(group);
        }

        // 所有的群我都需要关注显示
        return true;
    }

    // 初始化界面显示的成员信息
    private String buildGroupHolder(Group group) {
        List<MemberUserModel> userModels = group.getLatelyGroupMembers();
        if (userModels == null || userModels.size() == 0)
            return null;

        StringBuilder builder = new StringBuilder();
        for (MemberUserModel userModel : userModels) {
            builder.append(TextUtils.isEmpty(userModel.alias) ? userModel.name : userModel.alias);
            builder.append(", ");
        }

        builder.delete(builder.lastIndexOf(", "), builder.length());

        return builder.toString();
    }
}

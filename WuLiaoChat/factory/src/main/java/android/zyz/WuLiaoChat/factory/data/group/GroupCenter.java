package android.zyz.WuLiaoChat.factory.data.group;

import android.zyz.WuLiaoChat.factory.model.card.GroupCard;
import android.zyz.WuLiaoChat.factory.model.card.GroupMemberCard;


public interface GroupCenter {
    // 群卡片的处理
    void dispatch(GroupCard... cards);

    // 群成员的处理
    void dispatch(GroupMemberCard... cards);
}

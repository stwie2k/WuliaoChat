package android.zyz.WuLiaoChat.factory.data.user;

import android.zyz.WuLiaoChat.factory.model.card.UserCard;


public interface UserCenter {
    // 分发处理一堆用户卡片的信息，并更新到数据库
    void dispatch(UserCard... cards);
}

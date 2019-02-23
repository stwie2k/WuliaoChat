package android.zyz.WuLiaoChat.factory.data.message;

import android.zyz.WuLiaoChat.factory.model.card.MessageCard;

public interface MessageCenter {
    void dispatch(MessageCard... cards);
}

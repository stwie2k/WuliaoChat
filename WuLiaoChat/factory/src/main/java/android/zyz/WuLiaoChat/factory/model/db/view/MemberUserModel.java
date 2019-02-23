package android.zyz.WuLiaoChat.factory.model.db.view;

import android.zyz.WuLiaoChat.factory.model.db.AppDatabase;

import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.QueryModel;

import android.zyz.WuLiaoChat.factory.model.db.AppDatabase;

@QueryModel(database = AppDatabase.class)
public class MemberUserModel {
    @Column
    public String userId; // User-id/Member-userId
    @Column
    public String name; // User-name
    @Column
    public String alias; // Member-alias
    @Column
    public String portrait; // User-portrait
}

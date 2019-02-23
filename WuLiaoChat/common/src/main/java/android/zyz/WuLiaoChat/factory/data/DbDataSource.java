package android.zyz.WuLiaoChat.factory.data;

import java.util.List;


public interface DbDataSource<Data> extends DataSource {

    void load(SucceedCallback<List<Data>> callback);
}

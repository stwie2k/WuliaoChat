package android.zyz.WuLiaoChat.factory.presenter.contact;

import android.zyz.WuLiaoChat.factory.data.user.ContactDataSource;
import android.zyz.WuLiaoChat.factory.data.user.ContactRepository;
import android.support.v7.util.DiffUtil;

import android.zyz.WuLiaoChat.common.widget.recycler.RecyclerAdapter;
import android.zyz.WuLiaoChat.factory.data.DataSource;
import android.zyz.WuLiaoChat.factory.data.helper.UserHelper;
import android.zyz.WuLiaoChat.factory.data.user.ContactDataSource;
import android.zyz.WuLiaoChat.factory.data.user.ContactRepository;
import android.zyz.WuLiaoChat.factory.model.db.User;
import android.zyz.WuLiaoChat.factory.presenter.BaseSourcePresenter;
import android.zyz.WuLiaoChat.factory.utils.DiffUiDataCallback;

import java.util.List;


public class ContactPresenter extends BaseSourcePresenter<User, User, ContactDataSource, ContactContract.View>
        implements ContactContract.Presenter, DataSource.SucceedCallback<List<User>> {

    public ContactPresenter(ContactContract.View view) {
        // 初始化数据仓库
        super(new ContactRepository(), view);
    }


    @Override
    public void start() {
        super.start();

        // 加载网络数据
        UserHelper.refreshContacts();
    }

    // 运行到这里的时候是子线程
    @Override
    public void onDataLoaded(List<User> users) {
        // 无论怎么操作，数据变更，最终都会通知到这里来
        final ContactContract.View view = getView();
        if (view == null)
            return;

        RecyclerAdapter<User> adapter = view.getRecyclerAdapter();
        List<User> old = adapter.getItems();

        // 进行数据对比
        DiffUtil.Callback callback = new DiffUiDataCallback<>(old, users);
        DiffUtil.DiffResult result = DiffUtil.calculateDiff(callback);

        // 调用基类方法进行界面刷新
        refreshData(result, users);
    }
}

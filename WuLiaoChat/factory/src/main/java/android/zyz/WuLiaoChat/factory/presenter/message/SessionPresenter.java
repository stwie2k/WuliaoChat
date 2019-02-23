package android.zyz.WuLiaoChat.factory.presenter.message;

import android.zyz.WuLiaoChat.factory.data.message.SessionDataSource;
import android.zyz.WuLiaoChat.factory.data.message.SessionRepository;
import android.support.v7.util.DiffUtil;

import android.zyz.WuLiaoChat.factory.data.message.SessionDataSource;
import android.zyz.WuLiaoChat.factory.data.message.SessionRepository;
import android.zyz.WuLiaoChat.factory.model.db.Session;
import android.zyz.WuLiaoChat.factory.presenter.BaseSourcePresenter;
import android.zyz.WuLiaoChat.factory.utils.DiffUiDataCallback;

import java.util.List;

public class SessionPresenter extends BaseSourcePresenter<Session, Session,
        SessionDataSource, SessionContract.View> implements SessionContract.Presenter {

    public SessionPresenter(SessionContract.View view) {
        super(new SessionRepository(), view);
    }

    @Override
    public void onDataLoaded(List<Session> sessions) {
        SessionContract.View view = getView();
        if (view == null)
            return;

        // 差异对比
        List<Session> old = view.getRecyclerAdapter().getItems();
        DiffUiDataCallback<Session> callback = new DiffUiDataCallback<>(old, sessions);
        DiffUtil.DiffResult result = DiffUtil.calculateDiff(callback);

        // 刷新界面
        refreshData(result, sessions);
    }
}

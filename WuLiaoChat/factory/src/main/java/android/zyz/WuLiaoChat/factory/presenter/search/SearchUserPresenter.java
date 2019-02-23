package android.zyz.WuLiaoChat.factory.presenter.search;

import android.zyz.WuLiaoChat.factory.data.helper.UserHelper;
import android.zyz.WuLiaoChat.factory.model.card.UserCard;

import net.qiujuer.genius.kit.handler.Run;
import net.qiujuer.genius.kit.handler.runable.Action;
import android.zyz.WuLiaoChat.factory.data.DataSource;
import android.zyz.WuLiaoChat.factory.data.helper.UserHelper;
import android.zyz.WuLiaoChat.factory.model.card.UserCard;
import android.zyz.WuLiaoChat.factory.presenter.BasePresenter;

import java.util.List;

import retrofit2.Call;


public class SearchUserPresenter extends BasePresenter<SearchContract.UserView>
        implements SearchContract.Presenter, DataSource.Callback<List<UserCard>> {
    private Call searchCall;

    public SearchUserPresenter(SearchContract.UserView view) {
        super(view);
    }

    @Override
    public void search(String content) {
        start();

        Call call = searchCall;
        if (call != null && !call.isCanceled()) {
            call.cancel();
        }

        searchCall = UserHelper.search(content, this);
    }

    @Override
    public void onDataLoaded(final List<UserCard> userCards) {
        // 搜索成功
        final SearchContract.UserView view = getView();
        if(view!=null){
            Run.onUiAsync(new Action() {
                @Override
                public void call() {
                    view.onSearchDone(userCards);
                }
            });
        }
    }

    @Override
    public void onDataNotAvailable(final int strRes) {
        // 搜索失败
        final SearchContract.UserView view = getView();
        if(view!=null){
            Run.onUiAsync(new Action() {
                @Override
                public void call() {
                    view.showError(strRes);
                }
            });
        }
    }
}

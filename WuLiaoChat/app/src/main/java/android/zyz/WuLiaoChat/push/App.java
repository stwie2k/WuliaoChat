package android.zyz.WuLiaoChat.push;

import android.content.Context;

import com.igexin.sdk.PushManager;

import android.zyz.WuLiaoChat.common.app.Application;
import android.zyz.WuLiaoChat.factory.Factory;


public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        // 调用Factory进行初始化
        Factory.setup();
        // 推送进行初始化
        PushManager.getInstance().initialize(this);
    }

    @Override
    protected void showAccountView(Context context) {

    }
}

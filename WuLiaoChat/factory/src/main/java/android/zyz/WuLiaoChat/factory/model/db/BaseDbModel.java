package android.zyz.WuLiaoChat.factory.model.db;

import com.raizlabs.android.dbflow.structure.BaseModel;

import android.zyz.WuLiaoChat.factory.utils.DiffUiDataCallback;

public abstract class BaseDbModel<Model> extends BaseModel
        implements DiffUiDataCallback.UiDataDiffer<Model> {
}

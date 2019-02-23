package android.zyz.WuLiaoChat.common.tools;

import android.media.AudioManager;
import android.media.MediaPlayer;

import java.io.IOException;


public class AudioPlayHelper<Holder> implements MediaPlayer.OnErrorListener, MediaPlayer.OnCompletionListener {
    // 系统的播放器
    private MediaPlayer mediaPlayer;
    // 全局的播放状态监听器，用于回调当前的播放状态
    private RecordPlayListener<Holder> listener;
    // 范型的一个目标，当前播放的目标，可以是任意的类型
    private Holder holder;
    // 当前的播放地址
    private String currentPath;

    /**
     * 构造放方法
     *
     * @param listener 传入一个监听器
     */
    public AudioPlayHelper(RecordPlayListener<Holder> listener) {
        this.listener = listener;
        // 进行播放器创建
        this.mediaPlayer = createNewMediaPlayer();
    }

    /**
     * 创建一个新的播放器
     *
     * @return MediaPlayer
     */
    private MediaPlayer createNewMediaPlayer() {
        MediaPlayer mediaPlayer = new MediaPlayer();
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        // 不循环播放
        mediaPlayer.setLooping(false);
        // 设置播放错误时的回调为当前类
        mediaPlayer.setOnErrorListener(this);
        // 设置播放完成的相关监听
        mediaPlayer.setOnCompletionListener(this);
        return mediaPlayer;
    }

    public void trigger(Holder holder, String filePath) {
        // 判断播放器状态
        if (mediaPlayer == null)
            return;

        // 如果当前正在播放，而且播放的就是点击进来的语音，则代表想要停止这个语音
        if (mediaPlayer.isPlaying() && filePath.equalsIgnoreCase(currentPath)) {
            stop();
            return;
        }

        // 播放前先尝试进行停止操作
        stop();
        // 播放一个新的语音
        play(holder, filePath);
    }

    /**
     * 播放一个语音
     *
     * @param holder   目标
     * @param filePath 播放的语音的地址
     */
    private void play(Holder holder, String filePath) {
        if (mediaPlayer == null)
            return;

        // 设置当前的目标与地址
        this.holder = holder;
        this.currentPath = filePath;
        try {
            // 设置播放地址，并准备
            this.mediaPlayer.setDataSource(filePath);
            this.mediaPlayer.prepare();
        } catch (IOException e) {
            e.printStackTrace();
            // 错误情况下直接回调播放错误
            this.listener.onPlayError(holder);
            // 同时进行停止操作
            stop();
            return;
        }

        // 开始播放
        this.mediaPlayer.start();
        // 回调开始播放的状态
        this.listener.onPlayStart(holder);
    }

    /**
     * 进行停止操作
     */
    private void stop() {
        if (mediaPlayer == null)
            return;

        // 停止播放器，并重置状态
        mediaPlayer.stop();
        mediaPlayer.reset();
        currentPath = null;

        // 如果当前的目标不等于null则回调一次播放停止回调
        Holder holder = this.holder;
        // 仅仅只能回调一次停止，所以需要马上赋值null
        this.holder = null;
        if (holder != null) {
            listener.onPlayStop(holder);
        }
    }

    /**
     * 进行销毁的操作，如果退出界面则应当释放掉播放器
     */
    public void destroy() {
        MediaPlayer mediaPlayer = this.mediaPlayer;
        if (mediaPlayer != null) {
            this.mediaPlayer = null;
            stop();
            mediaPlayer.release();
        }
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        // 播放器的播放完成回调，在这里我们回调用户播放完成了
        stop();
    }

    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        // 错误情况下，回调错误并进行停止的流程
        Holder holder = this.holder;
        if (holder != null) {
            this.listener.onPlayError(holder);
        }
        stop();
        return true;
    }

    /**
     * 当前的播放监听器
     *
     * @param <Holder> 任意范型的目标Holder
     */
    public interface RecordPlayListener<Holder> {
        /**
         * 当播放开始时
         *
         * @param holder 当前的目标
         */
        void onPlayStart(Holder holder);

        /**
         * 当播放停止时
         *
         * @param holder 当前的目标
         */
        void onPlayStop(Holder holder);

        /**
         * 当播放移除时
         *
         * @param holder 当前的目标
         */
        void onPlayError(Holder holder);
    }
}

package android.zyz.WuLiaoChat.common.tools;

import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.os.SystemClock;
import android.zyz.WuLiaoChat.common.app.Application;
import android.util.Log;

import net.qiujuer.lame.Lame;
import net.qiujuer.lame.LameAsyncEncoder;
import net.qiujuer.lame.LameOutputStream;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class AudioRecordHelper {
    // 打印日志使用
    private static final String TAG = AudioRecordHelper.class.getSimpleName();
    // 采样频率集合，用于适应不同手机情况
    private static final int[] SAMPLE_RATES = new int[]{44100, 22050, 11025, 8000};
    // 状态回调
    private RecordCallback callback;
    // 缓存文件，无论那一个录音都复用同一个缓存文件
    private File tmpFile;

    private int minShortBufferSize;
    // 录制完成
    private boolean isDone;
    // 是否取消
    private boolean isCancel;


    public AudioRecordHelper(File tmpFile, RecordCallback callback) {
        this.tmpFile = tmpFile;
        this.callback = callback;
    }


    private AudioRecord initAudioRecord() {
        // 遍历采样频率
        for (int rate : SAMPLE_RATES) {
            // 编码比特率
            for (short audioFormat : new short[]{AudioFormat.ENCODING_PCM_16BIT, AudioFormat.ENCODING_PCM_8BIT}) {
                // 录音通道：双通道，单通道
                for (short channelConfig : new short[]{AudioFormat.CHANNEL_IN_STEREO, AudioFormat.CHANNEL_IN_MONO}) {
                    try {
                        Log.d(TAG, "Attempting rate " + rate + "Hz, bits: " + audioFormat + ", channel: "
                                + channelConfig);
                        // 尝试获取最小的缓存区间大小
                        int bufferSize = AudioRecord.getMinBufferSize(rate, channelConfig, audioFormat);

                        if (bufferSize != AudioRecord.ERROR_BAD_VALUE) {
                            // 如果初始化成功
                            AudioRecord recorder = new AudioRecord(MediaRecorder.AudioSource.DEFAULT, rate, channelConfig, audioFormat, bufferSize);
                            // 尝试进行构建
                            if (recorder.getState() == AudioRecord.STATE_INITIALIZED) {
                                minShortBufferSize = bufferSize / 2;
                                return recorder;
                            }
                        }
                    } catch (Exception e) {
                        Log.e(TAG, rate + "Exception, keep trying.", e);
                    }
                }
            }
        }
        return null;
    }


    private File initTmpFile() {
        if (tmpFile.exists()) {
            //noinspection ResultOfMethodCallIgnored
            tmpFile.delete();
        }
        try {
            if (tmpFile.createNewFile())
                return tmpFile;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    //进行异步录制
    public void recordAsync() {
        Thread thread = new Thread() {
            @Override
            public void run() {
                record();
            }
        };
        thread.start();
    }


    public File record() {
        isCancel = false;
        isDone = false;

        // 开始进行初始化
        AudioRecord audioRecorder;
        File file;
        if ((audioRecorder = initAudioRecord()) == null
                || (file = initTmpFile()) == null) {
            Application.showToast("Record init error!");
            return null;
        }

        // 初始化输出到文件的流
        BufferedOutputStream outputStream;
        try {
            outputStream = new BufferedOutputStream(new FileOutputStream(file));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }

        final int shortBufferSize = minShortBufferSize;
        final RecordCallback callback = this.callback;

        // 初始化Lame转码库相关参数，传入当前的输入采样率，通道，以及输出的mp3格式的采样率
        Lame lame = new Lame(audioRecorder.getSampleRate(),
                audioRecorder.getChannelCount(),
                audioRecorder.getSampleRate());
        LameOutputStream lameOutputStream = new LameOutputStream(lame, outputStream, shortBufferSize);
        LameAsyncEncoder lameAsyncEncoder = new LameAsyncEncoder(lameOutputStream, shortBufferSize);

        int readSize;
        long endTime;

        // 通知开始
        audioRecorder.startRecording();
        callback.onRecordStart();
        // 记录开始的时间
        final long startTime = SystemClock.uptimeMillis();

        // 在当前线程中循环的读取系统录制的用户音频
        while (true) {
            // 从异步Lame编码器中获取一个缓存的buffer，然后把用户的录音读取到里边
            final short[] buffer = lameAsyncEncoder.getFreeBuffer();
            // 开始进行读取
            readSize = audioRecorder.read(buffer, 0, shortBufferSize);
            // 如果读取成功
            if (AudioRecord.ERROR_INVALID_OPERATION != readSize) {
                // 那么把读取成功的数据，push到异步转码器Lame中，进行异步的处理
                lameAsyncEncoder.push(buffer, readSize);
            }

            // 回调进度
            endTime = SystemClock.uptimeMillis();
            callback.onProgress(endTime - startTime);

            // 如果没有完成标示则继续录制
            if (isDone) {
                break;
            }
        }

        // 进行录制完成
        audioRecorder.stop();
        // 释放录制器
        audioRecorder.release();
        // 当前线程等待异步处理器完成处理
        lameAsyncEncoder.awaitEnd();

        // 如果说不是取消，则通知回调
        if (!isCancel) {
            callback.onRecordDone(file, endTime - startTime);
        }

        // 返回文件
        return file;
    }


    public void stop(boolean isCancel) {
        this.isCancel = isCancel;
        this.isDone = true;
    }

    //录制的回调
    public interface RecordCallback {
        // 录制开始的回调
        void onRecordStart();

        // 回调进度，当前的时间
        void onProgress(long time);

        // 录制完成的回调，如果停止录制时传递的是取消，那么则不会回调该方法
        void onRecordDone(File file, long time);
    }
}

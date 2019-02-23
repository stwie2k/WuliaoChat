package android.zyz.WuLiaoChat.utils;

import android.graphics.BitmapFactory;
import android.util.Log;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.Target;

import android.zyz.WuLiaoChat.common.app.Application;

import java.io.File;


public final class PicturesCompressor {
    private PicturesCompressor() {

    }

    public static boolean compressImage(final String srcPath,
                                        final String savePath,
                                        final long targetSize) {
        return compressImage(srcPath, savePath, targetSize, 75, 1280, 1280 * 6, null, null, true);
    }

    public static File loadWithGlideCache(String path) {
        File tmp;
        try {
            tmp = Glide.with(Application.getInstance())
                    .load(path)
                    .downloadOnly(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
                    .get();
            String absPath = tmp.getAbsolutePath();
            Log.d("PicturesCompressor", "loadWithGlideCache:" + absPath);
            return tmp;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    public static boolean compressImage(final String srcPath,
                                        final String savePath,
                                        final long maxSize,
                                        final int minQuality,
                                        final int maxWidth,
                                        final int maxHeight,
                                        byte[] byteStorage,
                                        BitmapFactory.Options options,
                                        boolean exactDecode) {
        boolean loadWithGlide = false;
        // build source file
        File inTmp = new File(srcPath);
        final File sourceFile;
        if (inTmp.exists()) {
            sourceFile = inTmp;
        } else {
            File tmp = loadWithGlideCache(srcPath);
            if (tmp == null)
                return false;
            sourceFile = tmp;
            loadWithGlide = true;
        }

        // build save file
        final File saveFile = new File(savePath);
        File saveDir = saveFile.getParentFile();
        if (!saveDir.exists()) {
            if (!saveDir.mkdirs())
                return false;
        }

        // End clear the out file data
        if (saveFile.exists()) {
            if (!saveFile.delete())
                return false;
        }

        // if the in file size <= maxSize, we can copy to savePath
        if (sourceFile.length() <= maxSize && confirmImage(sourceFile, options)) {
            return StreamUtil.copy(sourceFile, saveFile);
        }

        File realCacheFile;
        if (loadWithGlide) {
            realCacheFile = sourceFile;
        } else {
            realCacheFile = loadWithGlideCache(sourceFile.getAbsolutePath());
            if (realCacheFile == null)
                return false;
        }

        // Doing
        File tempFile = BitmapUtil.Compressor.compressImage(realCacheFile, maxSize, minQuality, maxWidth,
                maxHeight, byteStorage, options, exactDecode);

        // Rename to out file
        return tempFile != null && StreamUtil.copy(tempFile, saveFile) && tempFile.delete();
    }

    public static boolean confirmImage(File file, BitmapFactory.Options opts) {
        if (opts == null) opts = BitmapUtil.createOptions();
        opts.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(file.getAbsolutePath(), opts);
        String mimeType = opts.outMimeType.toLowerCase();
        return mimeType.contains("jpeg") || mimeType.contains("png") || mimeType.contains("gif");
    }
}

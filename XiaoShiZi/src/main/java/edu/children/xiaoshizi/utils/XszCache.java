package edu.children.xiaoshizi.utils;

import android.os.Environment;
import android.os.StatFs;

import com.blankj.utilcode.util.FileUtils;

import java.io.File;

import edu.children.xiaoshizi.DemoApplication;

public class XszCache {
    private static String TAG="XszCache";
    private XszCache() {
    }

    public static File getCacheDir(String dirName) {
        File result;
        if (existsSdcard()) {
            File cacheDir = null;
            if (cacheDir == null) {
                result = new File(Environment.getExternalStorageDirectory(),
                        "Android/data/" + DemoApplication.getInstance().getPackageName() + "/cache/" + dirName);
            } else {
                result = new File(cacheDir, dirName);
            }
        } else {
            result = new File(DemoApplication.getInstance().getCacheDir(), dirName);
        }
        if (result.exists() || result.mkdirs()) {
            return result;
        } else {
            return null;
        }
    }

    /**
     * 检查磁盘空间是否大于10mb
     *
     * @return true 大于
     */
    public static boolean isDiskAvailable() {
        long size = getDiskAvailableSize();
        return size > 10 * 1024 * 1024; // > 10bm
    }

    /**
     * 获取磁盘可用空间
     *
     * @return byte 单位 kb
     */
    public static long getDiskAvailableSize() {
        if (!existsSdcard()) return 0;
        File path = Environment.getExternalStorageDirectory(); // 取得sdcard文件路径
        StatFs stat = new StatFs(path.getAbsolutePath());
        long blockSize = stat.getBlockSize();
        long availableBlocks = stat.getAvailableBlocks();
        return availableBlocks * blockSize;
        // (availableBlocks * blockSize)/1024 KIB 单位
        // (availableBlocks * blockSize)/1024 /1024 MIB单位
    }

    public static Boolean existsSdcard() {
        return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
    }

    public static long getCacheSize() {
        long imageSize=getFileOrDirSize(getCacheDir(Constant.CACHE_DIR_IMAGE));
        long fileSize=getFileOrDirSize(getCacheDir(Constant.CACHE_DIR_FILE));
        return imageSize+fileSize;
    }
    public static void clearCacheSize() {
        FileUtils.deleteFilesInDir(getCacheDir(Constant.CACHE_DIR_IMAGE));
        FileUtils.deleteFilesInDir(getCacheDir(Constant.CACHE_DIR_FILE));
    }

    public static long getFileOrDirSize(File file) {
        if (!file.exists()) return 0;
        if (!file.isDirectory()) return file.length();

        long length = 0;
        File[] list = file.listFiles();
        if (list != null) { // 文件夹被删除时, 子文件正在被写入, 文件属性异常返回null.
            for (File item : list) {
                length += getFileOrDirSize(item);
            }
        }

        return length;
    }
}

package com.bitsworking.videoshowcase;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Chris Hager <chris@linuxuser.at> on 07/09/14.
 */
public class Tools {
    public static String getSdCardDirectory() {
        return Environment.getExternalStorageDirectory().getAbsolutePath() + Constants.SD_DIRECTORY_VIDEOS;
    }

    public static boolean isCallable(Context context, Intent intent) {
        List<ResolveInfo> list = context.getPackageManager().queryIntentActivities(intent,
                PackageManager.MATCH_DEFAULT_ONLY);
        return list.size() > 0;
    }

    /* Checks if external storage is available for read and write */
    public static boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            return true;
        }
        return false;
    }

    /* Checks if external storage is available to at least read */
    public static boolean isExternalStorageReadable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state) ||
                Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
            return true;
        }
        return false;
    }

    public static ArrayList<String> getFiles(File dir) {
        ArrayList<String> fileList = new ArrayList<String>();

        File listFile[] = dir.listFiles();
        if (listFile != null && listFile.length > 0) {
            for (int i = 0; i < listFile.length; i++) {
                if (listFile[i].isDirectory()) {
//                    fileList.add(listFile[i].getName());
//                    getFiles(listFile[i]);
                } else {
//                    if (listFile[i].getName().endsWith(".png")
//                            || listFile[i].getName().endsWith(".jpg")
//                            || listFile[i].getName().endsWith(".jpeg")
//                            || listFile[i].getName().endsWith(".gif"))
//
//                    {
                    fileList.add(listFile[i].getName());
//                    }
                }

            }
        }
        return fileList;
    }

    public static String makeSHA1Hash(String input) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("SHA1");
        md.reset();
        byte[] buffer = input.getBytes();
        md.update(buffer);
        byte[] digest = md.digest();

        String hexStr = "";
        for (int i = 0; i < digest.length; i++) {
            hexStr +=  Integer.toString( ( digest[i] & 0xff ) + 0x100, 16).substring( 1 );
        }
        return hexStr;
    }

    public static boolean canWriteUsbStick() {
        File path = new File(Constants.STATS_PATH);
        if (!path.exists()) {
            Log.e("Tools", "usbstick does not exist");
            return false;
        } else if (!path.isDirectory()) {
            Log.e("Tools", "usbstick not a dir");
            return false;
        } else if (!path.canWrite()) {
            Log.e("Tools", "usbstick cannot write");
            return false;
        }
        return true;
    }
}

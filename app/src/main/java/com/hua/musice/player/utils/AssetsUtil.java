package com.hua.musice.player.utils;

import android.content.res.AssetManager;

import com.hua.musice.player.base.AppApplication;
import com.litesuits.common.io.IOUtils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/4/9.
 */

public class AssetsUtil {




    /**
     * 获取assets文件夹下的dirName文件夹下的所有文件名
     * @param dirName   文件夹名称,如果为空将获取assets文件夹下的所有文件
     * @return
     */
    public static List<File> getFiles(String dirName) {
        AssetManager assetManager = getAssets();
        List<File> files = new ArrayList<>();
        String[] fileNames = null;
        try {
            fileNames = assetManager.list(dirName);
            for (int i = 0; i < fileNames.length; i++) {
                files.add(new File(dirName + File.separator + fileNames[i]));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return files;
    }



    public static String readFile(String fileName){
        AssetManager assets = getAssets();
        String s = null;
        try {
            InputStream inputStream = assets.open(fileName);
             s = IOUtils.toString(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return s;
    }

    private static AssetManager getAssets() {
        return AppApplication.getContext().getAssets();
    }

}

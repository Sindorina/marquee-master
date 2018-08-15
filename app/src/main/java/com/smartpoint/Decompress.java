package com.smartpoint;

import android.content.Context;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class Decompress {
    private File _zipFile;
    private InputStream _zipFileStream;
    private Context context;
    private static  String ROOT_LOCATION = "";
    private static final String TAG = "UNZIPUTIL";
    private Handler handler;
    public Decompress(Context context, File zipFile, Handler handler) {
        _zipFile = zipFile;
        this.handler = handler;
        this.context = context;
        _dirChecker("");
        ROOT_LOCATION = Environment.getExternalStorageDirectory().getAbsolutePath() + "/unzip/";
    }

    public Decompress(Context context, InputStream zipFile, Handler handler) {
        _zipFileStream = zipFile;
        this.context = context;
        this.handler = handler;
        _dirChecker("");
        ROOT_LOCATION = Environment.getExternalStorageDirectory().getAbsolutePath() + "/unzip/";
    }

    public void unzip() {
        try  {
            Log.i(TAG, "Starting to unzip");
            InputStream fin = _zipFileStream;
            if(fin == null) {
                fin = new FileInputStream(_zipFile);
            }
            ZipInputStream zin = new ZipInputStream(fin);
            long curSize = 0;//当前数量
            ZipEntry ze = null;
            while ((ze = zin.getNextEntry()) != null) {
                Log.v(TAG, "Unzipping " + ze.getName());
                if(ze.isDirectory()) {
                    _dirChecker(ROOT_LOCATION + "/" + ze.getName());
                } else {
                    FileOutputStream fout = new FileOutputStream(new File(ROOT_LOCATION, ze.getName()));
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    byte[] buffer = new byte[1024];
                    int count;
                    // reading and writing
                    while((count = zin.read(buffer)) != -1) {
                        baos.write(buffer, 0, count);
                        byte[] bytes = baos.toByteArray();
                        fout.write(bytes);
                        baos.reset();
                        //Thread.sleep(50);
                        curSize+=count;//当前
                        long percent = curSize;
                        Message message = new Message();
                        message.obj = percent;
                        handler.sendMessage(message);
                    }
                    fout.close();
                    zin.closeEntry();
                }
            }
            zin.close();
            Log.i(TAG, "Finished unzip");
            handler.sendEmptyMessage(100);
        } catch(Exception e) {
            Log.e(TAG, "Unzip Error", e);
            handler.sendEmptyMessage(90);
        }

    }

    private void _dirChecker(String dir) {
        File f = new File(dir);
        Log.i(TAG, "creating dir " + dir);

        if(dir.length() >= 0 && !f.isDirectory() ) {
            f.mkdirs();
        }
    }
}
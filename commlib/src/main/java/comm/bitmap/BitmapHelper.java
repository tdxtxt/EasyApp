package comm.bitmap;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Environment;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import comm.helper.SDcardHelper;

/**
 * Created by Administrator on 2017/5/19.
 */

public class BitmapHelper {
    public static Bitmap load(String filepath) {
        Bitmap bitmap = null;
        try {
            FileInputStream fin = new FileInputStream(filepath);
            bitmap = BitmapFactory.decodeStream(fin);
            fin.close();
        }
        catch (FileNotFoundException e) {

        }
        catch (IOException e) {

        }
        return bitmap;
    }

    public static boolean save(Bitmap bitmap, String filepath) {
        try {
            FileOutputStream fos = new FileOutputStream(filepath);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            bitmap.recycle();
            fos.close();
            return true;
        }
        catch (FileNotFoundException e) {

        }
        catch (IOException e) {

        }
        return false;
    }

    public static Bitmap crop(Bitmap bitmap, Rect cropRect) {
        return Bitmap.createBitmap(bitmap,cropRect.left,cropRect.top,cropRect.width(),cropRect.height());
    }

    public static Bitmap cropWithCanvas(Bitmap bitmap, Rect cropRect) {
        Rect destRect = new Rect(0,0,cropRect.width(),cropRect.height());
        Bitmap cropped = Bitmap.createBitmap(cropRect.width(),cropRect.height(),Bitmap.Config.RGB_565);
        Canvas canvas = new Canvas(cropped);
        canvas.drawBitmap(bitmap,cropRect,destRect,null);
        return cropped;
    }

    public static Bitmap rotate(Bitmap bitmap, int degrees) {
        Matrix matrix = new Matrix();
        matrix.postRotate(degrees);
        return Bitmap.createBitmap(bitmap,0,0,bitmap.getWidth(),bitmap.getHeight(),matrix,true);
    }

    public static Bitmap rotateWithCanvas(Bitmap bitmap, int degrees) {

        int destWidth,destHeight;

        float centerX = bitmap.getWidth()/2;
        float centerY = bitmap.getHeight()/2;

        // We want to do the rotation at origin, but since the bounding
        // rectangle will be changed after rotation, so the delta values
        // are based on old & new width/height respectively.
        Matrix matrix = new Matrix();
        matrix.preTranslate(-centerX,-centerY);
        matrix.postRotate(degrees);
        if( degrees/90%2 == 0 ) {
            destWidth  = bitmap.getWidth();
            destHeight = bitmap.getHeight();
            matrix.postTranslate(centerX,centerY);
        }
        else {
            destWidth  = bitmap.getHeight();
            destHeight = bitmap.getWidth();
            matrix.postTranslate(centerY,centerX);
        }
        Bitmap cropped = Bitmap.createBitmap(destWidth,destHeight,Bitmap.Config.RGB_565);
        Canvas canvas = new Canvas(cropped);
        canvas.drawBitmap(bitmap, matrix, null);
        return cropped;
    }

    public static Bitmap getThumbnail(Bitmap bitmap,int width,int height) {
        return ThumbnailUtils.extractThumbnail(bitmap,width,height);
    }
    public static String saveImageToGallery(Bitmap bmp,Context context) {
        BufferedOutputStream bos = null;
        File file = null;
        try {
            if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                // 首先保存图片
                File appDir = SDcardHelper.getDCIMDir();
                SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss"); // 格式化时间
                String fileName = format.format(new Date()) + ".jpg";
                file = new File(appDir, fileName);
                if (!file.exists()) {
                    file.createNewFile();
                }
                bos = new BufferedOutputStream(new FileOutputStream(file));
                bmp.compress(Bitmap.CompressFormat.JPEG, 90, bos);//将图片压缩到流中
                // 通知图库更新
                context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(file)));
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                bos.flush();//输出
                bos.close();//关闭
                bmp.recycle();// 回收bitmap空间
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return (file != null) ? file.getAbsolutePath() : "保存失败";
    }
}

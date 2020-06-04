package com.haotang.pet.util.image;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import androidx.annotation.DrawableRes;

import com.haotang.pet.util.Utils;

/**
 * 作者：灼星
 * 时间：2020-04-09
 */
public class BitmapUtil {

    public static BitmapUtil instance;
    private BitmapUtil(){}

    public static BitmapUtil getInstance(){
        if (instance == null){
            synchronized (BitmapUtil.class){
                if(instance == null){
                    instance = new BitmapUtil();
                }
            }
        }
        return  instance;
    }

    /**
     * 裁剪图片drawable
     */
    public Bitmap cropDrawable (Resources res,@DrawableRes int id, int scaleWidth, int scaleHeight, int cropWidth, int cropHeight){
        //获取图片
        BitmapFactory.Options options = new BitmapFactory.Options();
        Bitmap bitmap = BitmapFactory.decodeResource(res,id,options);
        Utils.mLogError("图片高度："+" scaleWidth: "+scaleWidth+" scaleHeight: "
                +scaleHeight+" cropWidth: "+cropWidth+" cropHeight: "+cropHeight
                +" outHeight: "+options.outHeight+" outWidth: "+options.outWidth);
        //缩放图片
        bitmap = scaleBitmap(bitmap,scaleWidth,scaleHeight);
        Utils.mLogError("缩放后高度："+bitmap.getWidth()+" "+bitmap.getHeight());
        //裁剪图片
        bitmap = cropBitmap(bitmap,cropWidth,cropHeight);
        Utils.mLogError("裁剪后高度："+bitmap.getWidth()+" "+bitmap.getHeight());
        return bitmap;
    }

    /**
     * 根据给定的宽和高进行拉伸
     *
     * @param origin    原图
     * @param newWidth  新图的宽
     * @param newHeight 新图的高
     * @return new Bitmap
     */
    public Bitmap scaleBitmap(Bitmap origin, int newWidth, int newHeight) {
        if (origin == null) {
            return null;
        }
        int height = origin.getHeight();
        int width = origin.getWidth();
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);// 使用后乘
        Bitmap newBM = Bitmap.createBitmap(origin, 0, 0, width, height, matrix, false);
        if (!origin.isRecycled()) {
            origin.recycle();
        }
        return newBM;
    }

    /**
     * 按比例缩放图片
     *
     * @param origin 原图
     * @param ratio  比例
     * @return 新的bitmap
     */
    public Bitmap scaleBitmap(Bitmap origin, float ratio) {
        if (origin == null) {
            return null;
        }
        int width = origin.getWidth();
        int height = origin.getHeight();
        Matrix matrix = new Matrix();
        matrix.preScale(ratio, ratio);
        Bitmap newBM = Bitmap.createBitmap(origin, 0, 0, width, height, matrix, false);
        if (newBM.equals(origin)) {
            return newBM;
        }
        origin.recycle();
        return newBM;
    }

    /**
     * 裁剪
     *
     * @param bitmap 原图
     * @return 裁剪后的图像
     */
    public Bitmap cropBitmap(Bitmap bitmap) {
        int w = bitmap.getWidth(); // 得到图片的宽，高
        int h = bitmap.getHeight();
        int cropWidth = w >= h ? h : w;// 裁切后所取的正方形区域边长
        cropWidth /= 2;
        int cropHeight = (int) (cropWidth / 1.2);
        return Bitmap.createBitmap(bitmap, w / 3, 0, cropWidth, cropHeight, null, false);
    }

    /**
     * 裁剪
     *
     * @param bitmap 原图
     * @return 裁剪后的图像
     */
    public Bitmap cropBitmap(Bitmap bitmap,int width, int height) {
        Bitmap newBitmap;
        if (bitmap.getHeight() != height || bitmap.getWidth() != width){
            newBitmap = Bitmap.createBitmap(bitmap,  0, 0, width, height, null, false);
            if(!bitmap.isRecycled())
                bitmap.recycle();
        }else {
            newBitmap = bitmap;
        }
        return newBitmap;
    }

    /**
     * 选择变换
     *
     * @param origin 原图
     * @param alpha  旋转角度，可正可负
     * @return 旋转后的图片
     */
    public Bitmap rotateBitmap(Bitmap origin, float alpha) {
        if (origin == null) {
            return null;
        }
        int width = origin.getWidth();
        int height = origin.getHeight();
        Matrix matrix = new Matrix();
        matrix.setRotate(alpha);
        // 围绕原地进行旋转
        Bitmap newBM = Bitmap.createBitmap(origin, 0, 0, width, height, matrix, false);
        if (newBM.equals(origin)) {
            return newBM;
        }
        origin.recycle();
        return newBM;
    }

    /**
     * 偏移效果
     * @param origin 原图
     * @return 偏移后的bitmap
     */
    public Bitmap skewBitmap(Bitmap origin) {
        if (origin == null) {
            return null;
        }
        int width = origin.getWidth();
        int height = origin.getHeight();
        Matrix matrix = new Matrix();
        matrix.postSkew(-0.6f, -0.3f);
        Bitmap newBM = Bitmap.createBitmap(origin, 0, 0, width, height, matrix, false);
        if (newBM.equals(origin)) {
            return newBM;
        }
        origin.recycle();
        return newBM;
    }
}

package com.common.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Typeface;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Base64;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.util.Arrays;

/**
 * bmp文件工具类
 */
public class BmpUtils {
    private static final String TAG = "xgdBmpUtils";
    private Typeface typeface;

    public boolean isFileExist(String fileName) {
        File file = new File(Environment.getExternalStorageDirectory()
                + File.separator + fileName);
        return file.exists();
    }

    /**
     * create multiple director
     *
     * @param director 文件夹名称
     * @return true or false
     */
    public boolean createDir(String director) {
        if (isFileExist(director)) {
            return true;
        } else {
            File file = new File(Environment.getExternalStorageDirectory()
                    + File.separator + director);
            if (!file.exists()) {
                file.mkdirs();
                return false;
            }
            return true;
        }
    }


    public void deleteFile(File file) {
        if (file.exists()) {
            if (file.isFile()) {
                file.delete();
            } else if (file.isDirectory()) {
                File files[] = file.listFiles();
                for (int i = 0; i < files.length; i++) {
                    this.deleteFile(files[i]);
                }
            }
            file.delete();
        }
    }
    /**
     * 多位bitmap转换为单位bmp文件
     *
     * @param bitmap 需要转换的bitmap
     * @return 返回file文件
     */
    public byte[] toBmpBytes(Bitmap bitmap) {

        if (bitmap == null)
            return null;

        int nBmpWidth = bitmap.getWidth();
        int nBmpHeight = bitmap.getHeight();
        int nFixBmpHeight = ((nBmpHeight + 7) >> 3) << 3;
        int biBitCount = 1;
        int wWidth = ((((nBmpWidth * biBitCount) + 31) & ~31) >> 3);
        int bufferSize = wWidth * nFixBmpHeight;
        try {
            //bmp文件头
            int bfType = 0x4d42;
            long bfOffBits = 14 + 40 + 8;
            long bfSize = bfOffBits + bufferSize;
            int bfReserved1 = 0;
            int bfReserved2 = 0;
            ByteBuffer byteBuffer = ByteBuffer.allocate((int) bfSize);
            // 保存bmp文件头
            writeWord(byteBuffer, bfType);
            writeDword(byteBuffer, bfSize);
            writeWord(byteBuffer, bfReserved1);
            writeWord(byteBuffer, bfReserved2);
            writeDword(byteBuffer, bfOffBits);

            //bmp信息头
            long biSize = 40L;
            long biWidth = nBmpWidth;
            long biHeight = nFixBmpHeight;
            int biPlanes = 1;
//            int biBitCount = 1;
            long biCompression = 0L;
            long biSizeImage = bufferSize;
            long biXpelsPerMeter = 0L;
            long biYPelsPerMeter = 0L;
            long biClrUsed = 0L;
            long biClrImportant = 0L;
            // 保存bmp信息头
            writeDword(byteBuffer, biSize);
            writeLong(byteBuffer, biWidth);
            writeLong(byteBuffer, biHeight);
            writeWord(byteBuffer, biPlanes);
            writeWord(byteBuffer, biBitCount);
            writeDword(byteBuffer, biCompression);
            writeDword(byteBuffer, biSizeImage);
            writeLong(byteBuffer, biXpelsPerMeter);
            writeLong(byteBuffer, biYPelsPerMeter);
            writeDword(byteBuffer, biClrUsed);
            writeDword(byteBuffer, biClrImportant);
            writeLong(byteBuffer, 0xff000000L);
            writeLong(byteBuffer, 0xffffffffL);

            byte[] fix = new byte[(nFixBmpHeight - nBmpHeight) * wWidth];
            Arrays.fill(fix, (byte) 0xff);
            byteBuffer.put(fix);
            byte data[] = byteBuffer.array();
            int position = byteBuffer.position();
            int binary;
            int nCol, nRealCol;
            int wRow;
            int clr;
            int tmp;
            for (nCol = 0, nRealCol = nBmpHeight - 1; nCol < nBmpHeight; ++nCol, --nRealCol) {
                binary = 0;
                for (wRow = 0; wRow < (wWidth << 3); wRow++) {
                    if (wRow < nBmpWidth) {
                        clr = bitmap.getPixel(wRow, nCol);
                        tmp = (Color.red(clr) + Color.green(clr) + Color.blue(clr)) / 3 > 200 ? 1 : 0;
//                        tmp = (Color.red(clr) > 180 && Color.green(clr) > 180 && Color.blue(clr) > 180) ? 1 : 0;
                        binary <<= 1;
                        binary |= tmp;
                    } else {
                        binary <<= 1;
                    }

                    if ((wRow + 1) % 8 == 0) {
                        data[position + wWidth * nRealCol + (((wRow + 1) >> 3) - 1)] = (byte) binary;
                        binary = 0;
                    }
                }
            }
            return data;

        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private static void writeWord(ByteBuffer buffer, int value) throws IOException {
        buffer.put((byte) (value & 0xff));
        buffer.put((byte) (value >> 8 & 0xff));
    }

    private static void writeDword(ByteBuffer buffer, long value) throws IOException {
        buffer.put((byte) (value & 0xff));
        buffer.put((byte) (value >> 8 & 0xff));
        buffer.put((byte) (value >> 16 & 0xff));
        buffer.put((byte) (value >> 24 & 0xff));
    }

    private static void writeLong(ByteBuffer buffer, long value) throws IOException {
        buffer.put((byte) (value & 0xff));
        buffer.put((byte) (value >> 8 & 0xff));
        buffer.put((byte) (value >> 16 & 0xff));
        buffer.put((byte) (value >> 24 & 0xff));
    }

    // 缩放图片
    public Bitmap zoomImg(Bitmap bm, int newWidth, int newHeight) {
        // 获得图片的宽高
        int width = bm.getWidth();
        int height = bm.getHeight();
        // 计算缩放比例
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        // 取得想要缩放的matrix参数
        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);
        // 得到新的图片
        return Bitmap.createBitmap(bm, 0, 0, width, height, matrix, true);
    }

    //保存签名图片
    public void saveBitmapToBmp(Bitmap bitmap, String fileName){
        try {
            if (TextUtils.isEmpty(fileName)) fileName = "signature.bmp";
            File file = new File(Environment.getExternalStorageDirectory().getPath() + File.separator + fileName);
            if (!file.exists()) {
                file.createNewFile();
            }
            FileOutputStream os = new FileOutputStream(file);
            final byte[] data = toBmpBytes(bitmap);
            if (!bitmap.isRecycled()) bitmap.recycle();
            os.write(data);
            os.flush();
            os.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void saveBitmapToJPG(Bitmap bitmap, File photo) throws IOException {
        Bitmap newBitmap = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
//        Bitmap newBitmap = zoomImg(bitmap, 384, 192);
        Canvas canvas = new Canvas(newBitmap);
        canvas.drawColor(Color.WHITE);
        canvas.drawBitmap(bitmap, 0, 0, null);
        OutputStream stream = new FileOutputStream(photo);
        newBitmap.compress(Bitmap.CompressFormat.JPEG, 80, stream);
        stream.close();
    }

    /**
     * 将Bitmap转成String类型保存
     * @param bitmap 图片信息
     * @return 返回转成字符串数据
     */
    public String bitmap2Str(Bitmap bitmap){
        if (bitmap == null) return null;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] bytes = baos.toByteArray();// 转为byte数组
        return Base64.encodeToString(bytes, Base64.DEFAULT);
    }

    /**
     * 将String转成Bitmap
     * @param str 图片字符串
     * @return 返回转换后的图片
     */
    public Bitmap str2Bitmap(String str){
        Bitmap bitmap = null;
        try {
            if (TextUtils.isEmpty(str)) return bitmap;
            byte[] bitmapArray = Base64.decode(str, Base64.DEFAULT);
            bitmap = BitmapFactory.decodeByteArray(bitmapArray, 0, bitmapArray.length);
        }catch (Exception e){
            e.printStackTrace();
        }
        return bitmap;
    }
}

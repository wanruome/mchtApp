package com.ruomm.base.zxing;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import com.ruomm.base.tools.DisplayUtil;
import com.ruomm.base.tools.StringUtils;

import java.util.Hashtable;

public class ZxingCreateUtil {
    private static final int BLACK = 0xff000000;
    private static Bitmap createQRCode(String str, int widthAndHeight, ErrorCorrectionLevel errorCorrectionLevel)
            throws WriterException {
        Hashtable<EncodeHintType, Object> hints = new Hashtable<EncodeHintType, Object>();
        hints.put(EncodeHintType.ERROR_CORRECTION, errorCorrectionLevel);
        hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
        BitMatrix matrix = new MultiFormatWriter().encode(str, BarcodeFormat.QR_CODE, widthAndHeight, widthAndHeight,
                hints);
        int width = matrix.getWidth();
        int height = matrix.getHeight();
        int[] pixels = new int[width * height];

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                if (matrix.get(x, y)) {
                    pixels[y * width + x] = BLACK;
                }
            }
        }
        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        bitmap.setPixels(pixels, 0, width, 0, 0, width, height);
        return bitmap;
    }
    public static Bitmap createQRCode(String str, int widthAndHeight)
    {
                Bitmap bitmap=null;
                  try{
                      bitmap=createQRCode(str,widthAndHeight,ErrorCorrectionLevel.H);
                  }
                  catch (Exception e)
                  {
                        e.printStackTrace();
                        if(null!=bitmap)
                        {
                            bitmap.recycle();
                        }
                  }
                return bitmap;

    }
    public static Bitmap creatBarcode(Context context, String contents,
                                      int desiredWidth, int desiredHeight, boolean displayCode) {
        Bitmap ruseltBitmap = null;
//        int marginW = 20;
        BarcodeFormat barcodeFormat = BarcodeFormat.CODE_128;

        if (displayCode) {
            Bitmap barcodeBitmap = encodeAsBitmap(contents, barcodeFormat,
                    desiredWidth, desiredHeight);
            Bitmap codeBitmap = creatCodeBitmap(contents, desiredWidth , desiredHeight, context);
            if(null==codeBitmap)
            {
                return barcodeBitmap;
            }
            ruseltBitmap = mixtureBitmap(barcodeBitmap, codeBitmap, new PointF(
                    0, desiredHeight));
            return ruseltBitmap;
//            Bitmap codeBitmap = creatCodeBitmap(contents, desiredWidth + 2
//                    * marginW, desiredHeight, context);
//            ruseltBitmap = mixtureBitmap(barcodeBitmap, codeBitmap, new PointF(
//                    0, desiredHeight));
        } else {
            ruseltBitmap = encodeAsBitmap(contents, barcodeFormat,
                    desiredWidth, desiredHeight);
            return ruseltBitmap;
        }


    }


    protected static Bitmap encodeAsBitmap(String contents,
                                           BarcodeFormat format, int desiredWidth, int desiredHeight) {
        if(StringUtils.isEmpty(contents))
        {
            return null;
        }
        final int WHITE = 0xFFFFFFFF;
        final int BLACK = 0xFF000000;

        MultiFormatWriter writer = new MultiFormatWriter();
        BitMatrix result = null;
        try {
            result = writer.encode(contents, format, desiredWidth,
                    desiredHeight, null);
        } catch (WriterException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        int width = result.getWidth();
        int height = result.getHeight();
        int[] pixels = new int[width * height];
        // All are 0, or black, by default
        for (int y = 0; y < height; y++) {
            int offset = y * width;
            for (int x = 0; x < width; x++) {
                pixels[offset + x] = result.get(x, y) ? BLACK : WHITE;
            }
        }

        Bitmap bitmap = Bitmap.createBitmap(width, height,
                Bitmap.Config.ARGB_8888);
        bitmap.setPixels(pixels, 0, width, 0, 0, width, height);
        return bitmap;
    }


    protected static Bitmap creatCodeBitmap(String contents, int width,
                                            int height, Context context) {

        Paint paint_text = new Paint();
        paint_text.setAntiAlias(true);
        paint_text.setColor(Color.BLACK);
        paint_text.setTextSize(36);
        paint_text.setStyle(Paint.Style.FILL);
        int temp_length = contents.length();
        float chars_length[] = new float[temp_length];

        paint_text.getTextWidths(contents, chars_length);

        // 计算最大宽度
        float max_textwidth = 0;
        for (int temp = 0; temp < temp_length; temp++) {
            if (temp != temp_length - 1) {
                max_textwidth += chars_length[temp] * 1.0;
            }
            else {
                max_textwidth += chars_length[temp];
            }
        }
        float textSize=36*0.8f*width/max_textwidth;
        paint_text.setTextSize(textSize);
        max_textwidth = 0;
        paint_text.getTextWidths(contents, chars_length);
        for (int temp = 0; temp < temp_length; temp++) {
            if (temp != temp_length - 1) {
                max_textwidth += chars_length[temp] * 1.0;
            }
            else {
                max_textwidth += chars_length[temp];
            }
        }
        float heithSize[] = new float[2];
        paint_text.getTextWidths("绘图",heithSize);
        int textHeight=(int)(heithSize[0]+0.5);
        int cvHeight=textHeight*11/10;
//        int textHeight=DisplayUtil.sp2px(context,textSize);
        Bitmap newBitmap = Bitmap.createBitmap(
                width, cvHeight, Bitmap.Config.ARGB_4444);
        Canvas cv = new Canvas(newBitmap);
        cv.drawText(contents,(width-max_textwidth)/2,cvHeight,paint_text);
        cv.save(Canvas.ALL_SAVE_FLAG);
        cv.restore();
        return newBitmap;
//        TextView tv = new TextView(context);
//        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
//                width, LinearLayout.LayoutParams.WRAP_CONTENT);
//        tv.setLayoutParams(layoutParams);
//        tv.setText(contents);
//        tv.setHeight(height);
//        tv.setGravity(Gravity.CENTER_HORIZONTAL);
//        tv.setWidth(width);
//        tv.setDrawingCacheEnabled(true);
//        tv.setTextColor(Color.BLACK);
//
//        tv.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
//                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
//        tv.layout(0, 0, tv.getMeasuredWidth(), tv.getMeasuredHeight());
//
//        tv.buildDrawingCache();
//        Bitmap bitmapCode = tv.getDrawingCache();
//        return bitmapCode;
    }


    protected static Bitmap mixtureBitmap(Bitmap first, Bitmap second,
                                          PointF fromPoint) {
        if (first == null || second == null || fromPoint == null) {
            return null;
        }
        Bitmap newBitmap = Bitmap.createBitmap(
                first.getWidth(),
                first.getHeight() + second.getHeight(), Bitmap.Config.ARGB_4444);
        Canvas cv = new Canvas(newBitmap);
        cv.drawBitmap(first, 0, 0, null);

        cv.drawBitmap(second, fromPoint.x, fromPoint.y, null);
        cv.save(Canvas.ALL_SAVE_FLAG);
        cv.restore();

        return newBitmap;
    }

}

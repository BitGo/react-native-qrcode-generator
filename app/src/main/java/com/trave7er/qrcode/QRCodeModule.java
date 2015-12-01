package com.trave7er.qrcode;

import com.facebook.react.bridge.ReactMethod;
import com.google.zxing.BarcodeFormat;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.Callback;

import java.io.ByteArrayOutputStream;
import java.util.EnumMap;

import com.facebook.react.bridge.ReactContextBaseJavaModule;

import java.util.Map;

import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;

import android.graphics.Bitmap;
import android.util.Base64;

class QRCodeModule extends ReactContextBaseJavaModule {

  private static final int WHITE = 0xFFFFFFFF;
  private static final int BLACK = 0xFF000000;

  public QRCodeModule(ReactApplicationContext reactContext) {
    super(reactContext);
  }

  @Override
  public String getName() {
    return "QRCodeModule";
  }

  @ReactMethod
  public void base64Image(
        String message,
        int dimension,
        Callback success,
        Callback error) {
    try {
      Bitmap bitmap = encodeAsBitmap(message, dimension);
      ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
      bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
      byte[] byteArray = byteArrayOutputStream.toByteArray();
      String encoded = Base64.encodeToString(byteArray, Base64.DEFAULT);
      success.invoke(encoded);
    } catch(WriterException e) {
      error.invoke("" + e);
    }
  }

  Bitmap encodeAsBitmap(String message, int dimension) throws WriterException {
    String contentsToEncode = message;
    if (contentsToEncode == null) {
      return null;
    }
    Map<EncodeHintType,Object> hints = null;
    String encoding = "UTF-8";
    if (encoding != null) {
      hints = new EnumMap<>(EncodeHintType.class);
      hints.put(EncodeHintType.CHARACTER_SET, encoding);
    }
    BitMatrix result;
    try {
      result = new MultiFormatWriter().encode(contentsToEncode, BarcodeFormat.QR_CODE, dimension, dimension, hints);
    } catch (IllegalArgumentException iae) {
      // Unsupported format
      return null;
    }
    int width = result.getWidth();
    int height = result.getHeight();
    int[] pixels = new int[width * height];
    for (int y = 0; y < height; y++) {
      int offset = y * width;
      for (int x = 0; x < width; x++) {
        pixels[offset + x] = result.get(x, y) ? BLACK : WHITE;
      }
    }

    Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
    bitmap.setPixels(pixels, 0, width, 0, 0, width, height);
    return bitmap;
  }


}
package comm.helper;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.renderscript.Allocation;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;
import android.support.annotation.FloatRange;
import android.support.annotation.MainThread;
import android.view.View;

/**
 * 类描述：
 * 创建人：Administrator
 * 创建时间：2017/10/26 17:39
 * 传参：
 * 返回:
 */
public class BlurHelper {
    @MainThread
    public static Bitmap blur(Context context, View view, @FloatRange(from = 0, to = 25) float radius) {
        Bitmap sourceBitmap = getScreenshot(view);
        return blur(context, sourceBitmap, radius);
    }

    public static Bitmap blur(Context context, Bitmap origin, @FloatRange(from = 0, to = 25) float radius) {
        Bitmap scaled = Bitmap.createScaledBitmap(origin, origin.getWidth(), origin.getHeight(), false);
        Bitmap output = Bitmap.createBitmap(scaled);

        RenderScript rs = RenderScript.create(context);
        Allocation allIn = Allocation.createFromBitmap(rs, scaled);
        Allocation allOut = Allocation.createFromBitmap(rs, output);

        ScriptIntrinsicBlur blur = ScriptIntrinsicBlur.create(rs, allIn.getElement());

        blur.setRadius(radius);
        blur.setInput(allIn);
        blur.forEach(allOut);
        allOut.copyTo(output);
        return output;
    }

    @MainThread
    private static Bitmap getScreenshot(View v) {
        Bitmap b = Bitmap.createBitmap(v.getWidth(), v.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(b);
        v.draw(c);
        return b;
    }
}

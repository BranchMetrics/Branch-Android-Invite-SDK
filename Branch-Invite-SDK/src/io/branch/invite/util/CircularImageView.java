package io.branch.invite.util;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;

/**
 * Custom Image view class to show a n image in circle.
 */
public class CircularImageView extends ImageView {
    private Bitmap image;
    private final int borderWidth;
    private int canvasSize;
    private Paint paint;
    private final Paint paintBorder;


    public CircularImageView(Context context) {
        super(context);
        paint = new Paint();
        paint.setAntiAlias(true);

        paintBorder = new Paint();
        paintBorder.setAntiAlias(true);
        paintBorder.setColor(Color.WHITE);
        // No border required. Take as much spae as possible for the image
        borderWidth = 0;
    }

    @Override
    public void onDraw(Canvas canvas) {
        if (image != null) {
            canvasSize = canvas.getWidth();
            if (canvas.getHeight() < canvasSize)
                canvasSize = canvas.getHeight();

            @SuppressLint("DrawAllocation")
            BitmapShader shader = new BitmapShader(Bitmap.createScaledBitmap(
                    image, canvasSize, canvasSize, false),
                    Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
            paint.setShader(shader);

            int circleCenter = (canvasSize - (borderWidth * 2)) / 2;
            if (borderWidth > 0) {
                canvas.drawCircle(circleCenter + borderWidth, circleCenter
                        + borderWidth, ((canvasSize - (borderWidth * 2)) / 2)
                        + borderWidth - 4.0f, paintBorder);
            }
            canvas.drawCircle(circleCenter + borderWidth, circleCenter
                    + borderWidth, ((canvasSize - (borderWidth * 2)) / 2) - 4.0f, paint);
        }

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = getMeasuredSpec(widthMeasureSpec);
        int height = getMeasuredSpec(heightMeasureSpec + 2);
        setMeasuredDimension(width, height);
    }

    private int getMeasuredSpec(int measureSpec){
        int result;
        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);

        if (specMode == MeasureSpec.EXACTLY) {
            // The parent has determined an exact size for the child.
            result = specSize;
        } else if (specMode == MeasureSpec.AT_MOST) {
            // The child can be as large as it wants up to the specified size.
            result = specSize;
        } else {
            // The parent has not imposed any constraint on the child.
            result = canvasSize;
        }
        return result;
    }



    public void setCircularBitmap(Bitmap bitmap) {
        this.image = bitmap;
        super.setImageBitmap(bitmap);
        invalidate();
    }

    public void setCircularDrawable(Drawable drawable) {
        setCircularBitmap(drawableToBitmap(drawable));
    }


    public static Bitmap drawableToBitmap(Drawable drawable) {
        if (drawable instanceof BitmapDrawable) {
            return ((BitmapDrawable) drawable).getBitmap();
        }

        int width = drawable.getIntrinsicWidth();
        width = width > 0 ? width : 1;
        int height = drawable.getIntrinsicHeight();
        height = height > 0 ? height : 1;

        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);

        return bitmap;
    }
}

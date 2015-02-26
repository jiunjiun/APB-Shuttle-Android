package tw.jiunjiun.apb.shuttle.ImageTouch;

import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.graphics.RectF;
import android.util.DisplayMetrics;
import android.util.FloatMath;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;

/**
 * Created by jiun on 2015/2/26.
 *
 * http://givemepass.blogspot.tw/2012/11/blog-post.html
 */
public class ImageViewHelper {
    private ImageView imageView;
    private Matrix matrix = new Matrix();
    private Matrix savedMatrix = new Matrix();
    private Bitmap bitmap;

    private float minScaleR;// ³Ì¤pÁY©ñ¤ñ¨Ò

    private static final int NONE = 0;// ªì©lª¬ºA
    private static final int DRAG = 1;// ©ì¦²ª¬ºA
    private static final int ZOOM = 2;// ÁY©ñª¬ºA
    private int mode = NONE;

    private PointF prev = new PointF();
    private PointF mid = new PointF();
    private float dist = 1f;
    private DisplayMetrics dm;

    public ImageViewHelper(DisplayMetrics dm,ImageView imageView,Bitmap bitmap){
        this.dm = dm;
        this.imageView = imageView;
        this.bitmap = bitmap;
        setImageSize();
//        minZoom();
        center();
        imageView.setImageMatrix(matrix);
    }

    public Matrix getMatrix(){
        return matrix;
    }


    public void center() {
        center(true, true);
    }

    //¾î¦V¡BÁa¦V¸m¤¤
    public void center(boolean horizontal, boolean vertical) {

        Matrix m = new Matrix();
        m.set(matrix);
        RectF rect = new RectF(0, 0, bitmap.getWidth(), bitmap.getHeight());
        m.mapRect(rect);

        float height = rect.height();
        float width = rect.width();

        float deltaX = 0, deltaY = 0;

        if (vertical) {
            int screenHeight = dm.heightPixels;
            if (height < screenHeight) {
                deltaY = (screenHeight - height) / 2 - rect.top;
            } else if (rect.top > 0) {
//                deltaY = -rect.top;
            } else if (rect.bottom < screenHeight) {
                deltaY = imageView.getHeight() - rect.bottom;
            }
        }

        if (horizontal) {
            int screenWidth = dm.widthPixels;
            if (width < screenWidth) {
                deltaX = (screenWidth - width) / 2 - rect.left;
            } else if (rect.left > 0) {
//                deltaX = -rect.left;
            } else if (rect.right < screenWidth) {
                deltaX = screenWidth - rect.right;
            }
        }
//        Log.e("", "deltaX: " + deltaX + ", deltaY: " + deltaY);
//        Log.e("", "rect.left: " + rect.left + ", rect.top: " + rect.top);
        matrix.postTranslate(deltaX, deltaY);
    }

    //¨âÂIªº¶ZÂ÷
    public float spacing(MotionEvent event) {
        float x = event.getX(0) - event.getX(1);
        float y = event.getY(0) - event.getY(1);

//        Log.e("", "spacing: " + FloatMath.sqrt(x * x + y * y));
        return FloatMath.sqrt(x * x + y * y);
    }

    public void midPoint(PointF point, MotionEvent event) {
        float x = event.getX(0) + event.getX(1);
        float y = event.getY(0) + event.getY(1);
        point.set(x / 2, y / 2);
    }

    public void setImageSize(){
        imageView.setOnTouchListener(new View.OnTouchListener(){
            @Override
            public boolean onTouch(View arg0, MotionEvent event) {
                switch (event.getAction() & MotionEvent.ACTION_MASK) {
                    case MotionEvent.ACTION_DOWN:
                        savedMatrix.set(matrix);
                        prev.set(event.getX(), event.getY());
                        mode = DRAG;
                        break;

                    case MotionEvent.ACTION_POINTER_DOWN:
                        dist = spacing(event);
                        if (spacing(event) > 10f) {
                            savedMatrix.set(matrix);
                            midPoint(mid, event);
                            mode = ZOOM;
                        }
                        break;
                    case MotionEvent.ACTION_UP:
                    case MotionEvent.ACTION_POINTER_UP:
                        showMatrixLog();
                        minScale();
                        showMatrixLog();
                        mode = NONE;
                        break;
                    case MotionEvent.ACTION_MOVE:
                        if (mode == DRAG) {
                            matrix.set(savedMatrix);
                            matrix.postTranslate(event.getX() - prev.x, event.getY()
                                    - prev.y);

//                            Log.e("", " Translate x: " + (event.getX() - prev.x) + ", y: " + (event.getY() - prev.y));
                        } else if (mode == ZOOM) {
                            float newDist = spacing(event);
                            if (newDist > 10f) {
                                matrix.set(savedMatrix);
                                float tScale = newDist / dist;
//                                if(tScale < 1) tScale = 1.0f;
                                matrix.postScale(tScale, tScale, mid.x, mid.y);
                                Log.e("", " Scale tScale: " + tScale + ", x: " + mid.x + ", y: "+ mid.y);
                            }
                        }
                        break;
                }
                imageView.setImageMatrix(matrix);
                center();
                return true;
            }

        });
    }

    private void minScale() {
        Log.e("", "minScale" );
        float[] f = new float[9];
        matrix.getValues(f);

        float scalex = f[Matrix.MSCALE_X];
        if (scalex < 1) {
            Log.e("", "scalex: " + scalex );
            matrix.postScale(1, 1, mid.x, mid.y);
        }
    }

    private void showMatrixLog() {
        float[] f = new float[9];
        matrix.getValues(f);


        float scaleX = f[Matrix.MSCALE_X];
        float scaleY = f[Matrix.MSCALE_Y];

//        Log.e("", "matrix: scaleX: "+ scaleX +  ", scaleY: "+ scaleY);

        float scalex = f[Matrix.MSCALE_X];
        float skewy = f[Matrix.MSKEW_Y];
        float scale = (float) Math.sqrt(scalex * scalex + skewy * skewy);

        Log.e("", "matrix: scalex: "+ scalex +  ", skewy: "+ skewy + ", scale: "+ scale );
    }
}
//0.3035901
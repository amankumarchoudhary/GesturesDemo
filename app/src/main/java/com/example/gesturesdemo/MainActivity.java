package com.example.gesturesdemo;

import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;


public class MainActivity extends AppCompatActivity implements View.OnTouchListener,
        GestureDetector.OnGestureListener, GestureDetector.OnDoubleTapListener,
        ScaleGestureDetector.OnScaleGestureListener {

    private static final String TAG = "MainActivity";
    Boolean ableToZoom = true, a = false, incrementax = false, incrementay = false;
    float posx, posy, scalefactor = 1.0f, whereToZoomX = 0.0f, whereToZoomY = 0.0f,
            whereXTouchFirst=0.0f,whereYTouchFirst=0.0f,whereXTouchSecond=0.0f,whereYTouchSecond=0.0f;
    float ax = 0.0f, ay = 0.0f, azx = 0.0f, azy = 0.0f, currentSpan = 0.0f;
    GestureDetector gestureDetector;
    ScaleGestureDetector scaleGestureDetector;
    int heightOfImage,widthOfImage,height,width;
    Matrix matrix;

    //widgets
    private ImageView mImageView1, mImageView2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mImageView1 = findViewById(R.id.image1);
//        mImageView2 = findViewById(R.id.image2);

        mImageView1.setOnTouchListener(this);
//        mImageView2.setOnTouchListener(this);
        Log.d(TAG, "onCreate: aman" + mImageView1.getX() + "," + mImageView1.getY());

        gestureDetector = new GestureDetector(this, this);
//        setImage();
        scaleGestureDetector = new ScaleGestureDetector(this, this);


        /*final int[] finalHeight = new int[1];
        final int[] finalWidth = new int[1];
        ViewTreeObserver vto = mImageView1.getViewTreeObserver();
        vto.addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            public boolean onPreDraw() {
                mImageView1.getViewTreeObserver().removeOnPreDrawListener(this);
                finalHeight[0] = mImageView1.getMeasuredHeight();
                finalWidth[0] = mImageView1.getMeasuredWidth();
                return true;
            }
        });*/

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        height = displayMetrics.heightPixels;
        width = displayMetrics.widthPixels;

        matrix=mImageView1.getImageMatrix();
//        matrix.setTranslate(0.0f,-1000.0f);
//        mImageView1.setImageMatrix(matrix);
        float[] arr=new float[9];
        matrix.getValues(arr);
        System.out.println("Aman"+arr[2]+" "+arr[5]);


    }


    private void setImage() {
        Glide.with(MainActivity.this)
                .load(R.drawable.france_mtn)
                .into(mImageView1);


        /*Glide.with(this)
                .load(R.drawable.oregon_beach)
                .into(mImageView2);*/
    }


    //View.OnTouchListener methods
    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
//        Bitmap b=((BitmapDrawable)mImageView1.getBackground()).getBitmap();
        heightOfImage=mImageView1.getHeight();
        widthOfImage=mImageView1.getWidth();

        ax=width-widthOfImage;
        ay=(height-heightOfImage)/2;

        int action = motionEvent.getAction();
        int index=motionEvent.getActionIndex();
        if(index==1){
            whereXTouchSecond=motionEvent.getX(1);
            whereYTouchSecond=motionEvent.getY(1);
        }
        switch (action) {
            case (MotionEvent.ACTION_DOWN):
                whereXTouchFirst = motionEvent.getX();
                whereYTouchFirst = motionEvent.getY();
                Log.d(TAG, "Action dOWN called");
                posx = motionEvent.getX();
                posy = motionEvent.getY();
//                Log.d(TAG, "onTouch: called, (" + posx + "," + posy + ")");
                Log.d("math", "Action down called" + motionEvent.getX() + " , " + motionEvent.getY());
                break;

            case (MotionEvent.ACTION_MOVE):
//                Log.d(TAG, "Action movement called");
                Log.d(TAG, "onTouch: (" + motionEvent.getX() + "," + motionEvent.getY() + ")");
                if (posx - motionEvent.getX() > 5.0f || posx - motionEvent.getX() < -5.0f
                        || posy - motionEvent.getY() > 5.0f || posy - motionEvent.getY() < -5.0f) {
                    float bx = motionEvent.getX();
                    float by = motionEvent.getY();
                    Matrix matrix = mImageView1.getImageMatrix();

                    //We want to move image when it has zoomed.
//                    if (!ableToZoom) {

                    matrix.setScale(scalefactor, scalefactor,azx,azy);
                    if (bx - posx + ax > 0 && by - posy + ay > 0) {
                        matrix.postTranslate(0.0f, 0.0f);
                        ax = ay = 0.0f;
                        incrementax = false;
                        incrementay = false;
                    } else if (bx - posx + ax > 0) {
                        matrix.postTranslate(0.0f, by - posy + ay);
                        incrementax = false;
                        incrementay = true;
                    } else if (by - posy + ay > 0) {
                        matrix.postTranslate(bx - posx + ax, 0.0f);
                        incrementay = false;
                        incrementax = true;
                    } else {
                        matrix.postTranslate(bx - posx + ax, by - posy + ay);
                        incrementax = true;
                        incrementay = true;
                    }

//                    }


                    //We want to move image when it has not zoomed.
                    /*else {

                        if (bx - posx + ax > 0 && by - posy + ay > 0) {
                            matrix.setTranslate(0.0f, 0.0f);
                            ax = ay = 0.0f;
                            incrementax = false;
                            incrementay = false;
                        } else if (bx - posx + ax > 0) {
                            matrix.setTranslate(0.0f, by - posy + ay);
                            float[] arr = new float[9];
                            matrix.getValues(arr);
                            ax = arr[2];
                            ay = arr[5];
                            incrementax = false;
                            incrementay = true;
                        } else if (by - posy + ay > 0) {
                            matrix.setTranslate(bx - posx + ax, 0.0f);
                            float[] arr = new float[9];
                            matrix.getValues(arr);
                            ax = arr[2];
                            ay = arr[5];
                            incrementay = false;
                            incrementax = true;
                        } else {
                            matrix.setTranslate(bx - posx + ax, by - posy + ay);
                            float[] arr = new float[9];
                            matrix.getValues(arr);
                            incrementax = true;
                            incrementay = true;
                        }

                    }*/
                    mImageView1.setImageMatrix(matrix);
                    mImageView1.invalidate();
                    Log.d(TAG, "move: aman" + mImageView1.getX() + "," + mImageView1.getY());

                }
                break;


            case (MotionEvent.ACTION_UP):
                /*Log.d(TAG, "Action up called");
                whereToZoomX = 0.0f;
                whereToZoomY = 0.0f;
                if (incrementax && incrementay) {
                    ax = ax + motionEvent.getX() - posx;
                    ay = ay + motionEvent.getY() - posy;
                }else if(incrementax){
                    ax = ax + motionEvent.getX() - posx;
                }else if(incrementay){
                    ay = ay + motionEvent.getY() - posy;
                }
                if (a) {
                    ax = 0.0f;
                    ay = 0.0f;
                    a = false;
                }
                Log.d(TAG, "up: aman" + ax + "," + ay);
                Log.d("math", "Action up" + motionEvent.getX() + " , " + motionEvent.getY());*/
                if(incrementay||incrementax){
                    ax=ax+motionEvent.getX()-posx;
                    ay=ay+motionEvent.getY()-posy;
                }
                break;

            case (MotionEvent.ACTION_CANCEL):
                Log.d(TAG, "Action cancel called");
                break;

            case (MotionEvent.ACTION_OUTSIDE):
                Log.d(TAG, "Action outside: called");
                break;

            case (MotionEvent.ACTION_POINTER_DOWN):
                Log.d(TAG, "Pointer down called");
                Log.d("math", "Pointer down called" + motionEvent.getX() + " , " + motionEvent.getY());
                break;

            case (MotionEvent.ACTION_POINTER_UP):
                Log.d(TAG, "Pointer up called");
                Log.d("math", "Pointer up called" + motionEvent.getX() + " , " + motionEvent.getY());
                break;

        }


        gestureDetector.onTouchEvent(motionEvent);
        scaleGestureDetector.onTouchEvent(motionEvent);
        return true;
    }


    //Double Tap Listener
    @Override
    public boolean onSingleTapConfirmed(MotionEvent motionEvent) {
        Log.d(TAG, "onSingleTapConfirmed: called");
        return true;
    }

    @Override
    public boolean onDoubleTap(MotionEvent motionEvent) {
        Log.d(TAG, "onDoubleTap: called");
        /*if (ableToZoom) {
            Matrix matrix = mImageView1.getImageMatrix();
            matrix.setScale(2.0f, 2.0f, motionEvent.getX(), motionEvent.getY());
            mImageView1.setImageMatrix(matrix);
            mImageView1.invalidate();
            ableToZoom = false;
            azx = motionEvent.getX();
            azy = motionEvent.getY();
            Log.d(TAG, "zoom: aman" + mImageView1.getWidth() + "," + mImageView1.getHeight());
            DisplayMetrics displayMetrics = new DisplayMetrics();
            getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
            int height = displayMetrics.heightPixels;
            int width = displayMetrics.widthPixels;
            Log.d(TAG, "zoom: aman" + width + " " + height);

        } else {
            Matrix matrix = mImageView1.getImageMatrix();
            matrix.setScale(1.0f, 1.0f, motionEvent.getX(), motionEvent.getY());
            mImageView1.setImageMatrix(matrix);
            mImageView1.invalidate();
            ableToZoom = true;
            ax = 0.0f;
            ay = 0.0f;
            a = true;
            Log.d(TAG, "notzoom: aman" + mImageView1.getWidth() + "," + mImageView1.getHeight());
            DisplayMetrics displayMetrics = new DisplayMetrics();
            getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
            int height = displayMetrics.heightPixels;
            int width = displayMetrics.widthPixels;
            Log.d(TAG, "notzoom: aman" + width + " " + height);

        }*/
        return true;
    }

    @Override
    public boolean onDoubleTapEvent(MotionEvent motionEvent) {
        Log.d(TAG, "onDoubleTapEvent: called");
        return true;
    }


    //On Gesture Listener
    @Override
    public boolean onDown(MotionEvent motionEvent) {
        Log.d(TAG, "onDown: called");
        return true;
    }

    @Override
    public void onShowPress(MotionEvent motionEvent) {
        Log.d(TAG, "onShowPress: called");
    }

    @Override
    public boolean onSingleTapUp(MotionEvent motionEvent) {
        Log.d(TAG, "onSingleTapUp: called");
        return true;
    }

    @Override
    public boolean onScroll(MotionEvent motionEvent, MotionEvent motionEvent1, float v, float v1) {
//        Log.d(TAG, "onScroll: called");
        return true;
    }

    @Override
    public void onLongPress(MotionEvent motionEvent) {
        Log.d(TAG, "onLongPress: called");
    }

    @Override
    public boolean onFling(MotionEvent motionEvent, MotionEvent motionEvent1, float v, float v1) {
        Log.d(TAG, "onFling: called");
        return false;
    }


    //Scale Gesture Detector
    @Override
    public boolean onScale(ScaleGestureDetector scaleGestureDetector) {
        Log.d(TAG, "onScale: called");
        incrementax=incrementay=false;
        if (currentSpan - scaleGestureDetector.getCurrentSpan() < -10) {
            Matrix matrix = mImageView1.getImageMatrix();
            if(!(scalefactor>=3.0f)) {
                scalefactor += 0.02f;
                matrix.setScale(scalefactor, scalefactor, whereToZoomX, whereToZoomY);
                float[] arr = new float[9];
                matrix.getValues(arr);
                ax = arr[2];
                ay = arr[5];
                azx = whereToZoomX;
                azy = whereToZoomY;
                currentSpan=scaleGestureDetector.getCurrentSpan();
            }else {
                scalefactor = 3.0f;
                matrix.setScale(scalefactor, scalefactor, whereToZoomX, whereToZoomY);
                float[] arr = new float[9];
                matrix.getValues(arr);
                ax = arr[2];
                ay = arr[5];
                azx = whereToZoomX;
                azy = whereToZoomY;
                currentSpan=scaleGestureDetector.getCurrentSpan();
            }
            mImageView1.setImageMatrix(matrix);
            mImageView1.invalidate();
        } else if (currentSpan - scaleGestureDetector.getCurrentSpan() > 10) {
            Matrix matrix = mImageView1.getImageMatrix();
            if (!(scalefactor <= 1.0)){
                scalefactor -= 0.02f;
                matrix.setScale(scalefactor, scalefactor, whereToZoomX, whereToZoomY);
                float[] arr = new float[9];
                matrix.getValues(arr);
                ax = arr[2];
                ay = arr[5];
                azx=whereToZoomX;
                azy=whereToZoomY;
                currentSpan=scaleGestureDetector.getCurrentSpan();
            }else {
                scalefactor =1.0f;
                matrix.setScale(scalefactor, scalefactor, whereToZoomX, whereToZoomY);
                float[] arr = new float[9];
                matrix.getValues(arr);
                ax = arr[2];
                ay = arr[5];
                azx=whereToZoomX;
                azy=whereToZoomY;
                currentSpan=scaleGestureDetector.getCurrentSpan();
            }
            mImageView1.setImageMatrix(matrix);
            mImageView1.invalidate();
        }

        return true;
    }

    @Override
    public boolean onScaleBegin(ScaleGestureDetector scaleGestureDetector) {
        Log.d(TAG, "onScaleBegin: called");
        currentSpan = scaleGestureDetector.getCurrentSpan();

        if(whereXTouchFirst>whereXTouchSecond){
            whereToZoomX=((whereXTouchSecond+scaleGestureDetector.getCurrentSpanX()/2)+Math.abs(ax))/scalefactor;
        }else{
            whereToZoomX=((whereXTouchFirst+scaleGestureDetector.getCurrentSpanX()/2)+Math.abs(ax))/scalefactor;
        }

        if(whereYTouchFirst>whereYTouchSecond){
            whereToZoomY=((whereYTouchSecond+scaleGestureDetector.getCurrentSpanY()/2)+Math.abs(ay))/scalefactor;
        }else {
            whereToZoomY = ((whereYTouchFirst + scaleGestureDetector.getCurrentSpanY() / 2) + Math.abs(ay)) / scalefactor;
        }

        return true;
    }

    @Override
    public void onScaleEnd(ScaleGestureDetector scaleGestureDetector) {
        Log.d(TAG, "onScaleEnd: called");
        currentSpan = 0.0f;

    }


}




package com.ajdm05.androidfacetracker.Camera;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceView;
import android.view.ViewGroup;
import android.view.SurfaceHolder;

import com.google.android.gms.common.images.Size;
import com.google.android.gms.vision.CameraSource;

import java.io.IOException;

/**
 * Created by Angela Delgado on 7/28/2017.

 */

public class CameraPreview extends ViewGroup{

    private Context mContext;
    private SurfaceView mSurfaceView;
    private boolean mStartRequested;
    private boolean mSurfaceAvailable;
    private CameraSource mCameraSource;

    private CameraOverlay mCameraOverlay;

    //In the Ctor: Create the SurfaceView which implements the SurfaceHolder
    public CameraPreview(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        mStartRequested = false;
        mSurfaceAvailable = false;

        mSurfaceView = new SurfaceView(context);
        mSurfaceView.getHolder().addCallback(new SurfaceCallback());
        addView(mSurfaceView);
    }

    public void start(CameraSource cameraSource, CameraOverlay overlay) throws IOException {
        mCameraOverlay = overlay;
        if (cameraSource == null) {
            stop();
        }

        mCameraSource = cameraSource;

        if (mCameraSource != null) {
            mStartRequested = true;
            startWhenReady();
        }
    }

    public void stop() {
        if (mCameraSource != null) {
            mCameraSource.stop();
        }
    }

    //Start the camera by setting the info
    private void startWhenReady() throws IOException {
        if (mStartRequested && mSurfaceAvailable) {
            mCameraSource.start(mSurfaceView.getHolder());
            if (mCameraOverlay != null) {
                Size size = mCameraSource.getPreviewSize();
                int prevWidth = Math.min(size.getWidth(), size.getHeight());
                int prevHeight = Math.max(size.getWidth(), size.getHeight());
                mCameraOverlay.setCameraInfo(prevWidth, prevHeight, mCameraSource.getCameraFacing());
                mCameraOverlay.clear();
            }
            mStartRequested = false;
        }
    }

    //Build the preview layout
    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int width = 320;
        int height = 240;

        if (mCameraSource != null) {
            Size size = mCameraSource.getPreviewSize();
            if (size != null) {
                width = size.getWidth();
                height = size.getHeight();
            }
        }

        //This is because we're just rendering a portrait mode
        int tmp = width;
        width = height;
        height = tmp;

        for (int i = 0; i < getChildCount(); ++i) {
            getChildAt(i).layout(0, 0, width, height);
        }

        try {
            startWhenReady();
        } catch (IOException e) {
        }
    }

    // SurfaceHolder
    private class SurfaceCallback implements SurfaceHolder.Callback {

        //start the camera when the surface is created
        @Override
        public void surfaceCreated(SurfaceHolder surface) {
            mSurfaceAvailable = true;
            try {
                startWhenReady();
            } catch (IOException e) {
                Log.e("Android face tracker", "Something went wrong while creating the surface.");
            }
        }

        @Override
        public void surfaceDestroyed(SurfaceHolder surface) {
            mSurfaceAvailable = false;
        }

        @Override
        public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        }
    }
}

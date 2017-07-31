package com.ajdm05.androidfacetracker;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;

import com.ajdm05.androidfacetracker.Camera.CameraOverlay;
import com.google.android.gms.vision.face.Face;

/**
 * Created by Angela Delgado on 7/28/2017.
 */

public class FaceOverlay extends CameraOverlay.OverlayGraphic {

    private static final float BOX_STROKE_WIDTH = 5.0f;
    private Paint mFacePositionPaint;
    private Paint mBoxPaint;

    private volatile Face mFace;

    FaceOverlay(CameraOverlay overlay) {
        super(overlay);

        final int selectedColor = Color.GREEN;

        mFacePositionPaint = new Paint();
        mFacePositionPaint.setColor(selectedColor);

        mBoxPaint = new Paint();
        mBoxPaint.setColor(selectedColor);
        mBoxPaint.setStyle(Paint.Style.STROKE);
        mBoxPaint.setStrokeWidth(BOX_STROKE_WIDTH);
    }

    void updateFace(Face face) {
        mFace = face;
        postInvalidate();
    }

    //drawing the rectangle into the canvas
    @Override
    public void draw(Canvas canvas) {
        Face face = mFace;
        if (face == null) {
            return;
        }

        float x = translateX(face.getPosition().x + face.getWidth() / 2);
        float y = translateY(face.getPosition().y + face.getHeight() / 2);

        //Get face info
        Log.i("Right eye", String.valueOf(face.getIsRightEyeOpenProbability()));
        Log.i("Left eye", String.valueOf(face.getIsLeftEyeOpenProbability()));

        // Draws a bounding box around the face.
        float xOffset = scaleX(face.getWidth() / 2.0f);
        float yOffset = scaleY(face.getHeight() / 2.0f);
        float left = x - xOffset;
        float top = y - yOffset;
        float right = x + xOffset;
        float bottom = y + yOffset;
        canvas.drawRect(left, top, right, bottom, mBoxPaint);
    }
}

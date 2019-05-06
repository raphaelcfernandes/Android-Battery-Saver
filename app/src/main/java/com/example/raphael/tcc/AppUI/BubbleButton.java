package com.example.raphael.tcc.AppUI;

import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Build;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;

import com.example.raphael.tcc.R;

import java.util.Calendar;

public class BubbleButton{
    private WindowManager windowManager;
    private ImageView floatingButton;
    private String CUSTOM_INTENT="com.example.raphael.tcc.CreateMenuWindow";

    public void createFeedBackButton(final Context context){
        windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        floatingButton = new ImageView(context);
        floatingButton.setImageResource(R.mipmap.ic_cross_symbol);
        int LAYOUT_FLAG;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            LAYOUT_FLAG = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
        } else {
            LAYOUT_FLAG = WindowManager.LayoutParams.TYPE_PHONE;
        }
        final WindowManager.LayoutParams params = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                LAYOUT_FLAG,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT);
        params.gravity = Gravity.TOP | Gravity.START;
        params.x = 700;
        params.y = 300;
        windowManager.addView(floatingButton, params);
        floatingButton.setOnTouchListener(new View.OnTouchListener(){
            private final static int MAX_CLICK_DURATION = 100;
            private long startClickTime;
            private int initialX;
            private int initialY;
            private float initialTouchX;
            private float initialTouchY;
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        initialX = params.x;
                        initialY = params.y;
                        initialTouchX = event.getRawX();
                        initialTouchY = event.getRawY();
                        startClickTime = Calendar.getInstance().getTimeInMillis();
                        break;
                    case MotionEvent.ACTION_UP:
                        if(Calendar.getInstance().getTimeInMillis() - startClickTime < MAX_CLICK_DURATION) {
                            Intent i = new Intent(context,FeedBackPopUpWindow.class);
                            i.setAction(CUSTOM_INTENT);
                            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            context.startActivity(i);
                        }
                        break;
                    case MotionEvent.ACTION_MOVE:
                        params.x = initialX + (int) (event.getRawX() - initialTouchX);
                        params.y = initialY + (int) (event.getRawY() - initialTouchY);
                        windowManager.updateViewLayout(floatingButton, params);
                        break;
                }
                return true;
            }
        });
    }
    public void removeView()
    {
        try
        {
            windowManager.removeView(floatingButton);
        }
        catch(NullPointerException e)
        {

        }
    }

}

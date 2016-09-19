package com.example.raphael.tcc;

import android.app.Activity;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;

import java.util.Calendar;

public class FeedbackButton{
    private WindowManager windowManager;
    private ImageView floatingButton;
    private boolean mIsFloatingView = false;
    private BroadcastReceiver receiver;
    public void createFeedBackButton(Context context){
        mIsFloatingView=true;
        windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        floatingButton = new ImageView(context);
        floatingButton.setImageResource(R.mipmap.ic_cross_symbol);
        final WindowManager.LayoutParams params = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.TYPE_PHONE,
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
                            System.out.println("ok");
                            //Criar uma caixa de mensagem aqui. Chamar um objeto/metodo
                            //Enviar uma intent? Chamar por serviço?
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
    public void removeView() {
        if (floatingButton != null){
            windowManager.removeView(floatingButton);
            mIsFloatingView = false;
        }
    }
}

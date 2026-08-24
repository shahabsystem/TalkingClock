package com.yasin.farsitalkingclock;

import android.content.Context;
import android.graphics.*;
import android.view.View;
import java.util.*;

public class AnalogClockView extends View {
    private final Paint p=new Paint(Paint.ANTI_ALIAS_FLAG); private final ScheduleStore st; private final Calendar now=Calendar.getInstance();
    public AnalogClockView(Context c){super(c);st=new ScheduleStore(c);p.setStrokeCap(Paint.Cap.ROUND);setLayerType(View.LAYER_TYPE_SOFTWARE,null);}
    public void setNow(Calendar c){now.setTimeInMillis(c.getTimeInMillis());invalidate();}
    @Override protected void onDraw(Canvas c){super.onDraw(c);float cx=getWidth()/2f,cy=getHeight()/2f;float r=Math.min(getWidth(),getHeight())*.38f;int bg=UiTheme.surface(getContext());p.setStyle(Paint.Style.FILL);p.setColor(bg);c.drawCircle(cx,cy,r+dp(10),p);p.setStyle(Paint.Style.STROKE);p.setStrokeWidth(dp(2));p.setColor(UiTheme.accent(getContext()));c.drawCircle(cx,cy,r,p);
        for(int i=0;i<12;i++){double a=Math.PI*2*i/12-Math.PI/2;float x1=cx+(float)Math.cos(a)*(r-dp(5)),y1=cy+(float)Math.sin(a)*(r-dp(5));float x2=cx+(float)Math.cos(a)*(r-dp(15)),y2=cy+(float)Math.sin(a)*(r-dp(15));p.setStrokeWidth(i%3==0?dp(3):dp(1));c.drawLine(x1,y1,x2,y2,p);}
        int sec=now.get(Calendar.SECOND), min=now.get(Calendar.MINUTE), hour=now.get(Calendar.HOUR);if(hour==0)hour=12;drawHand(c,cx,cy,r*.52f,(hour%12+min/60f)/12f*360-90,dp(5),UiTheme.text(getContext()));drawHand(c,cx,cy,r*.72f,(min+sec/60f)/60f*360-90,dp(3),UiTheme.text(getContext()));drawHand(c,cx,cy,r*.78f,sec/60f*360-90,dp(1),UiTheme.accent(getContext()));p.setStyle(Paint.Style.FILL);p.setColor(UiTheme.accent(getContext()));c.drawCircle(cx,cy,dp(5),p);}
    void drawHand(Canvas c,float cx,float cy,float len,float deg,float width,int color){p.setStyle(Paint.Style.STROKE);p.setStrokeWidth(width);p.setColor(color);double a=Math.toRadians(deg);c.drawLine(cx,cy,cx+(float)Math.cos(a)*len,cy+(float)Math.sin(a)*len,p);}
    int dp(float v){return(int)(v*getResources().getDisplayMetrics().density+.5f);}
}

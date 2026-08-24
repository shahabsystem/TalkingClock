package com.yasin.farsitalkingclock;
import android.content.*; public class BootReceiver extends BroadcastReceiver{public void onReceive(Context c,Intent i){Scheduler.reschedule(c);}}

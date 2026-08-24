package com.yasin.farsitalkingclock;
import android.app.*;import android.content.*;import java.util.*;
public class Scheduler {
 static final int REQ_BASE=9000;
 public static void reschedule(Context c){AlarmManager am=(AlarmManager)c.getSystemService(Context.ALARM_SERVICE);ScheduleStore st=new ScheduleStore(c);Calendar base=Calendar.getInstance();
  for(Schedule s:st.all()){PendingIntent pi=PendingIntent.getBroadcast(c,REQ_BASE+s.id,new Intent(c,ClockReceiver.class).putExtra("id",s.id),PendingIntent.FLAG_UPDATE_CURRENT|PendingIntent.FLAG_IMMUTABLE);am.cancel(pi);if(!s.enabled||s.intervalMin<=0)continue;
   Calendar next=findNext(s,base,st);if(next==null)continue; long ms=next.getTimeInMillis(); if(android.os.Build.VERSION.SDK_INT>=31 && !am.canScheduleExactAlarms()){am.setAndAllowWhileIdle(AlarmManager.RTC_WAKEUP,ms,pi);}else if(android.os.Build.VERSION.SDK_INT>=23){am.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP,ms,pi);}else am.setExact(AlarmManager.RTC_WAKEUP,ms,pi);
  }}
 static Calendar findNext(Schedule s,Calendar base,ScheduleStore st){Calendar x=(Calendar)base.clone();x.set(Calendar.SECOND,0);x.set(Calendar.MILLISECOND,0);x.add(Calendar.MINUTE,1);for(int d=0;d<8;d++){int dow=x.get(Calendar.DAY_OF_WEEK);if(ClockReceiver.dayEnabled(s.daysMask,dow)){int m=x.get(Calendar.HOUR_OF_DAY)*60+x.get(Calendar.MINUTE);if(st.inGlobalWindow(x)&&!st.silentNow(x)&&m>=s.startMin&&m<=s.endMin){int delta=m-s.startMin;int rem=delta%s.intervalMin;if(rem==0)return x;int add=s.intervalMin-rem;if(m+add<=s.endMin){x.add(Calendar.MINUTE,add);return x;}}Calendar day=(Calendar)x.clone();day.set(Calendar.HOUR_OF_DAY,s.startMin/60);day.set(Calendar.MINUTE,s.startMin%60);day.set(Calendar.SECOND,0);day.set(Calendar.MILLISECOND,0);if(day.after(base)&&st.inGlobalWindow(day)&&!st.silentNow(day))return day;}x.add(Calendar.DAY_OF_YEAR,1);x.set(Calendar.HOUR_OF_DAY,0);x.set(Calendar.MINUTE,0);}return null;}
}

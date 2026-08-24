package com.yasin.farsitalkingclock;

import android.content.*;
import android.speech.tts.TextToSpeech;
import android.media.*;
import android.os.*;
import java.util.*;

public class ClockReceiver extends BroadcastReceiver {
    @Override public void onReceive(Context c, Intent i) {
        int id=i.getIntExtra("id",-1); ScheduleStore st=new ScheduleStore(c); Schedule target=null;
        for(Schedule s:st.all()) if(s.id==id){target=s;break;}
        Calendar now=Calendar.getInstance();
        if(target==null||!target.enabled||!st.inGlobalWindow(now)||st.silentNow(now)||target.intervalMin<1||!dayEnabled(target.daysMask,now.get(Calendar.DAY_OF_WEEK))){Scheduler.reschedule(c);return;}
        final Schedule s=target;
        final TextToSpeech[] t={null};
        final Runnable speakRunnable=new Runnable(){@Override public void run(){
            final TextToSpeech.OnInitListener listener=new TextToSpeech.OnInitListener(){@Override public void onInit(int status){
                if(status==TextToSpeech.SUCCESS){
                    t[0].setLanguage(Locale.US);
                    t[0].setSpeechRate(s.speechRate); t[0].setPitch(s.pitch);
                    String text=s.customText.trim().isEmpty()?spokenEnglish(now):expandEnglish(s.customText,now);
                    if(st.getBool("sayDate",false)) text += ", " + new java.text.SimpleDateFormat("MMMM d, yyyy",Locale.US).format(now.getTime());
                    if(st.getBool("sayDay",false)) text += ", " + new java.text.SimpleDateFormat("EEEE",Locale.US).format(now.getTime());
                    if(st.getBool("saySeconds",false)) text += ", second " + now.get(Calendar.SECOND);
                    int queue=st.getBool("ttsFlush",true)?TextToSpeech.QUEUE_FLUSH:TextToSpeech.QUEUE_ADD;
                    t[0].speak(text,queue,null,"clock");
                }
                Scheduler.reschedule(c);
            }};
            String engine=st.getTtsEnginePackage(c);
            if(engine==null)t[0]=new TextToSpeech(c.getApplicationContext(),listener);else t[0]=new TextToSpeech(c.getApplicationContext(),listener,engine);
            if(st.getBool("vibrate",false)){Vibrator v=(Vibrator)c.getSystemService(Context.VIBRATOR_SERVICE);if(v!=null&&Build.VERSION.SDK_INT>=26)v.vibrate(VibrationEffect.createOneShot(120,VibrationEffect.DEFAULT_AMPLITUDE));}
        }};
        playCue(c,st,new Runnable(){@Override public void run(){if(!st.cueOnly())speakRunnable.run();else Scheduler.reschedule(c);}});
    }

    static void playCue(Context c, ScheduleStore st, final Runnable done){
        int mode=st.cueMode(); if(mode==0){done.run();return;}
        if(mode==2&&!st.cueUri().trim().isEmpty()){
            try{
                MediaPlayer mp=new MediaPlayer(); mp.setDataSource(c,Uri.parse(st.cueUri()));
                mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener(){@Override public void onCompletion(MediaPlayer x){try{x.release();}catch(Exception ignored){}done.run();}});
                mp.setOnErrorListener(new MediaPlayer.OnErrorListener(){@Override public boolean onError(MediaPlayer x,int w,int e){try{x.release();}catch(Exception ignored){}done.run();return true;}});
                mp.prepare();float v=Math.max(.05f,Math.min(1f,st.cueVolume()/100f));mp.setVolume(v,v);mp.start();return;
            }catch(Exception ignored){}
        }
        playSine(c,st.cueFrequency(),st.cueDuration(),st.cueVolume(),done);
    }
    static void playSine(Context c,int frequency,int durationMs,int volumePercent,final Runnable done){
        final int sampleRate=44100; final int samples=Math.max(1,sampleRate*durationMs/1000); short[] data=new short[samples];double amp=Math.max(.02,Math.min(1.0,volumePercent/100.0));
        for(int i=0;i<samples;i++){double envelope=Math.min(1.0,Math.min(i/700.0,(samples-i)/900.0));data[i]=(short)(Math.sin(2*Math.PI*frequency*i/sampleRate)*32767*amp*envelope);}
        try{
            final AudioTrack track=new AudioTrack(AudioManager.STREAM_NOTIFICATION,sampleRate,AudioFormat.CHANNEL_OUT_MONO,AudioFormat.ENCODING_PCM_16BIT,data.length*2,AudioTrack.MODE_STATIC);
            track.write(data,0,data.length);track.play();
            new Handler(Looper.getMainLooper()).postDelayed(new Runnable(){@Override public void run(){try{track.stop();track.release();}catch(Exception ignored){}done.run();}},durationMs+40);
        }catch(Exception e){done.run();}
    }
    static String spokenEnglish(Calendar c){int h=c.get(Calendar.HOUR);if(h==0)h=12;int m=c.get(Calendar.MINUTE);if(m==0)return "It is "+h+" o'clock.";return "It is "+h+":"+String.format(Locale.US,"%02d",m)+".";}
    static String expandEnglish(String s,Calendar c){String r=s;int h=c.get(Calendar.HOUR);if(h==0)h=12;r=r.replace("{time}",spokenEnglish(c)).replace("{hour}",String.valueOf(h)).replace("{minute}",String.valueOf(c.get(Calendar.MINUTE))).replace("{second}",String.valueOf(c.get(Calendar.SECOND)));r=r.replace("{date}",new java.text.SimpleDateFormat("MMMM d, yyyy",Locale.US).format(c.getTime())).replace("{day}",new java.text.SimpleDateFormat("EEEE",Locale.US).format(c.getTime()));return r;}
    static boolean dayEnabled(int mask,int dow){int bit=(dow==Calendar.SUNDAY)?64:(1<<(dow-Calendar.MONDAY));return (mask&bit)!=0;}
}

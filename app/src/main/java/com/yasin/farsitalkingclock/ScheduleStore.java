package com.yasin.farsitalkingclock;

import android.content.*;
import android.speech.tts.TextToSpeech;
import org.json.*;
import java.util.*;

public class ScheduleStore {
    private final SharedPreferences p;
    public ScheduleStore(Context c){p=c.getSharedPreferences("clock",Context.MODE_PRIVATE);}
    public ArrayList<Schedule> all(){
        ArrayList<Schedule>a=new ArrayList<>();
        try{JSONArray j=new JSONArray(p.getString("schedules","[]"));for(int i=0;i<j.length();i++){JSONObject o=j.getJSONObject(i);
            a.add(new Schedule(o.getInt("id"),o.optString("name","اعلام ساعت"),o.optInt("start",480),o.optInt("end",1320),o.optInt("interval",60),o.optBoolean("enabled",true),o.optInt("days",127),o.optString("text",""),(float)o.optDouble("rate",1),(float)o.optDouble("pitch",1)));}}
        catch(Exception ignored){} return a;
    }
    public void save(ArrayList<Schedule>a){JSONArray j=new JSONArray();try{for(Schedule s:a){JSONObject o=new JSONObject();o.put("id",s.id);o.put("name",s.name);o.put("start",s.startMin);o.put("end",s.endMin);o.put("interval",s.intervalMin);o.put("enabled",s.enabled);o.put("days",s.daysMask);o.put("text",s.customText);o.put("rate",s.speechRate);o.put("pitch",s.pitch);j.put(o);}}catch(Exception ignored){}p.edit().putString("schedules",j.toString()).apply();}
    public int nextId(){int n=0;for(Schedule s:all())n=Math.max(n,s.id);return n+1;}
    public int globalStart(){return p.getInt("globalStart",420);} public int globalEnd(){return p.getInt("globalEnd",1380);}
    public int nightStart(){return p.getInt("nightStart",1380);} public int nightEnd(){return p.getInt("nightEnd",420);} public boolean nightEnabled(){return p.getBoolean("nightEnabled",false);}
    public boolean inWindow(Calendar c,int a,int b){int m=c.get(Calendar.HOUR_OF_DAY)*60+c.get(Calendar.MINUTE);return a<=b?m>=a&&m<=b:m>=a||m<=b;}
    public boolean inGlobalWindow(Calendar c){return inWindow(c,globalStart(),globalEnd());}
    public boolean silentNow(Calendar c){return nightEnabled()&&inWindow(c,nightStart(),nightEnd());}
    public void setGlobal(int a,int b){p.edit().putInt("globalStart",a).putInt("globalEnd",b).apply();}
    public void setNight(boolean e,int a,int b){p.edit().putBoolean("nightEnabled",e).putInt("nightStart",a).putInt("nightEnd",b).apply();}
    public int getInt(String k,int d){return p.getInt(k,d);} public void setInt(String k,int v){p.edit().putInt(k,v).apply();}
    public boolean getBool(String k,boolean d){return p.getBoolean(k,d);} public void setBool(String k,boolean v){p.edit().putBoolean(k,v).apply();}
    public void resetPrefs(){p.edit().clear().apply();}

    public int ttsLang(){return 0;}
    public int cueMode(){return p.getInt("cueMode",1);}
    public boolean cueOnly(){return p.getBoolean("cueOnly",false);}
    public String cueUri(){return p.getString("cueUri","");}
    public int cueDuration(){return p.getInt("cueDuration",260);}
    public int cueVolume(){return p.getInt("cueVolume",55);}
    public int cueFrequency(){return p.getInt("cueFrequency",620);}
    public void setCue(int mode,boolean only,String uri){p.edit().putInt("cueMode",mode).putBoolean("cueOnly",only).putString("cueUri",uri==null?"":uri).apply();}
    public void setCueTune(int duration,int volume,int frequency){p.edit().putInt("cueDuration",duration).putInt("cueVolume",volume).putInt("cueFrequency",frequency).apply();}
    public float defaultRate(){return p.getFloat("rate",1f);} public float defaultPitch(){return p.getFloat("pitch",1f);}
    public void setDefaults(float r,float pitch){p.edit().putFloat("rate",r).putFloat("pitch",pitch).apply();}
    public String getTtsEnginePackage(Context c){int idx=p.getInt("ttsEngine",0);if(idx<=0)return null;try{TextToSpeech probe=new TextToSpeech(c,null);List<TextToSpeech.EngineInfo> es=probe.getEngines();probe.shutdown();int n=idx-1;return n>=0&&n<es.size()?es.get(n).name:null;}catch(Exception e){return null;}}
}

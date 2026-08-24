package com.yasin.farsitalkingclock;

import android.app.*;import android.os.*;import android.content.*;import android.graphics.*;import android.graphics.drawable.*;import android.view.*;import android.widget.*;import android.speech.tts.TextToSpeech;import java.util.*;

public class SettingsActivity extends Activity{
    ScheduleStore st; LinearLayout body; String selectedCueUri="";
    int dp(float v){return(int)(v*getResources().getDisplayMetrics().density+.5f);} float fs(float z){return z*UiTheme.fontScale(this);} 
    GradientDrawable bg(int c,float r){GradientDrawable g=new GradientDrawable();g.setColor(c);g.setCornerRadius(dp(r));return g;}
    TextView t(String s,float z){TextView v=new TextView(this);v.setText(s);v.setTextColor(UiTheme.text(this));v.setTextSize(fs(z));v.setGravity(Gravity.RIGHT|Gravity.CENTER_VERTICAL);v.setPadding(dp(10),dp(3),dp(10),dp(3));return v;}
    LinearLayout.LayoutParams lp(int w,int h){return new LinearLayout.LayoutParams(w,h);}
    @Override public void onCreate(Bundle b){super.onCreate(b);st=new ScheduleStore(this);build();}
    void compact(Button b){b.setMinHeight(0);b.setMinimumHeight(0);b.setMinWidth(0);b.setMinimumWidth(0);b.setIncludeFontPadding(false);b.setPadding(dp(10),dp(4),dp(10),dp(4));}
    void compactSwitch(Switch x){x.setMinHeight(0);x.setMinimumHeight(0);x.setMinWidth(0);x.setMinimumWidth(0);x.setIncludeFontPadding(false);x.setTextSize(fs(12));}
    Button button(String text,float size,int color){Button b=new Button(this);b.setText(text);b.setTextColor(UiTheme.text(this));b.setTextSize(fs(size));b.setAllCaps(false);b.setBackground(bg(color,18));compact(b);return b;}

    void build(){
        getWindow().setStatusBarColor(UiTheme.bg(this));getWindow().setNavigationBarColor(UiTheme.bg(this));
        LinearLayout root=new LinearLayout(this);root.setOrientation(LinearLayout.VERTICAL);root.setBackgroundColor(UiTheme.bg(this));root.setPadding(dp(10),dp(7),dp(10),0);
        LinearLayout bar=new LinearLayout(this);bar.setGravity(Gravity.CENTER_VERTICAL);Button back=button("‹",28,UiTheme.card(this));back.setOnClickListener(v->finish());bar.addView(back,lp(dp(50),dp(46)));TextView title=t("تنظیمات و شخصی‌سازی",20);title.setTypeface(null,Typeface.BOLD);bar.addView(title,lp(0,dp(46)));((LinearLayout.LayoutParams)title.getLayoutParams()).weight=1;root.addView(bar);
        ScrollView sc=new ScrollView(this);sc.setFillViewport(true);body=new LinearLayout(this);body.setOrientation(LinearLayout.VERTICAL);body.setPadding(0,dp(4),0,dp(24));sc.addView(body);root.addView(sc,lp(-1,0));((LinearLayout.LayoutParams)sc.getLayoutParams()).weight=1;

        section("⏰ زمان‌بندی کلی");
        body.addView(t("بازه‌ای که برنامه‌های ساعت مجاز به فعالیت هستند",12),lp(-1,dp(30)));
        body.addView(timeField("شروع فعالیت برنامه",st.globalStart(),v->{st.setGlobal(v,st.globalEnd());}),lp(-1,dp(56)));
        body.addView(timeField("پایان فعالیت برنامه",st.globalEnd(),v->{st.setGlobal(st.globalStart(),v);}),lp(-1,dp(56)));
        Switch night=sw("فعال‌سازی سکوت شبانه",st.nightEnabled(),"nightEnabled");body.addView(night,lp(-1,dp(40)));
        body.addView(timeField("شروع سکوت",st.nightStart(),v->{st.setNight(night.isChecked(),v,st.nightEnd());}),lp(-1,dp(56)));
        body.addView(timeField("پایان سکوت",st.nightEnd(),v->{st.setNight(night.isChecked(),st.nightStart(),v);}),lp(-1,dp(56)));
        body.addView(choice("رفتار در سکوت",new String[]{"قطع کامل اعلام","فقط اعلان‌های مهم","پخش با صدای کم"},st.getInt("quietMode",0),v->st.setInt("quietMode",v)),lp(-1,dp(52)));

        section("🗣 موتور سخنگو");
        body.addView(info("زبان اعلام ساعت در این نسخه فقط English است. اگر موتور دیگری روی گوشی نصب باشد می‌توانید آن را انتخاب کنید."),lp(-1,dp(48)));
        body.addView(choice("موتور TTS",ttsEngines(),st.getInt("ttsEngine",0),v->st.setInt("ttsEngine",v)),lp(-1,dp(58)));
        SeekBar rate=seek(50,200,(int)(st.defaultRate()*100));body.addView(labelSeek("سرعت گفتار",rate,st.defaultRate(),true),lp(-1,dp(70)));
        SeekBar pitch=seek(50,200,(int)(st.defaultPitch()*100));body.addView(labelSeek("تُن صدا",pitch,st.defaultPitch(),false),lp(-1,dp(70)));
        body.addView(choice("حالت اعلام",new String[]{"طبیعی و کامل","کوتاه","رسمی","محاوره‌ای"},st.getInt("speechStyle",0),v->st.setInt("speechStyle",v)),lp(-1,dp(52)));
        body.addView(sw("توقف گفتار قبلی هنگام اعلام جدید",st.getBool("ttsFlush",true),"ttsFlush"),lp(-1,dp(40)));
        Button test=button("▶  تست صدای English",14,UiTheme.accent(this));test.setOnClickListener(v->testVoice(rate.getProgress()+(Integer)rate.getTag(),pitch.getProgress()+(Integer)pitch.getTag()));body.addView(test,lp(-1,dp(50)));
        Button sys=button("⚙  تنظیمات موتورهای گفتار گوشی",13,UiTheme.card(this));sys.setOnClickListener(v->{try{startActivity(new Intent("com.android.settings.TTS_SETTINGS"));}catch(Exception e){Toast.makeText(this,"تنظیمات موتور گفتار در این گوشی در دسترس نیست.",Toast.LENGTH_SHORT).show();}});body.addView(sys,lp(-1,dp(50)));

        section("🔔 صدای قبل از اعلام ساعت");
        selectedCueUri=st.cueUri();
        body.addView(choice("حالت صدا",new String[]{"خاموش","بوق ملایم قابل تنظیم","صدای سفارشی"},st.cueMode(),v->{st.setCue(v,st.cueOnly(),selectedCueUri);}),lp(-1,dp(54)));
        Switch cueOnly=sw("فقط بوق/صدای انتخابی پخش شود؛ ساعت گفته نشود",st.cueOnly(),"cueOnly");body.addView(cueOnly,lp(-1,dp(44)));
        SeekBar cueVol=seek(5,100,st.cueVolume());body.addView(labelSeekInt("بلندی بوق",cueVol,"٪",v->st.setCueTune(v,st.cueDuration(),st.cueFrequency())),lp(-1,dp(68)));
        SeekBar cueDur=seek(80,900,st.cueDuration());body.addView(labelSeekInt("طول بوق",cueDur," میلی‌ثانیه",v->st.setCueTune(v,st.cueVolume(),st.cueFrequency())),lp(-1,dp(68)));
        SeekBar cueFreq=seek(350,1200,st.cueFrequency());body.addView(labelSeekInt("تن بوق",cueFreq," هرتز",v->st.setCueTune(st.cueDuration(),st.cueVolume(),v)),lp(-1,dp(68)));
        Button pickCue=button("🎵 انتخاب صدای سفارشی از گوشی",13,UiTheme.card(this));pickCue.setOnClickListener(v->{Intent x=new Intent(Intent.ACTION_OPEN_DOCUMENT);x.addCategory(Intent.CATEGORY_OPENABLE);x.setType("audio/*");x.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION|Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION);try{startActivityForResult(x,4201);}catch(Exception e){Toast.makeText(this,"انتخاب فایل صوتی در این گوشی در دسترس نیست.",Toast.LENGTH_SHORT).show();}});body.addView(pickCue,lp(-1,dp(50)));
        Button testCue=button("▶  تست صدای پیش از اعلام",13,UiTheme.accent(this));testCue.setOnClickListener(v->ClockReceiver.playCue(this,st,()->{}));body.addView(testCue,lp(-1,dp(50)));
        body.addView(info("بوق داخلی با موج سینوسی نرم ساخته می‌شود و بلندی، طول و تُن آن قابل تنظیم است. صدای سفارشی نیز با همان کنترل بلندی پخش می‌شود."),lp(-1,dp(60)));

        section("📢 محتوای اعلام");
        body.addView(sw("اعلام ساعت",st.getBool("sayTime",true),"sayTime"),lp(-1,dp(40)));
        body.addView(sw("اعلام ثانیه",st.getBool("saySeconds",false),"saySeconds"),lp(-1,dp(40)));
        body.addView(sw("اعلام تاریخ",st.getBool("sayDate",false),"sayDate"),lp(-1,dp(40)));
        body.addView(sw("اعلام روز هفته",st.getBool("sayDay",false),"sayDay"),lp(-1,dp(40)));
        body.addView(sw("اعلام هنگام باز شدن برنامه",st.getBool("sayOnOpen",false),"sayOnOpen"),lp(-1,dp(40)));
        body.addView(sw("اعلام بعد از راه‌اندازی گوشی",st.getBool("sayOnBoot",false),"sayOnBoot"),lp(-1,dp(40)));

        section("🎨 ظاهر برنامه");
        body.addView(choice("پوسته برنامه",new String[]{"شبانه مدرن","تیره کلاسیک","سرمه‌ای","روشن مینیمال","شب نئونی"},st.getInt("theme",0),v->{st.setInt("theme",v);recreate();}),lp(-1,dp(56)));
        body.addView(choice("رنگ اصلی",new String[]{"آبی","بنفش","فیروزه‌ای","طلایی","سبز","قرمز"},st.getInt("accent",0),v->{st.setInt("accent",v);recreate();}),lp(-1,dp(56)));
        body.addView(choice("رنگ متن اصلی",new String[]{"سفید","خاکستری روشن","فیروزه‌ای روشن","زرد ملایم","سبز روشن","نارنجی"},st.getInt("fontColor",0),v->{st.setInt("fontColor",v);recreate();}),lp(-1,dp(56)));
        SeekBar font=seek(80,140,st.getInt("fontScale",100));body.addView(labelSeekInt("اندازه فونت رابط",font,"٪",v->st.setInt("fontScale",v)),lp(-1,dp(68)));
        body.addView(choice("نوع نمایش ساعت",new String[]{"دیجیتال بزرگ","دیجیتال 7-Segment","دیجیتال مینیمال","آنالوگ کلاسیک","دیجیتال کارت‌دار"},st.getInt("clockStyle",0),v->{st.setInt("clockStyle",v);recreate();}),lp(-1,dp(58)));
        body.addView(sw("نمایش ثانیه در صفحه اصلی",st.getBool("showSeconds",true),"showSeconds"),lp(-1,dp(40)));
        body.addView(sw("نمایش وضعیت سکوت شبانه",st.getBool("showQuiet",true),"showQuiet"),lp(-1,dp(40)));
        body.addView(sw("انیمیشن ملایم رابط",st.getBool("animations",true),"animations"),lp(-1,dp(40)));

        section("🔔 اعلان و رفتار");
        body.addView(sw("نمایش اعلان دائمی برای اطمینان از اجرای زمان‌بندی",st.getBool("persistentNotification",false),"persistentNotification"),lp(-1,dp(42)));
        body.addView(sw("لرزش کوتاه هنگام اعلام",st.getBool("vibrate",false),"vibrate"),lp(-1,dp(40)));
        body.addView(sw("کاهش صدای رسانه هنگام اعلام",st.getBool("duckAudio",true),"duckAudio"),lp(-1,dp(40)));
        body.addView(sw("شروع خودکار بعد از روشن شدن گوشی",st.getBool("autoStart",true),"autoStart"),lp(-1,dp(40)));
        body.addView(choice("فرمت ساعت",new String[]{"۲۴ ساعته","۱۲ ساعته"},st.getInt("hourFormat",0),v->st.setInt("hourFormat",v)),lp(-1,dp(56)));

        section("💾 اطلاعات و توسعه‌دهنده");
        TextView about=t("ساعت‌گوی\n\nتوسعه‌دهنده: حامد محمدی\nایمیل: hamedmohammadinikche@gmail.com\n\nاگر برنامه براتون مفید بود، یه قهوه مهمونم کنید ☕",13);about.setTextColor(Color.rgb(148,163,184));about.setGravity(Gravity.CENTER);about.setAutoLinkMask(android.text.util.Linkify.EMAIL_ADDRESSES);about.setLinksClickable(true);about.setMovementMethod(android.text.method.LinkMovementMethod.getInstance());about.setOnClickListener(v->{try{startActivity(new Intent(Intent.ACTION_VIEW,android.net.Uri.parse("https://coffeebede.com/shahabsystem")));}catch(Exception ignored){}});body.addView(about,lp(-1,dp(140)));
        Button reset=button("بازگردانی تنظیمات پیش‌فرض",13,Color.rgb(55,45,22));reset.setTextColor(Color.rgb(251,191,36));reset.setOnClickListener(v->{st.resetPrefs();recreate();});body.addView(reset,lp(-1,dp(52)));
        Button save=button("ذخیره و بازگشت",15,UiTheme.accent(this));save.setOnClickListener(v->{st.setDefaults((rate.getProgress()+(Integer)rate.getTag())/100f,(pitch.getProgress()+(Integer)pitch.getTag())/100f);Scheduler.reschedule(this);finish();});body.addView(save,lp(-1,dp(56)));
        setContentView(root);
    }

    void section(String s){TextView v=t(s,15);v.setTypeface(null,Typeface.BOLD);v.setTextColor(UiTheme.accent(this));v.setPadding(dp(8),dp(14),dp(8),dp(5));body.addView(v,lp(-1,dp(40)));}
    TextView info(String s){TextView v=t(s,11);v.setTextColor(Color.rgb(148,163,184));v.setGravity(Gravity.RIGHT|Gravity.CENTER_VERTICAL);return v;}
    Switch sw(String s,boolean c,String key){Switch x=new Switch(this);x.setText(s);x.setTextColor(UiTheme.text(this));x.setTextSize(fs(12));x.setChecked(c);x.setPadding(dp(5),0,dp(3),0);compactSwitch(x);x.setOnCheckedChangeListener((b,v)->{st.setBool(key,v);if(key.equals("nightEnabled"))st.setNight(v,st.nightStart(),st.nightEnd());});return x;}
    View choice(String title,String[] opts,int current,final ChoiceSaver save){Button b=button(title+"\n"+opts[Math.max(0,Math.min(current,opts.length-1))],12,UiTheme.card(this));b.setGravity(Gravity.RIGHT|Gravity.CENTER_VERTICAL);b.setOnClickListener(v->{AlertDialog d=new AlertDialog.Builder(this).setTitle(title).setSingleChoiceItems(opts,current,(dialog,which)->{save.save(which);b.setText(title+"\n"+opts[which]);dialog.dismiss();}).setNegativeButton("لغو",null).create();d.show();});return b;}
    interface ChoiceSaver{void save(int value);} interface IntSaver{void save(int value);}
    Button timeField(String label,int value,final IntSaver saver){Button b=button(label+"\n"+fmtTime(value),13,UiTheme.card(this));b.setGravity(Gravity.RIGHT|Gravity.CENTER_VERTICAL);b.setTag(value);b.setOnClickListener(v->{int cur=(Integer)b.getTag();TimePickerDialog d=new TimePickerDialog(this,(view,h,m)->{int n=h*60+m;b.setTag(n);b.setText(label+"\n"+fmtTime(n));saver.save(n);},cur/60,cur%60,true);d.setTitle(label);d.show();});return b;}
    String fmtTime(int m){return String.format(Locale.US,"%02d:%02d",m/60,m%60);}
    SeekBar seek(int min,int max,int value){SeekBar s=new SeekBar(this);s.setMax(max-min);s.setProgress(Math.max(0,Math.min(max-min,value-min)));s.setTag(min);return s;}
    View labelSeek(String label,SeekBar s,float val,boolean rateMode){LinearLayout box=new LinearLayout(this);box.setOrientation(LinearLayout.VERTICAL);TextView l=t(label+": "+String.format(Locale.US,"%.2fx",val),12);box.addView(l,lp(-1,dp(28)));box.addView(s,lp(-1,dp(40)));s.setOnSeekBarChangeListener(new SimpleSeek(){public void onProgressChanged(SeekBar b,int p,boolean f){float actual=(p+(Integer)b.getTag())/100f;l.setText(label+": "+String.format(Locale.US,"%.2fx",actual));}});return box;}
    View labelSeekInt(String label,SeekBar s,String unit,final IntSaver saver){LinearLayout box=new LinearLayout(this);box.setOrientation(LinearLayout.VERTICAL);int min=(Integer)s.getTag();TextView l=t(label+": "+(s.getProgress()+min)+unit,12);box.addView(l,lp(-1,dp(28)));box.addView(s,lp(-1,dp(40)));s.setOnSeekBarChangeListener(new SimpleSeek(){public void onProgressChanged(SeekBar b,int p,boolean f){int v=p+(Integer)b.getTag();l.setText(label+": "+v+unit);if(saver!=null)saver.save(v);}});return box;}
    abstract class SimpleSeek implements SeekBar.OnSeekBarChangeListener{public void onStartTrackingTouch(SeekBar b){}public void onStopTrackingTouch(SeekBar b){}}
    String[] ttsEngines(){ArrayList<String> names=new ArrayList<>();names.add("پیش‌فرض سیستم");try{TextToSpeech probe=new TextToSpeech(this,null);for(TextToSpeech.EngineInfo e:probe.getEngines())if(e!=null&&e.label!=null&&!e.label.trim().isEmpty())names.add(e.label);probe.shutdown();}catch(Exception ignored){}return names.toArray(new String[0]);}
    void testVoice(int r,int p){final float speechRate=Math.max(.5f,Math.min(2f,r/100f));final float speechPitch=Math.max(.5f,Math.min(2f,p/100f));final TextToSpeech[] t={null};final TextToSpeech.OnInitListener l=new TextToSpeech.OnInitListener(){@Override public void onInit(int status){if(status==TextToSpeech.SUCCESS){t[0].setLanguage(Locale.US);t[0].setSpeechRate(speechRate);t[0].setPitch(speechPitch);t[0].speak("This is a test of the selected English speech engine.",TextToSpeech.QUEUE_FLUSH,null,"settings");}}};String e=st.getTtsEnginePackage(this);if(e==null)t[0]=new TextToSpeech(this,l);else t[0]=new TextToSpeech(this,l,e);}
    @Override protected void onActivityResult(int requestCode,int resultCode,Intent data){super.onActivityResult(requestCode,resultCode,data);if(requestCode==4201&&resultCode==RESULT_OK&&data!=null&&data.getData()!=null){selectedCueUri=data.getData().toString();try{getContentResolver().takePersistableUriPermission(data.getData(),Intent.FLAG_GRANT_READ_URI_PERMISSION);}catch(Exception ignored){}st.setCue(2,st.cueOnly(),selectedCueUri);Toast.makeText(this,"صدای سفارشی انتخاب شد.",Toast.LENGTH_SHORT).show();}}
}

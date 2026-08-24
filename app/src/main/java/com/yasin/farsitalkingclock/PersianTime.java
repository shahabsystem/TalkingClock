package com.yasin.farsitalkingclock;
import java.util.*;

public class PersianTime {
 static final String[] H={"دوازده","یک","دو","سه","چهار","پنج","شش","هفت","هشت","نه","ده","یازده"};
 static final String[] M={"صفر","یک","دو","سه","چهار","پنج","شش","هفت","هشت","نه","ده","یازده","دوازده","سیزده","چهارده","پانزده","شانزده","هفده","هجده","نوزده"};

 public static String spoken(Calendar c){int h=c.get(Calendar.HOUR);if(h==0)h=12;int m=c.get(Calendar.MINUTE);String s="ساعت "+H[h];if(m>0)s+=" و "+minute(m);return s+" است.";}
 static String minute(int m){if(m<20)return M[m]+" دقیقه";if(m==20)return "بیست دقیقه";if(m==30)return "سی دقیقه";if(m==40)return "چهل دقیقه";if(m==50)return "پنجاه دقیقه";return faDigits(String.valueOf(m))+" دقیقه";}

 public static String spokenEnglish(Calendar c){
   int h=c.get(Calendar.HOUR); if(h==0)h=12; int m=c.get(Calendar.MINUTE);
   String s="It is "+englishNumber(h);
   if(m>0) s += " "+englishMinute(m);
   return s+".";
 }
 static String englishMinute(int m){
   if(m==1)return "and one minute"; if(m==2)return "and two minutes"; if(m==3)return "and three minutes";
   if(m==4)return "and four minutes"; if(m==5)return "and five minutes"; if(m==6)return "and six minutes";
   if(m==7)return "and seven minutes"; if(m==8)return "and eight minutes"; if(m==9)return "and nine minutes";
   if(m==10)return "and ten minutes"; if(m==11)return "and eleven minutes"; if(m==12)return "and twelve minutes";
   if(m==13)return "and thirteen minutes"; if(m==14)return "and fourteen minutes"; if(m==15)return "and fifteen minutes";
   if(m==16)return "and sixteen minutes"; if(m==17)return "and seventeen minutes"; if(m==18)return "and eighteen minutes";
   if(m==19)return "and nineteen minutes"; if(m==20)return "and twenty minutes"; if(m==30)return "and thirty minutes";
   if(m==40)return "and forty minutes"; if(m==50)return "and fifty minutes";
   return "and "+m+" minutes";
 }
 static String englishNumber(int n){
   String[] a={"zero","one","two","three","four","five","six","seven","eight","nine","ten","eleven","twelve"};
   return n>=0&&n<a.length?a[n]:String.valueOf(n);
 }
 public static String expand(String text,Calendar c){
   int h=c.get(Calendar.HOUR);if(h==0)h=12;
   return text.replace("{time}",spoken(c)).replace("{hour}",H[h]).replace("{minute}",digits(String.format(Locale.US,"%02d",c.get(Calendar.MINUTE))))
     .replace("{second}",digits(String.format(Locale.US,"%02d",c.get(Calendar.SECOND))))
     .replace("{date}",new java.text.SimpleDateFormat("d MMMM yyyy",new Locale("fa","IR")).format(c.getTime()))
     .replace("{day}",new java.text.SimpleDateFormat("EEEE",new Locale("fa","IR")).format(c.getTime()));
 }
 public static String expandEnglish(String text,Calendar c){
   int h=c.get(Calendar.HOUR);if(h==0)h=12;
   return text.replace("{time}",spokenEnglish(c)).replace("{hour}",englishNumber(h))
     .replace("{minute}",String.format(Locale.US,"%02d",c.get(Calendar.MINUTE)))
     .replace("{second}",String.format(Locale.US,"%02d",c.get(Calendar.SECOND)))
     .replace("{date}",new java.text.SimpleDateFormat("MMMM d, yyyy",Locale.US).format(c.getTime()))
     .replace("{day}",new java.text.SimpleDateFormat("EEEE",Locale.US).format(c.getTime()));
 }
 public static String digits(String s){return s.replace('0','۰').replace('1','۱').replace('2','۲').replace('3','۳').replace('4','۴').replace('5','۵').replace('6','۶').replace('7','۷').replace('8','۸').replace('9','۹');}
 static String faDigits(String s){return digits(s);}
}

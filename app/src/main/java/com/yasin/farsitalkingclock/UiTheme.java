package com.yasin.farsitalkingclock;

import android.content.Context;
import android.graphics.Color;

public final class UiTheme {
    private UiTheme() {}
    public static int bg(Context c) {
        int t = new ScheduleStore(c).getInt("theme", 0);
        switch (t) {
            case 1: return Color.rgb(18,18,20);
            case 2: return Color.rgb(8,22,38);
            case 3: return Color.rgb(245,247,250);
            case 4: return Color.rgb(13,13,18);
            default: return Color.rgb(7,12,25);
        }
    }
    public static int surface(Context c) {
        int t = new ScheduleStore(c).getInt("theme", 0);
        switch (t) {
            case 1: return Color.rgb(35,35,39);
            case 2: return Color.rgb(14,40,62);
            case 3: return Color.rgb(255,255,255);
            case 4: return Color.rgb(27,27,36);
            default: return Color.rgb(18,30,55);
        }
    }
    public static int card(Context c) {
        int t = new ScheduleStore(c).getInt("theme", 0);
        switch (t) {
            case 1: return Color.rgb(30,30,34);
            case 2: return Color.rgb(13,35,55);
            case 3: return Color.rgb(235,239,245);
            case 4: return Color.rgb(24,24,32);
            default: return Color.rgb(17,27,49);
        }
    }
    public static int accent(Context c) {
        int a = new ScheduleStore(c).getInt("accent", 0);
        switch (a) {
            case 1: return Color.rgb(139,92,246);
            case 2: return Color.rgb(20,184,166);
            case 3: return Color.rgb(245,158,11);
            case 4: return Color.rgb(34,197,94);
            case 5: return Color.rgb(239,68,68);
            default: return Color.rgb(37,99,235);
        }
    }
    public static int text(Context c) {
        int a = new ScheduleStore(c).getInt("fontColor", 0);
        switch (a) {
            case 1: return Color.rgb(226,232,240);
            case 2: return Color.rgb(103,232,249);
            case 3: return Color.rgb(253,224,71);
            case 4: return Color.rgb(134,239,172);
            case 5: return Color.rgb(251,146,60);
            default: return Color.WHITE;
        }
    }
    public static boolean getLightTheme(Context c) { return new ScheduleStore(c).getInt("theme",0)==3; }
    public static float fontScale(Context c) {
        int p = new ScheduleStore(c).getInt("fontScale", 100);
        return Math.max(.80f, Math.min(1.40f, p / 100f));
    }
}

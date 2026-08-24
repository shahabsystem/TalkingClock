package com.yasin.farsitalkingclock;

public class Schedule {
    public int id, startMin, endMin, intervalMin, daysMask;
    public String name, customText;
    public boolean enabled;
    public float speechRate = 1.0f, pitch = 1.0f;
    public Schedule(int id, String name, int startMin, int endMin, int intervalMin, boolean enabled, int daysMask, String customText, float rate, float pitch) {
        this.id=id; this.name=name; this.startMin=startMin; this.endMin=endMin; this.intervalMin=intervalMin;
        this.enabled=enabled; this.daysMask=daysMask; this.customText=customText==null?"":customText; this.speechRate=rate; this.pitch=pitch;
    }
}

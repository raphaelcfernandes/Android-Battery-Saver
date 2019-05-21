package com.example.raphael.tcc.DataBase;

import java.util.ArrayList;


public class AppData {

    public String name;
    public int brightness;
    public ArrayList<Integer> coresSpeeds = new ArrayList<>();
    public ArrayList<Integer> coresThresholds = new ArrayList<>();

    public boolean isEmpty() {
        return (name == null || name.isEmpty()) && coresSpeeds.isEmpty() && coresThresholds.isEmpty();
    }

    @Override
    public String toString() {
        return "{name: " + name + " - brightness: " + brightness + " - coresSpeeds: " + coresSpeeds + " - coresThresholds: " + coresThresholds +" }";
    }
}

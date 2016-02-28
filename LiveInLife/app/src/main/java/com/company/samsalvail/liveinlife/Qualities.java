package com.company.samsalvail.liveinlife;

/**
 * Created by Monet on 2/28/2016.
 */
public class Qualities {
    private String color;
    private String name;
    private double score;

    public Qualities (String color, String name, double score) {
        this.color = color;
        this.name = name;
        this.score = score;
    }

    public void set_color(String c) {
        color = c;
    }

    public void set_name(String n) {
        name = n;
    }

    public void set_score(double s) {
        score = s;
    }

    public String get_color() {
        return color;
    }

    public String get_name() {
        return name;
    }

    public double get_score() {
        return score;
    }
}

package com.example.GymApp.Data;

import java.util.ArrayList;
import java.util.List;

public class StayHealthyTip {
    private String tip;

    public StayHealthyTip(String tip) {
        this.tip = tip;
    }

    public String getTip() {
        return tip;
    }

    public static List<StayHealthyTip> tips = new ArrayList<>();

    static {
        tips.add(new StayHealthyTip("Stay hydrated by drinking water throughout the day."));
        tips.add(new StayHealthyTip("Eat a balanced diet rich in fruits, vegetables, and whole grains."));
        tips.add(new StayHealthyTip("Exercise regularly to stay fit and maintain a healthy weight."));
        tips.add(new StayHealthyTip("Get enough sleep each night to recharge your body and mind."));
        tips.add(new StayHealthyTip("Limit processed foods and added sugars in your diet to improve overall health."));
        tips.add(new StayHealthyTip("Practice good hygiene by washing your hands regularly and covering your mouth when you cough or sneeze."));
        tips.add(new StayHealthyTip("Avoid smoking and limit alcohol consumption for better health."));
        tips.add(new StayHealthyTip("Find enjoyable physical activities to stay active, such as hiking, swimming, or dancing."));
        tips.add(new StayHealthyTip("Take breaks from screen time to protect your eyes and mental well-being."));
        tips.add(new StayHealthyTip("Practice mindful eating by paying attention to your food and enjoying each bite."));
        tips.add(new StayHealthyTip("Stay informed about health news and consult professionals for any health concerns."));
        tips.add(new StayHealthyTip("Listen to your body and give it the rest and care it needs."));
    }

}
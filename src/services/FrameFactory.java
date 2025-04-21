package src.services;

import src.ui.*;

public class FrameFactory {

    public static void create(String frameName) {
        switch (frameName) {
            case "Intro":
                new IntroAnimation();
                break;
            case "Income":
                new IncomeFrame();
                break;
            case "Deduction":
                new DeductionFrame();
                break;
            default:
                throw new IllegalArgumentException("Unknown frame: " + frameName);
        }
    }
}

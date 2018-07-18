package com.artek;

import com.sun.javafx.application.LauncherImpl;

public class LauncherImp {
    public static void main(String[] args) {
        LauncherImpl.launchApplication(MainApp.class, AppPreloader.class, args);
    }
}

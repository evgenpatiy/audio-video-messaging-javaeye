package ua.itea.javaeye.app;

import ua.itea.javaeye.ui.Preferences;

public class Main {
    public static void main(String[] args) {
        (new Thread(new Preferences())).start();
    }
}

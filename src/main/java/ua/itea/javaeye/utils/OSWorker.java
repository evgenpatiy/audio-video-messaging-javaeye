package ua.itea.javaeye.utils;

import lombok.Getter;

public class OSWorker {
    public enum OS {
        MAC, WINDOWS, UNIX, SOLARIS
    }

    @Getter
    private OS os;

    public OSWorker() {
        String osName = System.getProperty("os.name").toLowerCase();
        if (osName.contains("win")) {
            os = OS.WINDOWS;
        } else if (osName.contains("mac")) {
            os = OS.MAC;
        } else if (osName.contains("nix") || osName.contains("nux") || osName.contains("bsd") || osName.contains("irix")
                || osName.contains("aix") || osName.contains("hp-ux")) {
            os = OS.UNIX;
        } else if (osName.contains("sunos") || osName.contains("solaris")) {
            os = OS.SOLARIS;
        } else {
            os = null;
            System.out.printf("%s%n", "Your operating system not supported, exiting...");
            System.exit(1);
        }
    }
}

package ru.peacockTeam.utils;

import java.io.InputStream;

public class FiosUtil {
    public final String resourceFileName = "lng.csv";

    public InputStream getResourceFileStream(){
        return this.getClass().getClassLoader().getResourceAsStream(resourceFileName);
    }
}

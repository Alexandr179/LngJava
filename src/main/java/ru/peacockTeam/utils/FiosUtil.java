package ru.peacockTeam.utils;

import org.springframework.stereotype.Component;
import java.io.InputStream;

import static ru.peacockTeam.Application.SOURCE_LNG_FILE;

@Component
public class FiosUtil {

    public InputStream getResourceFileStream(){
        return this.getClass().getClassLoader().getResourceAsStream(SOURCE_LNG_FILE);
    }
}

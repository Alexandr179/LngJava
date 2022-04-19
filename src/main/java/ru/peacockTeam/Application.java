package ru.peacockTeam;

import ru.peacockTeam.utils.Processing;
import java.io.IOException;

public class Application {

    public static void main(String[] args) {
        try {
            new Processing().go();
        } catch (IOException ignored) {
        }
    }
}

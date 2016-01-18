package ru.various.sdp2partpost;

import ru.various.sdp2partpost.enums.Result;

import java.util.HashSet;
import java.util.Set;

public class ColoredMessageFactory {
    private static Set<Result> redColoredResults = new HashSet<>();
    private static Set<Result> greenColoredResults = new HashSet<>();

    static {
        redColoredResults.add(Result.BUSY);
        redColoredResults.add(Result.EMPTY);
        redColoredResults.add(Result.ERROR);
        redColoredResults.add(Result.ERROR_CREDENTIALS);
        redColoredResults.add(Result.WRONG_SEQUENCE);

        greenColoredResults.add(Result.IMPORTED);
        greenColoredResults.add(Result.LOADED);
    }

    public static ColoredMessage getColoredMessage(Result result, String message) {
        if (redColoredResults.contains(result))
            return new RedColoredMessage(message);
        else if (greenColoredResults.contains(result))
            return new GreenColoredMessage(message);

        return null;
    }
}

package ru.various.sdp2partpost.refiners;

import ru.various.sdp2partpost.exceptions.IncompleteDataException;

import java.util.regex.Pattern;

/**
 * Abstract Refiner class to get some data from raw string
 * through regexp or another way
 */
public abstract class Refiner {
    private Pattern pattern;

    protected Refiner() {}

    protected Refiner(String regex) {
        this.pattern = Pattern.compile(regex);
    }

    protected Refiner(String regex, int flags) {
        this.pattern = Pattern.compile(regex, flags);
    }

    public Pattern getPattern() {
        return pattern;
    }

    public abstract Object refine (String raw) throws IncompleteDataException;
}

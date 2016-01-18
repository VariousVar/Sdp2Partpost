package ru.various.sdp2partpost.refiners;

import ru.various.sdp2partpost.enums.Gender;
import ru.various.sdp2partpost.exceptions.IncompleteDataException;

import java.util.regex.Matcher;

public class GenderRefiner extends Refiner {
    private static String regex = "((?<male>\\+м\\+)|(?<female>\\+ж\\+))";

    public GenderRefiner() {
        super(regex);
    }

    public GenderRefiner(int flags) {
        super(regex, flags);
    }

    @Override
    public Gender refine(String raw) throws IncompleteDataException {
        // raw must consist gender
        // throw Exception if not
        if (raw == null)
            throw new IncompleteDataException("Пол не указан");

        Matcher matcher = getPattern().matcher(raw.toLowerCase());
        if (matcher.find())
            if (matcher.group("male") != null) {
                return Gender.MALE;
            }
            else if (matcher.group("female") != null)
                return Gender.FEMALE;
            else
                throw new IncompleteDataException("Пол неопределен");
        else
            throw new IncompleteDataException("Пол неопределен");

    }
}

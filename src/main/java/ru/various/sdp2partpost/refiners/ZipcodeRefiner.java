package ru.various.sdp2partpost.refiners;

import ru.various.sdp2partpost.exceptions.IncompleteDataException;

import java.util.regex.Matcher;

/**
 * Class to get a zipcode throw regexp pattern.
 */
public class ZipcodeRefiner extends Refiner {
    private static String regex = "(\\d{6})";

    public ZipcodeRefiner() {
        super(regex);
    }

    public ZipcodeRefiner(int flags) {
        super(regex, flags);
    }

    /**
     * Refines an six position zipcode from raw string.
     * Throws exception if cannot find zipcode or zipcode pattern
     * found more than one time.
     * @param raw Raw string to find a zipcode into it
     * @return Zipcode
     * @throws IncompleteDataException
     */
    @Override
    public Integer refine(String raw) throws IncompleteDataException {
        // address must consist zipcode
        // throw Exception if not
        if (raw == null)
            throw new IncompleteDataException("Адрес отсутствует");

        Integer zipcode;
        Matcher matcher = getPattern().matcher(raw);

        // check zipcode
        if (matcher.find()) {
            zipcode = Integer.parseInt(matcher.group(1));
            // check to another 6-position number
            if (matcher.find())
                throw new IncompleteDataException("Индекс неопределен.");
        }
        else
            throw new IncompleteDataException("Индекс не обнаружен.");

        return zipcode;
    }
}

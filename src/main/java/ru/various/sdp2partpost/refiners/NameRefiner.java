package ru.various.sdp2partpost.refiners;

import ru.various.sdp2partpost.exceptions.IncompleteDataException;

import java.util.regex.Matcher;

/**
 * Class to check string to match regex and reform it to
 * template <Lastname> <F>.<M>.
 */
public class NameRefiner extends Refiner {

    private static String rusStartsOnBig = "([А-Я|Ё])[а-я|ё]{1,40}([-]([А-Я|Ё])[а-я|ё]{1,40})?";

    private static String rusBigDot = "[А-Я|Ё]\\.";

    private static String rusBigDotComplex = "[А-Я|Ё][-][А-Я|Ё]\\.";

    private static String regex =
            "^(" + rusStartsOnBig + ")[ ]+" +
            "((" + rusStartsOnBig + "[ ]+" + rusStartsOnBig + ")" +
            "|" +
            "((" + rusBigDot + ")|(" + rusBigDotComplex + "))[ ]*" +
            "((" + rusBigDot + ")|(" + rusBigDotComplex + ")))";

    public NameRefiner() {
        super(regex);
    }

    public NameRefiner(int flags) {
        super(regex, flags);
    }

    /**
     * Check raw to regex and reform it
     * @param raw raw name string
     * @return refined name
     * @throws IncompleteDataException
     */
    @Override
    public String refine (String raw) throws IncompleteDataException{
        // name string must consist name
        // throw Exception if not
        if (raw == null)
            throw new IncompleteDataException("ФИО отсутствует");

        String out = raw.trim();

        Matcher matcher = getPattern().matcher(out);

        if (matcher.find()) {
            out = out.substring(matcher.start(), matcher.end());

            int firstSpace = out.indexOf(" ");

            out = out.substring(0, firstSpace + 1) +
                    out.substring(firstSpace).
                            replaceAll(rusStartsOnBig, "$1.").
                            replaceAll(" ", "");
            return out;
        }
        else
            throw new IncompleteDataException("ФИО неполны.");
    }
}

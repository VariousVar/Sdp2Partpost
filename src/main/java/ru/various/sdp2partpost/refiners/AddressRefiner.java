package ru.various.sdp2partpost.refiners;

import ru.various.sdp2partpost.exceptions.IncompleteDataException;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * Class to get road address from raw address string
 */
public class AddressRefiner extends Refiner {
    private static String regex = "^\\d{1,4}$|^(\\d{1,4}/\\d{1,3})$";

    private static ArrayList<String> roadTypes = new ArrayList<String>();
    private static ArrayList<String> houseTypes = new ArrayList<String>();
    private static ArrayList<String> addHouseTypes = new ArrayList<String>();
    private static ArrayList<String> flatTypes = new ArrayList<String>();

    static {
        roadTypes.add("улица"); roadTypes.add("ул.");
        roadTypes.add("переулок"); roadTypes.add("пер.");
        roadTypes.add("проспект"); roadTypes.add("просп."); roadTypes.add("пр-кт");
        roadTypes.add("бульвар"); roadTypes.add("бул."); roadTypes.add("б-р");
        roadTypes.add("площадь"); roadTypes.add("пл.");
        roadTypes.add("проезд"); roadTypes.add("пр."); roadTypes.add("пр-д");
        roadTypes.add("ряд");
        roadTypes.add("линия");
        roadTypes.add("проулок");
        roadTypes.add("магистраль"); roadTypes.add("маг.");
        roadTypes.add("набережная"); roadTypes.add("наб.");
        roadTypes.add("дорога"); roadTypes.add("дор.");
        roadTypes.add("тупик"); roadTypes.add("туп.");
        roadTypes.add("шоссе"); roadTypes.add("ш.");
        roadTypes.add("кольцо");
        roadTypes.add("вал");
        roadTypes.add("разъезд"); roadTypes.add("рзд");
        roadTypes.add("въезд");
        roadTypes.add("тракт");

        houseTypes.add("дом"); houseTypes.add("д.");
        houseTypes.add("участок"); houseTypes.add("уч.");
        houseTypes.add("войсковая часть"); houseTypes.add("в/ч");
        houseTypes.add("владение");

        addHouseTypes.add("корпус"); addHouseTypes.add("корп.");
        addHouseTypes.add("строение"); addHouseTypes.add("стр.");

        flatTypes.add("квартира"); flatTypes.add("кв.");
        flatTypes.add("офис"); flatTypes.add("оф.");
        flatTypes.add("помещение"); flatTypes.add("пом.");
    }

    public AddressRefiner() {
        super(regex);
    }
    public AddressRefiner(int flags) {
        super(regex, flags);
    }

    /**
     * Refine raw address string to get road address and house number
     * @param raw raw address string
     * @return template like as
     *      "_road_type_. road_name, _house_type_. house_number,
     *      _additionalHouseType_. addHouseName, _flat_type_ flat"
     * @throws IncompleteDataException
     */

    @Override
    public String refine(String raw) throws IncompleteDataException {
        // raw address must consist identifiers
        // throw Exception if not
        if (raw == null)
            throw new IncompleteDataException("Адрес отсутствует");

        // out structure: road, house, addHouse, flat
        final String[] out = {"", "", "", ""};


        String[] stringParts = raw.split(",");

        for (String part : stringParts) {
            Iterator<String> roads = roadTypes.iterator();
            Iterator<String> houses = houseTypes.iterator();
            Iterator<String> addHouses = addHouseTypes.iterator();
            Iterator<String> flats = flatTypes.iterator();

            String lowerCasePart = part.toLowerCase();
            String next;

            while (roads.hasNext() && out[0].isEmpty()) {
                // if part has a road identifier
                next = roads.next();
                if (lowerCasePart.contains(next)) {
                    out[0] = formatComponent(part, next);
                    break;
                }
            }
            while (houses.hasNext() && out[1].isEmpty()) {
                //if part has a house identifier
                next = houses.next();
                if (lowerCasePart.contains(next)) {
                    out[1] = formatComponent(part, next);
                    break;
                }
            }
            while (addHouses.hasNext() && out[2].isEmpty()) {
                //if part has an additional house identifier
                next = addHouses.next();
                if (lowerCasePart.contains(next)) {
                    out[2] = formatComponent(part, next);
                    break;
                }
            }
            while (flats.hasNext() && out[3].isEmpty()) {
                //if part has a flat identifier
                next = flats.next();
                if (lowerCasePart.contains(next)) {
                    out[3] = formatComponent(part, next);
                    break;
                }
            }
            if (getPattern().matcher(part.trim()).find() && out[3].isEmpty()) {
                //if part has a flat number without identifier
                out[1] = part;
            }

        }

        return formatResult(out);
    }

    /**
     * Unifies component structure. Remove all spaces and add one between template string and rest of component string.
     * @param component raw component
     * @param template string that is the base of returned string
     * @return unified component
     */
    private String formatComponent(String component, String template) {
        return component.trim().replaceAll(" +", " ").replaceAll(template + " +", template + " ");
    }

    /**
     * Formats array of address component into string.
     * @param out array of address components: road, house, addHouse, flat.
     * @return formatted string
     * @throws IncompleteDataException
     */
    private String formatResult(String[] out) throws IncompleteDataException {
        if (out[0].isEmpty() && out[1].isEmpty() && out[2].isEmpty() && out[3].isEmpty()) {
            throw new IncompleteDataException("Строка адреса не содержит полного адреса.");
        }
        else {
            StringBuilder output = new StringBuilder();
            if (!out[0].isEmpty())
                output.append(out[0]);
            if (!out[1].isEmpty() && !out[0].isEmpty())
                output.append(", ").append(out[1]);
            if (!out[2].isEmpty() && !out[0].isEmpty())
                output.append(", ").append(out[2]);
            if (!out[3].isEmpty() && !out[0].isEmpty())
                output.append(", ").append(out[3]);

            return output.toString().trim();
        }
    }
}

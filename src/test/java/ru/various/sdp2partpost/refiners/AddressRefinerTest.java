package ru.various.sdp2partpost.refiners;

import org.junit.Assert;
import org.junit.Test;
import ru.various.sdp2partpost.exceptions.IncompleteDataException;


public class AddressRefinerTest {

    @Test
    public void testRefine() throws IncompleteDataException {
        Refiner refiner = new AddressRefiner();

        Assert.assertEquals("проезд 25/1", refiner.refine(" проезд   25/1  "));
        Assert.assertEquals("ул. 25/1", refiner.refine(" ул.  25/1  "));
        Assert.assertEquals("пр-кт 25/1", refiner.refine("    пр-кт   25/1  "));
        Assert.assertEquals("просп. М. Нагибина, д. 34", refiner.refine("просп. М. Нагибина, д. 34"));
        Assert.assertEquals("улица Маршала Жукова", refiner.refine("улица     Маршала     Жукова"));
//        Assert.assertEquals("", refiner.refine(""));
    }

}
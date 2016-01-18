package ru.various.sdp2partpost.refiners;

import org.junit.Assert;
import org.junit.Test;
import ru.various.sdp2partpost.enums.Gender;

public class GenderRefinerTest {

    @Test
    public void testRefine() throws Exception {
       Refiner refiner = new GenderRefiner();

       Assert.assertEquals(Gender.MALE, refiner.refine("мужской"));
       Assert.assertEquals(Gender.FEMALE, refiner.refine("женский"));
       Assert.assertEquals(Gender.MALE, refiner.refine("пример, мужской, 123"));
    }
}
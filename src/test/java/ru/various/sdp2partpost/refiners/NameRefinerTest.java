package ru.various.sdp2partpost.refiners;

import org.junit.Assert;
import org.junit.Test;

public class NameRefinerTest {

    @Test
    public void testRefine() throws Exception {
        Refiner refiner = new NameRefiner();

        Assert.assertEquals("Иванов И.И.", refiner.refine("Иванов Иван Иванович"));
        Assert.assertEquals("Иванов И.И.", refiner.refine("Иванов И.И."));
        Assert.assertEquals("Иванов-Петров И.И.", refiner.refine("Иванов-Петров Иван Иванович"));
        Assert.assertEquals("Иванов-Петров И.И.", refiner.refine("Иванов-Петров И.И."));
        Assert.assertEquals("Иванов И.И.", refiner.refine("Иванов И.И., Представитель командования Иванов И.И."));
        Assert.assertEquals("Иванов М.В.", refiner.refine("Иванов М.В. (Иановап кпу укпмВыаВ)"));
        Assert.assertEquals("Иванов М-В.В.", refiner.refine("Иванов М-В.В."));
        Assert.assertEquals("Иванов М-В.В.", refiner.refine("Иванов М-В.   В."));
        Assert.assertEquals("Иванов М-В.В-В.", refiner.refine("Иванов М-В.В-В."));
        Assert.assertEquals("Иванов М.В-В.", refiner.refine("Иванов М.В-В."));
        Assert.assertEquals("Иванов М.В-В.", refiner.refine("Иванов М.    В-В."));
    }
}
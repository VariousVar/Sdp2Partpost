package ru.various.sdp2partpost.cases;

import org.junit.Assert;
import org.junit.Test;

import java.util.Properties;


public class CaseContainerTest {
    private CaseContainer caseContainer;
    private CaseContainer caseContainerEmpty;
    private CaseContainer u1Container;
    private CaseContainer g2Container;


    private String query = "SELECT parts.${name}, parts.${address}, 'мужской' gender " +
            "FROM ${parts_table} parts " +
            "INNER JOIN ${case_table} casee " +
            "ON parts.${case_id} = casee.${id} " +
            "WHERE parts.${case_id} " +
            "IN " +
            "(SELECT casee.${id} " +
            "FROM ${case_table} casee " +
            "WHERE casee.${full_number} IN ${cases})";

    public CaseContainerTest() {
        Properties defaults = CaseProperties.builder().build();

        caseContainer = new CaseContainer("G1_PARTS", "G1_CASE", defaults, "11", "3", "9", "М");
        caseContainer.addCase("3-7/2014");
        caseContainer.addCase("3-8/2014");
        caseContainer.addCase("3-9/2014");

        caseContainerEmpty = new CaseContainer("G1_PARTS", "G1_CASE", defaults, "11", "3", "9", "М");

        u1Container = new CaseContainer("U1_PARTS", "U1_CASE", defaults, "10", "1");
        u1Container.addCase("1-9/2014");
        u1Container.addCase("10-2/2014");

        Properties g2 = CaseProperties.builder()
                .add("name", "party_name")
                .add("address", "adress2")
                .add("post_address", "adress")
                .add("full_number", "z_fullnumber").build();

    	g2Container = new CaseContainer("G2_PARTS", "GR2_DELO", g2, "33");
    	g2Container.addCase("33-878/2014");
    	g2Container.addCase("33-879/2014");
    }

    @Test
    public void testFillAllPasses() throws Exception {
//        query = "SELECT parts.${name}, parts.${address}, ${idd} " +
//                "FROM ${parts_table} parts " +
//                "INNER JOIN ${case_table} casee " +
//                "ON parts.${case_id} = casee.${id} " +
//                "WHERE parts.${case_id} " +
//                "IN " +
//                "(SELECT casee.${id} " +
//                "FROM ${case_table} casee " +
//                "WHERE casee.${full_number} IN ${cases})";

    	Assert.assertEquals("SELECT parts.name, parts.address, ${idd} " +
                        "FROM G1_PARTS parts " +
                        "INNER JOIN G1_CASE casee " +
                        "ON parts.case_id = casee.id " +
                        "WHERE parts.case_id " +
                        "IN " +
                        "(SELECT casee.id " +
                        "FROM G1_CASE casee " +
                        "WHERE casee.full_number IN ('10-2/2014','1-9/2014','1-7/2013'))",
                caseContainer.substitute(query));
    }

//    @Test
//    public void testGetConfiguredCasesString() {
//        Assert.assertEquals("('10-2/2014','1-9/2014','1-7/2013')", caseContainer.getConfiguredCasesString());
//    }
    @Test
    public void testGetProperties() {
    	Assert.assertEquals("('3-8/2014','3-9/2014','3-7/2014')", caseContainer.getProperties().getProperty("cases"));
    	Assert.assertEquals("('0-0/0000')", caseContainerEmpty.getProperties().getProperty("cases"));
    }
}
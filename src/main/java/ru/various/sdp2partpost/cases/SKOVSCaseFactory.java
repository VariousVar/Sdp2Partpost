package ru.various.sdp2partpost.cases;

import java.util.Properties;

/**
 * Конкретная фабрика различных картотек для СКОВС
 */
public class SKOVSCaseFactory extends CaseFactory {

    public SKOVSCaseFactory() {
        // Default prefs: G1, U1, U2, M
        Properties defaults = CaseProperties.builder().build();

        // Prefs for ADM1, ADM2
        Properties adm1 = CaseProperties.builder()
                .add("full_number", "case_number").build();
        Properties adm2 = CaseProperties.builder()
                .add("full_number", "case_number").build();

        // Prefs for ADM
        Properties adm = CaseProperties.builder()
                .add("name", "party_name")
                .add("address", "adress2")
                .add("post_address", "adress")
                .add("full_number", "case_number").build();

        // Prefs for G2
        Properties g2 = CaseProperties.builder()
                .add("name", "party_name")
                .add("address", "adress2")
                .add("post_address", "adress")
                .add("full_number", "z_fullnumber").build();

        // Prefs for G3
        Properties g3 = CaseProperties.builder()
                .add("id", "proc_id")
                .add("post_address", "").build();

        // Prefs for U3
        Properties u3 = CaseProperties.builder()
                .add("id", "proc_id").build();


        // Create CaseContainers
        caseContainers.add(new CaseContainer("ADM1_PARTS", "ADM1_CASE", adm1, "12"));
        caseContainers.add(new CaseContainer("ADM2_PARTS", "ADM2_CASE", adm2, "21"));
        caseContainers.add(new CaseContainer("ADM_PARTS", "ADM_CASE", adm, "5"));
        caseContainers.add(new CaseContainer("G1_PARTS", "G1_CASE", new Properties(defaults), "11", "3", "9", "М"));
        caseContainers.add(new CaseContainer("G2_PARTS", "GR2_DELO", g2, "33"));
        caseContainers.add(new CaseContainer("G33_PARTS", "G33_PROCEEDING", g3, "4Г"));
        caseContainers.add(new CaseContainer("M_PARTS", "M_CASE", new Properties(defaults), "3/2", "3/12", "3/7", "4/17"));
        caseContainers.add(new CaseContainer("U1_PARTS", "U1_CASE", new Properties(defaults), "10", "1"));
        caseContainers.add(new CaseContainer("U2_PARTS", "U2_CASE", new Properties(defaults), "22"));
        caseContainers.add(new CaseContainer("U33_OTHER_PARTS", "U33_PROCEEDING", u3, "4У"));
    }

    @Override
    public void processInput(String input) {
	    if (input == null)
		    return;

        String[] cases = input.split(",");

        CaseContainer lastContainer = caseContainers.get(0);

        for (String casee : cases) {
            // simple realization
            String prefix = casee.trim().split("\\-")[0];

            if (canBeAdded(prefix)) {
                // usually cases are the same in prefix
                if (lastContainer.containsPrefix(prefix))
                    lastContainer.addCase(casee.trim());
                else {
                    for (CaseContainer container : caseContainers) {
                        if (container.containsPrefix(prefix)) {
                            container.addCase(casee.trim());
                            lastContainer = container;
                        }
                    }
                }
            }
            // no such prefix in case containers
            else
                ignoredCases.add(casee.trim());
        }
    }
}

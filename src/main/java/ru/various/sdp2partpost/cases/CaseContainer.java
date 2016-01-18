package ru.various.sdp2partpost.cases;

import java.util.*;

import org.apache.commons.lang3.text.StrSubstitutor;

/**
 * Container to store dependencies between cases and SQL tables fields
 */
public class CaseContainer {
    private Set<String> prefixes;
    private java.util.Properties properties;
    private Set<String> cases;

    public CaseContainer(String partsTable, String caseTable, java.util.Properties properties, String... prefixes) {
        this.properties = properties;
        this.properties.put("parts_table", partsTable);
        this.properties.put("case_table", caseTable);

        this.cases = new HashSet<>();

        this.prefixes = new HashSet<>();
        Collections.addAll(this.prefixes, prefixes);
    }

    /**
     * Adds a case to container
     * @param casee case to add
     * @return true if success
     */
    public boolean addCase(String casee) {
        return cases.add(casee);
    }

    /**
     * Returns {@code true} if this container contains the specified prefix.
     * @param prefix prefix to check
     * @return {@code true} if this container contains the specified prefix
     */
    public boolean containsPrefix(String prefix) {
        return prefixes.contains(prefix);
    }

    /**
     * Substitute parameters in {@code query} correspond to
     * parameters in {@code properties}
     * @param query parametrized query
     * @return formatted {@code query} string
     */
    public String substitute(String query) {
        return StrSubstitutor.replace(query, getProperties());
    }

    /**
     * Compose {@code cases} into a string,
     * suitable for substitution in SQL query
     * @return formatted {@code cases} string
     */
    private String getConfiguredCasesString() {
        // substitute cases
        StringBuilder builder = new StringBuilder();

        // if container has no cases return really nonexistent case number
        if (cases.isEmpty())
            return builder.append("('0-0/0000')").toString();

        builder.append("(");

        for (String casee : cases) {
            builder.append("'").append(casee).append("'").append(",");
        }
        // delete last comma, if at least one case added and close bkt
        if (!cases.isEmpty()) {
            builder.deleteCharAt(builder.length() - 1);
        }
        builder.append(")");

        return builder.toString();
    }

    public void clear() {
        cases.clear();
    }

    public Set<String> getPrefixes() {
        return prefixes;
    }

    public java.util.Properties getProperties() {
        properties.put("cases", getConfiguredCasesString());
        return properties;
    }

    public Set<String> getCases() {
        return cases;
    }
}

package ru.various.sdp2partpost.cases;

import java.util.ArrayList;
import java.util.HashSet;

/**
 * Абстрактная фабрика различных картотек
 */
public abstract class CaseFactory implements InputProcessor {
    protected HashSet<String> cases = new HashSet<>();
    protected HashSet<String> ignoredCases = new HashSet<>();
    protected ArrayList<CaseContainer> caseContainers = new ArrayList<CaseContainer>();
    protected HashSet<String> prefixes = new HashSet<>();

    public HashSet<String> substituteAll(String query) {
        HashSet<String> result = new HashSet<>();

        for (CaseContainer container : caseContainers) {
            result.add(container.substitute(query));
        }

        return result;
    }

    public String substitute(String query, String prefix) {
        for (CaseContainer container : caseContainers) {
            if (container.containsPrefix(prefix))
                return container.substitute(query);
        }

        return query;
    }

    public void clear() {
        cases.clear();
        ignoredCases.clear();
        prefixes.clear();

        for (CaseContainer container : caseContainers) {
            container.clear();
        }

    }

    protected boolean canBeAdded(String prefix) {
        if (prefixes.isEmpty()) {
            computePrefixes();
        }

        return prefixes.contains(prefix);
    }

    protected void computePrefixes() {
        for (CaseContainer caseContainer : caseContainers) {
            prefixes.addAll(caseContainer.getPrefixes());
        }
    }

    public HashSet<String> getCases() {
        cases.clear();
        for (CaseContainer container : caseContainers) {
            cases.addAll(container.getCases());
        }

        return cases;
    }

    public HashSet<String> getIgnoredCases() {
        return ignoredCases;
    }

    public ArrayList<CaseContainer> getCaseContainers() {
        return caseContainers;
    }

    public HashSet<String> getPrefixes() {
        return prefixes;
    }
}

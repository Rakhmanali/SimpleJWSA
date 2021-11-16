package org.wsa.sjwsa;

import java.util.*;

public class ParameterCollection implements Iterable<Parameter> {
    private final List<Parameter> internalList = new ArrayList<>();

    private Map<String, Integer> lookup;
    private Map<String, Integer> lookupIgnoreCase;

    public ParameterCollection() {
        this.invalidateHashLookups();
    }

    void invalidateHashLookups() {
        lookup = null;
        lookupIgnoreCase = null;
    }

    public Parameter add(Parameter parameter) {
        this.internalList.add(parameter);
        return parameter;
    }

    public Parameter add(String name) {
        Parameter parameter = new Parameter(name);
        return this.add(parameter);
    }

    public Parameter add(String name, PgsqlDbType pgsqlDbType) {
        Parameter parameter = new Parameter(name, pgsqlDbType);
        return this.add(parameter);
    }

    public Parameter add(String name, PgsqlDbType pgsqlDbType, boolean isArray) {
        Parameter parameter = new Parameter(name, pgsqlDbType, isArray);
        return this.add(parameter);
    }

    public Parameter add(String name, PgsqlDbType pgsqlDbType, Object value) {
        Parameter parameter = new Parameter(name, pgsqlDbType, value);
        return this.add(parameter);
    }

    public Parameter add(String name, PgsqlDbType pgsqlDbType, boolean isArray, Object value) {
        Parameter parameter = new Parameter(name, pgsqlDbType, isArray, value);
        return this.add(parameter);
    }

    public int getCount() {
        return this.internalList.size();
    }

    public Iterator<Parameter> iterator() {
        return this.internalList.iterator();
    }

    public Parameter get(String name) {
        int index = this.indexOf(name);

        if (index == -1) {
            throw new IndexOutOfBoundsException("Parameter not found");
        }

        return this.internalList.get(index);
    }

    public void set(String name, Parameter value) {
        int index = this.indexOf(name);

        if (index == -1) {
            throw new IndexOutOfBoundsException("Parameter not found");
        }

        Parameter oldValue = this.internalList.get(index);

        if (value.getName().equals(oldValue.getName()) == false) {
            this.invalidateHashLookups();
        }

        this.internalList.set(index, value);
    }

    public int indexOf(String parameterName) {
        int retIndex;
        int scanIndex;

        // Using a dictionary is much faster for 5 or more items
        if (this.internalList.size() >= 5) {
            if (this.lookup == null) {
                this.lookup = new HashMap<>();
                for (scanIndex = 0; scanIndex < this.internalList.size(); scanIndex++) {
                    var item = this.internalList.get(scanIndex);
                    if (!this.lookup.containsKey(item.getName())) {
                        this.lookup.put(item.getName(), scanIndex);
                    }
                }
            }

            // Try to access the case sensitive parameter name first
            if (this.lookup.containsKey(parameterName) == true) {
                return this.lookup.get(parameterName);
            }

            // Case sensitive lookup failed, generate a case insensitive lookup
            if (this.lookupIgnoreCase == null) {
                this.lookupIgnoreCase = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);
                for (scanIndex = 0; scanIndex < this.internalList.size(); scanIndex++) {
                    var item = this.internalList.get(scanIndex);

                    // Store only the first of each distinct value
                    if (!this.lookupIgnoreCase.containsKey(item.getName())) {
                        this.lookupIgnoreCase.put(item.getName(), scanIndex);
                    }
                }
            }

            if (this.lookupIgnoreCase.containsKey(parameterName) == true) {
                return this.lookupIgnoreCase.get(parameterName);
            }

            return -1;
        }

        retIndex = -1;

        // Scan until a case insensitive match is found, and save its index for possible return.
        // Items that don't match loosely cannot possibly match exactly.
        for (scanIndex = 0; scanIndex < this.internalList.size(); scanIndex++) {
            var item = this.internalList.get(scanIndex);

            if (parameterName.compareToIgnoreCase(item.getName()) == 0) {
                retIndex = scanIndex;
                break;
            }
        }

        // Then continue the scan until a case sensitive match is found, and return it.
        // If a case insensitive match was found, it will be re-checked for an exact match.
        for (; scanIndex < this.internalList.size(); scanIndex++) {
            var item = this.internalList.get(scanIndex);

            if (item.getName().equals(parameterName)) {
                return scanIndex;
            }
        }

        return retIndex;
    }
}

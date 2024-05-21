package com.tlp.rating;

public enum ConstructionType {
    BRICK("BRICK"),
    STICKS("STICKS"),
    STRAW("STRAW");

    private final String code;

    ConstructionType(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    /**
     * Get enum instance by code, ignoring case.
     *
     * @param input The input code to search for.
     * @return The matching enum instance or null if none found.
     */
    public static ConstructionType getByCode(String input) {
        for (ConstructionType type : ConstructionType.values()) {
            if (type.code.equalsIgnoreCase(input)) {
                return type;
            }
        }
        return null; // No match found
    }
}


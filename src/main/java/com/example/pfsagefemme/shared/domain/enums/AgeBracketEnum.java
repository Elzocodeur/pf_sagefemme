package com.example.pfsagefemme.shared.domain.enums;

import java.util.Arrays;
import java.util.List;

/**
 * Tranches d'age utilisees par les rapports mensuels (5.2.1 / 5.2.2 / 5.2.3 / 5.2.4).
 * Les bornes sont inclusives.
 */
public enum AgeBracketEnum {
    BR_10_14("10-14", 10, 14),
    BR_15_19("15-19", 15, 19),
    BR_20_24("20-24", 20, 24),
    BR_25_49("25-49", 25, 49),
    BR_50_PLUS("≥50", 50, 200);

    private final String label;
    private final int min;
    private final int max;

    AgeBracketEnum(String label, int min, int max) {
        this.label = label;
        this.min = min;
        this.max = max;
    }

    public String getLabel() { return label; }
    public int getMin() { return min; }
    public int getMax() { return max; }

    public static List<AgeBracketEnum> all() { return Arrays.asList(values()); }
}

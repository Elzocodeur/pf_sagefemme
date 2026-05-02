package com.example.pfsagefemme.shared.domain.enums;

/** Classification clinique de l'infection a VIH selon l'OMS (4 stades). */
public enum StadeVihOmsEnum {
    STADE_I("Asymptomatique"),
    STADE_II("Symptomatique modere"),
    STADE_III("Symptomatique avance"),
    STADE_IV("Severe / SIDA");

    private final String description;

    StadeVihOmsEnum(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}

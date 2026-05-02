package com.example.pfsagefemme.shared.domain.enums;

/**
 * Catalogue des methodes contraceptives - source des lignes des tableaux
 * 5.2.1, 5.2.2, 5.2.4 et de la matrice de changement de methode 5.2.1.
 *
 * Chaque valeur expose sa categorie (regroupement) et sa sous-categorie (intitule fin).
 */
public enum MethodeContraceptiveEnum {

    PILULE_PP("Pilules", "PP"),
    PILULE_POP("Pilules", "POP"),
    INJECTABLE_IM("Injectables", "Intra Musculaire"),
    INJECTABLE_SC_PRESTATAIRE("Injectables - SC", "Offre par Prestataire"),
    INJECTABLE_SC_AUTO_INJECTION("Injectables - SC", "Auto injection"),
    DIU_TCU_380A("DIU", "TCu 380A"),
    DIU_TCU_375_SLEEK("DIU", "TCu 375 sleek"),
    DIU_POST_PARTUM("DIU", "Post Partum"),
    IMPLANT_1_CAPSULE("Implants", "1 capsule"),
    IMPLANT_2_CAPSULES("Implants", "2 capsules"),
    PRESERVATIF_MASCULIN("Preservatifs", "Masculin"),
    PRESERVATIF_FEMININ("Preservatifs", "Feminin"),
    MAMA("Methodes naturelles", "MAMA"),
    METHODE_JOUR_FIXE_COLLIER("Methodes naturelles", "Methode Jour Fixe/Collier"),
    MAO("Methodes naturelles", "MAO"),
    AUTRES_METHODES_NATURELLES("Methodes naturelles", "Autres Methodes Naturelles"),
    ANNEAU_VAGINAL_PROGESTATIFS("Anneau vaginal a progestatifs", ""),
    LIGATURE_DES_TROMPES("Methode chirurgicale", "Ligature des trompes"),
    AUTRES("Autres methodes", "A preciser");

    private final String categorie;
    private final String sousCategorie;

    MethodeContraceptiveEnum(String categorie, String sousCategorie) {
        this.categorie = categorie;
        this.sousCategorie = sousCategorie;
    }

    public String getCategorie() {
        return categorie;
    }

    public String getSousCategorie() {
        return sousCategorie;
    }
}

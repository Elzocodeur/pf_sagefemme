/**
 * Bounded context "Rapport mensuel PF" - rapport reglementaire transmis au
 * Service National de Sante de la Reproduction.
 *
 * Source : Rapport_Mensuel_PF.xlsx
 *   - 5.2.1 Nouvelles dans le Programme (LigneRapport, type NOUVELLES_PROGRAMME)
 *   - 5.2.2 Actives selon la methode    (LigneRapport, type ACTIVES_METHODE)
 *   - 5.2.3 Actives PVVIH                (champs {@code pvvihTranche*} de RapportMensuel)
 *   - 5.2.4 Inactives selon la methode   (LigneRapport, type INACTIVES_METHODE)
 *   - 5.2.1 Changement de methode        (LigneMatriceChangement, matrice DE -> VERS)
 */
package com.example.pfsagefemme.modules.rapport;

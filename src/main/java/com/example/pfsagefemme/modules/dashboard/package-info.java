/**
 * Bounded context "Dashboard" - vues agregees transverses pour la sage-femme :
 *  - cards de KPI (total / actives / arretees / abandons / pvvih) avec deltas mensuels
 *  - histogramme d'activite des 7 derniers jours
 *  - liste des abandons a relancer (priorite >15j)
 *  - actives recentes avec badge clinique
 *
 * Compose les ports out des modules patient + consultation - lecture seule.
 */
package com.example.pfsagefemme.modules.dashboard;

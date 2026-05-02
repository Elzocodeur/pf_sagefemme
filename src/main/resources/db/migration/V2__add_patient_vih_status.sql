-- =============================================================================
-- V2 - Ajoute le statut VIH detaille et la date de naissance sur les patientes.
-- =============================================================================

ALTER TABLE patientes
    ADD COLUMN date_naissance DATE,
    ADD COLUMN telephone      VARCHAR(30),
    ADD COLUMN stade_oms      VARCHAR(20),
    ADD COLUMN sous_arv       BOOLEAN;

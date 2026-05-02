-- =============================================================================
-- V4 - Cycle de vie de la patiente (ARRETEE / ABANDON) + traitement ARV detaille
--      + numero de dossier humain (PV-YYYY-NNN).
-- =============================================================================

ALTER TABLE patientes
    ADD COLUMN numero_dossier VARCHAR(30),
    ADD COLUMN traitement_arv VARCHAR(100),
    ADD COLUMN motif_arret    VARCHAR(255),
    ADD COLUMN date_arret     DATE;

CREATE UNIQUE INDEX idx_patientes_numero_dossier ON patientes(numero_dossier);
CREATE INDEX idx_patientes_statut_pvvih ON patientes(statut_patiente, pvvih);

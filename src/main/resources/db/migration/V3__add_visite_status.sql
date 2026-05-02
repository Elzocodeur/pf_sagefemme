-- =============================================================================
-- V3 - Ajoute un statut sur les visites (BROUILLON / VALIDEE).
-- =============================================================================

ALTER TABLE visites
    ADD COLUMN statut VARCHAR(20);

UPDATE visites SET statut = 'VALIDEE' WHERE statut IS NULL;

ALTER TABLE visites
    ALTER COLUMN statut SET NOT NULL,
    ALTER COLUMN statut SET DEFAULT 'BROUILLON';

CREATE INDEX idx_visites_statut ON visites(statut);

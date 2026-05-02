-- =============================================================================
-- V5 - Username pour login (separate de l'email) + identifiant externe + responsable du poste
-- =============================================================================

ALTER TABLE utilisateurs
    ADD COLUMN username VARCHAR(50);

CREATE UNIQUE INDEX idx_utilisateurs_username ON utilisateurs(username) WHERE username IS NOT NULL;

ALTER TABLE postes_sante
    ADD COLUMN identifiant_externe   VARCHAR(30),
    ADD COLUMN responsable_nom       VARCHAR(150),
    ADD COLUMN responsable_titre     VARCHAR(100),
    ADD COLUMN responsable_email     VARCHAR(150),
    ADD COLUMN responsable_telephone VARCHAR(30);

CREATE UNIQUE INDEX idx_postes_sante_identifiant_externe
    ON postes_sante(identifiant_externe) WHERE identifiant_externe IS NOT NULL;

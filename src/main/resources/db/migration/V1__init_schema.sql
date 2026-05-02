-- =============================================================================
-- V1 - Schema initial PlaniFem (Planification Familiale)
-- Source : Fiche de Consultation PF + Rapport Mensuel PF (Senegal)
-- =============================================================================

-- ===== Postes de sante =====
CREATE TABLE postes_sante (
    id          UUID PRIMARY KEY,
    nom         VARCHAR(150) NOT NULL,
    adresse     VARCHAR(255),
    telephone   VARCHAR(30),
    district    VARCHAR(100),
    region      VARCHAR(100),
    actif       BOOLEAN NOT NULL DEFAULT TRUE,
    created_at  TIMESTAMP NOT NULL,
    updated_at  TIMESTAMP NOT NULL,
    created_by  VARCHAR(100)
);

-- ===== Utilisateurs (heritage JOINED) =====
CREATE TABLE utilisateurs (
    id            UUID PRIMARY KEY,
    nom           VARCHAR(100) NOT NULL,
    prenom        VARCHAR(100) NOT NULL,
    email         VARCHAR(150) NOT NULL UNIQUE,
    mot_de_passe  VARCHAR(255) NOT NULL,
    telephone     VARCHAR(30),
    role          VARCHAR(30)  NOT NULL,
    actif         BOOLEAN NOT NULL DEFAULT TRUE,
    created_at    TIMESTAMP NOT NULL,
    updated_at    TIMESTAMP NOT NULL,
    created_by    VARCHAR(100)
);

CREATE TABLE super_admins (
    id            UUID PRIMARY KEY REFERENCES utilisateurs(id) ON DELETE CASCADE,
    niveau_acces  VARCHAR(50)
);

CREATE TABLE admin_postes (
    id              UUID PRIMARY KEY REFERENCES utilisateurs(id) ON DELETE CASCADE,
    poste_sante_id  UUID REFERENCES postes_sante(id)
);

CREATE TABLE sages_femmes (
    id              UUID PRIMARY KEY REFERENCES utilisateurs(id) ON DELETE CASCADE,
    matricule       VARCHAR(50) UNIQUE,
    specialite      VARCHAR(100),
    poste_sante_id  UUID REFERENCES postes_sante(id)
);

CREATE INDEX idx_sages_femmes_poste ON sages_femmes(poste_sante_id);
CREATE INDEX idx_admin_postes_poste ON admin_postes(poste_sante_id);

-- ===== Patientes =====
CREATE TABLE patientes (
    id                      UUID PRIMARY KEY,
    prenom                  VARCHAR(100) NOT NULL,
    nom                     VARCHAR(100) NOT NULL,
    age                     INTEGER,
    profession              VARCHAR(100),
    adresse                 VARCHAR(255),
    ethnie                  VARCHAR(30),
    religion                VARCHAR(30),
    situation_matrimoniale  VARCHAR(30),
    niveau_instruction      VARCHAR(30),
    profession_mari         VARCHAR(30),
    referee                 BOOLEAN,
    origine_reference       VARCHAR(150),
    source_information      VARCHAR(30),
    statut_patiente         VARCHAR(20),
    pvvih                   BOOLEAN,
    date_inscription        DATE,
    poste_sante_id          UUID REFERENCES postes_sante(id),
    sage_femme_id           UUID REFERENCES sages_femmes(id),
    created_at              TIMESTAMP NOT NULL,
    updated_at              TIMESTAMP NOT NULL,
    created_by              VARCHAR(100)
);

CREATE INDEX idx_patientes_nom         ON patientes(nom);
CREATE INDEX idx_patientes_prenom      ON patientes(prenom);
CREATE INDEX idx_patientes_poste_sante ON patientes(poste_sante_id);
CREATE INDEX idx_patientes_sage_femme  ON patientes(sage_femme_id);
CREATE INDEX idx_patientes_statut      ON patientes(statut_patiente);

-- ===== Patientes - genre de vie (multi-select) =====
CREATE TABLE patientes_genre_de_vie (
    patiente_id   UUID NOT NULL REFERENCES patientes(id) ON DELETE CASCADE,
    genre_de_vie  VARCHAR(30) NOT NULL,
    PRIMARY KEY (patiente_id, genre_de_vie)
);

-- ===== Fiches de consultation =====
CREATE TABLE fiches_consultation (
    id                            UUID PRIMARY KEY,
    numero                        VARCHAR(50) NOT NULL UNIQUE,
    date_consultation             DATE NOT NULL,
    patiente_id                   UUID NOT NULL UNIQUE REFERENCES patientes(id),
    sage_femme_id                 UUID REFERENCES sages_femmes(id),
    poste_sante_id                UUID REFERENCES postes_sante(id),

    -- Section II-A : Antecedents gynecologiques
    menarches_age                 INTEGER,
    duree_regles                  INTEGER,
    cycle_menstruel               INTEGER,
    date_dernieres_regles         DATE,

    -- Section II-B : Antecedents obstetricaux
    enfants_vivants               INTEGER,
    enfants_decedes               INTEGER,
    morts_nes                     INTEGER,
    avortements                   INTEGER,
    age_dernier_enfant            VARCHAR(50),
    allaitement_exclusif          VARCHAR(100),

    -- Section II-C : Antecedents chirurgicaux
    antecedents_chirurgicaux      TEXT,

    -- Section II-D : Antecedents hereditaires
    hypertension_arterielle       BOOLEAN,
    diabete                       BOOLEAN,
    drepanocytose                 BOOLEAN,

    -- Section II-E : Antecedents medicaux
    maladies_cardio_vasculaires   TEXT,
    ictere_moins_un_an            TEXT,
    migraines                     TEXT,
    autres_medicaux               TEXT,
    plaintes_actuelles            TEXT,

    -- Examen general
    taille_cm                     DOUBLE PRECISION,
    poids_kg                      DOUBLE PRECISION,
    muqueuses                     VARCHAR(255),
    varices                       VARCHAR(255),
    oedemes                       VARCHAR(255),
    exophtalmie                   VARCHAR(255),
    allergie_cuivre               BOOLEAN,

    -- Examen physique
    thyroide                      VARCHAR(255),
    foie                          VARCHAR(255),
    ganglions                     VARCHAR(255),
    coeur                         VARCHAR(255),
    rate                          VARCHAR(255),
    poumons                       VARCHAR(255),
    peau                          VARCHAR(255),

    -- Examen gynecologique
    seins                         VARCHAR(255),
    vulve                         VARCHAR(255),
    perinee                       VARCHAR(255),
    vagin_speculum                VARCHAR(255),
    col_speculum                  VARCHAR(255),
    col_toucher_vaginal           VARCHAR(255),
    uterus_toucher_vaginal        VARCHAR(255),
    annexes_toucher_vaginal       VARCHAR(255),
    pertes_toucher_vaginal        VARCHAR(255),

    -- Section III & IV
    examens_complementaires       TEXT,
    methode_choisie               VARCHAR(50),

    created_at                    TIMESTAMP NOT NULL,
    updated_at                    TIMESTAMP NOT NULL,
    created_by                    VARCHAR(100)
);

CREATE INDEX idx_fiches_sage_femme   ON fiches_consultation(sage_femme_id);
CREATE INDEX idx_fiches_poste_sante  ON fiches_consultation(poste_sante_id);
CREATE INDEX idx_fiches_date         ON fiches_consultation(date_consultation);

-- ===== Visites =====
CREATE TABLE visites (
    id                      UUID PRIMARY KEY,
    date_visite             DATE NOT NULL,
    date_dernieres_regles   DATE,
    poids_kg                DOUBLE PRECISION,
    tension_arterielle      VARCHAR(30),
    observations            TEXT,
    unites_fournies         VARCHAR(100),
    date_prochain_rdv       DATE,
    patiente_id             UUID NOT NULL REFERENCES patientes(id),
    fiche_consultation_id   UUID REFERENCES fiches_consultation(id),
    sage_femme_id           UUID REFERENCES sages_femmes(id),
    created_at              TIMESTAMP NOT NULL,
    updated_at              TIMESTAMP NOT NULL,
    created_by              VARCHAR(100)
);

CREATE INDEX idx_visites_date     ON visites(date_visite);
CREATE INDEX idx_visites_patiente ON visites(patiente_id);

-- ===== Changements de methode =====
CREATE TABLE changements_methode (
    id                  UUID PRIMARY KEY,
    date_changement     DATE NOT NULL,
    ancienne_methode    VARCHAR(50) NOT NULL,
    nouvelle_methode    VARCHAR(50) NOT NULL,
    motif               TEXT,
    patiente_id         UUID NOT NULL REFERENCES patientes(id),
    sage_femme_id       UUID REFERENCES sages_femmes(id),
    created_at          TIMESTAMP NOT NULL,
    updated_at          TIMESTAMP NOT NULL,
    created_by          VARCHAR(100)
);

CREATE INDEX idx_changements_patiente ON changements_methode(patiente_id);
CREATE INDEX idx_changements_date     ON changements_methode(date_changement);

-- ===== Rapports mensuels =====
CREATE TABLE rapports_mensuels (
    id                       UUID PRIMARY KEY,
    mois                     INTEGER NOT NULL CHECK (mois BETWEEN 1 AND 12),
    annee                    INTEGER NOT NULL,
    poste_sante_id           UUID REFERENCES postes_sante(id),
    sage_femme_id            UUID REFERENCES sages_femmes(id),
    statut                   VARCHAR(20) NOT NULL,
    date_generation          TIMESTAMP,

    -- Tableau 5.2.3 - PVVIH
    pvvih_tranche_10_14      INTEGER,
    pvvih_tranche_15_19      INTEGER,
    pvvih_tranche_20_24      INTEGER,
    pvvih_tranche_25_49      INTEGER,
    pvvih_tranche_50_plus    INTEGER,
    pvvih_total              INTEGER,

    created_at               TIMESTAMP NOT NULL,
    updated_at               TIMESTAMP NOT NULL,
    created_by               VARCHAR(100),

    CONSTRAINT uk_rapport_mois_annee_sage_femme UNIQUE (mois, annee, sage_femme_id)
);

CREATE INDEX idx_rapports_mois  ON rapports_mensuels(mois);
CREATE INDEX idx_rapports_annee ON rapports_mensuels(annee);
CREATE INDEX idx_rapports_poste ON rapports_mensuels(poste_sante_id);

-- ===== Lignes des tableaux 5.2.1 / 5.2.2 / 5.2.4 =====
CREATE TABLE lignes_rapport (
    id                   UUID PRIMARY KEY,
    rapport_mensuel_id   UUID NOT NULL REFERENCES rapports_mensuels(id) ON DELETE CASCADE,
    type_rapport         VARCHAR(30) NOT NULL,
    methode              VARCHAR(50) NOT NULL,
    tranche_10_14        INTEGER,
    tranche_15_19        INTEGER,
    tranche_20_24        INTEGER,
    tranche_25_49        INTEGER,
    tranche_50_plus      INTEGER,
    total                INTEGER,
    created_at           TIMESTAMP NOT NULL,
    updated_at           TIMESTAMP NOT NULL,
    created_by           VARCHAR(100)
);

CREATE INDEX idx_lignes_rapport_rapport ON lignes_rapport(rapport_mensuel_id);
CREATE INDEX idx_lignes_rapport_type    ON lignes_rapport(type_rapport);

-- ===== Matrice 5.2.1 changement de methode =====
CREATE TABLE lignes_matrice_changement (
    id                   UUID PRIMARY KEY,
    rapport_mensuel_id   UUID NOT NULL REFERENCES rapports_mensuels(id) ON DELETE CASCADE,
    methode_source       VARCHAR(50) NOT NULL,
    methode_cible        VARCHAR(50) NOT NULL,
    nombre               INTEGER NOT NULL,
    created_at           TIMESTAMP NOT NULL,
    updated_at           TIMESTAMP NOT NULL,
    created_by           VARCHAR(100)
);

CREATE INDEX idx_matrice_rapport ON lignes_matrice_changement(rapport_mensuel_id);

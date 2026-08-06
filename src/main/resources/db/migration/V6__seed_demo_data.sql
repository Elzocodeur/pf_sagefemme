-- =============================================================================
-- V6 — Données de démonstration
-- 3 nouveaux postes, 5 nouvelles sages-femmes, 50 patientes (statuts variés)
-- Mot de passe de tous les nouveaux comptes : Test1234!
-- =============================================================================

-- ---------------------------------------------------------------------------
-- 1. POSTES DE SANTÉ
-- ---------------------------------------------------------------------------
INSERT INTO postes_sante (id, nom, adresse, telephone, district, region, actif, created_at, updated_at)
VALUES
  ('aa000001-0000-0000-0000-000000000001', 'CS Pikine',       'Pikine Est, Dakar',          '+221 33 855 00 01', 'Pikine',      'Dakar',      true, NOW(), NOW()),
  ('aa000002-0000-0000-0000-000000000002', 'CS Saint-Louis',  'Sor, Saint-Louis',           '+221 33 961 00 02', 'Saint-Louis', 'Saint-Louis', true, NOW(), NOW()),
  ('aa000003-0000-0000-0000-000000000003', 'CS Thiès',        'Médina Fall, Thiès',         '+221 33 951 00 03', 'Thiès',       'Thiès',       true, NOW(), NOW());

-- ---------------------------------------------------------------------------
-- 2. UTILISATEURS (sages-femmes — mot de passe : Test1234!)
-- ---------------------------------------------------------------------------
INSERT INTO utilisateurs (id, nom, prenom, email, mot_de_passe, telephone, role, actif, username, created_at, updated_at)
VALUES
  ('bb000001-0000-0000-0000-000000000001', 'Sarr',    'Fatoumata', 'fatoumata.sarr@planifem.sn',  '$2b$10$inOJyXssY8Ix42vk8UYNdejGsbfnLMWvygl9jSZVqMyEvcdA9Jc.C', '+221 77 200 0001', 'SAGE_FEMME', true, 'fatoumata.sarr',  NOW(), NOW()),
  ('bb000002-0000-0000-0000-000000000002', 'Fall',    'Khady',     'khady.fall@planifem.sn',      '$2b$10$inOJyXssY8Ix42vk8UYNdejGsbfnLMWvygl9jSZVqMyEvcdA9Jc.C', '+221 77 200 0002', 'SAGE_FEMME', true, 'khady.fall',      NOW(), NOW()),
  ('bb000003-0000-0000-0000-000000000003', 'Ba',      'Mariama',   'mariama.ba@planifem.sn',      '$2b$10$inOJyXssY8Ix42vk8UYNdejGsbfnLMWvygl9jSZVqMyEvcdA9Jc.C', '+221 77 200 0003', 'SAGE_FEMME', true, 'mariama.ba',      NOW(), NOW()),
  ('bb000004-0000-0000-0000-000000000004', 'Ndiaye',  'Rokhaya',   'rokhaya.ndiaye@planifem.sn',  '$2b$10$inOJyXssY8Ix42vk8UYNdejGsbfnLMWvygl9jSZVqMyEvcdA9Jc.C', '+221 77 200 0004', 'SAGE_FEMME', true, 'rokhaya.ndiaye',  NOW(), NOW()),
  ('bb000005-0000-0000-0000-000000000005', 'Seck',    'Ndeye',     'ndeye.seck@planifem.sn',      '$2b$10$inOJyXssY8Ix42vk8UYNdejGsbfnLMWvygl9jSZVqMyEvcdA9Jc.C', '+221 77 200 0005', 'SAGE_FEMME', true, 'ndeye.seck',      NOW(), NOW());

-- ---------------------------------------------------------------------------
-- 3. SAGES-FEMMES (liaison utilisateurs <-> postes)
-- ---------------------------------------------------------------------------
INSERT INTO sages_femmes (id, matricule, specialite, poste_sante_id)
VALUES
  ('bb000001-0000-0000-0000-000000000001', 'MAT-SF-005', 'Planification familiale', 'aa000001-0000-0000-0000-000000000001'),
  ('bb000002-0000-0000-0000-000000000002', 'MAT-SF-006', 'Planification familiale', 'aa000001-0000-0000-0000-000000000001'),
  ('bb000003-0000-0000-0000-000000000003', 'MAT-SF-007', 'Planification familiale', 'aa000002-0000-0000-0000-000000000002'),
  ('bb000004-0000-0000-0000-000000000004', 'MAT-SF-008', 'Planification familiale', 'aa000003-0000-0000-0000-000000000003'),
  ('bb000005-0000-0000-0000-000000000005', 'MAT-SF-009', 'Planification familiale', 'aa000003-0000-0000-0000-000000000003');

-- Raccourcis de lecture (commentaires uniquement — pas de variables SQL)
-- PS1 = 5fa2b02c-0cdf-4a43-87b6-bb516e92bb0e  (CS Poste de Sante 1, existant)
-- PS2 = aa000001  (CS Pikine)
-- PS3 = aa000002  (CS Saint-Louis)
-- PS4 = aa000003  (CS Thiès)
-- SF1 = 8a837310-9f1f-495d-a8a7-345e340a78df  (existant, PS1)
-- SF2 = 9145fff3-b9a2-4d5f-adfc-09607d74ab5c  (existant, PS1)
-- SF3 = 6bad952a-6635-4ebe-b0f2-7d4523d14c7e  (existant, PS1)
-- SF4 = 6ac0bc98-77c7-4105-92dd-6202d6f898ca  (existant, PS1)
-- SF5 = bb000001  (Pikine)   SF6 = bb000002  (Pikine)
-- SF7 = bb000003  (Saint-Louis)
-- SF8 = bb000004  (Thiès)    SF9 = bb000005  (Thiès)

-- ---------------------------------------------------------------------------
-- 4. PATIENTES (50)
-- Colonnes : id, prenom, nom, age, date_naissance, telephone, profession,
--            adresse, ethnie, religion, situation_matrimoniale,
--            niveau_instruction, profession_mari, referee, source_information,
--            pvvih, stade_oms, sous_arv, traitement_arv,
--            statut_patiente, motif_arret, date_arret,
--            date_inscription, numero_dossier,
--            poste_sante_id, sage_femme_id, created_at, updated_at
-- ---------------------------------------------------------------------------

-- ── PS1 : CS Poste de Sante 1 (15 patientes) ──────────────────────────────
INSERT INTO patientes
  (id, prenom, nom, age, date_naissance, telephone, profession, adresse,
   ethnie, religion, situation_matrimoniale, niveau_instruction,
   referee, source_information,
   pvvih, stade_oms, sous_arv, traitement_arv,
   statut_patiente, motif_arret, date_arret,
   date_inscription, numero_dossier,
   poste_sante_id, sage_femme_id, created_at, updated_at)
VALUES
-- 01 ACTIVE 2024
  ('cc000001-0000-0000-0000-000000000001','Fatou','Diallo',28,'1996-03-10','+221 77 100 0001','Commerçante','Dakar Plateau',
   'WOLOF','MUSULMANE','MARIEE','PRIMAIRE',
   false,'AGENT_DE_SANTE',
   false,NULL,false,NULL,
   'ACTIVE',NULL,NULL,
   '2024-03-15','PV-2024-001',
   '5fa2b02c-0cdf-4a43-87b6-bb516e92bb0e','8a837310-9f1f-495d-a8a7-345e340a78df',NOW(),NOW()),

-- 02 ACTIVE 2024
  ('cc000002-0000-0000-0000-000000000002','Aminata','Sarr',32,'1992-07-22','+221 77 100 0002','Vendeuse','Dakar Médina',
   'WOLOF','MUSULMANE','MARIEE','SECONDAIRE',
   false,'AMIE_PARENT',
   false,NULL,false,NULL,
   'ACTIVE',NULL,NULL,
   '2024-06-20','PV-2024-002',
   '5fa2b02c-0cdf-4a43-87b6-bb516e92bb0e','9145fff3-b9a2-4d5f-adfc-09607d74ab5c',NOW(),NOW()),

-- 03 ARRETEE 2024 — motif : Désir de grossesse
  ('cc000003-0000-0000-0000-000000000003','Khady','Fall',25,'1999-11-05','+221 77 100 0003','Ménagère','Dakar Parcelles',
   'PEULH','MUSULMANE','MARIEE','PRIMAIRE',
   false,'AMIE_PARENT',
   false,NULL,false,NULL,
   'ARRETEE','Désir de grossesse','2025-04-01',
   '2024-01-10','PV-2024-003',
   '5fa2b02c-0cdf-4a43-87b6-bb516e92bb0e','8a837310-9f1f-495d-a8a7-345e340a78df',NOW(),NOW()),

-- 04 ABANDON 2024 — PVVIH stade II
  ('cc000004-0000-0000-0000-000000000004','Mariama','Ba',30,'1994-05-18','+221 77 100 0004','Vendeuse','Dakar Yoff',
   'PEULH','MUSULMANE','MARIEE','NON_SCOLARISEE',
   false,'AGENT_DE_SANTE',
   true,'STADE_II',true,'TDF+3TC+EFV',
   'ABANDON',NULL,NULL,
   '2024-04-05','PV-2024-004',
   '5fa2b02c-0cdf-4a43-87b6-bb516e92bb0e','6bad952a-6635-4ebe-b0f2-7d4523d14c7e',NOW(),NOW()),

-- 05 ACTIVE 2024 — PVVIH stade I sous ARV
  ('cc000005-0000-0000-0000-000000000005','Rokhaya','Ndiaye',35,'1989-09-30','+221 77 100 0005','Infirmière auxiliaire','Dakar Ouakam',
   'SERERE','CHRETIENNE','MARIEE','SECONDAIRE',
   false,'AGENT_DE_SANTE',
   true,'STADE_I',true,'TDF+3TC+DTG',
   'ACTIVE',NULL,NULL,
   '2024-07-12','PV-2024-005',
   '5fa2b02c-0cdf-4a43-87b6-bb516e92bb0e','6ac0bc98-77c7-4105-92dd-6202d6f898ca',NOW(),NOW()),

-- 06 NOUVELLE 2026
  ('cc000006-0000-0000-0000-000000000006','Adja','Sow',22,'2004-02-14','+221 77 100 0006','Étudiante','Dakar HLM',
   'WOLOF','MUSULMANE','CELIBATAIRE','SUPERIEUR',
   false,'TELEVISION',
   false,NULL,false,NULL,
   'NOUVELLE',NULL,NULL,
   '2026-01-08','PV-2026-003',
   '5fa2b02c-0cdf-4a43-87b6-bb516e92bb0e','8a837310-9f1f-495d-a8a7-345e340a78df',NOW(),NOW()),

-- 07 ACTIVE 2025
  ('cc000007-0000-0000-0000-000000000007','Ndeye','Diop',38,'1987-04-01','+221 77 100 0007','Commerçante','Dakar Grand-Dakar',
   'WOLOF','MUSULMANE','MARIEE','PRIMAIRE',
   false,'AGENT_DE_SANTE',
   false,NULL,false,NULL,
   'ACTIVE',NULL,NULL,
   '2025-02-14','PV-2025-001',
   '5fa2b02c-0cdf-4a43-87b6-bb516e92bb0e','9145fff3-b9a2-4d5f-adfc-09607d74ab5c',NOW(),NOW()),

-- 08 ARRETEE 2025 — motif : Effets secondaires
  ('cc000008-0000-0000-0000-000000000008','Aissatou','Mbaye',27,'1998-12-25','+221 77 100 0008','Couturière','Dakar Sicap',
   'PEULH','MUSULMANE','MARIEE','PRIMAIRE',
   false,'AMIE_PARENT',
   false,NULL,false,NULL,
   'ARRETEE','Effets secondaires','2025-12-15',
   '2025-03-01','PV-2025-002',
   '5fa2b02c-0cdf-4a43-87b6-bb516e92bb0e','6bad952a-6635-4ebe-b0f2-7d4523d14c7e',NOW(),NOW()),

-- 09 ACTIVE 2024 — PVVIH stade II sous ARV
  ('cc000009-0000-0000-0000-000000000009','Sokhna','Gueye',42,'1982-06-08','+221 77 100 0009','Aide-soignante','Dakar Fann',
   'WOLOF','MUSULMANE','MARIEE','SECONDAIRE',
   false,'AGENT_DE_SANTE',
   true,'STADE_II',true,'AZT+3TC+NVP',
   'ACTIVE',NULL,NULL,
   '2024-09-20','PV-2024-006',
   '5fa2b02c-0cdf-4a43-87b6-bb516e92bb0e','6ac0bc98-77c7-4105-92dd-6202d6f898ca',NOW(),NOW()),

-- 10 ABANDON 2024
  ('cc000010-0000-0000-0000-000000000010','Mame','Cisse',29,'1995-08-15','+221 77 100 0010','Étudiante','Dakar Liberté',
   'SERERE','CHRETIENNE','CELIBATAIRE','SUPERIEUR',
   false,'TELEVISION',
   false,NULL,false,NULL,
   'ABANDON',NULL,NULL,
   '2024-11-05','PV-2024-007',
   '5fa2b02c-0cdf-4a43-87b6-bb516e92bb0e','8a837310-9f1f-495d-a8a7-345e340a78df',NOW(),NOW()),

-- 11 ARRETEE 2025 — motif : Grossesse en cours
  ('cc000011-0000-0000-0000-000000000011','Yaye','Sy',33,'1993-01-20','+221 77 100 0011','Ménagère','Dakar Rufisque',
   'TOUCOULEUR','MUSULMANE','MARIEE','NON_SCOLARISEE',
   false,'AMIE_PARENT',
   false,NULL,false,NULL,
   'ARRETEE','Grossesse en cours','2026-01-20',
   '2025-04-10','PV-2025-003',
   '5fa2b02c-0cdf-4a43-87b6-bb516e92bb0e','9145fff3-b9a2-4d5f-adfc-09607d74ab5c',NOW(),NOW()),

-- 12 NOUVELLE 2026 — PVVIH stade I non sous ARV
  ('cc000012-0000-0000-0000-000000000012','Hawa','Thiam',24,'2002-03-05','+221 77 100 0012','Vendeuse','Dakar Keur Massar',
   'PEULH','MUSULMANE','MARIEE','PRIMAIRE',
   false,'AGENT_DE_SANTE',
   true,'STADE_I',false,NULL,
   'NOUVELLE',NULL,NULL,
   '2026-03-20','PV-2026-004',
   '5fa2b02c-0cdf-4a43-87b6-bb516e92bb0e','6bad952a-6635-4ebe-b0f2-7d4523d14c7e',NOW(),NOW()),

-- 13 ARRETEE 2024 — motif : Raisons financières
  ('cc000013-0000-0000-0000-000000000013','Nogaye','Diouf',31,'1993-09-10','+221 77 100 0013','Agricultrice','Dakar Ngor',
   'SERERE','CHRETIENNE','MARIEE','NON_SCOLARISEE',
   false,'AMIE_PARENT',
   false,NULL,false,NULL,
   'ARRETEE','Raisons financières','2025-02-28',
   '2024-02-28','PV-2024-008',
   '5fa2b02c-0cdf-4a43-87b6-bb516e92bb0e','6ac0bc98-77c7-4105-92dd-6202d6f898ca',NOW(),NOW()),

-- 14 ACTIVE 2025
  ('cc000014-0000-0000-0000-000000000014','Kine','Toure',26,'2000-07-15','+221 77 100 0014','Commerçante','Dakar Almadies',
   'DIOLA','CHRETIENNE','MARIEE','SECONDAIRE',
   false,'AMIE_PARENT',
   false,NULL,false,NULL,
   'ACTIVE',NULL,NULL,
   '2025-05-18','PV-2025-004',
   '5fa2b02c-0cdf-4a43-87b6-bb516e92bb0e','8a837310-9f1f-495d-a8a7-345e340a78df',NOW(),NOW()),

-- 15 ARRETEE 2024 — motif : Ménopause précoce
  ('cc000015-0000-0000-0000-000000000015','Ndella','Camara',44,'1980-10-12','+221 77 100 0015','Ménagère','Dakar Plateau',
   'MANDINGUE','MUSULMANE','MARIEE','NON_SCOLARISEE',
   false,'AGENT_DE_SANTE',
   false,NULL,false,NULL,
   'ARRETEE','Ménopause précoce','2025-01-10',
   '2024-06-01','PV-2024-009',
   '5fa2b02c-0cdf-4a43-87b6-bb516e92bb0e','9145fff3-b9a2-4d5f-adfc-09607d74ab5c',NOW(),NOW()),

-- ── PS2 : CS Pikine (13 patientes) ────────────────────────────────────────

-- 16 ACTIVE 2024
  ('cc000016-0000-0000-0000-000000000016','Coumba','Wade',29,'1995-04-22','+221 77 200 0001','Couturière','Pikine Guinaw Rail',
   'WOLOF','MUSULMANE','MARIEE','PRIMAIRE',
   false,'AMIE_PARENT',
   false,NULL,false,NULL,
   'ACTIVE',NULL,NULL,
   '2024-02-10','PV-2024-010',
   'aa000001-0000-0000-0000-000000000001','bb000001-0000-0000-0000-000000000001',NOW(),NOW()),

-- 17 ACTIVE 2024
  ('cc000017-0000-0000-0000-000000000017','Marème','Faye',34,'1990-11-30','+221 77 200 0002','Ménagère','Pikine Thiaroye',
   'WOLOF','MUSULMANE','MARIEE','NON_SCOLARISEE',
   false,'AGENT_DE_SANTE',
   false,NULL,false,NULL,
   'ACTIVE',NULL,NULL,
   '2024-05-22','PV-2024-011',
   'aa000001-0000-0000-0000-000000000001','bb000002-0000-0000-0000-000000000002',NOW(),NOW()),

-- 18 NOUVELLE 2026
  ('cc000018-0000-0000-0000-000000000018','Seynabou','Lo',21,'2005-08-09','+221 77 200 0003','Étudiante','Pikine Yeumbeul',
   'WOLOF','MUSULMANE','CELIBATAIRE','SECONDAIRE',
   false,'TELEVISION',
   false,NULL,false,NULL,
   'NOUVELLE',NULL,NULL,
   '2026-02-15','PV-2026-005',
   'aa000001-0000-0000-0000-000000000001','bb000001-0000-0000-0000-000000000001',NOW(),NOW()),

-- 19 ARRETEE 2025 — motif : Désir de grossesse
  ('cc000019-0000-0000-0000-000000000019','Nafissatou','Ly',36,'1988-02-17','+221 77 200 0004','Sage-femme','Pikine Diamaguène',
   'PEULH','MUSULMANE','MARIEE','SUPERIEUR',
   true,'AGENT_DE_SANTE',
   false,NULL,false,NULL,
   'ARRETEE','Désir de grossesse','2025-10-05',
   '2025-01-20','PV-2025-005',
   'aa000001-0000-0000-0000-000000000001','bb000002-0000-0000-0000-000000000002',NOW(),NOW()),

-- 20 ABANDON 2024 — PVVIH stade III sous ARV
  ('cc000020-0000-0000-0000-000000000020','Bintou','Kane',28,'1996-06-03','+221 77 200 0005','Vendeuse','Pikine Nimzatt',
   'MANDINGUE','MUSULMANE','MARIEE','PRIMAIRE',
   false,'AGENT_DE_SANTE',
   true,'STADE_III',true,'TDF+3TC+EFV',
   'ABANDON',NULL,NULL,
   '2024-08-15','PV-2024-012',
   'aa000001-0000-0000-0000-000000000001','bb000001-0000-0000-0000-000000000001',NOW(),NOW()),

-- 21 ACTIVE 2025 — PVVIH stade I sous ARV
  ('cc000021-0000-0000-0000-000000000021','Ramatoulaye','Diallo',31,'1994-12-25','+221 77 200 0006','Enseignante','Pikine Citée Goorgi',
   'PEULH','MUSULMANE','MARIEE','SUPERIEUR',
   false,'AGENT_DE_SANTE',
   true,'STADE_I',true,'TDF+3TC+DTG',
   'ACTIVE',NULL,NULL,
   '2025-03-05','PV-2025-006',
   'aa000001-0000-0000-0000-000000000001','bb000002-0000-0000-0000-000000000002',NOW(),NOW()),

-- 22 ACTIVE 2025
  ('cc000022-0000-0000-0000-000000000022','Oumou','Sarr',27,'1998-09-18','+221 77 200 0007','Commerçante','Pikine Mbao',
   'WOLOF','MUSULMANE','MARIEE','PRIMAIRE',
   false,'AMIE_PARENT',
   false,NULL,false,NULL,
   'ACTIVE',NULL,NULL,
   '2025-06-10','PV-2025-007',
   'aa000001-0000-0000-0000-000000000001','bb000001-0000-0000-0000-000000000001',NOW(),NOW()),

-- 23 ARRETEE 2024 — motif : Migration
  ('cc000023-0000-0000-0000-000000000023','Diariatou','Ba',40,'1984-03-27','+221 77 200 0008','Ménagère','Pikine Dalifort',
   'PEULH','MUSULMANE','MARIEE','NON_SCOLARISEE',
   false,'AMIE_PARENT',
   false,NULL,false,NULL,
   'ARRETEE','Migration','2025-03-12',
   '2024-03-12','PV-2024-013',
   'aa000001-0000-0000-0000-000000000001','bb000002-0000-0000-0000-000000000002',NOW(),NOW()),

-- 24 ACTIVE 2024
  ('cc000024-0000-0000-0000-000000000024','Astou','Ndiaye',25,'2000-01-07','+221 77 200 0009','Couturière','Pikine Tally Icotaf',
   'WOLOF','MUSULMANE','MARIEE','SECONDAIRE',
   false,'AGENT_DE_SANTE',
   false,NULL,false,NULL,
   'ACTIVE',NULL,NULL,
   '2024-10-01','PV-2024-014',
   'aa000001-0000-0000-0000-000000000001','bb000001-0000-0000-0000-000000000001',NOW(),NOW()),

-- 25 ACTIVE 2025
  ('cc000025-0000-0000-0000-000000000025','Fatimata','Sow',30,'1995-05-20','+221 77 200 0010','Infirmière','Pikine Keur Massar Sud',
   'PEULH','MUSULMANE','MARIEE','SUPERIEUR',
   false,'AGENT_DE_SANTE',
   false,NULL,false,NULL,
   'ACTIVE',NULL,NULL,
   '2025-08-20','PV-2025-008',
   'aa000001-0000-0000-0000-000000000001','bb000002-0000-0000-0000-000000000002',NOW(),NOW()),

-- 26 ABANDON 2025
  ('cc000026-0000-0000-0000-000000000026','Awa','Mbaye',23,'2002-10-11','+221 77 200 0011','Étudiante','Pikine Diamaguène Sicap',
   'WOLOF','MUSULMANE','CELIBATAIRE','SECONDAIRE',
   false,'TELEVISION',
   false,NULL,false,NULL,
   'ABANDON',NULL,NULL,
   '2025-02-28','PV-2025-009',
   'aa000001-0000-0000-0000-000000000001','bb000001-0000-0000-0000-000000000001',NOW(),NOW()),

-- 27 NOUVELLE 2026
  ('cc000027-0000-0000-0000-000000000027','Yacine','Gueye',33,'1993-07-04','+221 77 200 0012','Vendeuse','Pikine Thiaroye-Gare',
   'WOLOF','MUSULMANE','MARIEE','PRIMAIRE',
   false,'AMIE_PARENT',
   false,NULL,false,NULL,
   'NOUVELLE',NULL,NULL,
   '2026-04-05','PV-2026-006',
   'aa000001-0000-0000-0000-000000000001','bb000002-0000-0000-0000-000000000002',NOW(),NOW()),

-- 28 ACTIVE 2024 — PVVIH stade II sous ARV
  ('cc000028-0000-0000-0000-000000000028','Kadiatou','Diallo',38,'1986-08-13','+221 77 200 0013','Commerçante','Pikine Guinaw Rail Nord',
   'PEULH','MUSULMANE','MARIEE','PRIMAIRE',
   false,'AGENT_DE_SANTE',
   true,'STADE_II',true,'AZT+3TC+NVP',
   'ACTIVE',NULL,NULL,
   '2024-12-01','PV-2024-015',
   'aa000001-0000-0000-0000-000000000001','bb000001-0000-0000-0000-000000000001',NOW(),NOW()),

-- ── PS3 : CS Saint-Louis (11 patientes) ───────────────────────────────────

-- 29 ACTIVE 2024
  ('cc000029-0000-0000-0000-000000000029','Ndéye Fatou','Ndiaye',32,'1992-05-29','+221 77 300 0001','Institutrice','Saint-Louis Sor',
   'WOLOF','MUSULMANE','MARIEE','SUPERIEUR',
   false,'AGENT_DE_SANTE',
   false,NULL,false,NULL,
   'ACTIVE',NULL,NULL,
   '2024-01-20','PV-2024-016',
   'aa000002-0000-0000-0000-000000000002','bb000003-0000-0000-0000-000000000003',NOW(),NOW()),

-- 30 ACTIVE 2025 — PVVIH stade I sous ARV
  ('cc000030-0000-0000-0000-000000000030','Marième','Sy',27,'1999-02-14','+221 77 300 0002','Ménagère','Saint-Louis Guet-Ndar',
   'TOUCOULEUR','MUSULMANE','MARIEE','PRIMAIRE',
   false,'AGENT_DE_SANTE',
   true,'STADE_I',true,'TDF+3TC+DTG',
   'ACTIVE',NULL,NULL,
   '2025-03-15','PV-2025-010',
   'aa000002-0000-0000-0000-000000000002','bb000003-0000-0000-0000-000000000003',NOW(),NOW()),

-- 31 ARRETEE 2025 — motif : Effets secondaires
  ('cc000031-0000-0000-0000-000000000031','Aminata','Kane',35,'1990-11-08','+221 77 300 0003','Commerçante','Saint-Louis Léona',
   'WOLOF','MUSULMANE','MARIEE','SECONDAIRE',
   false,'AMIE_PARENT',
   false,NULL,false,NULL,
   'ARRETEE','Effets secondaires','2026-01-15',
   '2025-07-01','PV-2025-011',
   'aa000002-0000-0000-0000-000000000002','bb000003-0000-0000-0000-000000000003',NOW(),NOW()),

-- 32 ACTIVE 2024
  ('cc000032-0000-0000-0000-000000000032','Fatoumata','Sow',29,'1995-09-17','+221 77 300 0004','Vendeuse','Saint-Louis Hydrobase',
   'PEULH','MUSULMANE','MARIEE','NON_SCOLARISEE',
   false,'AMIE_PARENT',
   false,NULL,false,NULL,
   'ACTIVE',NULL,NULL,
   '2024-04-18','PV-2024-017',
   'aa000002-0000-0000-0000-000000000002','bb000003-0000-0000-0000-000000000003',NOW(),NOW()),

-- 33 NOUVELLE 2026
  ('cc000033-0000-0000-0000-000000000033','Coumba','Diaw',20,'2006-01-23','+221 77 300 0005','Étudiante','Saint-Louis Pikine',
   'SERERE','CHRETIENNE','CELIBATAIRE','SECONDAIRE',
   false,'TELEVISION',
   false,NULL,false,NULL,
   'NOUVELLE',NULL,NULL,
   '2026-05-10','PV-2026-007',
   'aa000002-0000-0000-0000-000000000002','bb000003-0000-0000-0000-000000000003',NOW(),NOW()),

-- 34 ABANDON 2024
  ('cc000034-0000-0000-0000-000000000034','Awa','Fall',43,'1981-04-06','+221 77 300 0006','Commerçante','Saint-Louis Bouetave',
   'WOLOF','MUSULMANE','VEUVE','PRIMAIRE',
   false,'AMIE_PARENT',
   false,NULL,false,NULL,
   'ABANDON',NULL,NULL,
   '2024-05-25','PV-2024-018',
   'aa000002-0000-0000-0000-000000000002','bb000003-0000-0000-0000-000000000003',NOW(),NOW()),

-- 35 ACTIVE 2025
  ('cc000035-0000-0000-0000-000000000035','Rokhaya','Ba',26,'2000-07-31','+221 77 300 0007','Sage-femme auxiliaire','Saint-Louis Nord',
   'PEULH','MUSULMANE','MARIEE','SUPERIEUR',
   false,'AGENT_DE_SANTE',
   false,NULL,false,NULL,
   'ACTIVE',NULL,NULL,
   '2025-09-08','PV-2025-012',
   'aa000002-0000-0000-0000-000000000002','bb000003-0000-0000-0000-000000000003',NOW(),NOW()),

-- 36 ACTIVE 2024 — PVVIH stade II sous ARV
  ('cc000036-0000-0000-0000-000000000036','Aïda','Thiam',33,'1991-12-18','+221 77 300 0008','Infirmière','Saint-Louis Etreky',
   'PEULH','MUSULMANE','MARIEE','SUPERIEUR',
   false,'AGENT_DE_SANTE',
   true,'STADE_II',true,'TDF+3TC+EFV',
   'ACTIVE',NULL,NULL,
   '2024-07-30','PV-2024-019',
   'aa000002-0000-0000-0000-000000000002','bb000003-0000-0000-0000-000000000003',NOW(),NOW()),

-- 37 ARRETEE 2024 — motif : Déménagement
  ('cc000037-0000-0000-0000-000000000037','Maguette','Ndiaye',28,'1998-05-25','+221 77 300 0009','Couturière','Saint-Louis Ndar Toute',
   'WOLOF','MUSULMANE','MARIEE','PRIMAIRE',
   false,'AMIE_PARENT',
   false,NULL,false,NULL,
   'ARRETEE','Déménagement','2025-09-15',
   '2024-09-15','PV-2024-020',
   'aa000002-0000-0000-0000-000000000002','bb000003-0000-0000-0000-000000000003',NOW(),NOW()),

-- 38 ACTIVE 2025
  ('cc000038-0000-0000-0000-000000000038','Dieynaba','Cisse',36,'1989-08-11','+221 77 300 0010','Ménagère','Saint-Louis Khor',
   'PEULH','MUSULMANE','MARIEE','NON_SCOLARISEE',
   false,'AMIE_PARENT',
   false,NULL,false,NULL,
   'ACTIVE',NULL,NULL,
   '2025-11-02','PV-2025-013',
   'aa000002-0000-0000-0000-000000000002','bb000003-0000-0000-0000-000000000003',NOW(),NOW()),

-- 39 ACTIVE 2025
  ('cc000039-0000-0000-0000-000000000039','Fatou','Mbaye',24,'2001-03-28','+221 77 300 0011','Vendeuse','Saint-Louis Île',
   'SERERE','CHRETIENNE','MARIEE','SECONDAIRE',
   false,'AGENT_DE_SANTE',
   false,NULL,false,NULL,
   'ACTIVE',NULL,NULL,
   '2025-12-10','PV-2025-014',
   'aa000002-0000-0000-0000-000000000002','bb000003-0000-0000-0000-000000000003',NOW(),NOW()),

-- ── PS4 : CS Thiès (11 patientes) ─────────────────────────────────────────

-- 40 ACTIVE 2024
  ('cc000040-0000-0000-0000-000000000040','Ndéye','Diouf',30,'1994-06-02','+221 77 400 0001','Ménagère','Thiès Médina Fall',
   'WOLOF','MUSULMANE','MARIEE','PRIMAIRE',
   false,'AMIE_PARENT',
   false,NULL,false,NULL,
   'ACTIVE',NULL,NULL,
   '2024-03-01','PV-2024-021',
   'aa000003-0000-0000-0000-000000000003','bb000004-0000-0000-0000-000000000004',NOW(),NOW()),

-- 41 ACTIVE 2025 — PVVIH stade I sous ARV
  ('cc000041-0000-0000-0000-000000000041','Hélène','Faye',28,'1998-10-19','+221 77 400 0002','Institutrice','Thiès Randoulène',
   'SERERE','CHRETIENNE','MARIEE','SUPERIEUR',
   false,'AGENT_DE_SANTE',
   true,'STADE_I',true,'TDF+3TC+DTG',
   'ACTIVE',NULL,NULL,
   '2025-04-12','PV-2025-015',
   'aa000003-0000-0000-0000-000000000003','bb000005-0000-0000-0000-000000000005',NOW(),NOW()),

-- 42 NOUVELLE 2026
  ('cc000042-0000-0000-0000-000000000042','Binta','Badji',22,'2004-01-16','+221 77 400 0003','Étudiante','Thiès Diakhao',
   'DIOLA','CHRETIENNE','CELIBATAIRE','SECONDAIRE',
   false,'TELEVISION',
   false,NULL,false,NULL,
   'NOUVELLE',NULL,NULL,
   '2026-01-25','PV-2026-008',
   'aa000003-0000-0000-0000-000000000003','bb000004-0000-0000-0000-000000000004',NOW(),NOW()),

-- 43 ARRETEE 2024 — motif : Désir de grossesse
  ('cc000043-0000-0000-0000-000000000043','Angèle','Diatta',37,'1987-07-22','+221 77 400 0004','Agricultrice','Thiès Mbour Route',
   'DIOLA','CHRETIENNE','MARIEE','NON_SCOLARISEE',
   false,'AMIE_PARENT',
   false,NULL,false,NULL,
   'ARRETEE','Désir de grossesse','2025-06-10',
   '2024-06-10','PV-2024-022',
   'aa000003-0000-0000-0000-000000000003','bb000005-0000-0000-0000-000000000005',NOW(),NOW()),

-- 44 ABANDON 2025
  ('cc000044-0000-0000-0000-000000000044','Mariama','Diallo',25,'2000-11-07','+221 77 400 0005','Couturière','Thiès Cité Lamy',
   'PEULH','MUSULMANE','MARIEE','PRIMAIRE',
   false,'AMIE_PARENT',
   false,NULL,false,NULL,
   'ABANDON',NULL,NULL,
   '2025-02-20','PV-2025-016',
   'aa000003-0000-0000-0000-000000000003','bb000004-0000-0000-0000-000000000004',NOW(),NOW()),

-- 45 ACTIVE 2024
  ('cc000045-0000-0000-0000-000000000045','Ngoné','Sarr',39,'1985-04-14','+221 77 400 0006','Commerçante','Thiès Nord',
   'SERERE','CHRETIENNE','MARIEE','SECONDAIRE',
   false,'AMIE_PARENT',
   false,NULL,false,NULL,
   'ACTIVE',NULL,NULL,
   '2024-08-05','PV-2024-023',
   'aa000003-0000-0000-0000-000000000003','bb000005-0000-0000-0000-000000000005',NOW(),NOW()),

-- 46 ACTIVE 2025 — PVVIH stade II sous ARV
  ('cc000046-0000-0000-0000-000000000046','Khady','Diop',34,'1992-02-09','+221 77 400 0007','Ménagère','Thiès Keur Blondin',
   'WOLOF','MUSULMANE','MARIEE','NON_SCOLARISEE',
   false,'AGENT_DE_SANTE',
   true,'STADE_II',true,'AZT+3TC+NVP',
   'ACTIVE',NULL,NULL,
   '2025-05-30','PV-2025-017',
   'aa000003-0000-0000-0000-000000000003','bb000004-0000-0000-0000-000000000004',NOW(),NOW()),

-- 47 ACTIVE 2024
  ('cc000047-0000-0000-0000-000000000047','Fatoumata','Sy',31,'1993-08-28','+221 77 400 0008','Commerçante','Thiès Escale',
   'TOUCOULEUR','MUSULMANE','MARIEE','SECONDAIRE',
   false,'AGENT_DE_SANTE',
   false,NULL,false,NULL,
   'ACTIVE',NULL,NULL,
   '2024-10-15','PV-2024-024',
   'aa000003-0000-0000-0000-000000000003','bb000005-0000-0000-0000-000000000005',NOW(),NOW()),

-- 48 ABANDON 2025
  ('cc000048-0000-0000-0000-000000000048','Safiatou','Camara',26,'2000-05-03','+221 77 400 0009','Vendeuse','Thiès Ballabey',
   'MANDINGUE','MUSULMANE','MARIEE','PRIMAIRE',
   false,'AMIE_PARENT',
   false,NULL,false,NULL,
   'ABANDON',NULL,NULL,
   '2025-07-08','PV-2025-018',
   'aa000003-0000-0000-0000-000000000003','bb000004-0000-0000-0000-000000000004',NOW(),NOW()),

-- 49 NOUVELLE 2026
  ('cc000049-0000-0000-0000-000000000049','Ndella','Wade',23,'2003-09-12','+221 77 400 0010','Étudiante','Thiès Fass',
   'WOLOF','MUSULMANE','CELIBATAIRE','SUPERIEUR',
   false,'TELEVISION',
   false,NULL,false,NULL,
   'NOUVELLE',NULL,NULL,
   '2026-03-01','PV-2026-009',
   'aa000003-0000-0000-0000-000000000003','bb000005-0000-0000-0000-000000000005',NOW(),NOW()),

-- 50 NOUVELLE 2026
  ('cc000050-0000-0000-0000-000000000050','Dienaba','Seck',29,'1997-12-31','+221 77 400 0011','Vendeuse','Thiès Mbour',
   'WOLOF','MUSULMANE','MARIEE','PRIMAIRE',
   false,'AGENT_DE_SANTE',
   false,NULL,false,NULL,
   'NOUVELLE',NULL,NULL,
   '2026-05-22','PV-2026-010',
   'aa000003-0000-0000-0000-000000000003','bb000004-0000-0000-0000-000000000004',NOW(),NOW());

-- ---------------------------------------------------------------------------
-- 5. GENRE DE VIE (quelques patientes)
-- ---------------------------------------------------------------------------
INSERT INTO patientes_genre_de_vie (patiente_id, genre_de_vie) VALUES
  ('cc000004-0000-0000-0000-000000000004', 'TABAC'),
  ('cc000009-0000-0000-0000-000000000009', 'TABAC'),
  ('cc000009-0000-0000-0000-000000000009', 'ALCOOL'),
  ('cc000020-0000-0000-0000-000000000020', 'TABAC'),
  ('cc000028-0000-0000-0000-000000000028', 'TABAC'),
  ('cc000036-0000-0000-0000-000000000036', 'ALCOOL');

-- ---------------------------------------------------------------------------
-- 6. FICHES DE CONSULTATION (1 par patiente)
-- Colonnes minimales : id, numero, date_consultation, patiente_id,
--   sage_femme_id, poste_sante_id,
--   menarches_age, enfants_vivants, enfants_decedes, avortements,
--   taille_cm, poids_kg, hypertension_arterielle, diabete, drepanocytose,
--   allergie_cuivre, methode_choisie, created_at, updated_at
-- ---------------------------------------------------------------------------
INSERT INTO fiches_consultation
  (id, numero, date_consultation, patiente_id, sage_femme_id, poste_sante_id,
   menarches_age, enfants_vivants, enfants_decedes, avortements,
   taille_cm, poids_kg, hypertension_arterielle, diabete, drepanocytose, allergie_cuivre,
   methode_choisie, created_at, updated_at)
VALUES
-- PS1
  ('dd000001-0000-0000-0000-000000000001','FC-2024-001','2024-03-15','cc000001-0000-0000-0000-000000000001','8a837310-9f1f-495d-a8a7-345e340a78df','5fa2b02c-0cdf-4a43-87b6-bb516e92bb0e', 13,2,0,0, 162,62, false,false,false,false, 'PILULE_PP',NOW(),NOW()),
  ('dd000002-0000-0000-0000-000000000002','FC-2024-002','2024-06-20','cc000002-0000-0000-0000-000000000002','9145fff3-b9a2-4d5f-adfc-09607d74ab5c','5fa2b02c-0cdf-4a43-87b6-bb516e92bb0e', 14,3,0,0, 158,70, false,false,false,false, 'INJECTABLE_IM',NOW(),NOW()),
  ('dd000003-0000-0000-0000-000000000003','FC-2024-003','2024-01-10','cc000003-0000-0000-0000-000000000003','8a837310-9f1f-495d-a8a7-345e340a78df','5fa2b02c-0cdf-4a43-87b6-bb516e92bb0e', 13,1,0,0, 165,58, false,false,false,false, 'DIU_TCU_380A',NOW(),NOW()),
  ('dd000004-0000-0000-0000-000000000004','FC-2024-004','2024-04-05','cc000004-0000-0000-0000-000000000004','6bad952a-6635-4ebe-b0f2-7d4523d14c7e','5fa2b02c-0cdf-4a43-87b6-bb516e92bb0e', 14,2,1,0, 160,55, false,false,false,false, 'IMPLANT_1_CAPSULE',NOW(),NOW()),
  ('dd000005-0000-0000-0000-000000000005','FC-2024-005','2024-07-12','cc000005-0000-0000-0000-000000000005','6ac0bc98-77c7-4105-92dd-6202d6f898ca','5fa2b02c-0cdf-4a43-87b6-bb516e92bb0e', 12,3,0,1, 167,72, true,false,false,false, 'INJECTABLE_IM',NOW(),NOW()),
  ('dd000006-0000-0000-0000-000000000006','FC-2026-003','2026-01-08','cc000006-0000-0000-0000-000000000006','8a837310-9f1f-495d-a8a7-345e340a78df','5fa2b02c-0cdf-4a43-87b6-bb516e92bb0e', 14,0,0,0, 163,54, false,false,false,false, 'PILULE_POP',NOW(),NOW()),
  ('dd000007-0000-0000-0000-000000000007','FC-2025-001','2025-02-14','cc000007-0000-0000-0000-000000000007','9145fff3-b9a2-4d5f-adfc-09607d74ab5c','5fa2b02c-0cdf-4a43-87b6-bb516e92bb0e', 13,4,0,0, 161,78, false,true,false,false, 'DIU_TCU_380A',NOW(),NOW()),
  ('dd000008-0000-0000-0000-000000000008','FC-2025-002','2025-03-01','cc000008-0000-0000-0000-000000000008','6bad952a-6635-4ebe-b0f2-7d4523d14c7e','5fa2b02c-0cdf-4a43-87b6-bb516e92bb0e', 14,1,0,0, 159,60, false,false,false,false, 'PILULE_PP',NOW(),NOW()),
  ('dd000009-0000-0000-0000-000000000009','FC-2024-006','2024-09-20','cc000009-0000-0000-0000-000000000009','6ac0bc98-77c7-4105-92dd-6202d6f898ca','5fa2b02c-0cdf-4a43-87b6-bb516e92bb0e', 13,5,0,1, 164,68, true,false,false,false, 'IMPLANT_1_CAPSULE',NOW(),NOW()),
  ('dd000010-0000-0000-0000-000000000010','FC-2024-007','2024-11-05','cc000010-0000-0000-0000-000000000010','8a837310-9f1f-495d-a8a7-345e340a78df','5fa2b02c-0cdf-4a43-87b6-bb516e92bb0e', 14,0,0,0, 166,57, false,false,false,false, 'INJECTABLE_IM',NOW(),NOW()),
  ('dd000011-0000-0000-0000-000000000011','FC-2025-003','2025-04-10','cc000011-0000-0000-0000-000000000011','9145fff3-b9a2-4d5f-adfc-09607d74ab5c','5fa2b02c-0cdf-4a43-87b6-bb516e92bb0e', 13,2,0,0, 160,65, false,false,false,false, 'IMPLANT_2_CAPSULES',NOW(),NOW()),
  ('dd000012-0000-0000-0000-000000000012','FC-2026-004','2026-03-20','cc000012-0000-0000-0000-000000000012','6bad952a-6635-4ebe-b0f2-7d4523d14c7e','5fa2b02c-0cdf-4a43-87b6-bb516e92bb0e', 14,1,0,0, 162,59, false,false,false,false, 'INJECTABLE_IM',NOW(),NOW()),
  ('dd000013-0000-0000-0000-000000000013','FC-2024-008','2024-02-28','cc000013-0000-0000-0000-000000000013','6ac0bc98-77c7-4105-92dd-6202d6f898ca','5fa2b02c-0cdf-4a43-87b6-bb516e92bb0e', 13,3,1,0, 158,72, false,false,true,false, 'PILULE_PP',NOW(),NOW()),
  ('dd000014-0000-0000-0000-000000000014','FC-2025-004','2025-05-18','cc000014-0000-0000-0000-000000000014','8a837310-9f1f-495d-a8a7-345e340a78df','5fa2b02c-0cdf-4a43-87b6-bb516e92bb0e', 14,1,0,0, 163,56, false,false,false,false, 'DIU_TCU_380A',NOW(),NOW()),
  ('dd000015-0000-0000-0000-000000000015','FC-2024-009','2024-06-01','cc000015-0000-0000-0000-000000000015','9145fff3-b9a2-4d5f-adfc-09607d74ab5c','5fa2b02c-0cdf-4a43-87b6-bb516e92bb0e', 12,6,0,2, 157,80, true,true,false,false, 'PRESERVATIF_MASCULIN',NOW(),NOW()),
-- PS2
  ('dd000016-0000-0000-0000-000000000016','FC-2024-010','2024-02-10','cc000016-0000-0000-0000-000000000016','bb000001-0000-0000-0000-000000000001','aa000001-0000-0000-0000-000000000001', 13,2,0,0, 162,61, false,false,false,false, 'PILULE_PP',NOW(),NOW()),
  ('dd000017-0000-0000-0000-000000000017','FC-2024-011','2024-05-22','cc000017-0000-0000-0000-000000000017','bb000002-0000-0000-0000-000000000002','aa000001-0000-0000-0000-000000000001', 13,3,0,0, 160,75, false,false,false,false, 'INJECTABLE_IM',NOW(),NOW()),
  ('dd000018-0000-0000-0000-000000000018','FC-2026-005','2026-02-15','cc000018-0000-0000-0000-000000000018','bb000001-0000-0000-0000-000000000001','aa000001-0000-0000-0000-000000000001', 14,0,0,0, 164,52, false,false,false,false, 'IMPLANT_1_CAPSULE',NOW(),NOW()),
  ('dd000019-0000-0000-0000-000000000019','FC-2025-005','2025-01-20','cc000019-0000-0000-0000-000000000019','bb000002-0000-0000-0000-000000000002','aa000001-0000-0000-0000-000000000001', 13,2,0,0, 167,63, false,false,false,false, 'DIU_TCU_380A',NOW(),NOW()),
  ('dd000020-0000-0000-0000-000000000020','FC-2024-012','2024-08-15','cc000020-0000-0000-0000-000000000020','bb000001-0000-0000-0000-000000000001','aa000001-0000-0000-0000-000000000001', 13,2,0,0, 161,58, false,false,false,false, 'INJECTABLE_IM',NOW(),NOW()),
  ('dd000021-0000-0000-0000-000000000021','FC-2025-006','2025-03-05','cc000021-0000-0000-0000-000000000021','bb000002-0000-0000-0000-000000000002','aa000001-0000-0000-0000-000000000001', 14,1,0,0, 166,64, false,false,false,false, 'IMPLANT_1_CAPSULE',NOW(),NOW()),
  ('dd000022-0000-0000-0000-000000000022','FC-2025-007','2025-06-10','cc000022-0000-0000-0000-000000000022','bb000001-0000-0000-0000-000000000001','aa000001-0000-0000-0000-000000000001', 13,1,0,0, 160,59, false,false,false,false, 'PILULE_PP',NOW(),NOW()),
  ('dd000023-0000-0000-0000-000000000023','FC-2024-013','2024-03-12','cc000023-0000-0000-0000-000000000023','bb000002-0000-0000-0000-000000000002','aa000001-0000-0000-0000-000000000001', 13,4,0,1, 158,77, true,false,false,false, 'IMPLANT_2_CAPSULES',NOW(),NOW()),
  ('dd000024-0000-0000-0000-000000000024','FC-2024-014','2024-10-01','cc000024-0000-0000-0000-000000000024','bb000001-0000-0000-0000-000000000001','aa000001-0000-0000-0000-000000000001', 14,1,0,0, 163,55, false,false,false,false, 'DIU_TCU_380A',NOW(),NOW()),
  ('dd000025-0000-0000-0000-000000000025','FC-2025-008','2025-08-20','cc000025-0000-0000-0000-000000000025','bb000002-0000-0000-0000-000000000002','aa000001-0000-0000-0000-000000000001', 13,2,0,0, 168,66, false,false,false,false, 'INJECTABLE_IM',NOW(),NOW()),
  ('dd000026-0000-0000-0000-000000000026','FC-2025-009','2025-02-28','cc000026-0000-0000-0000-000000000026','bb000001-0000-0000-0000-000000000001','aa000001-0000-0000-0000-000000000001', 14,0,0,0, 165,53, false,false,false,false, 'PILULE_POP',NOW(),NOW()),
  ('dd000027-0000-0000-0000-000000000027','FC-2026-006','2026-04-05','cc000027-0000-0000-0000-000000000027','bb000002-0000-0000-0000-000000000002','aa000001-0000-0000-0000-000000000001', 13,2,0,0, 161,67, false,false,false,false, 'DIU_TCU_380A',NOW(),NOW()),
  ('dd000028-0000-0000-0000-000000000028','FC-2024-015','2024-12-01','cc000028-0000-0000-0000-000000000028','bb000001-0000-0000-0000-000000000001','aa000001-0000-0000-0000-000000000001', 13,3,0,0, 159,71, true,false,false,false, 'INJECTABLE_IM',NOW(),NOW()),
-- PS3
  ('dd000029-0000-0000-0000-000000000029','FC-2024-016','2024-01-20','cc000029-0000-0000-0000-000000000029','bb000003-0000-0000-0000-000000000003','aa000002-0000-0000-0000-000000000002', 13,2,0,0, 164,60, false,false,false,false, 'PILULE_PP',NOW(),NOW()),
  ('dd000030-0000-0000-0000-000000000030','FC-2025-010','2025-03-15','cc000030-0000-0000-0000-000000000030','bb000003-0000-0000-0000-000000000003','aa000002-0000-0000-0000-000000000002', 14,1,0,0, 162,57, false,false,false,false, 'INJECTABLE_IM',NOW(),NOW()),
  ('dd000031-0000-0000-0000-000000000031','FC-2025-011','2025-07-01','cc000031-0000-0000-0000-000000000031','bb000003-0000-0000-0000-000000000003','aa000002-0000-0000-0000-000000000002', 13,3,0,0, 161,69, false,false,false,false, 'DIU_TCU_380A',NOW(),NOW()),
  ('dd000032-0000-0000-0000-000000000032','FC-2024-017','2024-04-18','cc000032-0000-0000-0000-000000000032','bb000003-0000-0000-0000-000000000003','aa000002-0000-0000-0000-000000000002', 13,1,0,0, 163,56, false,false,false,false, 'IMPLANT_1_CAPSULE',NOW(),NOW()),
  ('dd000033-0000-0000-0000-000000000033','FC-2026-007','2026-05-10','cc000033-0000-0000-0000-000000000033','bb000003-0000-0000-0000-000000000003','aa000002-0000-0000-0000-000000000002', 14,0,0,0, 165,51, false,false,false,false, 'PILULE_POP',NOW(),NOW()),
  ('dd000034-0000-0000-0000-000000000034','FC-2024-018','2024-05-25','cc000034-0000-0000-0000-000000000034','bb000003-0000-0000-0000-000000000003','aa000002-0000-0000-0000-000000000002', 12,5,0,2, 157,82, true,true,false,false, 'INJECTABLE_IM',NOW(),NOW()),
  ('dd000035-0000-0000-0000-000000000035','FC-2025-012','2025-09-08','cc000035-0000-0000-0000-000000000035','bb000003-0000-0000-0000-000000000003','aa000002-0000-0000-0000-000000000002', 14,1,0,0, 166,58, false,false,false,false, 'DIU_TCU_380A',NOW(),NOW()),
  ('dd000036-0000-0000-0000-000000000036','FC-2024-019','2024-07-30','cc000036-0000-0000-0000-000000000036','bb000003-0000-0000-0000-000000000003','aa000002-0000-0000-0000-000000000002', 13,3,0,0, 162,63, false,false,false,false, 'IMPLANT_1_CAPSULE',NOW(),NOW()),
  ('dd000037-0000-0000-0000-000000000037','FC-2024-020','2024-09-15','cc000037-0000-0000-0000-000000000037','bb000003-0000-0000-0000-000000000003','aa000002-0000-0000-0000-000000000002', 14,2,0,0, 160,62, false,false,false,false, 'PILULE_PP',NOW(),NOW()),
  ('dd000038-0000-0000-0000-000000000038','FC-2025-013','2025-11-02','cc000038-0000-0000-0000-000000000038','bb000003-0000-0000-0000-000000000003','aa000002-0000-0000-0000-000000000002', 13,4,0,1, 158,74, false,false,false,false, 'IMPLANT_2_CAPSULES',NOW(),NOW()),
  ('dd000039-0000-0000-0000-000000000039','FC-2025-014','2025-12-10','cc000039-0000-0000-0000-000000000039','bb000003-0000-0000-0000-000000000003','aa000002-0000-0000-0000-000000000002', 14,0,0,0, 164,53, false,false,false,false, 'INJECTABLE_IM',NOW(),NOW()),
-- PS4
  ('dd000040-0000-0000-0000-000000000040','FC-2024-021','2024-03-01','cc000040-0000-0000-0000-000000000040','bb000004-0000-0000-0000-000000000004','aa000003-0000-0000-0000-000000000003', 13,2,0,0, 162,64, false,false,false,false, 'PILULE_PP',NOW(),NOW()),
  ('dd000041-0000-0000-0000-000000000041','FC-2025-015','2025-04-12','cc000041-0000-0000-0000-000000000041','bb000005-0000-0000-0000-000000000005','aa000003-0000-0000-0000-000000000003', 14,1,0,0, 165,60, false,false,false,false, 'IMPLANT_1_CAPSULE',NOW(),NOW()),
  ('dd000042-0000-0000-0000-000000000042','FC-2026-008','2026-01-25','cc000042-0000-0000-0000-000000000042','bb000004-0000-0000-0000-000000000004','aa000003-0000-0000-0000-000000000003', 14,0,0,0, 163,52, false,false,false,false, 'DIU_TCU_380A',NOW(),NOW()),
  ('dd000043-0000-0000-0000-000000000043','FC-2024-022','2024-06-10','cc000043-0000-0000-0000-000000000043','bb000005-0000-0000-0000-000000000005','aa000003-0000-0000-0000-000000000003', 13,3,0,1, 159,70, false,false,false,false, 'INJECTABLE_IM',NOW(),NOW()),
  ('dd000044-0000-0000-0000-000000000044','FC-2025-016','2025-02-20','cc000044-0000-0000-0000-000000000044','bb000004-0000-0000-0000-000000000004','aa000003-0000-0000-0000-000000000003', 14,1,0,0, 161,58, false,false,false,false, 'PILULE_PP',NOW(),NOW()),
  ('dd000045-0000-0000-0000-000000000045','FC-2024-023','2024-08-05','cc000045-0000-0000-0000-000000000045','bb000005-0000-0000-0000-000000000005','aa000003-0000-0000-0000-000000000003', 12,4,1,0, 158,76, true,false,false,false, 'DIU_TCU_380A',NOW(),NOW()),
  ('dd000046-0000-0000-0000-000000000046','FC-2025-017','2025-05-30','cc000046-0000-0000-0000-000000000046','bb000004-0000-0000-0000-000000000004','aa000003-0000-0000-0000-000000000003', 13,3,0,0, 160,68, true,false,false,false, 'INJECTABLE_IM',NOW(),NOW()),
  ('dd000047-0000-0000-0000-000000000047','FC-2024-024','2024-10-15','cc000047-0000-0000-0000-000000000047','bb000005-0000-0000-0000-000000000005','aa000003-0000-0000-0000-000000000003', 14,2,0,0, 162,61, false,false,false,false, 'IMPLANT_1_CAPSULE',NOW(),NOW()),
  ('dd000048-0000-0000-0000-000000000048','FC-2025-018','2025-07-08','cc000048-0000-0000-0000-000000000048','bb000004-0000-0000-0000-000000000004','aa000003-0000-0000-0000-000000000003', 14,1,0,0, 164,55, false,false,false,false, 'IMPLANT_2_CAPSULES',NOW(),NOW()),
  ('dd000049-0000-0000-0000-000000000049','FC-2026-009','2026-03-01','cc000049-0000-0000-0000-000000000049','bb000005-0000-0000-0000-000000000005','aa000003-0000-0000-0000-000000000003', 14,0,0,0, 166,54, false,false,false,false, 'PILULE_POP',NOW(),NOW()),
  ('dd000050-0000-0000-0000-000000000050','FC-2026-010','2026-05-22','cc000050-0000-0000-0000-000000000050','bb000004-0000-0000-0000-000000000004','aa000003-0000-0000-0000-000000000003', 13,2,0,0, 161,63, false,false,false,false, 'INJECTABLE_IM',NOW(),NOW());

-- ---------------------------------------------------------------------------
-- 7. VISITES
-- ACTIVE patients : 2-3 visites | ARRETEE/ABANDON : 1-2 | NOUVELLE : 0-1
-- Colonnes : id, date_visite, poids_kg, tension_arterielle,
--   observations, unites_fournies, date_prochain_rdv,
--   patiente_id, fiche_consultation_id, sage_femme_id, statut, created_at, updated_at
-- ---------------------------------------------------------------------------
INSERT INTO visites
  (id, date_visite, poids_kg, tension_arterielle, observations, unites_fournies,
   date_prochain_rdv, patiente_id, fiche_consultation_id, sage_femme_id,
   statut, created_at, updated_at)
VALUES

-- Patiente 01 (ACTIVE, pilule combinée) — 3 visites
  ('ee000001-0000-0000-0000-000000000001','2024-06-15',62,'110/70','Bonne tolérance',                    '3 plaquettes','2024-09-15','cc000001-0000-0000-0000-000000000001','dd000001-0000-0000-0000-000000000001','8a837310-9f1f-495d-a8a7-345e340a78df','VALIDEE',NOW(),NOW()),
  ('ee000002-0000-0000-0000-000000000002','2024-09-18',61,'115/72','Pas de plaintes particulières',       '3 plaquettes','2024-12-18','cc000001-0000-0000-0000-000000000001','dd000001-0000-0000-0000-000000000001','8a837310-9f1f-495d-a8a7-345e340a78df','VALIDEE',NOW(),NOW()),
  ('ee000003-0000-0000-0000-000000000003','2024-12-20',63,'112/70','Suivi régulier, bonne observance',    '3 plaquettes','2025-03-20','cc000001-0000-0000-0000-000000000001','dd000001-0000-0000-0000-000000000001','8a837310-9f1f-495d-a8a7-345e340a78df','VALIDEE',NOW(),NOW()),

-- Patiente 02 (ACTIVE, injectable DMPA) — 2 visites
  ('ee000004-0000-0000-0000-000000000004','2024-09-20',70,'118/75','Injection administrée, pas d''effets',NULL,'2024-12-20','cc000002-0000-0000-0000-000000000002','dd000002-0000-0000-0000-000000000002','9145fff3-b9a2-4d5f-adfc-09607d74ab5c','VALIDEE',NOW(),NOW()),
  ('ee000005-0000-0000-0000-000000000005','2024-12-22',71,'120/78','Rappel injection effectué',            NULL,'2025-03-22','cc000002-0000-0000-0000-000000000002','dd000002-0000-0000-0000-000000000002','9145fff3-b9a2-4d5f-adfc-09607d74ab5c','VALIDEE',NOW(),NOW()),

-- Patiente 03 (ARRETEE, DIU) — 2 visites avant arrêt
  ('ee000006-0000-0000-0000-000000000006','2024-04-10',58,'108/65','DIU bien positionné, pas de douleur',NULL,'2024-07-10','cc000003-0000-0000-0000-000000000003','dd000003-0000-0000-0000-000000000003','8a837310-9f1f-495d-a8a7-345e340a78df','VALIDEE',NOW(),NOW()),
  ('ee000007-0000-0000-0000-000000000007','2024-07-15',57,'110/68','Exprime désir de grossesse prochaine',NULL,'2025-01-15','cc000003-0000-0000-0000-000000000003','dd000003-0000-0000-0000-000000000003','8a837310-9f1f-495d-a8a7-345e340a78df','VALIDEE',NOW(),NOW()),

-- Patiente 04 (ABANDON, implant, PVVIH) — 1 visite
  ('ee000008-0000-0000-0000-000000000008','2024-07-05',55,'105/65','Implant bien toléré, suivi ARV OK',   NULL,'2024-10-05','cc000004-0000-0000-0000-000000000004','dd000004-0000-0000-0000-000000000004','6bad952a-6635-4ebe-b0f2-7d4523d14c7e','VALIDEE',NOW(),NOW()),

-- Patiente 05 (ACTIVE, injectable DMPA, PVVIH) — 3 visites
  ('ee000009-0000-0000-0000-000000000009','2024-10-12',72,'125/80','Injection DMPA + suivi VIH régulier',  NULL,'2025-01-12','cc000005-0000-0000-0000-000000000005','dd000005-0000-0000-0000-000000000005','6ac0bc98-77c7-4105-92dd-6202d6f898ca','VALIDEE',NOW(),NOW()),
  ('ee000010-0000-0000-0000-000000000010','2025-01-14',71,'122/78','CD4 stable, rappel injection effectué',NULL,'2025-04-14','cc000005-0000-0000-0000-000000000005','dd000005-0000-0000-0000-000000000005','6ac0bc98-77c7-4105-92dd-6202d6f898ca','VALIDEE',NOW(),NOW()),
  ('ee000011-0000-0000-0000-000000000011','2025-04-16',73,'120/76','Bonne observance ARV et contraception',NULL,'2025-07-16','cc000005-0000-0000-0000-000000000005','dd000005-0000-0000-0000-000000000005','6ac0bc98-77c7-4105-92dd-6202d6f898ca','VALIDEE',NOW(),NOW()),

-- Patiente 07 (ACTIVE, DIU) — 2 visites
  ('ee000012-0000-0000-0000-000000000012','2025-05-14',78,'130/85','Contrôle DIU — bien en place',        NULL,'2025-08-14','cc000007-0000-0000-0000-000000000007','dd000007-0000-0000-0000-000000000007','9145fff3-b9a2-4d5f-adfc-09607d74ab5c','VALIDEE',NOW(),NOW()),
  ('ee000013-0000-0000-0000-000000000013','2025-08-16',77,'128/82','Pas de complications',                 NULL,'2025-11-16','cc000007-0000-0000-0000-000000000007','dd000007-0000-0000-0000-000000000007','9145fff3-b9a2-4d5f-adfc-09607d74ab5c','VALIDEE',NOW(),NOW()),

-- Patiente 08 (ARRETEE, pilule) — 1 visite avant arrêt
  ('ee000014-0000-0000-0000-000000000014','2025-06-01',60,'112/70','Nausées légères mentionnées',          '3 plaquettes','2025-09-01','cc000008-0000-0000-0000-000000000008','dd000008-0000-0000-0000-000000000008','6bad952a-6635-4ebe-b0f2-7d4523d14c7e','VALIDEE',NOW(),NOW()),

-- Patiente 09 (ACTIVE, implant, PVVIH) — 3 visites
  ('ee000015-0000-0000-0000-000000000015','2024-12-20',68,'122/76','Implant efficace, suivi VIH intégré',  NULL,'2025-03-20','cc000009-0000-0000-0000-000000000009','dd000009-0000-0000-0000-000000000009','6ac0bc98-77c7-4105-92dd-6202d6f898ca','VALIDEE',NOW(),NOW()),
  ('ee000016-0000-0000-0000-000000000016','2025-03-22',67,'120/74','Charge virale indétectable',           NULL,'2025-06-22','cc000009-0000-0000-0000-000000000009','dd000009-0000-0000-0000-000000000009','6ac0bc98-77c7-4105-92dd-6202d6f898ca','VALIDEE',NOW(),NOW()),
  ('ee000017-0000-0000-0000-000000000017','2025-06-24',68,'118/72','Excellent suivi, renouvellement implant bientôt',NULL,'2025-09-24','cc000009-0000-0000-0000-000000000009','dd000009-0000-0000-0000-000000000009','6ac0bc98-77c7-4105-92dd-6202d6f898ca','VALIDEE',NOW(),NOW()),

-- Patiente 10 (ABANDON, injectable) — 1 visite
  ('ee000018-0000-0000-0000-000000000018','2025-02-05',57,'110/68','Injection DMPA administrée',           NULL,'2025-05-05','cc000010-0000-0000-0000-000000000010','dd000010-0000-0000-0000-000000000010','8a837310-9f1f-495d-a8a7-345e340a78df','VALIDEE',NOW(),NOW()),

-- Patiente 11 (ARRETEE, norplant) — 2 visites avant arrêt
  ('ee000019-0000-0000-0000-000000000019','2025-07-10',65,'115/72','Norplant bien positionné',             NULL,'2025-10-10','cc000011-0000-0000-0000-000000000011','dd000011-0000-0000-0000-000000000011','9145fff3-b9a2-4d5f-adfc-09607d74ab5c','VALIDEE',NOW(),NOW()),
  ('ee000020-0000-0000-0000-000000000020','2025-10-12',66,'118/74','Signale grossesse souhaitée',          NULL,'2026-01-12','cc000011-0000-0000-0000-000000000011','dd000011-0000-0000-0000-000000000011','9145fff3-b9a2-4d5f-adfc-09607d74ab5c','VALIDEE',NOW(),NOW()),

-- Patiente 12 (NOUVELLE, injectable DMPA) — 1 visite initiale
  ('ee000021-0000-0000-0000-000000000021','2026-04-20',59,'110/70','Première injection — bonne tolérance', NULL,'2026-07-20','cc000012-0000-0000-0000-000000000012','dd000012-0000-0000-0000-000000000012','6bad952a-6635-4ebe-b0f2-7d4523d14c7e','VALIDEE',NOW(),NOW()),

-- Patiente 13 (ARRETEE, pilule) — 1 visite
  ('ee000022-0000-0000-0000-000000000022','2024-05-28',72,'125/80','Rappel plaquettes pilule',             '3 plaquettes','2024-08-28','cc000013-0000-0000-0000-000000000013','dd000013-0000-0000-0000-000000000013','6ac0bc98-77c7-4105-92dd-6202d6f898ca','VALIDEE',NOW(),NOW()),

-- Patiente 14 (ACTIVE, DIU) — 1 visite
  ('ee000023-0000-0000-0000-000000000023','2025-08-18',56,'108/65','Contrôle DIU — bien positionné',       NULL,'2025-11-18','cc000014-0000-0000-0000-000000000014','dd000014-0000-0000-0000-000000000014','8a837310-9f1f-495d-a8a7-345e340a78df','VALIDEE',NOW(),NOW()),

-- Patiente 15 (ARRETEE, préservatif) — 2 visites avant arrêt
  ('ee000024-0000-0000-0000-000000000024','2024-09-01',80,'140/90','Tension élevée surveillée, conseils',  NULL,'2024-12-01','cc000015-0000-0000-0000-000000000015','dd000015-0000-0000-0000-000000000015','9145fff3-b9a2-4d5f-adfc-09607d74ab5c','VALIDEE',NOW(),NOW()),
  ('ee000025-0000-0000-0000-000000000025','2024-12-05',81,'138/88','Signes de ménopause, orientation',     NULL,NULL,'cc000015-0000-0000-0000-000000000015','dd000015-0000-0000-0000-000000000015','9145fff3-b9a2-4d5f-adfc-09607d74ab5c','VALIDEE',NOW(),NOW()),

-- Patiente 16 (ACTIVE, pilule, PS2) — 3 visites
  ('ee000026-0000-0000-0000-000000000026','2024-05-10',61,'112/70','Bonne observance',                     '3 plaquettes','2024-08-10','cc000016-0000-0000-0000-000000000016','dd000016-0000-0000-0000-000000000016','bb000001-0000-0000-0000-000000000001','VALIDEE',NOW(),NOW()),
  ('ee000027-0000-0000-0000-000000000027','2024-08-12',62,'110/68','Pas de plaintes',                      '3 plaquettes','2024-11-12','cc000016-0000-0000-0000-000000000016','dd000016-0000-0000-0000-000000000016','bb000001-0000-0000-0000-000000000001','VALIDEE',NOW(),NOW()),
  ('ee000028-0000-0000-0000-000000000028','2024-11-14',61,'114/72','Suivi régulier',                       '3 plaquettes','2025-02-14','cc000016-0000-0000-0000-000000000016','dd000016-0000-0000-0000-000000000016','bb000001-0000-0000-0000-000000000001','VALIDEE',NOW(),NOW()),

-- Patiente 17 (ACTIVE, injectable, PS2) — 2 visites
  ('ee000029-0000-0000-0000-000000000029','2024-08-22',75,'122/78','Injection DMPA — bien tolérée',        NULL,'2024-11-22','cc000017-0000-0000-0000-000000000017','dd000017-0000-0000-0000-000000000017','bb000002-0000-0000-0000-000000000002','VALIDEE',NOW(),NOW()),
  ('ee000030-0000-0000-0000-000000000030','2024-11-24',74,'120/76','Rappel injection',                     NULL,'2025-02-24','cc000017-0000-0000-0000-000000000017','dd000017-0000-0000-0000-000000000017','bb000002-0000-0000-0000-000000000002','VALIDEE',NOW(),NOW()),

-- Patiente 19 (ARRETEE, DIU, PS2) — 1 visite avant arrêt
  ('ee000031-0000-0000-0000-000000000031','2025-04-20',63,'115/72','DIU bien toléré, souhaite enfant',     NULL,'2025-07-20','cc000019-0000-0000-0000-000000000019','dd000019-0000-0000-0000-000000000019','bb000002-0000-0000-0000-000000000002','VALIDEE',NOW(),NOW()),

-- Patiente 20 (ABANDON, injectable, PVVIH, PS2) — 1 visite
  ('ee000032-0000-0000-0000-000000000032','2024-11-15',58,'105/65','ARV stade III, injection administrée', NULL,'2025-02-15','cc000020-0000-0000-0000-000000000020','dd000020-0000-0000-0000-000000000020','bb000001-0000-0000-0000-000000000001','VALIDEE',NOW(),NOW()),

-- Patiente 21 (ACTIVE, implant, PVVIH, PS2) — 2 visites
  ('ee000033-0000-0000-0000-000000000033','2025-06-05',64,'115/72','Implant + ARV, suivi intégré',         NULL,'2025-09-05','cc000021-0000-0000-0000-000000000021','dd000021-0000-0000-0000-000000000021','bb000002-0000-0000-0000-000000000002','VALIDEE',NOW(),NOW()),
  ('ee000034-0000-0000-0000-000000000034','2025-09-07',65,'118/74','CD4 excellent, bonne observance',      NULL,'2025-12-07','cc000021-0000-0000-0000-000000000021','dd000021-0000-0000-0000-000000000021','bb000002-0000-0000-0000-000000000002','VALIDEE',NOW(),NOW()),

-- Patiente 22 (ACTIVE, pilule, PS2) — 1 visite
  ('ee000035-0000-0000-0000-000000000035','2025-09-10',59,'110/68','Pilule bien tolérée',                  '3 plaquettes','2025-12-10','cc000022-0000-0000-0000-000000000022','dd000022-0000-0000-0000-000000000022','bb000001-0000-0000-0000-000000000001','VALIDEE',NOW(),NOW()),

-- Patiente 23 (ARRETEE, norplant, PS2) — 2 visites avant arrêt
  ('ee000036-0000-0000-0000-000000000036','2024-06-12',77,'132/82','Norplant — légère prise de poids',     NULL,'2024-09-12','cc000023-0000-0000-0000-000000000023','dd000023-0000-0000-0000-000000000023','bb000002-0000-0000-0000-000000000002','VALIDEE',NOW(),NOW()),
  ('ee000037-0000-0000-0000-000000000037','2024-09-14',78,'130/80','Prévue partir à l''étranger',          NULL,NULL,'cc000023-0000-0000-0000-000000000023','dd000023-0000-0000-0000-000000000023','bb000002-0000-0000-0000-000000000002','VALIDEE',NOW(),NOW()),

-- Patiente 24 (ACTIVE, DIU, PS2) — 2 visites
  ('ee000038-0000-0000-0000-000000000038','2025-01-01',55,'108/65','Contrôle DIU annuel — bon résultat',   NULL,'2025-04-01','cc000024-0000-0000-0000-000000000024','dd000024-0000-0000-0000-000000000024','bb000001-0000-0000-0000-000000000001','VALIDEE',NOW(),NOW()),
  ('ee000039-0000-0000-0000-000000000039','2025-04-03',56,'110/68','Pas de symptômes',                     NULL,'2025-07-03','cc000024-0000-0000-0000-000000000024','dd000024-0000-0000-0000-000000000024','bb000001-0000-0000-0000-000000000001','VALIDEE',NOW(),NOW()),

-- Patiente 25 (ACTIVE, injectable, PS2) — 1 visite
  ('ee000040-0000-0000-0000-000000000040','2025-11-20',66,'118/74','Injection administrée sans incident',  NULL,'2026-02-20','cc000025-0000-0000-0000-000000000025','dd000025-0000-0000-0000-000000000025','bb000002-0000-0000-0000-000000000002','VALIDEE',NOW(),NOW()),

-- Patiente 26 (ABANDON, pilule, PS2) — 1 visite
  ('ee000041-0000-0000-0000-000000000041','2025-05-28',53,'108/65','Plaquettes fournies, consulte peu',    '3 plaquettes','2025-08-28','cc000026-0000-0000-0000-000000000026','dd000026-0000-0000-0000-000000000026','bb000001-0000-0000-0000-000000000001','VALIDEE',NOW(),NOW()),

-- Patiente 27 (NOUVELLE, DIU, PS2) — 1 visite initiale
  ('ee000042-0000-0000-0000-000000000042','2026-05-05',67,'115/72','Pose DIU effectuée, conseils donnés',  NULL,'2026-08-05','cc000027-0000-0000-0000-000000000027','dd000027-0000-0000-0000-000000000027','bb000002-0000-0000-0000-000000000002','VALIDEE',NOW(),NOW()),

-- Patiente 28 (ACTIVE, injectable, PVVIH, PS2) — 2 visites
  ('ee000043-0000-0000-0000-000000000043','2025-03-01',71,'130/85','Injection DMPA + bilan VIH',           NULL,'2025-06-01','cc000028-0000-0000-0000-000000000028','dd000028-0000-0000-0000-000000000028','bb000001-0000-0000-0000-000000000001','VALIDEE',NOW(),NOW()),
  ('ee000044-0000-0000-0000-000000000044','2025-06-03',70,'128/82','Rappel injection, ARV stable',         NULL,'2025-09-03','cc000028-0000-0000-0000-000000000028','dd000028-0000-0000-0000-000000000028','bb000001-0000-0000-0000-000000000001','VALIDEE',NOW(),NOW()),

-- Patiente 29 (ACTIVE, pilule, PS3) — 3 visites
  ('ee000045-0000-0000-0000-000000000045','2024-04-20',60,'110/70','Bonne tolérance pilule',               '3 plaquettes','2024-07-20','cc000029-0000-0000-0000-000000000029','dd000029-0000-0000-0000-000000000029','bb000003-0000-0000-0000-000000000003','VALIDEE',NOW(),NOW()),
  ('ee000046-0000-0000-0000-000000000046','2024-07-22',61,'112/72','Suivi régulier',                       '3 plaquettes','2024-10-22','cc000029-0000-0000-0000-000000000029','dd000029-0000-0000-0000-000000000029','bb000003-0000-0000-0000-000000000003','VALIDEE',NOW(),NOW()),
  ('ee000047-0000-0000-0000-000000000047','2024-10-24',60,'108/68','Pas d''incidents',                     '3 plaquettes','2025-01-24','cc000029-0000-0000-0000-000000000029','dd000029-0000-0000-0000-000000000029','bb000003-0000-0000-0000-000000000003','VALIDEE',NOW(),NOW()),

-- Patiente 30 (ACTIVE, injectable, PVVIH, PS3) — 2 visites
  ('ee000048-0000-0000-0000-000000000048','2025-06-15',57,'112/70','Injection + suivi VIH stade I',        NULL,'2025-09-15','cc000030-0000-0000-0000-000000000030','dd000030-0000-0000-0000-000000000030','bb000003-0000-0000-0000-000000000003','VALIDEE',NOW(),NOW()),
  ('ee000049-0000-0000-0000-000000000049','2025-09-17',58,'110/68','Rappel injection, ARV bien observé',   NULL,'2025-12-17','cc000030-0000-0000-0000-000000000030','dd000030-0000-0000-0000-000000000030','bb000003-0000-0000-0000-000000000003','VALIDEE',NOW(),NOW()),

-- Patiente 31 (ARRETEE, DIU, PS3) — 1 visite avant arrêt
  ('ee000050-0000-0000-0000-000000000050','2025-10-01',69,'120/76','DIU — légères crampes, tolérées',      NULL,'2026-01-01','cc000031-0000-0000-0000-000000000031','dd000031-0000-0000-0000-000000000031','bb000003-0000-0000-0000-000000000003','VALIDEE',NOW(),NOW()),

-- Patiente 32 (ACTIVE, implant, PS3) — 2 visites
  ('ee000051-0000-0000-0000-000000000051','2024-07-18',56,'108/65','Implant posé, bonne installation',     NULL,'2024-10-18','cc000032-0000-0000-0000-000000000032','dd000032-0000-0000-0000-000000000032','bb000003-0000-0000-0000-000000000003','VALIDEE',NOW(),NOW()),
  ('ee000052-0000-0000-0000-000000000052','2024-10-20',57,'110/68','Contrôle implant — satisfaisante',     NULL,'2025-01-20','cc000032-0000-0000-0000-000000000032','dd000032-0000-0000-0000-000000000032','bb000003-0000-0000-0000-000000000003','VALIDEE',NOW(),NOW()),

-- Patiente 34 (ABANDON, injectable, PS3) — 1 visite
  ('ee000053-0000-0000-0000-000000000053','2024-08-25',82,'138/88','Injection administrée, suivi difficile',NULL,'2024-11-25','cc000034-0000-0000-0000-000000000034','dd000034-0000-0000-0000-000000000034','bb000003-0000-0000-0000-000000000003','VALIDEE',NOW(),NOW()),

-- Patiente 35 (ACTIVE, DIU, PS3) — 1 visite
  ('ee000054-0000-0000-0000-000000000054','2025-12-08',58,'110/68','Contrôle DIU — bien en place',         NULL,'2026-03-08','cc000035-0000-0000-0000-000000000035','dd000035-0000-0000-0000-000000000035','bb000003-0000-0000-0000-000000000003','VALIDEE',NOW(),NOW()),

-- Patiente 36 (ACTIVE, implant, PVVIH, PS3) — 2 visites
  ('ee000055-0000-0000-0000-000000000055','2024-10-30',63,'118/74','Implant + ARV, charge virale contrôlée',NULL,'2025-01-30','cc000036-0000-0000-0000-000000000036','dd000036-0000-0000-0000-000000000036','bb000003-0000-0000-0000-000000000003','VALIDEE',NOW(),NOW()),
  ('ee000056-0000-0000-0000-000000000056','2025-02-01',64,'120/76','Excellent contrôle, renouvellement',   NULL,'2025-05-01','cc000036-0000-0000-0000-000000000036','dd000036-0000-0000-0000-000000000036','bb000003-0000-0000-0000-000000000003','VALIDEE',NOW(),NOW()),

-- Patiente 37 (ARRETEE, pilule, PS3) — 2 visites avant arrêt
  ('ee000057-0000-0000-0000-000000000057','2024-12-15',62,'115/72','Pilule bien tolérée, déménagement prévu',NULL,NULL,'cc000037-0000-0000-0000-000000000037','dd000037-0000-0000-0000-000000000037','bb000003-0000-0000-0000-000000000003','VALIDEE',NOW(),NOW()),
  ('ee000058-0000-0000-0000-000000000058','2025-03-15',63,'112/70','Dernier suivi avant départ',           '3 plaquettes',NULL,'cc000037-0000-0000-0000-000000000037','dd000037-0000-0000-0000-000000000037','bb000003-0000-0000-0000-000000000003','VALIDEE',NOW(),NOW()),

-- Patiente 38 (ACTIVE, norplant, PS3) — 1 visite
  ('ee000059-0000-0000-0000-000000000059','2026-02-02',74,'118/74','Contrôle norplant — satisfaisant',     NULL,'2026-05-02','cc000038-0000-0000-0000-000000000038','dd000038-0000-0000-0000-000000000038','bb000003-0000-0000-0000-000000000003','VALIDEE',NOW(),NOW()),

-- Patiente 39 (ACTIVE, injectable, PS3) — 1 visite
  ('ee000060-0000-0000-0000-000000000060','2026-03-10',53,'108/65','Première injection post-inscription',  NULL,'2026-06-10','cc000039-0000-0000-0000-000000000039','dd000039-0000-0000-0000-000000000039','bb000003-0000-0000-0000-000000000003','VALIDEE',NOW(),NOW()),

-- Patiente 40 (ACTIVE, pilule, PS4) — 3 visites
  ('ee000061-0000-0000-0000-000000000061','2024-06-01',64,'112/70','Bonne observance pilule',               '3 plaquettes','2024-09-01','cc000040-0000-0000-0000-000000000040','dd000040-0000-0000-0000-000000000040','bb000004-0000-0000-0000-000000000004','VALIDEE',NOW(),NOW()),
  ('ee000062-0000-0000-0000-000000000062','2024-09-03',63,'110/68','Pas de plaintes',                      '3 plaquettes','2024-12-03','cc000040-0000-0000-0000-000000000040','dd000040-0000-0000-0000-000000000040','bb000004-0000-0000-0000-000000000004','VALIDEE',NOW(),NOW()),
  ('ee000063-0000-0000-0000-000000000063','2024-12-05',64,'115/72','Suivi régulier confirmé',              '3 plaquettes','2025-03-05','cc000040-0000-0000-0000-000000000040','dd000040-0000-0000-0000-000000000040','bb000004-0000-0000-0000-000000000004','VALIDEE',NOW(),NOW()),

-- Patiente 41 (ACTIVE, implant, PVVIH, PS4) — 2 visites
  ('ee000064-0000-0000-0000-000000000064','2025-07-12',60,'115/72','Implant + ARV stade I, bilan OK',      NULL,'2025-10-12','cc000041-0000-0000-0000-000000000041','dd000041-0000-0000-0000-000000000041','bb000005-0000-0000-0000-000000000005','VALIDEE',NOW(),NOW()),
  ('ee000065-0000-0000-0000-000000000065','2025-10-14',61,'112/70','CD4 satisfaisant, implant en place',   NULL,'2026-01-14','cc000041-0000-0000-0000-000000000041','dd000041-0000-0000-0000-000000000041','bb000005-0000-0000-0000-000000000005','VALIDEE',NOW(),NOW()),

-- Patiente 43 (ARRETEE, injectable, PS4) — 1 visite avant arrêt
  ('ee000066-0000-0000-0000-000000000066','2024-09-10',70,'115/72','Injection DMPA — début désir grossesse',NULL,'2024-12-10','cc000043-0000-0000-0000-000000000043','dd000043-0000-0000-0000-000000000043','bb000005-0000-0000-0000-000000000005','VALIDEE',NOW(),NOW()),

-- Patiente 44 (ABANDON, pilule, PS4) — 1 visite
  ('ee000067-0000-0000-0000-000000000067','2025-05-20',58,'110/70','Plaquettes fournies, suivi irrégulier','3 plaquettes','2025-08-20','cc000044-0000-0000-0000-000000000044','dd000044-0000-0000-0000-000000000044','bb000004-0000-0000-0000-000000000004','VALIDEE',NOW(),NOW()),

-- Patiente 45 (ACTIVE, DIU, PS4) — 2 visites
  ('ee000068-0000-0000-0000-000000000068','2024-11-05',76,'138/86','Contrôle DIU, tension surveillée',     NULL,'2025-02-05','cc000045-0000-0000-0000-000000000045','dd000045-0000-0000-0000-000000000045','bb000005-0000-0000-0000-000000000005','VALIDEE',NOW(),NOW()),
  ('ee000069-0000-0000-0000-000000000069','2025-02-07',75,'136/84','DIU bien placé, tension améliorée',    NULL,'2025-05-07','cc000045-0000-0000-0000-000000000045','dd000045-0000-0000-0000-000000000045','bb000005-0000-0000-0000-000000000005','VALIDEE',NOW(),NOW()),

-- Patiente 46 (ACTIVE, injectable, PVVIH, PS4) — 2 visites
  ('ee000070-0000-0000-0000-000000000070','2025-08-30',68,'130/85','Injection DMPA + suivi VIH stade II',  NULL,'2025-11-30','cc000046-0000-0000-0000-000000000046','dd000046-0000-0000-0000-000000000046','bb000004-0000-0000-0000-000000000004','VALIDEE',NOW(),NOW()),
  ('ee000071-0000-0000-0000-000000000071','2025-12-02',69,'128/82','Rappel injection, charge virale contrôlée',NULL,'2026-03-02','cc000046-0000-0000-0000-000000000046','dd000046-0000-0000-0000-000000000046','bb000004-0000-0000-0000-000000000004','VALIDEE',NOW(),NOW()),

-- Patiente 47 (ACTIVE, implant, PS4) — 2 visites
  ('ee000072-0000-0000-0000-000000000072','2025-01-15',61,'110/68','Implant stable, pas de plaintes',      NULL,'2025-04-15','cc000047-0000-0000-0000-000000000047','dd000047-0000-0000-0000-000000000047','bb000005-0000-0000-0000-000000000005','VALIDEE',NOW(),NOW()),
  ('ee000073-0000-0000-0000-000000000073','2025-04-17',62,'112/70','Renouvellement implant dans 2 ans',    NULL,'2025-07-17','cc000047-0000-0000-0000-000000000047','dd000047-0000-0000-0000-000000000047','bb000005-0000-0000-0000-000000000005','VALIDEE',NOW(),NOW()),

-- Patiente 48 (ABANDON, norplant, PS4) — 1 visite
  ('ee000074-0000-0000-0000-000000000074','2025-10-08',55,'108/65','Norplant — suivi difficile à maintenir',NULL,'2026-01-08','cc000048-0000-0000-0000-000000000048','dd000048-0000-0000-0000-000000000048','bb000004-0000-0000-0000-000000000004','VALIDEE',NOW(),NOW()),

-- Patiente 50 (NOUVELLE, injectable, PS4) — 1 visite initiale
  ('ee000075-0000-0000-0000-000000000075','2026-06-05',63,'115/72','Première injection, bien tolérée',     NULL,'2026-09-05','cc000050-0000-0000-0000-000000000050','dd000050-0000-0000-0000-000000000050','bb000004-0000-0000-0000-000000000004','VALIDEE',NOW(),NOW());

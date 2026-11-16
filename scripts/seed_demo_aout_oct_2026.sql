-- =============================================================================
-- Données de démo : activité du 1er août 2026 au 3 octobre 2026
--
-- Script LOCAL (hors Flyway, pour ne pas toucher la prod). Ré-exécutable :
-- tout ce qu'il crée est marqué created_by = 'seed-aout-oct-2026' et supprimé
-- au début de chaque exécution.
--
--   docker exec -i pf_sagefemme_db psql -U postgres -d pf_sagefemme \
--     < scripts/seed_demo_aout_oct_2026.sql
--
-- Contenu :
--   - ~140 nouvelles patientes inscrites entre le 01/08 et aujourd'hui (fiche +
--     visites de suivi selon la méthode, RDV passés / manqués / à venir)
--   - visites de suivi août -> octobre pour les patientes actives du seed V6
--   - changements de méthode, arrêts, abandons, PVVIH
--   - visites du jour (dont quelques brouillons) pour le journal
-- =============================================================================

BEGIN;

-- Nettoyage d'une exécution précédente
DELETE FROM changements_methode WHERE created_by = 'seed-aout-oct-2026';
DELETE FROM visites             WHERE created_by = 'seed-aout-oct-2026';
DELETE FROM visites             WHERE patiente_id IN (SELECT id FROM patientes WHERE created_by = 'seed-aout-oct-2026');
DELETE FROM changements_methode WHERE patiente_id IN (SELECT id FROM patientes WHERE created_by = 'seed-aout-oct-2026');
DELETE FROM fiches_consultation WHERE created_by = 'seed-aout-oct-2026';
DELETE FROM patientes           WHERE created_by = 'seed-aout-oct-2026';

DO $$
DECLARE
    v_seed       CONSTANT text := 'seed-aout-oct-2026';
    v_debut      CONSTANT date := DATE '2026-08-01';
    v_today      CONSTANT date := CURRENT_DATE;
    v_ps1        CONSTANT uuid := '5fa2b02c-0cdf-4a43-87b6-bb516e92bb0e';

    prenoms text[] := ARRAY['Aminata','Fatou','Awa','Mariama','Khady','Ndeye','Coumba','Aissatou','Bineta','Sokhna',
                            'Astou','Dieynaba','Penda','Rama','Yacine','Adama','Seynabou','Maimouna','Oumou','Ramatoulaye',
                            'Rokhaya','Nafissatou','Marème','Fatoumata','Djeneba','Kiné','Anta','Ngoné','Mame Diarra','Arame',
                            'Absa','Daba','Salimata','Hawa','Codou','Thioro','Khoudia','Nogaye','Sophie','Marie'];
    noms    text[] := ARRAY['Diop','Ndiaye','Fall','Sow','Ba','Gueye','Sarr','Faye','Diallo','Cissé',
                            'Mbaye','Thiam','Sy','Kane','Seck','Niang','Diouf','Camara','Touré','Dieng',
                            'Sall','Wade','Mendy','Badji','Sène','Ndour','Diatta','Kébé','Lô','Samb'];
    quartiers_dakar text[] := ARRAY['Dakar Plateau','Dakar Médina','Parcelles Assainies','Grand Yoff','HLM','Ouakam','Yoff','Fass','Colobane','Liberté 6'];
    quartiers_pikine text[] := ARRAY['Pikine Est','Guédiawaye','Thiaroye','Keur Massar','Pikine Icotaf','Dalifort'];
    quartiers_sl text[] := ARRAY['Sor','Guet Ndar','Balacoss','Pikine Saint-Louis','Ndiolofène'];
    quartiers_thies text[] := ARRAY['Médina Fall','Randoulène','Grand Thiès','Diakhao','Nguinth'];
    professions text[] := ARRAY['Ménagère','Ménagère','Ménagère','Commerçante','Commerçante','Couturière','Vendeuse','Étudiante',
                                'Enseignante','Agricultrice','Coiffeuse','Restauratrice','Aide-soignante','Secrétaire'];
    ethnies text[] := ARRAY['WOLOF','WOLOF','WOLOF','PEULH','PEULH','SERERE','SERERE','TOUCOULEUR','DIOLA','MANDINGUE','AUTRES'];
    situations text[] := ARRAY['MARIEE','MARIEE','MARIEE','MARIEE','MARIEE','CELIBATAIRE','CELIBATAIRE','DIVORCEE','VEUVE'];
    instructions text[] := ARRAY['NON_SCOLARISEE','PRIMAIRE','PRIMAIRE','SECONDAIRE','SECONDAIRE','SUPERIEUR'];
    prof_maris text[] := ARRAY['SANS','CULTIVATEUR','SALARIE','SALARIE','TRAVAILLE_A_SON_COMPTE','TRAVAILLE_A_SON_COMPTE','AUTRES'];
    sources text[] := ARRAY['AGENT_DE_SANTE','AGENT_DE_SANTE','AGENT_DE_SANTE','AMIE_PARENT','AMIE_PARENT','MARI','RADIO','TELEVISION','GROUPEMENT','AFFICHE'];
    -- Méthodes pondérées (injectable et implant dominants, comme au Sénégal)
    methodes text[] := ARRAY['INJECTABLE_IM','INJECTABLE_IM','INJECTABLE_IM','INJECTABLE_IM',
                             'INJECTABLE_SC_PRESTATAIRE','INJECTABLE_SC_PRESTATAIRE','INJECTABLE_SC_AUTO_INJECTION',
                             'IMPLANT_1_CAPSULE','IMPLANT_1_CAPSULE','IMPLANT_2_CAPSULES','IMPLANT_2_CAPSULES','IMPLANT_2_CAPSULES',
                             'PILULE_PP','PILULE_PP','PILULE_POP','PILULE_POP',
                             'DIU_TCU_380A','DIU_TCU_380A','DIU_POST_PARTUM',
                             'PRESERVATIF_MASCULIN','PRESERVATIF_MASCULIN','PRESERVATIF_FEMININ',
                             'MAMA','METHODE_JOUR_FIXE_COLLIER'];
    motifs_arret text[] := ARRAY['Désir de grossesse','Désir de grossesse','Effets secondaires (saignements)',
                                 'Opposition du conjoint','Déménagement','Ménopause'];
    motifs_changement text[] := ARRAY['Effets secondaires (céphalées)','Saignements irréguliers','Prise de poids',
                                      'Souhaite une méthode de longue durée','Oublis fréquents de la pilule',
                                      'Préférence personnelle'];
    plaintes text[] := ARRAY['Aucune plainte','Aucune plainte','Aucune plainte','Céphalées occasionnelles',
                             'Douleurs pelviennes légères','Fatigue','Leucorrhées'];

    r_sf        record;
    sfs         uuid[];
    sf_postes   uuid[];
    n_sf        int;
    i           int;
    k           int;
    v_id        uuid;
    v_sf        uuid;
    v_poste     uuid;
    v_age       int;
    v_insc      date;
    v_methode   text;
    v_new_meth  text;
    v_statut    text;
    v_pvvih     boolean;
    v_date      date;
    v_rdv       date;
    v_prev      date;
    v_poids     double precision;
    v_nb_vis    int;
    v_adresse   text;
    v_num       int := 0;
    v_rand      double precision;
    v_arret     boolean;
    v_abandon   boolean;
    v_changed   boolean;
    v_vis_stat  text;
BEGIN
    PERFORM setseed(0.2026);

    -- Sages-femmes et leur poste. Les SF du PS1 (poste du chef "chef_ps1")
    -- sont dupliquées pour concentrer l'activité sur ce poste pour la démo.
    FOR r_sf IN SELECT id, poste_sante_id FROM sages_femmes ORDER BY matricule LOOP
        sfs := sfs || r_sf.id;  sf_postes := sf_postes || r_sf.poste_sante_id;
        IF r_sf.poste_sante_id = v_ps1 THEN
            sfs := sfs || r_sf.id || r_sf.id;  sf_postes := sf_postes || r_sf.poste_sante_id || r_sf.poste_sante_id;
        END IF;
    END LOOP;
    n_sf := array_length(sfs, 1);

    -- -------------------------------------------------------------------------
    -- 1. Nouvelles patientes inscrites depuis le 1er août
    -- -------------------------------------------------------------------------
    FOR i IN 1..140 LOOP
        v_id    := gen_random_uuid();
        k       := 1 + floor(random() * n_sf)::int;
        v_sf    := sfs[k];
        v_poste := sf_postes[k];

        -- Inscription répartie sur la période ; ~10 % aujourd'hui ou cette semaine
        IF i <= 8 THEN
            v_insc := v_today;
        ELSIF i <= 16 THEN
            v_insc := v_today - (1 + floor(random() * 6))::int;
        ELSE
            v_insc := v_debut + floor(random() * (v_today - v_debut + 1))::int;
        END IF;
        -- pas d'inscription le dimanche
        IF extract(isodow FROM v_insc) = 7 AND v_insc <> v_today THEN v_insc := v_insc - 1; END IF;

        v_rand := random();
        v_age := CASE WHEN v_rand < 0.03 THEN 14
                      WHEN v_rand < 0.15 THEN 15 + floor(random() * 5)::int
                      WHEN v_rand < 0.35 THEN 20 + floor(random() * 5)::int
                      WHEN v_rand < 0.97 THEN 25 + floor(random() * 25)::int
                      ELSE 50 + floor(random() * 3)::int END;
        v_pvvih   := random() < 0.12;
        v_methode := methodes[1 + floor(random() * array_length(methodes, 1))::int];
        IF v_age < 18 AND v_methode LIKE 'DIU%' THEN v_methode := 'IMPLANT_1_CAPSULE'; END IF;

        v_adresse := CASE v_poste
            WHEN 'aa000001-0000-0000-0000-000000000001' THEN quartiers_pikine[1 + floor(random() * array_length(quartiers_pikine, 1))::int]
            WHEN 'aa000002-0000-0000-0000-000000000002' THEN quartiers_sl[1 + floor(random() * array_length(quartiers_sl, 1))::int]
            WHEN 'aa000003-0000-0000-0000-000000000003' THEN quartiers_thies[1 + floor(random() * array_length(quartiers_thies, 1))::int]
            ELSE quartiers_dakar[1 + floor(random() * array_length(quartiers_dakar, 1))::int] END;

        v_num := v_num + 1;
        INSERT INTO patientes (id, prenom, nom, age, date_naissance, profession, adresse, telephone,
                               ethnie, religion, situation_matrimoniale, niveau_instruction, profession_mari,
                               referee, origine_reference, source_information, statut_patiente, pvvih,
                               stade_oms, sous_arv, traitement_arv, numero_dossier, date_inscription,
                               poste_sante_id, sage_femme_id, created_at, updated_at, created_by)
        VALUES (v_id,
                prenoms[1 + floor(random() * array_length(prenoms, 1))::int],
                noms[1 + floor(random() * array_length(noms, 1))::int],
                v_age,
                make_date(2026 - v_age, 1 + floor(random() * 12)::int, 1 + floor(random() * 28)::int),
                CASE WHEN v_age < 20 THEN 'Étudiante' ELSE professions[1 + floor(random() * array_length(professions, 1))::int] END,
                v_adresse,
                '+221 7' || (array['0','6','7','8'])[1 + floor(random() * 4)::int] || ' ' ||
                    lpad(floor(random() * 1000)::text, 3, '0') || ' ' || lpad(floor(random() * 10000)::text, 4, '0'),
                ethnies[1 + floor(random() * array_length(ethnies, 1))::int],
                CASE WHEN random() < 0.9 THEN 'MUSULMANE' ELSE 'CHRETIENNE' END,
                CASE WHEN v_age < 18 THEN 'CELIBATAIRE' ELSE situations[1 + floor(random() * array_length(situations, 1))::int] END,
                instructions[1 + floor(random() * array_length(instructions, 1))::int],
                prof_maris[1 + floor(random() * array_length(prof_maris, 1))::int],
                random() < 0.15, NULL,
                sources[1 + floor(random() * array_length(sources, 1))::int],
                'NOUVELLE', v_pvvih,
                CASE WHEN v_pvvih THEN (array['STADE_I','STADE_I','STADE_II','STADE_III'])[1 + floor(random() * 4)::int] END,
                CASE WHEN v_pvvih THEN random() < 0.85 END,
                CASE WHEN v_pvvih THEN 'TDF/3TC/DTG' END,
                'PV-2026-' || lpad((100 + v_num)::text, 4, '0'),
                v_insc, v_poste, v_sf,
                v_insc + time '08:30' + (floor(random() * 480) || ' minutes')::interval, now(), v_seed);

        IF random() < 0.05 THEN
            INSERT INTO patientes_genre_de_vie (patiente_id, genre_de_vie) VALUES (v_id, 'TABAC');
        END IF;

        INSERT INTO fiches_consultation (id, numero, date_consultation, patiente_id, sage_femme_id, poste_sante_id,
                                         menarches_age, duree_regles, cycle_menstruel, date_dernieres_regles,
                                         enfants_vivants, enfants_decedes, morts_nes, avortements, age_dernier_enfant,
                                         hypertension_arterielle, diabete, drepanocytose, plaintes_actuelles,
                                         taille_cm, poids_kg, muqueuses, seins, col_speculum, allergie_cuivre,
                                         methode_choisie, created_at, updated_at, created_by)
        VALUES (gen_random_uuid(), 'FC-2026-' || lpad((100 + v_num)::text, 4, '0'), v_insc, v_id, v_sf, v_poste,
                12 + floor(random() * 4)::int, 3 + floor(random() * 4)::int, 26 + floor(random() * 7)::int,
                v_insc - (5 + floor(random() * 20))::int,
                CASE WHEN v_age < 20 THEN floor(random() * 2)::int ELSE floor(random() * 6)::int END,
                CASE WHEN random() < 0.1 THEN 1 ELSE 0 END, 0, CASE WHEN random() < 0.12 THEN 1 ELSE 0 END,
                (array['6 mois','1 an','2 ans','3 ans','5 ans'])[1 + floor(random() * 5)::int],
                random() < 0.06, random() < 0.03, random() < 0.02,
                plaintes[1 + floor(random() * array_length(plaintes, 1))::int],
                150 + floor(random() * 25), 50 + floor(random() * 30),
                'Bien colorées', 'Normaux', 'Col sain', false,
                v_methode, v_insc + time '09:00', now(), v_seed);

        -- Trajectoire de la patiente
        v_rand    := random();
        v_arret   := v_rand < 0.08 AND v_insc < v_today - 20;
        v_abandon := NOT v_arret AND v_rand < 0.20 AND v_insc < v_today - 30;
        v_changed := false;
        v_poids   := 50 + floor(random() * 30);
        v_date    := v_insc;
        v_nb_vis  := 0;
        v_statut  := 'NOUVELLE';

        LOOP
            v_nb_vis := v_nb_vis + 1;

            -- Changement de méthode (~12 % des patientes, à la 2e ou 3e visite)
            IF v_nb_vis IN (2, 3) AND NOT v_changed AND random() < 0.35 THEN
                v_new_meth := methodes[1 + floor(random() * array_length(methodes, 1))::int];
                IF v_new_meth <> v_methode THEN
                    INSERT INTO changements_methode (id, date_changement, ancienne_methode, nouvelle_methode, motif,
                                                     patiente_id, sage_femme_id, created_at, updated_at, created_by)
                    VALUES (gen_random_uuid(), v_date, v_methode, v_new_meth,
                            motifs_changement[1 + floor(random() * array_length(motifs_changement, 1))::int],
                            v_id, v_sf, v_date + time '10:00', now(), v_seed);
                    v_methode := v_new_meth;
                    v_changed := true;
                END IF;
            END IF;

            -- Prochain RDV selon la méthode
            v_rdv := v_date + CASE
                WHEN v_methode LIKE 'INJECTABLE%' THEN 90
                WHEN v_methode LIKE 'PILULE%' THEN CASE WHEN v_nb_vis = 1 THEN 30 ELSE 90 END
                WHEN v_methode LIKE 'IMPLANT%' OR v_methode LIKE 'DIU%' THEN CASE WHEN v_nb_vis = 1 THEN 30 ELSE 180 END
                ELSE 30 END;

            v_vis_stat := CASE WHEN v_date = v_today AND random() < 0.3 THEN 'BROUILLON' ELSE 'VALIDEE' END;
            INSERT INTO visites (id, date_visite, date_dernieres_regles, poids_kg, tension_arterielle, observations,
                                 unites_fournies, date_prochain_rdv, patiente_id, sage_femme_id, statut,
                                 created_at, updated_at, created_by)
            VALUES (gen_random_uuid(), v_date, v_date - (5 + floor(random() * 20))::int,
                    v_poids + round((random() * 2 - 1)::numeric, 1),
                    (105 + floor(random() * 30))::int || '/' || (65 + floor(random() * 20))::int,
                    CASE WHEN v_nb_vis = 1 THEN 'Première consultation PF, counseling effectué, méthode ' || v_methode
                         WHEN v_changed AND v_nb_vis IN (2, 3) THEN 'Changement de méthode effectué'
                         ELSE (array['Bonne tolérance','RAS','Suivi régulier, bonne observance',
                                     'Légers saignements, rassurée','Pas de plaintes particulières'])[1 + floor(random() * 5)::int] END,
                    CASE WHEN v_methode LIKE 'PILULE%' THEN CASE WHEN v_nb_vis = 1 THEN '1 plaquette' ELSE '3 plaquettes' END
                         WHEN v_methode LIKE 'INJECTABLE%' THEN '1 dose'
                         WHEN v_methode LIKE 'PRESERVATIF%' THEN '30 unités'
                         WHEN v_methode LIKE 'IMPLANT%' AND v_nb_vis = 1 THEN '1 implant'
                         WHEN v_methode LIKE 'DIU%' AND v_nb_vis = 1 THEN '1 DIU' END,
                    v_rdv, v_id, v_sf, v_vis_stat,
                    v_date + time '08:30' + (floor(random() * 480) || ' minutes')::interval, now(), v_seed);

            IF v_nb_vis > 1 THEN v_statut := 'ACTIVE'; END IF;
            EXIT WHEN v_arret AND v_nb_vis >= 1 AND random() < 0.6;
            EXIT WHEN v_abandon;

            -- La patiente revient-elle ? (15 % de RDV manqués)
            EXIT WHEN v_rdv > v_today;
            EXIT WHEN random() < 0.15;
            v_prev := v_date;
            v_date := LEAST(v_rdv + (floor(random() * 8) - 2)::int, v_today);
            EXIT WHEN v_date <= v_prev;
        END LOOP;

        -- Statut final
        IF v_arret THEN
            UPDATE patientes SET statut_patiente = 'ARRETEE',
                                 date_arret = LEAST(v_date + (floor(random() * 10))::int, v_today),
                                 motif_arret = motifs_arret[1 + floor(random() * array_length(motifs_arret, 1))::int]
             WHERE id = v_id;
        ELSIF v_abandon THEN
            UPDATE patientes SET statut_patiente = 'ABANDON' WHERE id = v_id;
        ELSIF v_statut = 'ACTIVE' OR v_insc < v_today - 14 THEN
            UPDATE patientes SET statut_patiente = 'ACTIVE' WHERE id = v_id;
        END IF;
    END LOOP;

    -- -------------------------------------------------------------------------
    -- 2. Suivi août -> octobre des patientes actives existantes (seed V6)
    -- -------------------------------------------------------------------------
    FOR r_sf IN
        SELECT p.id, p.sage_femme_id, f.methode_choisie AS methode,
               (SELECT max(v.date_prochain_rdv) FROM visites v WHERE v.patiente_id = p.id) AS dernier_rdv,
               (SELECT v.poids_kg FROM visites v WHERE v.patiente_id = p.id ORDER BY v.date_visite DESC LIMIT 1) AS poids
          FROM patientes p
          JOIN fiches_consultation f ON f.patiente_id = p.id
         WHERE p.created_by IS DISTINCT FROM v_seed
           AND p.statut_patiente IN ('ACTIVE', 'NOUVELLE')
    LOOP
        v_date   := GREATEST(COALESCE(r_sf.dernier_rdv, v_debut), v_debut) + (floor(random() * 6) - 1)::int;
        v_poids  := COALESCE(r_sf.poids, 60);
        v_nb_vis := 0;
        CONTINUE WHEN random() < 0.1;  -- quelques-unes ne reviennent pas
        WHILE v_date <= v_today LOOP
            v_nb_vis := v_nb_vis + 1;
            -- ~20 % changent de méthode lors du renouvellement
            IF v_nb_vis = 1 AND random() < 0.2 THEN
                v_new_meth := methodes[1 + floor(random() * array_length(methodes, 1))::int];
                IF v_new_meth <> r_sf.methode THEN
                    INSERT INTO changements_methode (id, date_changement, ancienne_methode, nouvelle_methode, motif,
                                                     patiente_id, sage_femme_id, created_at, updated_at, created_by)
                    VALUES (gen_random_uuid(), v_date, r_sf.methode, v_new_meth,
                            motifs_changement[1 + floor(random() * array_length(motifs_changement, 1))::int],
                            r_sf.id, r_sf.sage_femme_id, v_date + time '10:00', now(), v_seed);
                    r_sf.methode := v_new_meth;
                END IF;
            END IF;
            v_rdv := v_date + CASE
                WHEN r_sf.methode LIKE 'INJECTABLE%' OR r_sf.methode LIKE 'PILULE%' THEN 90
                WHEN r_sf.methode LIKE 'IMPLANT%' OR r_sf.methode LIKE 'DIU%' THEN 180
                ELSE 30 END;
            INSERT INTO visites (id, date_visite, date_dernieres_regles, poids_kg, tension_arterielle, observations,
                                 unites_fournies, date_prochain_rdv, patiente_id, sage_femme_id, statut,
                                 created_at, updated_at, created_by)
            VALUES (gen_random_uuid(), v_date, v_date - (5 + floor(random() * 20))::int,
                    v_poids + round((random() * 2 - 1)::numeric, 1),
                    (105 + floor(random() * 30))::int || '/' || (65 + floor(random() * 20))::int,
                    (array['Renouvellement effectué','Bonne tolérance','RAS','Suivi régulier, bonne observance'])[1 + floor(random() * 4)::int],
                    CASE WHEN r_sf.methode LIKE 'PILULE%' THEN '3 plaquettes'
                         WHEN r_sf.methode LIKE 'INJECTABLE%' THEN '1 dose'
                         WHEN r_sf.methode LIKE 'PRESERVATIF%' THEN '30 unités' END,
                    v_rdv, r_sf.id, r_sf.sage_femme_id, 'VALIDEE',
                    v_date + time '08:30' + (floor(random() * 480) || ' minutes')::interval, now(), v_seed);
            EXIT WHEN random() < 0.1;
            v_date := v_rdv + (floor(random() * 8) - 2)::int;
        END LOOP;
        IF v_nb_vis > 0 THEN
            UPDATE patientes SET statut_patiente = 'ACTIVE', updated_at = now() WHERE id = r_sf.id;
        END IF;
    END LOOP;
END $$;

-- La fiche porte la méthode en cours : on y reporte le dernier changement (comme le fait l'API).
UPDATE fiches_consultation f
   SET methode_choisie = c.nouvelle_methode, updated_at = now()
  FROM (SELECT DISTINCT ON (patiente_id) patiente_id, nouvelle_methode
          FROM changements_methode
         ORDER BY patiente_id, date_changement DESC, created_at DESC) c
 WHERE c.patiente_id = f.patiente_id
   AND f.methode_choisie IS DISTINCT FROM c.nouvelle_methode;

COMMIT;

-- Récapitulatif
SELECT ps.nom AS poste,
       count(DISTINCT p.id) FILTER (WHERE p.date_inscription >= DATE '2026-08-01') AS nouvelles_patientes,
       count(v.id) FILTER (WHERE v.date_visite >= DATE '2026-08-01')               AS visites_depuis_aout,
       count(v.id) FILTER (WHERE v.date_visite = CURRENT_DATE)                     AS visites_aujourdhui,
       count(v.id) FILTER (WHERE v.date_prochain_rdv BETWEEN CURRENT_DATE AND CURRENT_DATE + 30) AS rdv_30_jours
  FROM postes_sante ps
  LEFT JOIN patientes p ON p.poste_sante_id = ps.id
  LEFT JOIN visites v   ON v.patiente_id = p.id
 GROUP BY ps.nom ORDER BY ps.nom;

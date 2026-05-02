package com.example.pfsagefemme.modules.postesante.infrastructure.persistence;

import com.example.pfsagefemme.modules.postesante.domain.model.entity.PosteSante;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

/** Repository Spring Data - detail d'infrastructure. */
public interface PosteSanteJpaRepository extends JpaRepository<PosteSante, UUID> {

    List<PosteSante> findByActifTrue();

    List<PosteSante> findByRegion(String region);

    List<PosteSante> findByDistrict(String district);

    @org.springframework.data.jpa.repository.Query("SELECT COUNT(p) FROM PosteSante p WHERE p.actif = TRUE")
    long countActifs();

    @org.springframework.data.jpa.repository.Query(
            "SELECT COUNT(DISTINCT p.district) FROM PosteSante p WHERE p.district IS NOT NULL")
    long countDistinctDistricts();

    @org.springframework.data.jpa.repository.Query(
            "SELECT COUNT(p) FROM PosteSante p WHERE EXTRACT(YEAR FROM p.createdAt) = :year")
    long countByYear(@org.springframework.data.repository.query.Param("year") int year);
}

package com.example.pfsagefemme.modules.postesante.domain.port.out;

import com.example.pfsagefemme.modules.postesante.domain.model.entity.PosteSante;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/** Port sortant - operations de persistance sur les postes de sante. */
public interface PosteSanteRepository {

    PosteSante save(PosteSante posteSante);

    Optional<PosteSante> findById(UUID id);

    List<PosteSante> findAll();

    List<PosteSante> findByActifTrue();

    List<PosteSante> findByRegion(String region);

    List<PosteSante> findByDistrict(String district);

    long countActifs();

    long countDistinctDistricts();

    long countByYear(int year);

    void deleteById(UUID id);
}

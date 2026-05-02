package com.example.pfsagefemme.modules.postesante.infrastructure.persistence;

import com.example.pfsagefemme.modules.postesante.domain.model.entity.PosteSante;
import com.example.pfsagefemme.modules.postesante.domain.port.out.PosteSanteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/** Adapter sortant - implemente le port via Spring Data JPA. */
@Component
@RequiredArgsConstructor
public class PosteSantePersistenceAdapter implements PosteSanteRepository {

    private final PosteSanteJpaRepository jpaRepository;

    @Override
    public PosteSante save(PosteSante posteSante) {
        return jpaRepository.save(posteSante);
    }

    @Override
    public Optional<PosteSante> findById(UUID id) {
        return jpaRepository.findById(id);
    }

    @Override
    public List<PosteSante> findAll() {
        return jpaRepository.findAll();
    }

    @Override
    public List<PosteSante> findByActifTrue() {
        return jpaRepository.findByActifTrue();
    }

    @Override
    public List<PosteSante> findByRegion(String region) {
        return jpaRepository.findByRegion(region);
    }

    @Override
    public List<PosteSante> findByDistrict(String district) {
        return jpaRepository.findByDistrict(district);
    }

    @Override
    public long countActifs() {
        return jpaRepository.countActifs();
    }

    @Override
    public long countDistinctDistricts() {
        return jpaRepository.countDistinctDistricts();
    }

    @Override
    public long countByYear(int year) {
        return jpaRepository.countByYear(year);
    }

    @Override
    public void deleteById(UUID id) {
        jpaRepository.deleteById(id);
    }
}

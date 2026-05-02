package com.example.pfsagefemme.modules.postesante.domain.port.in;

import com.example.pfsagefemme.modules.postesante.domain.model.entity.PosteSante;

import java.util.List;

public interface ListPosteSantesUseCase {
    List<PosteSante> list(boolean onlyActive);
}

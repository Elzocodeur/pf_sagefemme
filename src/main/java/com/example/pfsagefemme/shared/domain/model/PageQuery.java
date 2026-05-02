package com.example.pfsagefemme.shared.domain.model;

/**
 * Requete de pagination cote domaine - independante de Spring Data.
 */
public record PageQuery(int page, int size, String sortBy, SortDirection direction) {

    public PageQuery {
        if (page < 0) throw new IllegalArgumentException("page must be >= 0");
        if (size < 1 || size > 200) throw new IllegalArgumentException("size must be in [1, 200]");
        if (direction == null) direction = SortDirection.ASC;
    }

    public static PageQuery of(int page, int size) {
        return new PageQuery(page, size, null, SortDirection.ASC);
    }
}

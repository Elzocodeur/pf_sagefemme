/**
 * Shared kernel - domaine.
 *
 * Briques de domaine partagees par tous les bounded contexts :
 *  - exceptions metier de base (DomainException, BusinessRuleException, ResourceNotFoundException)
 *  - value objects transverses (PageQuery, PageResult, SortDirection)
 *
 * Aucune dependance vers Spring, JPA ou Jackson.
 */
package com.example.pfsagefemme.shared.domain;

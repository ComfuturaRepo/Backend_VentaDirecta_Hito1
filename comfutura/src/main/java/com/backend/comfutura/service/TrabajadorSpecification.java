package com.backend.comfutura.service;

import com.backend.comfutura.model.Trabajador;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class TrabajadorSpecification {

    // Método para filtros simples (el que necesitas)
    public static Specification<Trabajador> withSimpleFilters(
            String search,
            Boolean activo,
            Integer areaId,
            Integer cargoId,
            Integer empresaId) {

        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            // Filtro por búsqueda de texto
            if (StringUtils.hasText(search)) {
                String searchPattern = "%" + search.toLowerCase() + "%";
                Predicate p1 = criteriaBuilder.like(criteriaBuilder.lower(root.get("nombres")), searchPattern);
                Predicate p2 = criteriaBuilder.like(criteriaBuilder.lower(root.get("apellidos")), searchPattern);
                Predicate p3 = criteriaBuilder.like(criteriaBuilder.lower(root.get("dni")), searchPattern);
                Predicate p4 = criteriaBuilder.like(criteriaBuilder.lower(root.get("correoCorporativo")), searchPattern);
                predicates.add(criteriaBuilder.or(p1, p2, p3, p4));
            }

            // Filtro por estado activo
            if (activo != null) {
                predicates.add(criteriaBuilder.equal(root.get("activo"), activo));
            }

            // Filtro por área (un solo ID)
            if (areaId != null) {
                predicates.add(criteriaBuilder.equal(root.get("area").get("idArea"), areaId));
            }

            // Filtro por cargo (un solo ID)
            if (cargoId != null) {
                predicates.add(criteriaBuilder.equal(root.get("cargo").get("idCargo"), cargoId));
            }

            // Filtro por empresa (un solo ID)
            if (empresaId != null) {
                predicates.add(criteriaBuilder.equal(root.get("empresa").get("id"), empresaId));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }

    // Método para filtros avanzados (con arrays/listas)
    public static Specification<Trabajador> withDynamicFilters(
            String search,
            Boolean activo,
            List<Integer> areaIds,
            List<Integer> cargoIds,
            List<Integer> empresaIds,
            List<Boolean> puedeSerLiquidador,
            List<Boolean> puedeSerEjecutante,
            List<Boolean> puedeSerAnalistaContable,
            List<Boolean> puedeSerJefaturaResponsable,
            List<Boolean> puedeSerCoordinadorTiCw) {

        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            // Filtro por búsqueda de texto
            if (StringUtils.hasText(search)) {
                String searchPattern = "%" + search.toLowerCase() + "%";
                Predicate nombrePredicate = criteriaBuilder.like(
                        criteriaBuilder.lower(root.get("nombres")), searchPattern);
                Predicate apellidoPredicate = criteriaBuilder.like(
                        criteriaBuilder.lower(root.get("apellidos")), searchPattern);
                Predicate dniPredicate = criteriaBuilder.like(
                        criteriaBuilder.lower(root.get("dni")), searchPattern);
                Predicate emailPredicate = criteriaBuilder.like(
                        criteriaBuilder.lower(root.get("correoCorporativo")), searchPattern);

                predicates.add(criteriaBuilder.or(
                        nombrePredicate, apellidoPredicate, dniPredicate, emailPredicate
                ));
            }

            // Filtro por estado activo
            if (activo != null) {
                predicates.add(criteriaBuilder.equal(root.get("activo"), activo));
            }

            // Filtro por áreas (array/list)
            if (areaIds != null && !areaIds.isEmpty()) {
                predicates.add(root.get("area").get("idArea").in(areaIds));
            }

            // Filtro por cargos (array/list)
            if (cargoIds != null && !cargoIds.isEmpty()) {
                predicates.add(root.get("cargo").get("idCargo").in(cargoIds));
            }

            // Filtro por empresas (array/list)
            if (empresaIds != null && !empresaIds.isEmpty()) {
                predicates.add(criteriaBuilder.and(
                        root.get("empresa").isNotNull(),
                        root.get("empresa").get("id").in(empresaIds)
                ));
            }

            // Filtro por roles (campos booleanos)
            if (puedeSerLiquidador != null && !puedeSerLiquidador.isEmpty()) {
                predicates.add(root.get("puedeSerLiquidador").in(puedeSerLiquidador));
            }

            if (puedeSerEjecutante != null && !puedeSerEjecutante.isEmpty()) {
                predicates.add(root.get("puedeSerEjecutante").in(puedeSerEjecutante));
            }

            if (puedeSerAnalistaContable != null && !puedeSerAnalistaContable.isEmpty()) {
                predicates.add(root.get("puedeSerAnalistaContable").in(puedeSerAnalistaContable));
            }

            if (puedeSerJefaturaResponsable != null && !puedeSerJefaturaResponsable.isEmpty()) {
                predicates.add(root.get("puedeSerJefaturaResponsable").in(puedeSerJefaturaResponsable));
            }

            if (puedeSerCoordinadorTiCw != null && !puedeSerCoordinadorTiCw.isEmpty()) {
                predicates.add(root.get("puedeSerCoordinadorTiCw").in(puedeSerCoordinadorTiCw));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}
package org.film.util;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.film.dto.request.filter.FilterContext;
import org.film.dto.request.filter.FilterCriteria;
import org.film.entity.AbstractEntity;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class SpecificationHelper {

    public static <T extends AbstractEntity> Specification<T> createSpecification(FilterContext filterContext, Boolean ignoreRemove) {
        if(filterContext == null) {
            filterContext = new FilterContext();
        }
        return createSpecification(filterContext.getFilter(), ignoreRemove);
    }

    public static <T extends AbstractEntity> Specification<T> createSpecification(List<FilterCriteria> criteriaList, Boolean ignoreRemove) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (!Boolean.TRUE.equals(ignoreRemove)) {
                predicates.add(cb.isFalse(root.get("isDeleted")));
                predicates.add(cb.isNull(root.get("isDeleted")));
            }

            if (criteriaList != null && !criteriaList.isEmpty()) {
                for (FilterCriteria criteria : criteriaList) {
                    if (isValidCriteria(criteria)) {
                        Predicate predicate = buildPredicate(root, cb, criteria);
                        if (predicate != null) {
                            predicates.add(predicate);
                        }
                    }
                }
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }


    private static boolean isValidCriteria(FilterCriteria criteria) {
        return criteria != null
                && StringUtils.hasText(criteria.getField())
                && criteria.getOperator() != null;
    }

    private static <T extends AbstractEntity> Predicate buildPredicate(Root<T> root,
                                                                       CriteriaBuilder cb,
                                                                       FilterCriteria criteria) {

        var path = root.get(criteria.getField());

        switch (criteria.getOperator()) {
            case EQUALS:
                return cb.equal(path, criteria.getValue());

            case NOT_EQUALS:
                return cb.notEqual(path, criteria.getValue());

            case CONTAINS:
                if (criteria.getValue() == null) {
                    return null;
                }
                return cb.like(
                        cb.lower(path.as(String.class)),
                        "%" + criteria.getValue().toLowerCase() + "%"
                );

            case IN:
                if (criteria.getValue() == null) {
                    return null;
                }
                String[] values = criteria.getValue().split(",");
                return path.in((Object[]) values);

            default:
                return null;
        }
    }

}
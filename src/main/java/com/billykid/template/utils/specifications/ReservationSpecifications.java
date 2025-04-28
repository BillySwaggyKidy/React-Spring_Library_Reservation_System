package com.billykid.template.utils.specifications;

import java.util.Date;

import org.springframework.data.jpa.domain.Specification;

import com.billykid.template.entity.Reservation;
import com.billykid.template.entity.DBUser;

import jakarta.persistence.criteria.Join;

public class ReservationSpecifications {
    // Filter by reservation user name
    public static Specification<Reservation> hasUsername(String username) {
        return (root, query, criteriaBuilder) -> {
            if (username == null || username.isEmpty()) {
                return null;
            }
            Join<Reservation, DBUser> userJoin = root.join("user");
            return criteriaBuilder.like(criteriaBuilder.lower(userJoin.<String>get("username")), "%" + username.toLowerCase() + "%");
        };
    }

    // Filter by if the reservation start on/after given date
    public static Specification<Reservation> hasBeginDate(Date beginDate) {
        return (root, query, criteriaBuilder) -> {
            if (beginDate == null) {
                return null;
            }
            return criteriaBuilder.greaterThanOrEqualTo(root.get("beginDate"), beginDate);
        };
    }

    // Filter by if the reservation end on/before given date
    public static Specification<Reservation> hasEndDate(Date endDate) {
        return (root, query, criteriaBuilder) -> {
            if (endDate == null) {
                return null;
            }

            return criteriaBuilder.lessThanOrEqualTo(root.get("endDate"), endDate);
        };
    }
}

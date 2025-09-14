package com.billykid.library.utils.specifications;

import java.time.LocalDate;

import org.springframework.data.jpa.domain.Specification;

import com.billykid.library.entity.DBUser;
import com.billykid.library.entity.Reservation;

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
    public static Specification<Reservation> hasBeginDate(LocalDate beginDate) {
        return (root, query, criteriaBuilder) -> {
            if (beginDate == null) {
                return null;
            }
            return criteriaBuilder.greaterThanOrEqualTo(root.get("beginDate"), beginDate);
        };
    }

    // Filter by if the reservation end on/before given date
    public static Specification<Reservation> hasEndDate(LocalDate endDate) {
        return (root, query, criteriaBuilder) -> {
            if (endDate == null) {
                return null;
            }

            return criteriaBuilder.lessThanOrEqualTo(root.get("endDate"), endDate);
        };
    }
}

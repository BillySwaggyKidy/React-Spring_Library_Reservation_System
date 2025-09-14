package com.billykid.library.utils.specifications;

import org.springframework.data.jpa.domain.Specification;

import com.billykid.library.entity.DBUser;
import com.billykid.library.utils.enums.UserRole;

public class UserSpecifications {
    public static Specification<DBUser> hasUsername(String username) {
        return (root, query, criteriaBuilder) -> {
            if (username == null || username.isEmpty()) {
                return null;
            }
            return criteriaBuilder.like(criteriaBuilder.lower(root.get("username")), "%" + username.toLowerCase() + "%");
        };
    }

    public static Specification<DBUser> hasEmail(String email) {
        return (root, query, criteriaBuilder) -> {
            if (email == null || email.isEmpty()) {
                return null;
            }
            return criteriaBuilder.like(criteriaBuilder.lower(root.get("email")), "%" + email.toLowerCase() + "%");
        };
    }

    public static Specification<DBUser> hasRole(UserRole role) {
        return (root, query, criteriaBuilder) -> {
            if (role == null) {
                return null;
            }
            return criteriaBuilder.equal(root.get("role"),role);
        };
    }

    public static Specification<DBUser> isActive() {
        return (root, query, criteriaBuilder) -> criteriaBuilder.isTrue(root.get("active"));
    }
}

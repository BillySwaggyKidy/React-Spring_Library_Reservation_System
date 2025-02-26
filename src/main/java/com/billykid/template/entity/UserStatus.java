package com.billykid.template.entity;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Table(name="user_status")
public class UserStatus {

    @Id
    @Column(name="id",columnDefinition="char(4)")
    private String id;

    @Column(name = "name", nullable = false, unique = true)
    private String name;
}

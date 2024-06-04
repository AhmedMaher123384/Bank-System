package com.App.BankingSystem.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "roles")
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name ;
    @ManyToMany(mappedBy = "roles", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private Set<Users> users = new HashSet<>();
}

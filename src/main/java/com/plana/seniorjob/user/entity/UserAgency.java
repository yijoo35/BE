package com.plana.seniorjob.user.entity;

import com.plana.seniorjob.agency.entity.Agency;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Setter @Getter
public class UserAgency {
    @Id
    @GeneratedValue
    private Long id;

    private String username;
    private String password;

    @ManyToOne
    private Agency agency;
}

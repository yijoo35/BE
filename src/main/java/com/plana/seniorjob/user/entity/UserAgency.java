package com.plana.seniorjob.user.entity;

import com.plana.seniorjob.agency.entity.Agency;
import com.plana.seniorjob.user.enums.MemberType;
import jakarta.persistence.*;
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

    @Enumerated(EnumType.STRING)
    private MemberType memberType; // AGENCY

    @ManyToOne
    private Agency agency;
}

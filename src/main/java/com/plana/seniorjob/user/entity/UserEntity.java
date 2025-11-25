package com.plana.seniorjob.user.entity;

import com.plana.seniorjob.user.enums.MemberType;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter @Setter
@Builder @AllArgsConstructor @NoArgsConstructor
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String kakaoId;
    private String name;
    private String email;
    private String gender;
    private String phoneNumber;
    private String birthyear;

    @Enumerated(EnumType.STRING)
    private MemberType memberType; //normal

}

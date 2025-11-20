package com.plana.seniorjob.user.repository;

import com.plana.seniorjob.user.entity.UserAgency;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.*;

public interface UserAgencyRepository extends JpaRepository<UserAgency, Long> {
    boolean existsByUsername(String username);
    Optional<UserAgency> findByUsername(String username);
}

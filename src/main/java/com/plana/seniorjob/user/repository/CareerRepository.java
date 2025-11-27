package com.plana.seniorjob.user.repository;

import com.plana.seniorjob.user.entity.Career;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CareerRepository extends JpaRepository<Career, Long> {

    List<Career> findAllByUserResume_ResumeId(Long resumeId);
}
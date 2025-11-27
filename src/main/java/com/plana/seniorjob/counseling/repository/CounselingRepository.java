package com.plana.seniorjob.counseling.repository;

import com.plana.seniorjob.counseling.entity.Counseling;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CounselingRepository extends JpaRepository<Counseling, Long> {

}
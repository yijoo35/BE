package com.plana.seniorjob.counseling.repository;

import com.plana.seniorjob.counseling.entity.Counseling;
import com.plana.seniorjob.counseling.enums.CounselingStatus;
import com.plana.seniorjob.user.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CounselingRepository extends JpaRepository<Counseling, Long> {

    List<Counseling> findByClientAndStatusIn(UserEntity client, List<CounselingStatus> statuses); // <<-- 정의 추가

    List<Counseling> findAllByClientOrderByCreatedAtDesc(UserEntity client);

}
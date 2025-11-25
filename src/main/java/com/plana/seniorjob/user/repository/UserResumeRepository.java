package com.plana.seniorjob.user.repository;

import com.plana.seniorjob.counseling.entity.Counseling;
import com.plana.seniorjob.user.entity.UserEntity;
import com.plana.seniorjob.user.entity.UserResume;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserResumeRepository extends JpaRepository<UserResume, Long> {
    Optional<UserResume> findByClient(UserEntity client);
    Optional<UserResume> findByCounseling(Counseling counseling);
    Optional<UserResume> findByClientAndCounseling(UserEntity client, Counseling counseling);

}
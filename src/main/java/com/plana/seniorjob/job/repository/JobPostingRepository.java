package com.plana.seniorjob.job.repository;

import com.plana.seniorjob.job.entity.JobPosting;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface JobPostingRepository extends JpaRepository<JobPosting, Long> {

    Optional<JobPosting> findByJobId(String jobId);
}

package com.plana.seniorjob.job.repository;

import com.plana.seniorjob.job.entity.JobPosting;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface JobPostingRepository extends JpaRepository<JobPosting, Long> {

    Optional<JobPosting> findByJobId(String jobId);

    @Query("""
    SELECT j
    FROM JobPosting j
    WHERE (:keyword IS NULL
        OR j.recrtTitle LIKE %:keyword%
        OR j.oranNm LIKE %:keyword%
        OR j.jobclsNm LIKE %:keyword%
        OR j.plDetAddr LIKE %:keyword%
    )
    ORDER BY 
        CASE WHEN j.deadline = '마감' THEN 1 ELSE 0 END ASC,
        j.toDd ASC,
        j.createDy DESC
""") //제목, 주소, 직종, 기업이름
    List<JobPosting> searchJobs(@Param("keyword") String keyword);

    @Query("""
    SELECT j
    FROM JobPosting j
    ORDER BY 
        CASE WHEN j.deadline = '마감' THEN 1 ELSE 0 END ASC,
        j.toDd ASC,
        j.createDy DESC
""")
    List<JobPosting> findJobs();
}

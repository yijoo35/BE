package com.plana.seniorjob.agency.repository;

import com.plana.seniorjob.agency.entity.Agency;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.*;

public interface AgencyRepository extends JpaRepository<Agency, Long> {

    Optional<Agency> findByOrgCd(String orgCd);

    @Query("""
    SELECT a FROM Agency a
    WHERE a.orgName LIKE %:keyword%
       OR a.zipAddr LIKE %:keyword%
       OR a.dtlAddr LIKE %:keyword%
""")
    List<Agency> searchByKeyword(@Param("keyword") String keyword);

    @Query("SELECT a FROM Agency a WHERE a.lat IS NULL OR a.lng IS NULL")
    List<Agency> findAllWithoutCoordinates();

    @Query("""
SELECT a
FROM Agency a
WHERE a.lat IS NOT NULL AND a.lng IS NOT NULL
AND (6371 * acos(
            cos(radians(:lat))
          * cos(radians(a.lat))
          * cos(radians(a.lng) - radians(:lng))
          + sin(radians(:lat)) * sin(radians(a.lat))
    )) <= :distance
""")
    List<Agency> findAgenciesWithinDistance(
            @Param("lat") double lat,
            @Param("lng") double lng,
            @Param("distance") double distance
    );

    @Query("""
    SELECT a FROM Agency a
    WHERE a.orgName LIKE CONCAT(:keyword, '%')
""")
    List<Agency> autocomplete(@Param("keyword") String keyword);
}

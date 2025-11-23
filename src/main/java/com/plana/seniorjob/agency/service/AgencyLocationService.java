package com.plana.seniorjob.agency.service;

import com.plana.seniorjob.agency.dto.AgencyDistanceDTO;
import com.plana.seniorjob.agency.entity.Agency;
import com.plana.seniorjob.agency.repository.AgencyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AgencyLocationService {

    private final AgencyRepository agencyRepository;

    public List<AgencyDistanceDTO> findNearby(double lat, double lng) {
        double distanceLimit = 2.0; // 반경 2km

        List<Agency> agencies =
                agencyRepository.findAgenciesWithinDistance(lat, lng, distanceLimit);

        return agencies.stream()
                .map(a -> {

                    double dist = calculateDistance(
                            lat, lng,
                            a.getLat(), a.getLng()
                    );

                    return new AgencyDistanceDTO(
                            a.getOrgCd(),
                            a.getOrgName(),
                            a.getZipAddr(),
                            a.getDtlAddr(),
                            a.getTel(),
                            a.getLat(),
                            a.getLng(),
                            dist,
                            formatDistance(dist)
                    );
                })
                .sorted((a, b) -> Double.compare(a.getDistanceKm(), b.getDistanceKm()))
                .toList();
    }

    public double calculateDistance(double lat1, double lng1, double lat2, double lng2) {
        double R = 6371;
        double dLat = Math.toRadians(lat2 - lat1);
        double dLng = Math.toRadians(lng2 - lng1);

        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(dLng / 2) * Math.sin(dLng / 2);

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        return R * c;  // km
    }

    public String formatDistance(double km) {
        if (km < 1) {
            int meters = (int) (km * 1000);
            return meters + "m";
        }
        return String.format("%.1fkm", km);
    }
}

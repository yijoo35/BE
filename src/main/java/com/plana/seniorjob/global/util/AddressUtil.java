package com.plana.seniorjob.global.util;

import org.springframework.stereotype.Component;

@Component
public class AddressUtil {

    public String extractRegion(String plDetAddr) {

        if (plDetAddr == null || plDetAddr.isBlank()) return plDetAddr;

        String addr = plDetAddr.trim();
        if (Character.isDigit(addr.charAt(0))) {
            int spaceIdx = addr.indexOf(" ");
            if (spaceIdx > 0) {
                addr = addr.substring(spaceIdx + 1);
            }
        }

        String[] parts = addr.split(" ");
        if (parts.length < 2) return addr;

        StringBuilder region = new StringBuilder();

        region.append(parts[0]);

        for (int i = 1; i < parts.length; i++) {
            if (parts[i].endsWith("시") || parts[i].endsWith("군") || parts[i].endsWith("구")) {
                region.append(" ").append(parts[i]);
            }
        }

        return region.toString();
    }
}
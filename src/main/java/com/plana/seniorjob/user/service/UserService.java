package com.plana.seniorjob.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Year;

@Service
@RequiredArgsConstructor
public class UserService {
    public Integer calculateKoreanAge(String birthYearString){
        if(birthYearString == null || birthYearString.length() != 4){
            return null;
        }

        try {
            int currentYear = Year.now().getValue();
            int birthYear = Integer.parseInt(birthYearString);

            return currentYear - birthYear+1;
        }catch(NumberFormatException e){
            return null;
        }
    }
}

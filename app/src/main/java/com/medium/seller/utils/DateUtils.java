package com.medium.seller.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class DateUtils {

    public static int calculateAge(String birthDateStr) {
        if (birthDateStr == null || birthDateStr.isEmpty()) return 0;
        
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        try {
            Date birthDate = sdf.parse(birthDateStr);
            Calendar birth = Calendar.getInstance();
            birth.setTime(birthDate);
            
            Calendar today = Calendar.getInstance();
            
            int age = today.get(Calendar.YEAR) - birth.get(Calendar.YEAR);
            
            if (today.get(Calendar.DAY_OF_YEAR) < birth.get(Calendar.DAY_OF_YEAR)) {
                age--;
            }
            
            return age;
        } catch (ParseException e) {
            e.printStackTrace();
            return 0;
        }
    }
}

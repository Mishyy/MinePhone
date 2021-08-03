package com.celerry.minephone.util;

import org.bukkit.World;

public final class TimeUtils {
    private static String[] phases = {"Full Moon", "Waning Gibbous", "Third Quarter", "Waning Crescent", "New Moon", "Waxing Crescent", "First Quarter", "Waxing Gibbous"};

    public static int getMoonPhase(World world) {
        int days = (int) world.getFullTime() / 24000;
        int phase = days % 8;
        return phase;
    }

    public static String getMoonPhaseString(World world) {
        int phase = getMoonPhase(world);
        return phases[phase];
    }

    public static String getTime(World world) {
        long time = world.getTime();
        String mins = String.valueOf((float)time % 1000.0F / 1000.0F * 60.0F);
        String minutes = String.valueOf(Float.valueOf(mins).intValue());
        if (Integer.parseInt(minutes) < 10 && Integer.parseInt(minutes) > 0) {
            minutes = "0" + minutes;
        } else if (Integer.parseInt(minutes) == 0) {
            minutes = minutes + "0";
        }

        long hours = time / 1000L + 6L;
        if (hours > 24L) {
            hours -= 24L;
        }

        // Fix the "24AM" error
        if(hours == 24) {
            hours = 0;
        }

        long twelve_hours = hours;
        String day_half = "AM";
        if (hours >= 12L && hours < 24L) {
            twelve_hours = hours - 12L;
            day_half = "PM";
        }

        // Fix the "0:00 PM" error when it should be "12:00 PM"
        if(hours == 12) {
            twelve_hours = 12;
        }

        return twelve_hours + ":" + minutes + " " + day_half;
    }

}

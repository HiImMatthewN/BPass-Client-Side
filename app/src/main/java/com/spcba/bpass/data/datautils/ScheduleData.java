package com.spcba.bpass.data.datautils;

import com.spcba.bpass.data.datamodels.Trip;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class ScheduleData {
    public static ArrayList<Trip> getMarketMarketSched(){
        ArrayList<Trip> trips = new ArrayList<>();
        trips.add(new Trip(getSchedule(26,5,2021),"Alabang Starmall","Market-Market","7:00AM"
                ,"7:30AM",55));
        trips.add(new Trip(getSchedule(26,5,2021),"Alabang Starmall","Market-Market","8:00AM"
                ,"8:30AM",55));
        trips.add(new Trip(getSchedule(26,5,2021),"Alabang Starmall","Market-Market","9:00AM"
                ,"9:30AM",55));
        trips.add(new Trip(getSchedule(26,5,2021),"Alabang Starmall","Market-Market","10:00AM"
                ,"10:30AM",55));
        trips.add(new Trip(getSchedule(26,5,2021),"Alabang Starmall","Market-Market","11:00AM"
                ,"11:30AM",55));
        trips.add(new Trip(getSchedule(26,5,2021),"Alabang Starmall","Market-Market","12:0PM"
                ,"12:30PM",55));
        trips.add(new Trip(getSchedule(26,5,2021),"Alabang Starmall","Market-Market","13:00PM"
                ,"13:3PAM",55));
        trips.add(new Trip(getSchedule(26,5,2021),"Alabang Starmall","Market-Market","14:00PM"
                ,"14:30PM",55));



        return trips;
    }
    public static ArrayList<Trip> getMegaMallSched(){
        ArrayList<Trip> trips = new ArrayList<>();
        //To Megamall
        trips.add(new Trip(getSchedule(26,5,2021),"Alabang Starmall","Megamall","7:15AM"
                ,"8:15AM",75));
        trips.add(new Trip(getSchedule(26,5,2021),"Alabang Starmall","Megamall","8:15AM"
                ,"9:15AM",75));
        trips.add(new Trip(getSchedule(26,5,2021),"Alabang Starmall","Megamall","9:15AM"
                ,"10:15AM",75));
        trips.add(new Trip(getSchedule(26,5,2021),"Alabang Starmall","Megamall","10:15AM"
                ,"11:15AM",75));
        trips.add(new Trip(getSchedule(26,5,2021),"Alabang Starmall","Megamall","11:15AM"
                ,"12:15PM",75));
        trips.add(new Trip(getSchedule(26,5,2021),"Alabang Starmall","Megamall","12:15PM"
                ,"13:15PM",75));
        trips.add(new Trip(getSchedule(26,5,2021),"Alabang Starmall","Megamall","13:15PM"
                ,"14:15PM",75));
        trips.add(new Trip(getSchedule(26,5,2021),"Alabang Starmall","Megamall","14:15PM"
                ,"15:15PM",75));
        return trips;
    }
    public static ArrayList<Trip> getCubaoSchedule(){
        ArrayList<Trip> trips = new ArrayList<>();
        //To Megamall
        trips.add(new Trip(getSchedule(26,5,2021),"Alabang Starmall","Cubao","7:45AM"
                ,"9:00AM",75));
        trips.add(new Trip(getSchedule(26,5,2021),"Alabang Starmall","Cubao","8:45AM"
                ,"19:00AM",75));
        trips.add(new Trip(getSchedule(26,5,2021),"Alabang Starmall","Cubao","9:45AM"
                ,"11:00AM",75));
        trips.add(new Trip(getSchedule(26,5,2021),"Alabang Starmall","Cubao","10:45AM"
                ,"12:00PM",75));
        trips.add(new Trip(getSchedule(26,5,2021),"Alabang Starmall","Cubao","11:45AM"
                ,"13:00PM",75));
        trips.add(new Trip(getSchedule(26,5,2021),"Alabang Starmall","Cubao","12:45PM"
                ,"14:00PM",75));
        trips.add(new Trip(getSchedule(26,5,2021),"Alabang Starmall","Cubao","13:45PM"
                ,"15:00PM",75));
        trips.add(new Trip(getSchedule(26,5,2021),"Alabang Starmall","Cubao","14:45PM"
                ,"16:00PM",75));
        return trips;



    }
    public static Date getSchedule(int day,int month,int year){
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_MONTH,day);
        calendar.set(Calendar.MONTH,month-1);
        calendar.set(Calendar.YEAR,year);

        return calendar.getTime();



    }

}

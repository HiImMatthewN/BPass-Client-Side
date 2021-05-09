package com.spcba.bpass.data.datautils;

import com.spcba.bpass.data.datamodels.Destination;

import java.util.ArrayList;

public class ScheduleData {
    public static ArrayList<Destination> getMarketMarketSched(){
        ArrayList<Destination> destinations = new ArrayList<>();
        destinations.add(new Destination("Alabang Starmall","Market-Market","7:00AM"
                ,"7:30AM",55));
        destinations.add(new Destination("Alabang Starmall","Market-Market","8:00AM"
                ,"8:30AM",55));
        destinations.add(new Destination("Alabang Starmall","Market-Market","9:00AM"
                ,"9:30AM",55));
        destinations.add(new Destination("Alabang Starmall","Market-Market","10:00AM"
                ,"10:30AM",55));
        destinations.add(new Destination("Alabang Starmall","Market-Market","11:00AM"
                ,"11:30AM",55));
        destinations.add(new Destination("Alabang Starmall","Market-Market","12:0PM"
                ,"12:30PM",55));
        destinations.add(new Destination("Alabang Starmall","Market-Market","13:00PM"
                ,"13:3PAM",55));
        destinations.add(new Destination("Alabang Starmall","Market-Market","14:00PM"
                ,"14:30PM",55));



        return destinations;
    }
    public static ArrayList<Destination> getMegaMallSched(){
        ArrayList<Destination> destinations = new ArrayList<>();
        //To Megamall
        destinations.add(new Destination("Alabang Starmall","Megamall","7:15AM"
                ,"8:15AM",75));
        destinations.add(new Destination("Alabang Starmall","Megamall","8:15AM"
                ,"9:15AM",75));
        destinations.add(new Destination("Alabang Starmall","Megamall","9:15AM"
                ,"10:15AM",75));
        destinations.add(new Destination("Alabang Starmall","Megamall","10:15AM"
                ,"11:15AM",75));
        destinations.add(new Destination("Alabang Starmall","Megamall","11:15AM"
                ,"12:15PM",75));
        destinations.add(new Destination("Alabang Starmall","Megamall","12:15PM"
                ,"13:15PM",75));
        destinations.add(new Destination("Alabang Starmall","Megamall","13:15PM"
                ,"14:15PM",75));
        destinations.add(new Destination("Alabang Starmall","Megamall","14:15PM"
                ,"15:15PM",75));
        return destinations;
    }
    public static ArrayList<Destination> getCubaoSchedule(){
        ArrayList<Destination> destinations = new ArrayList<>();
        //To Megamall
        destinations.add(new Destination("Alabang Starmall","Cubao","7:45AM"
                ,"9:00AM",75));
        destinations.add(new Destination("Alabang Starmall","Cubao","8:45AM"
                ,"19:00AM",75));
        destinations.add(new Destination("Alabang Starmall","Cubao","9:45AM"
                ,"11:00AM",75));
        destinations.add(new Destination("Alabang Starmall","Cubao","10:45AM"
                ,"12:00PM",75));
        destinations.add(new Destination("Alabang Starmall","Cubao","11:45AM"
                ,"13:00PM",75));
        destinations.add(new Destination("Alabang Starmall","Cubao","12:45PM"
                ,"14:00PM",75));
        destinations.add(new Destination("Alabang Starmall","Cubao","13:45PM"
                ,"15:00PM",75));
        destinations.add(new Destination("Alabang Starmall","Cubao","14:45PM"
                ,"16:00PM",75));
        return destinations;



    }


}

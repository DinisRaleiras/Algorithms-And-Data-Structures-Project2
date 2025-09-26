/**
 * @author Dinis Raleiras (67819) d.raleiras@campus.fct.unl.pt
 * @author Filipe Nobre (67850) fm.nobre@campus.fct.unl.pt
 * @Fase 2
 */

import java.io.*;
import java.util.Scanner;

import dataStructures.*;
import Exceptions.*;
import Railway.*;
import Schedule.*;
import Date.*;
import Station.*;
import Line.*;

public class Main {

    // User commands
    private static final String INSERT_LINE = "IL";
    private static final String REMOVE_LINE ="RL";
    private static final String LIST_STATIONS = "CL";
    private static final String INSERT_SCHEDULE = "IH";
    private static final String REMOVE_SCHEDULE = "RH";
    private static final String LIST_SCHEDULES = "CH";
    private static final String BEST_SCHEDULE = "MH";
    private static final String END_APPLICATION = "TA";
    private static final String LIST_LINES = "CE";
    private static final String LIST_SCHEDULES_BY_STATION = "LC";

    //Messages
    private static final String SUCCESS_LINE  ="Inserção de linha com sucesso.";
    private static final String REMOVE_LINE_SUCCESS = "Remoção de linha com sucesso.";
    private static final String SUCCESS_SCHEDULE = "Criação de horário com sucesso.";
    private static final String APP_TERMINATED = "Aplicação terminada.";
    private static final String INVALID_COMMAND = "Comando inválido.";
    private static final String REMOVED_SCHEDULE = "Remoção de horário com sucesso.";
    private static final String TRAIN_MESSAGE = "Comboio %d ";


    // Exceptions
    private static final String EXISTING_LINE = "Linha existente.";
    private static final String INEXISTENT_LINE = "Linha inexistente.";
    private static final String INVALID_SCHEDULE = "Horário inválido.";
    private static final String INEXISTENT_SCHEDULE = "Horário inexistente.";
    private static final String INEXISTENT_DEPARTURE_STATION = "Estação de partida inexistente.";
    private static final String IMPOSSIBLE_ROUTE = "Percurso impossível.";
    private static final String INEXISTENT_STATION = "Estação inexistente.";

    // Data file
    private static final String DATA_FILE = "storedData.dat";

    public static void main(String[] args) {
        Main.executeCommand();
    }

    /**
     * Executes the commands given by the user.
     */
    private static void executeCommand() {
        Railway railway = load();
        Scanner in = new Scanner(System.in);
        String command = in.next().toUpperCase();
        while (!command.equals(END_APPLICATION)) {
            switch (command) {
                case INSERT_LINE -> insertLine(railway, in);
                case REMOVE_LINE -> removeLine(railway, in);
                case LIST_STATIONS -> listStations(railway, in);
                case INSERT_SCHEDULE -> insertSchedule(railway, in);
                case REMOVE_SCHEDULE -> removeSchedule(railway, in);
                case LIST_SCHEDULES -> listSchedules(railway, in);
                case BEST_SCHEDULE -> bestSchedule(railway, in);
                case LIST_LINES -> listLines(railway, in);
                case LIST_SCHEDULES_BY_STATION -> listSchedulesByStation(railway, in);
                default -> invalidCommand(in);
            }
            command = in.next().toUpperCase();
        }
        System.out.println(APP_TERMINATED);
        save(railway);
    }

    /**
     * Lists the schedules of a station.
     * @param railway
     * @param in
     */
    private static void listSchedulesByStation(Railway railway, Scanner in) {
        String stationName = in.nextLine().trim();
        try {
            Iterator<Entry<Integer, Date>> it = railway.stationSchedulesIterator(stationName);
            while(it.hasNext()){
                Entry<Integer, Date> entry = it.next();
                System.out.printf(TRAIN_MESSAGE, entry.getKey());
                toString(entry.getValue());
            }

        }catch(StationDoesNotExistException e){
            System.out.println(INEXISTENT_STATION);
        }
    }

    /**
     * Inserts a new line in the Railway.
     * @param railway : the Railway where the line will be inserted
     * @param in     : the Scanner used to read the input
     */
    private static void insertLine(Railway railway, Scanner in) {
    	String lineName = in.nextLine().trim();
        Queue<String> stationsNames = new QueueInList<>();
        String stationName = in.nextLine().trim();
        while (!stationName.equals("")) {
            stationsNames.enqueue(stationName);
            stationName = in.nextLine().trim();
        }
        try {
            railway.addLine(lineName, stationsNames);
            System.out.println(SUCCESS_LINE);
        }catch(LineAlreadyExistsException e){
            System.out.println(EXISTING_LINE);
        }

    }

    /**
     * Removes a line in the Railway.
     * @param railway : the Railway where the line is going to be removed
     * @param in      : the Scanner used to read the input
     */
    private static void removeLine(Railway railway, Scanner in) {
    	String lineName = in.nextLine().trim();
        try{
            railway.removeLine(lineName);
            System.out.println(REMOVE_LINE_SUCCESS);
        }catch(LineDoesNotExistException e){
            System.out.println(INEXISTENT_LINE);
        }
    }

    /**
     * Lists the stations of a line.
     * @param railway : the Railway where the line is to be found and list its stations.
     * @param in      : the Scanner used to read the input
     */
    private static void listStations(Railway railway, Scanner in) {
        String lineName = in.nextLine().trim();
        try{
            Iterator<Station> it = railway.lineStationsIterator(lineName);
            while(it.hasNext()){
                System.out.println(it.next().getName());
            }
        }catch(LineDoesNotExistException e){
            System.out.println(INEXISTENT_LINE);
        }

    }

    /**
     * Inserts a new schedule in a line in the Railway.
     * @param railway : the Railway where the schedule will be inserted
     * @param in : the Scanner used to read the input
     */
    private static void insertSchedule(Railway railway, Scanner in) {
        String lineName = in.nextLine().trim();
        int trainNumber = in.nextInt();
        in.nextLine();
        Queue<Entry<String, Entry<Integer, Integer>>> schedule = new QueueInList<>();
        String line;

        do {
            line = in.nextLine().trim();
            if (!line.isEmpty()) {
                schedule.enqueue(parseStationAndTime(line));
            }
        } while (!line.isEmpty());

        try {
            railway.addSchedule(lineName, trainNumber, schedule);
            System.out.println(SUCCESS_SCHEDULE);
        } catch (LineDoesNotExistException e) {
            System.out.println(INEXISTENT_LINE);
        } catch (InvalidScheduleException e) {
            System.out.println(INVALID_SCHEDULE);
        }
    }

    /**
     * Removes a schedule in a line in the Railway.
     * @param railway : the Railway where the schedule is going to be removed
     * @param in : the Scanner used to read the input
     */
    private static void removeSchedule(Railway railway, Scanner in) {
        String lineName = in.nextLine().trim();
        String line = in.nextLine().trim();
        Entry<String, Entry<Integer, Integer>> stationAndTime = parseStationAndTime(line);
        String stationName = stationAndTime.getKey();
        int hours = stationAndTime.getValue().getKey();
        int minutes = stationAndTime.getValue().getValue();

        try {
            railway.removeSchedule(lineName, stationName, hours, minutes);
            System.out.println(REMOVED_SCHEDULE);
        } catch (LineDoesNotExistException e) {
            System.out.println(INEXISTENT_LINE);
        } catch (ScheduleDoesNotExistException e) {
            System.out.println(INEXISTENT_SCHEDULE);
        }
    }

    /**
     * Lists the schedule of all the trains in a line.
     * @param railway : the Railway where the line is to be found and list its schedules.
     * @param in : the Scanner used to read the input
     */
    private static void listSchedules(Railway railway, Scanner in) {
    	String lineName = in.nextLine().trim();
        String stationName = in.nextLine().trim();
        try {
            Iterator<Entry<Date, Schedule>> it = railway.lineSchedulesIterator(lineName, stationName);
            while(it.hasNext()){
                Schedule schedule = it.next().getValue();
                System.out.println(schedule.getNumber());
                Iterator<Entry<Station, Date>> it2 = schedule.scheduleIterator();
                while(it2.hasNext()){
                    Entry<Station, Date> entry = it2.next();
                    System.out.print(entry.getKey().getName() + " ");
                    toString(entry.getValue());
                }
            }
        }catch(LineDoesNotExistException e){
            System.out.println(INEXISTENT_LINE);
        }catch(StationDoesNotExistException e) {
            System.out.println(INEXISTENT_DEPARTURE_STATION);
        }
    }

    /**
     * Gives the best schedule between all schedules of a line.
     * @param railway : the Railway where the stations are to be found
     * @param in : the Scanner used to read the input
     */
    private static void bestSchedule(Railway railway, Scanner in) {
    	String lineName = in.nextLine().trim();
        String departureStation = in.nextLine().trim();
        String arrivalStation = in.nextLine().trim();
        String[] date = in.nextLine().split(":");
        int hours = Integer.parseInt(date[0]);
        int minutes = Integer.parseInt(date[1]);
        try{
            Schedule schedule = railway.bestSchedule(lineName, departureStation, arrivalStation, hours, minutes);
            Iterator<Entry<Station, Date>> it = schedule.scheduleIterator();
            System.out.println(schedule.getNumber());
            while(it.hasNext()){
                Entry<Station, Date> entry = it.next();
                System.out.print(entry.getKey().getName() + " ");
                toString(entry.getValue());
            }
        }catch(LineDoesNotExistException e){
            System.out.println(INEXISTENT_LINE);
    }   catch(StationDoesNotExistException e){
            System.out.println(INEXISTENT_DEPARTURE_STATION);
        }catch(ImpossibleRouteException e){
            System.out.println(IMPOSSIBLE_ROUTE);
        }
    }

    /**
     * Loads the object Railway, stored in a file.
     * @return : the Railway
     */
    private static Railway load() {
        Railway railway = null;
        try {
            FileInputStream file = new FileInputStream(DATA_FILE);
            ObjectInputStream input = new ObjectInputStream(file);
            railway = (Railway) input.readObject();
            input.close();
            file.close();
        } catch (FileNotFoundException e) {
            railway = new RailwayClass();
        } catch (ClassNotFoundException e) {
            railway = new RailwayClass();
        } catch (IOException e) {
            railway = new RailwayClass();
        }
        return railway;
    }

    /**
     * Saves the object Railway in a file.
     * @param railway
     */
    private static void save(Railway railway) {
        try {
            FileOutputStream file = new FileOutputStream(DATA_FILE);
            ObjectOutputStream output = new ObjectOutputStream(file);
            output.writeObject(railway);
            output.flush();
            output.close();
            file.close();
        } catch (IOException e) {
        	
        }
    }

    /**
     * Lists the lines of a station.
     * @param railway
     * @param in
     */
    private static void listLines(Railway railway, Scanner in) {
        String stationName = in.nextLine().trim();
        try {
            Iterator<Line> it = railway.stationLinesIterator(stationName);
            while(it.hasNext()) {
                Line line = it.next();
                System.out.println(line.getName());
            }
        }catch(StationDoesNotExistException e) {
            System.out.println(INEXISTENT_STATION);
        }
    }
    
    /**
     * It handles invalid command situations
     * @param in
     */
    private static void invalidCommand(Scanner in) {
    	System.out.println(INVALID_COMMAND);
    	in.nextLine();
    }

    /**
     * Prints a string representation of the date.
     */
    private static void toString(Date date){
        if(date.getHour() >= 10  && date.getMinutes() >= 10){
            System.out.println(Integer.toString(date.getHour()) + ":" + Integer.toString(date.getMinutes()));
        }else if(date.getHour() >= 10){
            System.out.println(Integer.toString(date.getHour()) + ":0" + Integer.toString(date.getMinutes()));
        }else if(date.getMinutes() >= 10){
            System.out.println("0" + Integer.toString(date.getHour()) + ":" + Integer.toString(date.getMinutes()));
        }else{
            System.out.println("0" + Integer.toString(date.getHour()) + ":0" + Integer.toString(date.getMinutes()));
        }
    }

    /**
     * Parses a line to a station and a time.
     * @param line
     * @return the station and the time
     */
    private static Entry<String, Entry<Integer, Integer>> parseStationAndTime(String line) {
        String[] args = line.split(" ");
        StringBuilder stationNameBuilder = new StringBuilder();

        for (int i = 0; i < args.length - 1; i++) {
            if (i > 0) stationNameBuilder.append(" ");
            stationNameBuilder.append(args[i]);
        }

        String stationName = stationNameBuilder.toString();
        String[] timeParts = args[args.length - 1].split(":");
        int hours = Integer.parseInt(timeParts[0]);
        int minutes = Integer.parseInt(timeParts[1]);

        return new EntryClass<>(stationName, new EntryClass<>(hours, minutes));
    }
}

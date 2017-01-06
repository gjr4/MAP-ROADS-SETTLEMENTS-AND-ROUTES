

import java.io.IOException;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;

/**
 * Main class to test the Road and Settlement classes
 *
 * @author Chris Loftus (add your name and change version number/date)
 * @version 1.0 (24th February 2016)
 */
public class Application {

    private Scanner scan;
    private Map map;

    public Application() {
        scan = new Scanner(System.in);
        map = new Map();
    }

    private void runMenu() throws IOException {
        String userChoice;
        do {
            printMenu();
            userChoice = scan.next();
            switch (userChoice) {
                case "1": // create a settlement
                    createNewsettlement();
                    break;
                case "2": // delete a settlement
                    deleteSettlement();
                    break;
                case "3": // create a road
                    createNewroad();
                    break;
                case "4": // delete a road
                    deleteRoad();
                    break;
                case "5": //display map
                    map.display();
                    break;
                case "6"://save settlementList
                    save();
                    break;
                case "7": // quit the session
                    break;
                default:
                    System.out.println("Invalid entry please re-enter a value between 1-7");
            }
        } while (!userChoice.equals("7")); // keeps looping meni whilst !quit
    }

    private void createNewsettlement() { // creates user settlement
        Settlement settlement;
        String settlementName;

        settlementName = askForsettlementname();
        SettlementType settlementType = askForsettlementtype();
        int settlementpopulation = getPopulation();
        settlement = new Settlement(settlementName, settlementType);
        settlement.setPopulation(settlementpopulation);
        map.addSettlement(settlement);
        System.out.println("settlement entered sucessfully");
    }

    private int getPopulation() { // when user enters population this is run
        Boolean valid;
        int population = 0;

        do {
            try {
                valid = true;
                System.out.println("Enter settlement population:");
                population = scan.nextInt();
            } catch (InputMismatchException e) {// catches the error of a integer is not entered
                scan.next();
                valid = false;
                System.out.println("Invalid value. Try again.");
            }
        } while (!valid);
        return population;
    }

    private String askForsettlementname() {
        Boolean valid = false;
        String settlementName;

        do {
            System.out.println("Enter settlement name:");
            settlementName = scan.next();
            if (map.searchForsettlement(settlementName) == null)
                valid = true;
            else
                System.out.println("Settlement already exists!");
        } while (!valid);
        return settlementName;
    }

    private SettlementType askForsettlementtype() {
        SettlementType result = null;
        boolean valid;
        do {
            valid = false;
            System.out.print("Enter a settlement type ");
            for (SettlementType sType : SettlementType.values()) {
                System.out.print(sType + " "); // prints out the options that can be entered for settlement type
            }
            System.out.println(":");
            String choice = scan.next().toUpperCase();
            try {
                result = SettlementType.valueOf(choice);
                valid = true;
            } catch (IllegalArgumentException iae) {
                System.out.println(choice + " is not one of the options. Try again.");
            }
        } while (!valid);
        return result;
    }

    private void createNewroad() { // creates new user defined road
        Settlement sourceSettlement, destinationSettlement;
        Boolean valid;

        System.out.println("Enter Road name");
        String roadName = scan.next();
        Classification roadClass = askForRoadClassifier();
        System.out.println("Source Settlement");
        sourceSettlement = getRoadsettlement();
        System.out.println("Destination settlement");
        destinationSettlement = getRoadsettlement();
        double length = getRoadlength();
        valid = true;
        for (Road road : sourceSettlement.findRoads(roadName)) {
            if (road.getDestinationSettlement().equals(destinationSettlement)) {
                valid = false;
            }
        }
        if (!valid)
            System.out.println("Road " + roadName + " already exists from " + sourceSettlement.getName() +
                    " to " + destinationSettlement.getName());
        else {

            new Road(roadName, roadClass, sourceSettlement, destinationSettlement, length);
        }
    }

    private Settlement getRoadsettlement() {
        Boolean valid = false;
        String settlementName;
        Settlement settlement;

        do {
            System.out.println("Enter settlement name:");
            settlementName = scan.next();
            settlement = map.searchForsettlement(settlementName); // searches for the settlement by name reference
            if (settlement == null)
                System.out.println("Settlement does not exist!");
            else
                valid = true;
        } while (!valid);
        return settlement;
    }

    private Classification askForRoadClassifier() {
        Classification result = null;
        boolean valid;
        do {
            valid = false;
            System.out.println("Enter a road classification: ");
            for (Classification cls : Classification.values()) {
                System.out.print(cls + " "); // similar to when asking for settlement type. prints possibilities
            }
            System.out.println(":");
            String choice = scan.next().toUpperCase();
            try {
                result = Classification.valueOf(choice);
                valid = true;
            } catch (IllegalArgumentException iae) {
                System.out.println(choice + " is not one of the options. Try again.");
            }
        } while (!valid);
        return result;
    }

    private double getRoadlength() {
        Boolean valid;
        double length = 0;

        do {
            try {
                valid = true;
                System.out.println("Enter road length:");
                length = scan.nextDouble();
            } catch (InputMismatchException e) { //catches for a non bouble input
                scan.next();
                valid = false;
                System.out.println("Invalid value. Try again.");
            }
        } while (!valid);
        return length;
    }


    private void save() throws IOException {
        map.save();
    }

    private void load() {
        map.load();
    }

    private void deleteRoad() { // deletes road by forst searching that it exists and then which two settlements to delete for
        System.out.println("Enter road name to delete:");
        String roadName = scan.next();
        System.out.println("Enter source settlement name:");
        String sourceName = scan.next();
        System.out.println("Enter destination settlement name:");
        String destinationName = scan.next();
        Settlement source = map.searchForsettlement(sourceName);
        if (source == null)
            System.out.println("Source settlement " + sourceName + " does not exist");
        else {
            Settlement destination = map.searchForsettlement(destinationName);
            if (destination == null)
                System.out.println("Destination settlement " + destinationName + " does not exist");
            else {
                Boolean valid = false;
                for (Road road : source.findRoads(roadName)) {
                    if (road.getDestinationSettlement().equals(destination)) {
                        source.delete(road);
                        System.out.println("Road " + roadName + " running from " + sourceName + " to " + destinationName + " deleted!");
                        valid = true;
                    }
                }
                if (!valid)
                    System.out.println("Settlement " + sourceName + " does not have road " + roadName);
            }

        }
    }

    private void deleteSettlement() {
        System.out.println("What settlement do you wish to delete?");
        String userinName = scan.next();
        Settlement foundSettlement = map.searchForsettlement(userinName);
        if (foundSettlement != null) {
            foundSettlement.deleteRoads(); // deletes all roads for userinname if it is found
            map.settlements.remove(map.settlements.indexOf(foundSettlement));
            System.out.println("Deleted " + foundSettlement.getName() + " and all it's roads");
        } else
            System.out.println("Settlement not found");

    }


    private void printMenu() {
        System.out.println("1-Create Settlement");
        System.out.println("2-Delete Settlement");
        System.out.println("3-Create Road");
        System.out.println("4-Delete Road");
        System.out.println("5-Display Map");
        System.out.println("6-Save");
        System.out.println("7-Quit");
    }

    public static void main(String args[]) throws IOException {
        Application app = new Application();
        app.load();
        app.runMenu();
        app.save();
    }

}

import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * @author Chris Loftus
 * @version 1.0 (25th February 2016)
 */

public class Map {

    public ArrayList<Settlement> settlements;

    public Map() {
        settlements = new ArrayList<Settlement>();
    }

    /**
     * In this version we display the result of calling toString on the command
     * line. Future versions may display graphically
     */
    public void display() {
        System.out.println(toString());
    }

    public void addSettlement(Settlement newSettlement) throws IllegalArgumentException {
        settlements.add(newSettlement);
    }

    public Settlement searchForsettlement(String settlementName) {

        for (Settlement current : settlements) {
            if (current.getName().equalsIgnoreCase(settlementName)) {
                return current;
            }
        }
        return null;
    }

    // STEPS 7-10: INSERT METHODS HERE, i.e. those similar to addSettlement and required
    // by the Application class

    public void load() {
        File file = new File("settlements.txt");
        try {
            Scanner settlementFile = new Scanner(file);
            while (settlementFile.hasNext()) {
                String inData = settlementFile.nextLine();
                String[] params = inData.split(":", 3); // use split function here  to load in correct order
                String settlementName = params[0];
                int population = Integer.parseInt(params[1]);
                SettlementType sType = SettlementType.valueOf(params[2]);
                Settlement settlement = new Settlement(settlementName, sType);
                settlement.setPopulation(population);
                addSettlement(settlement);
            }
            settlementFile.close();

            File file1 = new File("roads.txt");
            Scanner roadsFile = new Scanner(file1);
            while (roadsFile.hasNext()) {
                String inData = roadsFile.nextLine();
                String[] params = inData.split(":", 5); // do the same thing here with split as above
                String rName = params[0];
                Classification rClass = Classification.valueOf(params[1]);
                String sourceName = params[2];
                String destName = params[3];
                double length = Double.valueOf(params[4]); // need to change length to a double
                Settlement sourceSettlement = searchForsettlement(sourceName);
                Settlement destinationSettlement = searchForsettlement(destName);
                Boolean exists = false;
                for (Road road : sourceSettlement.findRoads(rName)) {
                    if (road.getDestinationSettlement().equals(destinationSettlement)) {
                        exists = true;
                    }
                }
                if (!exists)
                    new Road(params[0], Classification.valueOf(params[1]), sourceSettlement, destinationSettlement,
                            Double.valueOf(params[4]));
            }
            roadsFile.close();
            System.out.println("Map loaded");
        } catch (FileNotFoundException e) {
            System.out.println("No data exists - empty DB");
        }

    }


    public void save() throws IOException {
        PrintWriter settlementsFile = new PrintWriter(new FileWriter("settlements.txt"));
        for (Settlement current : settlements) {
            settlementsFile.println(current.getName() + ":" + current.getPopulation() + ":" + current.getKind());
        }
        settlementsFile.close();

        PrintWriter roadsfile = new PrintWriter(new FileWriter("roads.txt"));
        for (Settlement current : settlements)
            for (Road rCurrent : current.getAllRoads())
                roadsfile.println(rCurrent.getName() + ":" + rCurrent.getClassification() + ":"
                        + rCurrent.getSourceSettlement().getName() + ":" + rCurrent.getDestinationSettlement().getName()
                        + ":" + rCurrent.getLength());

        System.out.println("Map saved");
        roadsfile.close();

    }

    public String toString() { // saves the whole tostring print inside result as specified
        String result = "Map Settlements:\n";
        for (Settlement current : settlements) {
            result += "Settlement name = " + current.getName() + "\n";
            result += "Population = " + current.getPopulation() + "\n";
            result += "Kind = " + current.getKind() + "\n";
            result += "Roads =\n";
            for (Road cRoad : current.getAllRoads()) {
                result += cRoad.getName() + " connects " + cRoad.getSourceSettlement().getName()
                        + " to " + cRoad.getDestinationSettlement().getName() + "\n";
            }
            result += "\n";
        }


        result += "\n\nMap roads:\n";
        for (Settlement current : settlements) {
            for (Road cRoad : current.getAllRoads())
                if (cRoad.getSourceSettlement().equals(current)) {
                    result += "Road name = " + cRoad.getName() + "\n";
                    result += "Classification = " + cRoad.getClassification() + "\n";
                    result += "Length = " + cRoad.getLength() + "\n";
                    result += "Source Settlement = " + cRoad.getSourceSettlement().getName() + "\n";
                    result += "Destination Settlement = " + cRoad.getDestinationSettlement().getName() + "\n\n";
                }

        }
        return result;
    }
}

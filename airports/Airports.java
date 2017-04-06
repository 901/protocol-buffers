import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.server.*;
import AirportData.AirportDataProto.Airport;
import AirportData.AirportDataProto.AirportList;
import java.util.*;
import java.io.*;
import java.lang.Math;

// this is the class with remote methods

public class Airports
  extends UnicastRemoteObject
  implements AirportsInterface {

    public List<Airport> airportList;

    public class AirportNode {
        String code;
        double distance;

        public AirportNode(String code, double distance){
            this.code = code;
            this.distance = distance;
        }
    }

    public Airports() throws RemoteException {
        System.out.println("New instance of Airports created");
        // Createand populate data structure of airports
        System.out.print("Loading data from file... ");
        try {
            this.airportList = AirportList.parseFrom(new FileInputStream("airports-proto.bin")).getAirportList();
        } catch (FileNotFoundException e) {
            System.out.println("Error: File \"airports-proto.bin\" not found on server");
            e.printStackTrace();
        } catch (IOException e) {
            System.out.println("Error: Tried to parse uninitialized message.");
            e.printStackTrace();
        } catch (Exception e) {
            System.out.println("Evil code gnomes messed something up. Unexpected error!");
            e.printStackTrace();
        }
        System.out.println("Success");
    }

    public String[] find_airports(double latitude, double longitude) throws RemoteException {
        // TODO: return 5 nearest airports
        // code=code, name=name, state=state, distance: x miles
        System.out.print("Looking up: "+latitude+", "+longitude+"... ");
        String[] airports = new String[5];

        List<AirportNode> nodes = new ArrayList<AirportNode>();

        for (Airport airport : airportList)
            nodes.add(new AirportNode(airport.getCode(), compute_distance(latitude, longitude, airport.getLat(), airport.getLon())));

        AirportNode min;

        for(int i = 0; i < 5; i++) {

            min = nodes.get(0);

            for(AirportNode node : nodes)
                if(node.distance < min.distance)
                    min = node;

            nodes.remove(min);

            for(Airport airport: airportList)
                if(airport.getCode() == min.code)
                    airports[i] = "code="+airport.getCode()+", name="+airport.getName()+", state="+airport.getState()+", distance: "+min.distance+" miles";
        }

        System.out.println("Success!");

        return airports;
    }

    public double compute_distance(double originLat, double originLong, double destLat, double destLong) throws RemoteException {
        originLat = Math.toRadians(originLat);
        originLong = Math.toRadians(originLong);
        destLat = Math.toRadians(destLat);
        destLong = Math.toRadians(destLong);

        double angle = Math.acos(Math.sin(originLat)*Math.sin(destLat) + Math.cos(originLat)*Math.cos(destLat)*Math.cos(destLong-originLong));

        return 60*Math.toDegrees(angle) * 1.1507794;
    }

}

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.server.*;
import PlaceData.PlaceDataProto.Place;
import PlaceData.PlaceDataProto.PlaceList;
import java.util.List;
import java.io.*;

// this is the class with remote methods

public class Places
  extends UnicastRemoteObject
  implements PlacesInterface {

    public List<Place> placeList;

    public Places() throws RemoteException {
        System.out.println("New instance of Places created");
        // Create and populate data structure of places
        System.out.print("Loading data from file... ");
        try {
            this.placeList = PlaceList.parseFrom(new FileInputStream("places-proto.bin")).getPlaceList(); // Iterator
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

    public String find_place(String city, String state) throws RemoteException {
        // return place_name, state: latitude, longitude
        System.out.print("Looking up: "+city+", "+state+"... ");
        for (Place place : this.placeList) {
            if (place.getName().contains(city) && place.getState().equals(state)) {
                System.out.println("Success");
                return place.getName()+", "+place.getState()+": "+place.getLat()+", "+place.getLon();
            }
        }
        System.out.println("Failure");
        return "";
    }
}

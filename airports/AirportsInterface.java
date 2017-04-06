import java.rmi.Remote;
import java.rmi.RemoteException;

public interface AirportsInterface extends Remote {
    public String[] find_airports(double latitude, double longitude) throws RemoteException;
    //public double compute_distance(double originLat, double originLong, double destLat, double destLong) throws RemoteException;
}

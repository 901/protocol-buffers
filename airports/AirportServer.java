import java.rmi.Naming;
import java.rmi.RemoteException;
// import java.rmi.RMISecurityManager;
import java.rmi.server.UnicastRemoteObject;

public class AirportServer {
    public static void main(String args[]) {
        if (args.length != 1) {
            System.err.println("usage: java AirportServer rmi_port");
            System.exit(1);
        }
        // Create and install a security manager
/*
        if (System.getSecurityManager() == null)
            System.setSecurityManager(new RMISecurityManager());
*/
        try {
            // first command-line argument is the port of the rmiregistry
            int port = Integer.parseInt(args[0]);
            String url = "//localhost:" + port + "/Airports";
            System.out.println("binding " + url);
            Naming.rebind(url, new Airports());
            // Naming.rebind("Airport", new Airport());
            System.out.println("server " + url + " is running...");
        }
        catch (Exception e) {
            System.out.println("Airport server failed:" + e.getMessage());
        }
    }
}

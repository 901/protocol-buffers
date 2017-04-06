import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.ConnectException;
import java.rmi.UnknownHostException;

public class Client  {
    public static void main(String[] args) {
        try {
            if (args.length < 2) {
                System.err.println("usage: java Client -h registry_host -p registry_port city state \n");
                System.exit(1);
            }

            // Read input variables
            //String def_host = "localhost";
	    String airport_host = "localhost";
	    String places_host = "localhost";
            //int def_port = 1099;
	    int airport_port = 1099;
	    int places_port = 1099;
            boolean cityArg = false;
            boolean stateArg = false;
            String city = "";
            String state = "";

            for (int i = 0; i < args.length; i++) {
                if (args[i].compareTo("-ha") == 0) {
                    i++;
                    airport_host = args[i];
                } else if (args[i].compareTo("-pa") == 0) {
                    i++;
                    try {
                        airport_port = Integer.parseInt(args[i]);
                    } catch(NumberFormatException e) {
                        System.out.println("Invalid port number");
                        System.exit(2);
                    }

                    if(airport_port < 1024) {
                        System.out.println("Port number is less than 1024, exiting...");
                        System.exit(3);
                    }
		
                } else if (args[i].compareTo("-hp") == 0) {
                    i++;
                    places_host = args[i];
                } else if (args[i].compareTo("-pp") == 0) {
                    i++;
                    try {
                        places_port = Integer.parseInt(args[i]);
                    } catch(NumberFormatException e) {
                        System.out.println("Invalid port number");
                        System.exit(2);
                    }
	 
                    if(places_port < 1024) {
                        System.out.println("Port number is less than 1024, exiting...");
                        System.exit(3);
                    }
		} else if (stateArg) {
                    System.out.println("Extra arguments found");
                    System.exit(4);
                } else if(!cityArg) {
                    city = args[i];
                    cityArg = true;
                } else {
                    state = args[i];
                    stateArg = true;
                }
            }

            if(!cityArg || !stateArg) {
                System.out.println("City or state missing in args");
                System.exit(4);
            }

            String placeURL = "//" + places_host + ":" + places_port + "/Places";
            String airportURL = "//" + airport_host + ":" + airport_port + "/Airports";

            System.out.println("looking up " + placeURL);
            PlacesInterface places = (PlacesInterface)Naming.lookup(placeURL);

            System.out.println("looking up " + airportURL);
            AirportsInterface airports = (AirportsInterface)Naming.lookup(airportURL);

            // call the remote method and print the return
            System.out.println("Searching up: "+city+", "+state);
            String s = places.find_place(city, state);
            System.out.println(s);

            if (s.equals("")) {
                System.out.println("Place \""+city+", "+state+"\" not found!");
                System.exit(5);
            }

            // Extract latitude and longitude
            String tmp = s.split(":")[1];
            double latitude = Double.parseDouble(tmp.split(",")[0]);
            double longitude = Double.parseDouble(tmp.split(",")[1]);

            String[] top_five = airports.find_airports(latitude, longitude);
            for (String a : top_five)
                System.out.println(a);

        } catch (Exception e) {
            System.out.println("Client exception: " + e);
        }
    }
}

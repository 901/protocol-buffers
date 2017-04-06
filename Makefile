# ayy
all: compile

compile:
	javac Client.java places/*.java airports/*.java airports/AirportData/*.java places/PlaceData/*.java

clean:
	rm *.class places/*.class airports/*.class airports/AirportData/*.class places/PlaceData/*.class

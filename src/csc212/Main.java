package csc212;

import java.util.Scanner;

public class Main {

    private static final Scanner input = new Scanner(System.in);

    private static String ask(String message) {
        System.out.print(message);
        return input.nextLine().trim();
    }

    private static int askInt(String message) {
        return Integer.parseInt(ask(message));
    }

    private static IDateTime askDate(String message) {
        String[] f = ask(message + " (MM/DD/YYYY HH:MM): ")
                .split("[ /:]+");

        if (f.length != 5) {
            throw new IllegalArgumentException("Invalid date/time");
        }

        return new DateTime(
                Integer.parseInt(f[2]),
                Integer.parseInt(f[0]),
                Integer.parseInt(f[1]),
                Integer.parseInt(f[3]),
                Integer.parseInt(f[4])
        );
    }

    private static <T> void printList(LinkedList<T> list) {
        if (list.empty()) {
            System.out.println("No results found.");
            return;
        }

        list.findFirst();

        while (true) {
            System.out.println(list.retrieve());

            if (list.last()) {
                break;
            }

            list.findNext();
        }
    }

    private static void menu() {
        System.out.println("\n===== Ride-Sharing System =====");
        System.out.println("1. List all riders");
        System.out.println("2. Search riders by home city");
        System.out.println("3. Search riders by name");
        System.out.println("4. Add a rider");
        System.out.println("5. List all drivers");
        System.out.println("6. Search drivers by vehicle type");
        System.out.println("7. Add a driver");
        System.out.println("8. List all rides alphabetically");
        System.out.println("9. Search rides by pickup location");
        System.out.println("10. Search rides by rider name");
        System.out.println("11. List shared ride participants");
        System.out.println("12. Add a ride");
        System.out.println("0. Exit");
    }

    private static void addRide(RideSharingSystem system) {
        String type = ask("Ride type (PRIVATE/SHARED): ")
                .toUpperCase();

        String pickup = ask("Pickup location: ");
        IDateTime start = askDate("Pickup time");
        IDateTime end = askDate("Drop-off time");
        String dropoff = ask("Drop-off location: ");
        int driverId = askInt("Driver ID: ");

        boolean success;

        if (type.equals("PRIVATE")) {
            int riderId = askInt("Rider ID: ");

            success = system.schedulePrivateRide(
                    pickup, start, end, dropoff, riderId, driverId
            );

        } else if (type.equals("SHARED")) {
            String[] parts = ask(
                    "Rider IDs separated by semicolons: "
            ).split(";");

            int[] ids = new int[parts.length];

            for (int i = 0; i < parts.length; i++) {
                ids[i] = Integer.parseInt(parts[i].trim());
            }

            success = system.scheduleSharedRide(
                    pickup, start, end, dropoff, ids, driverId
            );

        } else {
            System.out.println("Invalid ride type.");
            return;
        }

        System.out.println(
                success
                ? "Ride scheduled successfully."
                : "Failed to schedule ride."
        );
    }

    public static void main(String[] args) {

        RideSharingSystem system = new RideSharingSystem();

        // Load CSV files automatically
        System.out.println("Loading CSV files...");

        boolean ridersLoaded =
                system.loadRidersFromCSV("data/riders_100.csv");
        System.out.println("Riders loaded: " + ridersLoaded);

        boolean driversLoaded =
                system.loadDriversFromCSV("data/drivers_30.csv");
        System.out.println("Drivers loaded: " + driversLoaded);

        boolean ridesLoaded =
                system.loadRidesFromCSV("data/rides_40.csv");
        System.out.println("Rides loaded: " + ridesLoaded);

        if (!ridersLoaded || !driversLoaded || !ridesLoaded) {
            System.out.println(
                    "Warning: Some CSV files could not be loaded."
            );
        }

        while (true) {
            menu();

            try {
                int option = askInt("Choose an option: ");

                switch (option) {

                    case 0:
                        System.out.println("Goodbye!");
                        return;

                    case 1:
                        printList(system.getAllRiders());
                        break;

                    case 2:
                        printList(system.searchRidersByHomeCity(
                                ask("Home city: ")
                        ));
                        break;

                    case 3:
                        printList(system.searchRidersByName(
                                ask("Full name: ")
                        ));
                        break;

                    case 4: {
                        int id = askInt("Rider ID: ");
                        String name = ask("Name: ");
                        String email = ask("Email: ");
                        String phone = ask("Phone: ");
                        String city = ask("Home city: ");

                        boolean added = system.addRider(
                                new Rider(id, name, email, phone, city)
                        );

                        System.out.println(
                                added ? "Rider added."
                                      : "Failed to add rider."
                        );
                        break;
                    }

                    case 5:
                        printList(system.getAllDrivers());
                        break;

                    case 6: {
                        VehicleType type = VehicleType.valueOf(
                                ask("Vehicle type: ").toUpperCase()
                        );

                        printList(
                                system.searchDriversByVehicleType(type)
                        );
                        break;
                    }

                    case 7: {
                        int id = askInt("Driver ID: ");
                        String name = ask("Name: ");
                        String phone = ask("Phone: ");
                        String plate = ask("Vehicle plate: ");

                        VehicleType type = VehicleType.valueOf(
                                ask("Vehicle type: ").toUpperCase()
                        );

                        boolean added = system.addDriver(
                                new Driver(
                                        id, name, phone, plate, type
                                )
                        );

                        System.out.println(
                                added ? "Driver added."
                                      : "Failed to add driver."
                        );
                        break;
                    }

                    case 8:
                        printList(system.getAllRidesAlphabetically());
                        break;

                    case 9:
                        printList(system.searchRidesByPickupLocation(
                                ask("Pickup location: ")
                        ));
                        break;

                    case 10:
                        printList(system.searchRidesByRiderName(
                                ask("Rider name: ")
                        ));
                        break;

                    case 11:
                        printList(system.getSharedRideParticipants(
                                ask("Pickup location: ")
                        ));
                        break;

                    case 12:
                        addRide(system);
                        break;

                    default:
                        System.out.println("Invalid option.");
                }

            } catch (IllegalArgumentException e) {
                System.out.println("Invalid input: " + e.getMessage());
            }
        }
    }
}
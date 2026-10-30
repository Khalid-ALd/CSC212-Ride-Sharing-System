# CSC212 – Ride-Sharing System

A Java-based ride-sharing management system developed for the CSC212 project.

## Overview

The system manages riders, drivers, private rides, and shared rides. It loads the provided CSV datasets at startup and offers a command-line menu for searching, listing, adding, and scheduling rides.

## Features

- Load riders, drivers, and rides from CSV files.
- Add and search riders and drivers.
- Search rides by pickup location and rider name.
- List rides alphabetically by pickup location.
- Schedule private and shared rides.
- Validate rider and driver availability to prevent overlapping ride intervals.
- Maintain ride history for riders and drivers.
- Apply cascading deletion when removing riders or drivers.
- Use a custom generic linked list implementation.

## Technologies

- Java
- Object-Oriented Programming
- Interfaces and inheritance
- Generics
- Custom Linked List
- CSV file handling

## Project Structure

```text
CSC212-Ride-Sharing-System/
├── src/
│   └── csc212/
│       ├── Main.java
│       ├── RideSharingSystem.java
│       ├── Rider.java
│       ├── Driver.java
│       ├── Ride.java
│       ├── PrivateRide.java
│       ├── SharedRide.java
│       ├── DateTime.java
│       ├── VehicleType.java
│       ├── LinkedList.java
│       ├── Node.java
│       └── interfaces and list classes
├── data/
│   ├── riders_100.csv
│   ├── drivers_30.csv
│   └── rides_40.csv
├── README.md
└── .gitignore
```

## Requirements

- JDK 21 or a compatible Java version.
- Run the program from the project root directory so the relative `data/` paths resolve correctly.

## Compile and Run

From the project root:

```bash
javac -d out src/csc212/*.java
java -cp out csc212.Main
```

The application automatically loads the CSV files from the `data` directory before displaying the menu.

## Notes

This repository contains the Java source code and the CSV datasets supplied with the project. Compiled `.class` files and IDE-specific configuration files are intentionally excluded.

package org.example;

import org.example.controllers.ShowController;
import org.example.exceptions.*;
import org.example.models.Ticket;

import java.util.List;
import java.util.Scanner;

public class App {
    public static void main( String[] args ) {
        ShowController showController = new ShowController();
        Scanner scanner = new Scanner(System.in);
        String input = null;
        System.out.println("Welcome to the show booking application. Choose an option or type Exit to exit.");
        String user = null;
        do {
            if (user == null) {
                displayLoginChoices();
                input = scanner.nextLine();
                user = login(input, scanner);
            }
            else {
                if(input.equals("Logout")) {
                    user = null;
                    continue;
                }
                handleInput(input, showController);
                displayMenu(user);
                input = scanner.nextLine();
            }
        } while(!input.equals("Exit"));
    }
    public static void displayLoginChoices() {
        System.out.println("Admin");
        System.out.println("Buyer");
        System.out.print("Log in as: ");
    }

    public static void displayBuyerChoices() {
        System.out.println("Availability <Show number>");
        System.out.println("Book <Show number> <Phone number> <Comma separated list of seats>");
        System.out.println("Cancel <Ticket number> <Phone Number>");
    }

    public static void displayAdminChoices() {
        System.out.println("Setup <Show number> <Number of rows> <Number of seats per row> <Cancellation window in minutes>");
        System.out.println("View <Show number>");
    }

    public static void displayMenu(String user) {
        System.out.println("---------------------------------------------------------------------");
        System.out.println("Commands");
        if(user.equals("Admin")) {
            displayAdminChoices();
        }
        if(user.equals("Buyer")) {
            displayBuyerChoices();
        }
        System.out.println("Logout");
        System.out.println("---------------------------------------------------------------------");
        System.out.print("Please enter a command: ");
    }
    public static String login(String option, Scanner scanner) {
        if(option.equalsIgnoreCase("Admin")) {
            return "Admin";
        }
        else if(option.equalsIgnoreCase("Buyer")) {
            return "Buyer";
        }
        return null;
    }
    public static void handleInput(String input, ShowController showController) {
        String[] args = input.split(" ");
        if(input.startsWith("Setup")) {
            try {
                showController.setupShow(
                        Integer.parseInt(args[1]),
                        Integer.parseInt(args[2]),
                        Integer.parseInt(args[3]),
                        Integer.parseInt(args[4]));
                System.out.println("Show set up successfully.");
            }
            catch (InvalidArgumentException e) {
                System.out.println(e.getMessage());
            }
            catch (ShowAlreadyExistsException e) {
                System.out.println("Show with ID: " + args[1] + " already exists.");
            }
        }
        else if(input.startsWith("View")) {
            int showNumber = Integer.parseInt(args[1]);
            try {
                String output = showController.viewShow(showNumber);
                System.out.println(output);
            }
            catch(ShowNotFoundException e) {
                System.out.println("Show with number: " + showNumber + " does not exist.");
            }
        }
        else if(input.startsWith("Availability")) {
            int showNumber = Integer.parseInt(args[1]);
            try {
                String output = showController.showAvailability(showNumber);
                System.out.println(output);
            }
            catch(ShowNotFoundException e) {
                System.out.println("Show with number: " + showNumber + " does not exist.");
            }
        }
        else if(input.startsWith("Book")) {
            int showNumber = Integer.parseInt(args[1]);
            String phoneNumber = args[2];
            String seats = args[3];
            try {
                List<Ticket> result = showController.bookSeats(showNumber, phoneNumber, seats);
                if(result.size() > 0) {
                    System.out.print("Booking successful. Booking ID(s): ");
                    result.forEach(ticket -> System.out.print(ticket.getNumber() + " "));
                    System.out.println();
                }
                else
                    System.out.println("Booking failed, please try again.");
            }
            catch(InvalidArgumentException e) {
                System.out.println(e.getMessage());
            }
            catch(RowNotFoundException | SeatNotFoundException e) {
                System.out.println("Invalid row and seat selection.");
            } catch (ShowNotFoundException e) {
                System.out.println("Show with ID: " + showNumber + " does not exist.");
            }
        }
        else if(input.startsWith("Cancel")) {
            int ticketNumber = Integer.parseInt(args[1]);
            String phoneNumber = args[2];
            try {
                if(showController.cancelBooking(ticketNumber, phoneNumber))
                    System.out.println("Ticket ID: " + ticketNumber + " was cancelled successfully.");
                else
                    System.out.println("Ticket ID: " + ticketNumber + " was not found.");

            }
            catch(ShowNotFoundException e) {
                System.out.println("Invalid ticket");
            }
            catch(InvalidArgumentException e) {
                System.out.println(e.getMessage());
            }
        }
    }
}

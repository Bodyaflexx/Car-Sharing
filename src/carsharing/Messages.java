package carsharing;

import java.util.List;

public class Messages {
    public static void printMenu() {
        System.out.println("""
                1. Log in as a manager
                2. Log in as a customer
                3. Create a customer
                0. Exit
                """);
    }
    public static void printSubMenu() {
        System.out.println("1. Company list");
        System.out.println("2. Create a company");
        System.out.println("0. Back");
    }
    public static void printGreeting() {
        System.out.println("Welcome to Car Sharing");
    }
    public static void printCompanies(List<String> companies) {
        int i = 1;
        System.out.println("Choose the company:");
        for (String company : companies) {
            System.out.println(i++ + ". " + company);
        }
        System.out.println("0. Back");
    }
    public static void printCars(List<String> cars) {
        int i = 1;
        System.out.println("Car list:");
        for (String car : cars) {
            System.out.println(i++ + ". " + car);
        }
        System.out.println("0. Back");
    }
    public static void printCarMenu(String name) {
        System.out.printf("""
                %s company:
                1. Car list
                2. Create a car
                0. Back
                """,name);
    }
    public static void menuForCustomer(){
        System.out.println("""
                1. Rent a car
                2. Return a rented car
                3. My rented car
                0. Back
                """);
    }
    public static void printCustomers(List<String> customers) {
        int i = 1;
        System.out.println("Choose the customer:");
        for (String customer : customers) {
            System.out.println(i++ + ". " + customer);
        }
        System.out.println("0. Back");
    }
}

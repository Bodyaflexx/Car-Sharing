package carsharing;


import java.sql.*;
import java.util.List;
import java.util.Scanner;


public class Menu {
    static final String DB_URL = "jdbc:h2:";
    static final String PATH_TO_DB = "D:\\java\\Car Sharing\\Car Sharing\\task\\src\\carsharing\\db\\";
    Scanner scanner = new Scanner(System.in);
    int choice = -1;
    private final String name;
    private Connection conn = null;
    private Statement stmt = null;
    String sql;

    public Menu(String name) {
        this.name = name;
    }

    public void startMenu() {
        try {
            String path = PATH_TO_DB + name;
            conn = DriverManager.getConnection(DB_URL + path);
            conn.setAutoCommit(true);
            stmt = conn.createStatement();
            sql = Requests.createCompanyTable();
            stmt.executeUpdate(sql);
            sql = Requests.createCarTable();
            stmt.executeUpdate(sql);
            sql = Requests.createCustomer();
            stmt.executeUpdate(sql);
            Messages.printGreeting();
            CompanyDao companyDao = new CompanyDaoImpl(conn, stmt);
            while (true) {
                Messages.printMenu();
                choice = scanner.nextInt();
                switch (choice) {
                    case 1 -> managerMenu(companyDao);
                    case 2 -> customerMenu(companyDao);
                    case 3 -> createCustomerMenu(companyDao);
                    case 0 -> {
                        System.out.println("Goodbye");
                        return;
                    }
                    default -> System.out.println("Invalid choice");
                }
            }
        } catch (Exception se) {
            se.printStackTrace();
        } finally {
            try {
                if (stmt != null) stmt.close();
            } catch (SQLException ignored) {
            }
            try {
                if (conn != null) conn.close();
            } catch (SQLException se) {
                se.printStackTrace();
            }
        }
    }

    private void managerMenu(CompanyDao companyDao) {
        Messages.printSubMenu();
        choice = scanner.nextInt();
        while (true) {
            switch (choice) {
                case 1 -> {
                    List<String> companies = companyDao.getAllCompanies();
                    if (companies.isEmpty()) {
                        System.out.println("The company list is empty!");
                    } else {
                        Messages.printCompanies(companies);
                        choice = scanner.nextInt();
                        carMenu(companyDao, companies);
                    }
                }
                case 2 -> {
                    System.out.println("Enter the company name:");
                    Scanner scanner1 = new Scanner(System.in);
                    String companyName = scanner1.nextLine();

                    companyDao.createCompany(new Company(companyName));
                    System.out.println("The company was created!");
                }
                case 0 -> {
                    return;
                }
                default -> System.out.println("Invalid choice");
            }
            Messages.printSubMenu();
            choice = scanner.nextInt();
        }

    }

    private void customerMenu(CompanyDao companyDao) {
        if (choice == 0) {
            return;
        }
        List<String> customers = companyDao.getAllCustomers();
        if (customers.isEmpty()) {
            System.out.println("The customer list is empty!");
        } else {
            Messages.printCustomers(customers);
            int customerNum = scanner.nextInt();
            while (true) {
                if (customerNum != 0) {
                    Messages.menuForCustomer();
                    choice = scanner.nextInt();
                    switch (choice) {
                        case 1 -> {
                            List<String> companies = companyDao.getAllCompanies();
                            if (companies.isEmpty()) {
                                System.out.println("The company list is empty!");
                                return;
                            } else {
                                companyDao.rentCar(customers.get(customerNum - 1));
                            }
                        }
                        case 2 -> companyDao.returnCar(customers.get(customerNum - 1));
                        case 3 -> companyDao.showCar(customers.get(customerNum - 1));
                        case 0 -> {
                            return;
                        }
                        default -> System.out.println("Invalid choice");
                    }
                }
            }
        }
    }

    private void createCustomerMenu(CompanyDao companyDao) {
        System.out.println("Enter the customer name:");
        Scanner scanner1 = new Scanner(System.in);
        String customerName = scanner1.nextLine();
        companyDao.createCustomer(customerName);
        System.out.println("The customer was added!");
    }

    private void carMenu(CompanyDao companyDao, List<String> companies) {
        if (choice == 0) {
            return;
        }
        String companyName = companies.get(choice - 1);
        Messages.printCarMenu(companyName);
        choice = scanner.nextInt();
        while (true) {
            switch (choice) {
                case 1 -> {
                    List<String> cars = companyDao.getAllCars(companyName);
                    if (cars.isEmpty()) {
                        System.out.println("The car list is empty!");
                    } else {
                        Messages.printCars(cars);
                        choice = scanner.nextInt();
                        if (choice == 0) {
                            return;
                        }
                    }
                }
                case 2 -> {
                    System.out.println("Enter the car name:");
                    Scanner scanner1 = new Scanner(System.in);
                    String carName = scanner1.nextLine();

                    companyDao.createCar(new Car(carName), companyName);
                    System.out.println("The car was added!");
                }
                case 0 -> {
                    return;
                }
                default -> System.out.println("Invalid choice");
            }
            Messages.printCarMenu(companyName);
            choice = scanner.nextInt();
        }
    }
}

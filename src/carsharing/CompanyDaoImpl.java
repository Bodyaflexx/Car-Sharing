package carsharing;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;


public class CompanyDaoImpl implements CompanyDao {
    private final Connection conn;
    private Statement stmt;

    public CompanyDaoImpl(Connection conn, Statement stmt) {
        this.conn = conn;
        this.stmt = stmt;
    }

    @Override
    public List<String> getAllCompanies() {
        ResultSet rs;
        try {
            stmt = conn.createStatement();
            rs = stmt.executeQuery("SELECT COUNT(*) FROM company");
            if (rs.next()) {
                int count = rs.getInt(1);
                switch (count) {
                    case 0:
                        break;
                    default:
                        List<String> companies = new ArrayList<>();
                        String sql = "SELECT * FROM COMPANY ORDER BY ID";
                        rs = stmt.executeQuery(sql);
                        while (rs.next()) {
                            String name = rs.getString("NAME");
                            companies.add(name);

                        }
                        return companies;
                }
            }
        } catch (Exception ignored) {
        }
        return Collections.emptyList();
    }

    @Override
    public void createCompany(Company company) {
        String name = company.getCompanyName();
        try {
            String sql = String.format("SELECT COUNT(*) FROM company WHERE name = '%s'", name);
            PreparedStatement statement = conn.prepareStatement(sql);
            ResultSet resultSet = statement.executeQuery();
            resultSet.next();
            int count = resultSet.getInt(1);
            if (count == 1) {
                stmt.executeUpdate("DROP table COMPANY");
            }
            stmt = conn.createStatement();
            stmt.executeUpdate(Requests.insertCompany(name));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<String> getAllCars(String companyName) {
        ResultSet rs;
        try {
            stmt = conn.createStatement();
            rs = stmt.executeQuery("SELECT COUNT(*) FROM CAR");
            if (rs.next()) {
                int count = rs.getInt(1);
                switch (count) {
                    case 0:
                        break;
                    default:
                        List<String> cars = new ArrayList<>();
                        String s = String.format("SELECT id FROM company WHERE name = '%s';", companyName);
                        rs = stmt.executeQuery(s);
                        rs.next();
                        int id = rs.getInt("id");
                        String sql = String.format("SELECT * FROM CAR WHERE COMPANY_ID = '%s' ORDER BY ID", id);
                        rs = stmt.executeQuery(sql);
                        while (rs.next()) {
                            String name = rs.getString("NAME");
                            cars.add(name);

                        }
                        return cars;
                }
            }
        } catch (Exception ignored) {
        }
        return Collections.emptyList();
    }

    @Override
    public List<String> getAllFreeCars(String companyName) {
        ResultSet rs;
        try {
            stmt = conn.createStatement();
            rs = stmt.executeQuery("SELECT COUNT(*) FROM CAR");
            if (rs.next()) {
                int count = rs.getInt(1);
                switch (count) {
                    case 0:
                        break;
                    default:
                        List<String> cars = new ArrayList<>();
                        String s = String.format("SELECT id FROM company WHERE name = '%s';", companyName);
                        rs = stmt.executeQuery(s);
                        rs.next();
                        int id = rs.getInt("id");
                        String sql = String.format("""
                                SELECT c.NAME
                                FROM CAR c
                                LEFT JOIN CUSTOMER cu ON c.ID = cu.RENTED_CAR_ID
                                WHERE cu.ID IS NULL AND c.COMPANY_ID = %d
                                ORDER BY c.ID;
                                """, id);
                        rs = stmt.executeQuery(sql);
                        while (rs.next()) {
                            String name = rs.getString("NAME");
                            cars.add(name);

                        }
                        return cars;
                }
            }
        } catch (Exception ignored) {
        }
        return Collections.emptyList();
    }

    @Override
    public void createCar(Car car, String companyName) {
        String name = car.getName();
        try {
            stmt = conn.createStatement();
            ResultSet rs;
            String sql = String.format("SELECT COUNT(*) FROM car WHERE name = '%s'", name);
            rs = stmt.executeQuery(sql);
            rs.next();
            int count = rs.getInt(1);
            if (count == 1) {
                stmt.executeUpdate("DROP table CAR");
            }
            String s = String.format("SELECT id FROM company WHERE name = '%s';", companyName);
            rs = stmt.executeQuery(s);
            rs.next();
            int id = rs.getInt("id");
            stmt.executeUpdate(Requests.insertCar(name, id));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void createCustomer(String customerName) {
        try {
            stmt = conn.createStatement();
            stmt.executeUpdate(Requests.insertCustomer(customerName));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<String> getAllCustomers() {
        ResultSet rs;
        try {
            stmt = conn.createStatement();
            rs = stmt.executeQuery("SELECT COUNT(*) FROM CUSTOMER");
            if (rs.next()) {
                int count = rs.getInt(1);
                switch (count) {
                    case 0:
                        break;
                    default:
                        List<String> companies = new ArrayList<>();
                        String sql = "SELECT * FROM CUSTOMER ORDER BY ID";
                        rs = stmt.executeQuery(sql);
                        while (rs.next()) {
                            String name = rs.getString("NAME");
                            companies.add(name);

                        }
                        return companies;
                }
            }
        } catch (Exception ignored) {
        }
        return Collections.emptyList();
    }

    @Override
    public void rentCar(String customerName) {
        try {
            stmt = conn.createStatement();
            ResultSet rs;
            String sql = String.format("SELECT RENTED_CAR_ID FROM CUSTOMER WHERE NAME = '%s'", customerName);
            rs = stmt.executeQuery(sql);
            rs.next();
            int id = rs.getInt("RENTED_CAR_ID");
            sql = Requests.customerCar(id);
            rs = stmt.executeQuery(sql);
            if(rs.next()){
                System.out.println("You've already rented a car!");
            } else{
                Scanner scanner = new Scanner(System.in);
                Messages.printCompanies(getAllCompanies());
                String companyName = getAllCompanies().get(scanner.nextInt()-1);
                List<String> cars = getAllFreeCars(companyName);
                if(cars.isEmpty()){
                    System.out.println("No available cars in the 'Company name' company");
                }else{
                    Messages.printCars(cars);
                    int carNumber = scanner.nextInt();
                    if(carNumber == 0){
                        return;
                    }

                    sql = String.format("SELECT ID FROM CAR WHERE NAME = '%s'", cars.get(carNumber-1));
                    rs = stmt.executeQuery(sql);
                    rs.next();
                    int carId = rs.getInt("ID");

                    sql = Requests.rentCar(carId,customerName);
                    stmt.executeUpdate(sql);
                    System.out.printf("You rented '%s'\n",cars.get(carNumber-1));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void returnCar(String customerName) {
        try {
            stmt = conn.createStatement();
            ResultSet rs;
            String sql = String.format("SELECT RENTED_CAR_ID FROM CUSTOMER WHERE CUSTOMER.NAME = '%s'", customerName);
            rs = stmt.executeQuery(sql);
            rs.next();
            int id = rs.getInt("RENTED_CAR_ID");
            sql = Requests.customerCar(id);
            rs = stmt.executeQuery(sql);
            if(!rs.next()){
                System.out.println("You didn't rent a car!");
            } else{
                sql = Requests.returnRentedCar(id);
                stmt.executeUpdate(sql);
                System.out.println("You've returned a rented car!");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void showCar(String customerName) {
        try {
            stmt = conn.createStatement();
            ResultSet rs;
            String sql = String.format("SELECT RENTED_CAR_ID FROM CUSTOMER WHERE CUSTOMER.NAME = '%s'", customerName);
            rs = stmt.executeQuery(sql);
            rs.next();
            int id = rs.getInt("RENTED_CAR_ID");
            sql = Requests.customerCar(id);
            rs = stmt.executeQuery(sql);
            if (rs.next()) {
                String carName = rs.getString("name");
                sql = Requests.carCompany(id);
                rs = stmt.executeQuery(sql);
                rs.next();
                String companyName = rs.getString("name");
                System.out.printf("""
                Your rented car:
                %s
                Company:
                %s
                """,carName,companyName);
            } else {
                System.out.println("You didn't rent a car!");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}

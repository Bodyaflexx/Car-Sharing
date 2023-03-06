package carsharing;

import java.util.List;

public interface CompanyDao {
    List<String> getAllCompanies();
    void createCompany(Company company);
    List<String> getAllCars(String companyName);
    List<String> getAllFreeCars(String companyId);
    void createCar(Car car,String name);
    void createCustomer(String customerName);
    List<String> getAllCustomers();
    void rentCar(String customerName);
    void returnCar(String customerName);
    void showCar(String customerName);
}

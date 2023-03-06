package carsharing;


public class Requests {
    public static String createCompanyTable(){
        return """
                CREATE TABLE IF NOT EXISTS COMPANY (
                    ID INT AUTO_INCREMENT PRIMARY KEY,
                    NAME VARCHAR UNIQUE NOT NULL
                );
                """;
    }
    public static String createCarTable(){
        return """
                CREATE TABLE IF NOT EXISTS CAR (
                    ID INT AUTO_INCREMENT PRIMARY KEY,
                    NAME VARCHAR UNIQUE NOT NULL,
                    COMPANY_ID INT NOT NULL,
                    FOREIGN KEY (COMPANY_ID) REFERENCES COMPANY(ID)
                );
                """;
    }
    public static String createCustomer(){
        return """
                CREATE TABLE IF NOT EXISTS CUSTOMER (
                    ID INT AUTO_INCREMENT PRIMARY KEY,
                    NAME VARCHAR UNIQUE NOT NULL,
                    RENTED_CAR_ID INT,
                    FOREIGN KEY (RENTED_CAR_ID) REFERENCES CAR(ID)
                );
                """;
    }
    public static String insertCompany(String name){
        return String.format("INSERT INTO COMPANY (name) VALUES ('%s')", name);
    }
    public static String insertCar(String name,int id){
        return String.format("INSERT INTO CAR (name, COMPANY_ID) VALUES ('%s',%d)", name,id);
    }
    public static String insertCustomer(String name){
        return String.format("INSERT INTO CUSTOMER (name, RENTED_CAR_ID) VALUES ('%s',NULL)", name);
    }
    public static String rentCar(int carId,String name){
        return String.format("""
                UPDATE CUSTOMER
                SET RENTED_CAR_ID = %d
                WHERE NAME = '%s';
                """,carId,name);
    }
    public static String returnRentedCar(int userId){
        return String.format("""
                UPDATE CUSTOMER
                SET RENTED_CAR_ID = NULL
                WHERE RENTED_CAR_ID = %d;
                """,userId);
    }
    public static String customerCar(int userId){
        return String.format("SELECT CAR.NAME AS NAME, COMPANY.NAME AS CNAME FROM CUSTOMER JOIN CAR ON " +
                "CUSTOMER.RENTED_CAR_ID = CAR.ID JOIN COMPANY ON CAR.COMPANY_ID = " +
                "COMPANY.ID WHERE CUSTOMER.ID = " + userId);
    }
    public static String carCompany(int userId){
        return String.format("""
                SELECT COMPANY.NAME
                FROM CAR
                JOIN COMPANY ON CAR.COMPANY_ID = COMPANY.ID
                JOIN CUSTOMER ON CUSTOMER.RENTED_CAR_ID = CAR.ID
                WHERE CUSTOMER.ID = %d;
                """,userId);
    }
}

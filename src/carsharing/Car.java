package carsharing;

public class Car {
    private int id;
    private String name;
    private int companyId;

    public Car(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getCompanyId() {
        return companyId;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCompanyId(int companyId) {
        this.companyId = companyId;
    }
}

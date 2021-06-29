package helper;

import sk.durovic.model.Car;
import sk.durovic.model.Company;

public class CarBuilder {
    private Car car;

    public CarBuilder(Car car) {
        this.car = car;
    }

    public CarBuilder(){
        this.car = new Car();
        this.car.setCompany(new Company());
    }

    public Car build(){
        return this.car;
    }

    public CarBuilder setId(Long id){
        this.car.setId(id);
        return this;
    }

    public CarBuilder setBrand(String brand){
        this.car.setBrand(brand);
        return this;
    }

    public CarBuilder setModel(String model){
        this.car.setModel(model);
        return this;
    }

    public CarBuilder setCompany(Company company){
        this.car.setCompany(company);
        return this;
    }

    public CarBuilder setIdCompany(Long id){
        this.car.getCompany().setId(id);
        return this;
    }

    public CarBuilder setEmailCompany(String email){
        this.car.getCompany().setEmail(email);
        return this;
    }

    public static Car getCarForTest(){
        return new CarBuilder().setId(1L).setIdCompany(1L).setEmailCompany("abc@abc.com").build();
    }
}

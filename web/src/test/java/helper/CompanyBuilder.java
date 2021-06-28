package helper;

import sk.durovic.model.Company;

public class CompanyBuilder {
    private Company company;

    public CompanyBuilder() {
        this.company = new Company();
    }

    public CompanyBuilder setName(String name){
        this.company.setName(name);
        return this;
    }

    public CompanyBuilder setId(Long id){
        this.company.setId(id);
        return this;
    }

    public CompanyBuilder setEmail(String email){
        this.company.setEmail(email);
        return this;
    }

    public Company build(){
        return this.company;
    }
}

package sk.durovic.converters;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import sk.durovic.commands.CompanyCommand;
import sk.durovic.model.Company;
import sk.durovic.model.CompanyCredentials;

@Component
public class CompanyCommandToCompany implements Converter<CompanyCommand, Company> {

    @Override
    public Company convert(CompanyCommand companyCommand) {
        Company company = new Company();
        company.setName(companyCommand.getName());
        company.setPhone(companyCommand.getPhone());
        company.setEmail(companyCommand.getEmail());
        company.setCity(companyCommand.getCity());
        company.setAddress(companyCommand.getAddress());

        return company;
    }

    public CompanyCredentials convertToCredentials(CompanyCommand companyCommand){
        CompanyCredentials cc = new CompanyCredentials(companyCommand.getEmail(), companyCommand.getPassword());
        return cc;
    }
}

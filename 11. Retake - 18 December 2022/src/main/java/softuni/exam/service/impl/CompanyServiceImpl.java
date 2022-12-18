package softuni.exam.service.impl;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import softuni.exam.models.dto.CompanyDTO;
import softuni.exam.models.dto.wrapper.CompaniesWrapperDTO;
import softuni.exam.models.entity.Company;
import softuni.exam.models.entity.Country;
import softuni.exam.repository.CompanyRepository;
import softuni.exam.repository.CountryRepository;
import softuni.exam.service.CompanyService;
import softuni.exam.util.ValidationUtils;
import softuni.exam.util.XmlParser;

import javax.xml.bind.JAXBException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static softuni.exam.constants.Messages.*;
import static softuni.exam.constants.Paths.COMPANIES_XML_PATH;

@Service
public class CompanyServiceImpl implements CompanyService {
    private final CompanyRepository companyRepository;
    private final CountryRepository countryRepository;
    private final XmlParser xmlParser;
    private final ValidationUtils validator;
    private final ModelMapper mapper;
    private final StringBuilder result;

    @Autowired
    public CompanyServiceImpl(CompanyRepository companyRepository, CountryRepository countryRepository, XmlParser xmlParser, ValidationUtils validator, ModelMapper mapper, StringBuilder result) {
        this.companyRepository = companyRepository;
        this.countryRepository = countryRepository;
        this.xmlParser = xmlParser;
        this.validator = validator;
        this.mapper = mapper;
        this.result = result;
    }

    @Override
    public boolean areImported() {
        return this.companyRepository.count() > 0;
    }

    @Override
    public String readCompaniesFromFile() throws IOException {
        return Files.readString(Path.of(COMPANIES_XML_PATH));
    }

    @Override
    public String importCompanies() throws IOException, JAXBException {

        CompaniesWrapperDTO companies = xmlParser.parseXml(CompaniesWrapperDTO.class, COMPANIES_XML_PATH);

        for (CompanyDTO companyDto : companies.getCompanies()) {

            boolean isValid = this.validator.isValid(companyDto);

            if (this.companyRepository.findFirstByName(companyDto.getName()).isPresent()) {
                isValid = false;
            }

            if (isValid) {

                Company company = this.mapper.map(companyDto, Company.class);

                Country country = this.countryRepository.findFirstById(companyDto.getCountry()).get();

                company.setCountry(country);

                this.companyRepository.save(company);

                result.append(String.format(VALID_COMPANY_FORMAT,
                        companyDto.getName(),
                        companyDto.getCountry()));

            } else {
                result.append(INVALID_COMPANY).append(System.lineSeparator());
            }
        }
        return result.toString();
    }
}

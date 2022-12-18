package softuni.exam.web.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import softuni.exam.service.CompanyService;
import softuni.exam.service.CountryService;
import softuni.exam.service.JobService;
import softuni.exam.service.PersonService;

import javax.xml.bind.JAXBException;
import java.io.FileNotFoundException;
import java.io.IOException;

@Controller
@RequestMapping("/import")
public class ImportController extends BaseController {

    private final CountryService countryService;
    private final JobService jobService;
    private final PersonService personService;
    private final CompanyService companyService;

    @Autowired
    public ImportController(CountryService countryService, JobService jobService, PersonService personService, CompanyService companyService) {
        this.countryService = countryService;
        this.jobService = jobService;
        this.personService = personService;
        this.companyService = companyService;
    }


    @GetMapping("/json")
    public ModelAndView importJson() {

        boolean[] areImported = new boolean[]{
                this.countryService.areImported(),
                this.personService.areImported()
        };

        return super.view("json/import-json", "areImported", areImported);
    }


    @GetMapping("/xml")
    public ModelAndView importXml() {
        boolean[] areImported = new boolean[]{
                this.companyService.areImported(),
                this.jobService.areImported()
        };

        return super.view("xml/import-xml", "areImported", areImported);
    }


    @GetMapping("/companies")
    public ModelAndView importCompanies() throws IOException {
        String carsXmlFileContent = this.companyService.readCompaniesFromFile();
        return super.view("xml/import-companies", "companies", carsXmlFileContent);
    }

    @PostMapping("/companies")
    public ModelAndView importCarsConfirm() throws JAXBException, IOException {
        System.out.println(this.companyService.importCompanies());

        return super.redirect("/import/xml");
    }

    @GetMapping("/jobs")
    public ModelAndView importJobs() throws IOException {
        String jobsXmlFileContent = this.jobService.readJobsFileContent();

        return super.view("xml/import-jobs", "jobs", jobsXmlFileContent);
    }

    @PostMapping("/jobs")
    public ModelAndView importJobsConfirm() throws JAXBException, FileNotFoundException, IOException {
        System.out.println(this.jobService.importJobs());

        return super.redirect("/import/xml");
    }

    @GetMapping("/countries")
    public ModelAndView importCountries() throws IOException {
        String fileContent = this.countryService.readCountriesFileContent();

        return super.view("json/import-countries", "countries", fileContent);
    }

    @PostMapping("/countries")
    public ModelAndView importCountriesConfirm() throws IOException {
        System.out.println(this.countryService.importCountries());
        return super.redirect("/import/json");
    }

    @GetMapping("/people")
    public ModelAndView importPeople() throws IOException {
        String fileContent = this.personService.readPeopleFromFile();

        return super.view("json/import-people", "people", fileContent);
    }

    @PostMapping("/people")
    public ModelAndView importPeopleConfirm() throws IOException, JAXBException {
        System.out.println(this.personService.importPeople());
        return super.redirect("/import/json");
    }
}

package softuni.exam.web.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;
import softuni.exam.service.CompanyService;
import softuni.exam.service.CountryService;
import softuni.exam.service.JobService;
import softuni.exam.service.PersonService;

@Controller
public class HomeController extends BaseController {

    private final CountryService countriesService;
    private final JobService jobsService;
    private final PersonService personService;
    private final CompanyService companiesService;

    @Autowired
    public HomeController(CountryService countryService, JobService jobService, PersonService personService, CompanyService companyService) {
        this.countriesService = countryService;
        this.jobsService = jobService;
        this.personService = personService;
        this.companiesService = companyService;
    }


    @GetMapping("/")
    public ModelAndView index() {
        boolean areImported = this.countriesService.areImported() &&
                this.jobsService.areImported() &&
                this.personService.areImported() &&
                this.companiesService.areImported();

        return super.view("index", "areImported", areImported);
    }
}

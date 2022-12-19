package softuni.exam.web.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import softuni.exam.service.JobService;

@Controller
@RequestMapping("/export")
public class ExportController extends BaseController {

    private final JobService jobsService;

    @Autowired
    public ExportController(JobService jobService) {
        this.jobsService = jobService;
    }


    @GetMapping("/best-jobs")
    public ModelAndView exportTheBestJobs() {
        String bestJobs = this.jobsService.getBestJobs();

        return super.view("export/export-best-jobs.html", "bestJobs", bestJobs);
    }
}

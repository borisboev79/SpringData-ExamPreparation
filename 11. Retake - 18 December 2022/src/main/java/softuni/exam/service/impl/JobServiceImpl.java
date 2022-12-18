package softuni.exam.service.impl;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import softuni.exam.models.dto.JobDTO;
import softuni.exam.models.dto.JobExportDTO;
import softuni.exam.models.dto.wrapper.JobsWrapperDTO;
import softuni.exam.models.entity.Company;
import softuni.exam.models.entity.Job;
import softuni.exam.repository.CompanyRepository;
import softuni.exam.repository.JobRepository;
import softuni.exam.service.JobService;
import softuni.exam.util.ValidationUtils;
import softuni.exam.util.XmlParser;

import javax.xml.bind.JAXBException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

import static softuni.exam.constants.Messages.*;
import static softuni.exam.constants.Paths.JOBS_XML_PATH;

@Service
public class JobServiceImpl implements JobService {
    private final JobRepository jobRepository;
    private final CompanyRepository companyRepository;
    private final XmlParser xmlParser;
    private final ValidationUtils validator;
    private final ModelMapper mapper;
    private final StringBuilder result;

    @Autowired
    public JobServiceImpl(JobRepository jobRepository, CompanyRepository companyRepository, XmlParser xmlParser, ValidationUtils validator, ModelMapper mapper, StringBuilder result) {
        this.jobRepository = jobRepository;
        this.companyRepository = companyRepository;
        this.xmlParser = xmlParser;
        this.validator = validator;
        this.mapper = mapper;
        this.result = result;
    }


    @Override
    public boolean areImported() {
        return this.jobRepository.count() > 0;
    }

    @Override
    public String readJobsFileContent() throws IOException {
        return Files.readString(Path.of(JOBS_XML_PATH));
    }

    @Override
    public String importJobs() throws IOException, JAXBException {
        JobsWrapperDTO jobs = xmlParser.parseXml(JobsWrapperDTO.class, JOBS_XML_PATH);

        for (JobDTO jobDto : jobs.getJobs()) {

            boolean isValid = this.validator.isValid(jobDto);

            if (this.jobRepository.findFirstByTitle(jobDto.getTitle()).isPresent()) {
                isValid = false;
            }

            if (isValid) {

                Job job = this.mapper.map(jobDto, Job.class);

                Company company = this.companyRepository.getById(jobDto.getCompany());

                job.setCompany(company);

                this.jobRepository.save(job);

                result.append(String.format(VALID_JOB_FORMAT,
                        jobDto.getTitle()));

            } else {
                result.append(INVALID_JOB).append(System.lineSeparator());
            }
        }
        return result.toString();
    }

    @Override
    public String getBestJobs() {
        return this.jobRepository.findAllJobsOrderBySalaryDesc()
                .orElseThrow(NoSuchElementException::new)
                .stream()
                .map(job -> mapper.map(job, JobExportDTO.class).toString())
                .collect(Collectors.joining(System.lineSeparator()));

    }
}

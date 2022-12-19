package softuni.exam.service;

import javax.xml.bind.JAXBException;
import java.io.IOException;

public interface JobService {

    boolean areImported();

    String readJobsFileContent() throws IOException;

    String importJobs() throws IOException, JAXBException;

    String getBestJobs();
}

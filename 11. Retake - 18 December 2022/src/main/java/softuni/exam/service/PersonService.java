package softuni.exam.service;

import javax.xml.bind.JAXBException;
import java.io.IOException;

public interface PersonService {

    boolean areImported();

    String readPeopleFromFile() throws IOException;

    String importPeople() throws IOException, JAXBException;
}

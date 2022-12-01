package softuni.exam.service;

import javax.xml.bind.JAXBException;
import java.io.FileNotFoundException;
import java.io.IOException;

public interface PictureService {
    String importPictures() throws JAXBException, FileNotFoundException;

    boolean areImported();

    String readPicturesXmlFile() throws IOException;
}

package softuni.exam.service;


import java.io.IOException;

public interface CountryService {

    boolean areImported();

    String readCountriesFileContent() throws IOException;

    String importCountries() throws IOException;
}

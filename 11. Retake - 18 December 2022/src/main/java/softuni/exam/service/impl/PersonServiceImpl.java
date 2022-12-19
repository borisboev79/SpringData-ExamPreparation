package softuni.exam.service.impl;

import com.google.gson.Gson;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import softuni.exam.models.dto.PersonDTO;
import softuni.exam.models.entity.Country;
import softuni.exam.models.entity.Person;
import softuni.exam.repository.CountryRepository;
import softuni.exam.repository.PersonRepository;
import softuni.exam.service.PersonService;
import softuni.exam.util.ValidationUtils;

import javax.xml.bind.JAXBException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;

import static softuni.exam.constants.Messages.INVALID_PERSON;
import static softuni.exam.constants.Messages.VALID_PERSON_FORMAT;
import static softuni.exam.constants.Paths.PEOPLE_JSON_PATH;

@Service
public class PersonServiceImpl implements PersonService {
    private final PersonRepository personRepository;
    private final CountryRepository countryRepository;
    private final Gson gson;
    private final ValidationUtils validator;
    private final ModelMapper mapper;
    private final StringBuilder result;

    @Autowired
    public PersonServiceImpl(PersonRepository personRepository, CountryRepository countryRepository, Gson gson, ValidationUtils validator, ModelMapper mapper, StringBuilder result) {
        this.personRepository = personRepository;
        this.countryRepository = countryRepository;
        this.gson = gson;
        this.validator = validator;
        this.mapper = mapper;
        this.result = result;
    }

    @Override
    public boolean areImported() {
        return this.personRepository.count() > 0;
    }

    @Override
    public String readPeopleFromFile() throws IOException {
        return Files.readString(Path.of(PEOPLE_JSON_PATH));
    }

    @Override
    public String importPeople() throws IOException, JAXBException {
        List<PersonDTO> people = Arrays.stream(gson.fromJson(readPeopleFromFile(), PersonDTO[].class)).toList();

        for (PersonDTO personDto : people) {
            boolean isValid = this.validator.isValid(personDto);

            if (this.personRepository.findFirstByFirstNameOrEmailOrPhone(personDto.getFirstName(), personDto.getEmail(), personDto.getPhone()).isPresent()) {
                isValid = false;
            }

            if (isValid) {

                Person person = this.mapper.map(personDto, Person.class);

                Country country = this.countryRepository.getById(personDto.getCountry());

                person.setCountry(country);

                this.personRepository.save(person);

                result.append(String.format(VALID_PERSON_FORMAT,
                        personDto.getFirstName(),
                        personDto.getLastName()));


            } else {
                result.append(INVALID_PERSON).append(System.lineSeparator());
            }
        }
        return result.toString();
    }
}

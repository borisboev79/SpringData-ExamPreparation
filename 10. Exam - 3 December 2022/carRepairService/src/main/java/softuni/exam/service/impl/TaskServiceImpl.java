package softuni.exam.service.impl;

import softuni.exam.constants.CarType;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import softuni.exam.models.dto.TaskDTO;
import softuni.exam.models.dto.TaskExportDTO;
import softuni.exam.models.dto.wrapper.CarsWrapperDTO;
import softuni.exam.models.dto.wrapper.TasksWrapperDTO;
import softuni.exam.models.entity.Car;
import softuni.exam.models.entity.Mechanic;
import softuni.exam.models.entity.Part;
import softuni.exam.models.entity.Task;
import softuni.exam.repository.CarRepository;
import softuni.exam.repository.MechanicRepository;
import softuni.exam.repository.PartRepository;
import softuni.exam.repository.TaskRepository;
import softuni.exam.service.TaskService;
import softuni.exam.util.ValidationUtils;
import softuni.exam.util.XmlParser;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

import static softuni.exam.constants.Messages.INVALID_TASK;
import static softuni.exam.constants.Messages.VALID_TASK_FORMAT;
import static softuni.exam.constants.Paths.CARS_XML_PATH;
import static softuni.exam.constants.Paths.TASKS_XML_PATH;

@Service
public class TaskServiceImpl implements TaskService {
    private final TaskRepository taskRepository;
    private final CarRepository carRepository;
    private final MechanicRepository mechanicRepository;
    private final XmlParser xmlParser;
    private final PartRepository partRepository;
    private final ValidationUtils validator;
    private final ModelMapper mapper;
    private final StringBuilder result;

    public TaskServiceImpl(TaskRepository taskRepository, CarRepository carRepository,
                           MechanicRepository mechanicRepository, XmlParser xmlParser,
                           PartRepository partRepository, ValidationUtils validator,
                           ModelMapper mapper, StringBuilder result) {
        this.taskRepository = taskRepository;
        this.carRepository = carRepository;
        this.mechanicRepository = mechanicRepository;
        this.xmlParser = xmlParser;
        this.partRepository = partRepository;
        this.validator = validator;
        this.mapper = mapper;
        this.result = result;
    }

    @Override
    public boolean areImported() {
        return this.taskRepository.count() > 0;
    }

    @Override
    public String readTasksFileContent() throws IOException {
        return Files.readString(Path.of(TASKS_XML_PATH));
    }

    @Override
    public String importTasks() throws IOException, JAXBException {

        TasksWrapperDTO tasks = xmlParser.parseXml(TasksWrapperDTO.class, TASKS_XML_PATH);

        for (TaskDTO taskDto : tasks.getTasks()) {

            boolean isValid = this.validator.isValid(taskDto);

            if (this.mechanicRepository.findFirstByFirstName(taskDto.getMechanic().getFirstName()).isEmpty()) {
                isValid = false;
            }

            if (isValid) {

                Task task = this.mapper.map(taskDto, Task.class);

                Car car = this.carRepository.findFirstById(taskDto.getCar().getId()).get();
                Mechanic mechanic = this.mechanicRepository.findFirstByFirstName(taskDto.getMechanic().getFirstName()).get();
                Part part = this.partRepository.findFirstById(taskDto.getPart().getId()).get();

                task.setCar(car);
                task.setMechanic(mechanic);
                task.setPart(part);

                this.taskRepository.save(task);

                result.append(String.format(VALID_TASK_FORMAT,
                        taskDto.getPrice()));

            } else {
                result.append(INVALID_TASK).append(System.lineSeparator());
            }
        }
        return result.toString();
    }

    @Override
    public String getCoupeCarTasksOrderByPrice() {

     CarType carType = CarType.coupe;

     return this.taskRepository.findAllByCar_CarTypeOrderByPriceDesc(carType).orElseThrow(NoSuchElementException::new)
             .stream().map(task -> mapper.map(task, TaskExportDTO.class).toString()).collect(Collectors.joining(System.lineSeparator()));

    }
}

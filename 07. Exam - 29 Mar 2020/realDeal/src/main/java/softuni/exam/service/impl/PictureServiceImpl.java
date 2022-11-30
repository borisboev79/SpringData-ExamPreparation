package softuni.exam.service.impl;

import com.google.gson.Gson;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import softuni.exam.models.dto.PictureDTO;
import softuni.exam.models.entity.Car;
import softuni.exam.models.entity.Picture;
import softuni.exam.repository.CarRepository;
import softuni.exam.repository.PictureRepository;
import softuni.exam.service.PictureService;
import softuni.exam.util.ValidationUtil;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;

import static softuni.exam.util.constants.Messages.INVALID_PICTURE;
import static softuni.exam.util.constants.Messages.VALID_PICTURE_FORMAT;
import static softuni.exam.util.constants.Paths.PICTURES_JSON_PATH;

@Service
public class PictureServiceImpl implements PictureService {
    private final PictureRepository pictureRepository;
    private final CarRepository carRepository;
    private final Gson gson;
    private final ValidationUtil validator;
    private final ModelMapper mapper;
    private final StringBuilder result;

    @Autowired
    public PictureServiceImpl(PictureRepository pictureRepository, CarRepository carRepository, @Qualifier("dateTimeConverter") Gson gson, ValidationUtil validator, ModelMapper mapper, StringBuilder result) {
        this.pictureRepository = pictureRepository;
        this.carRepository = carRepository;
        this.gson = gson;
        this.validator = validator;
        this.mapper = mapper;
        this.result = result;
    }

    @Override
    public boolean areImported() {
        return this.pictureRepository.count() > 0;
    }

    @Override
    public String readPicturesFromFile() throws IOException {
        return Files.readString(Path.of(PICTURES_JSON_PATH));
    }

    @Override
    public String importPictures() throws IOException {

        List<PictureDTO> pictures = Arrays.stream(gson.fromJson(readPicturesFromFile(), PictureDTO[].class)).toList();

        for (PictureDTO pictureDto : pictures) {
            boolean isValid = this.validator.isValid(pictureDto);

            if (this.pictureRepository.findFirstByName(pictureDto.getName()).isPresent()) {
                isValid = false;
            }

            if (isValid) {

                Picture picture = this.mapper.map(pictureDto, Picture.class);

                Car car = this.carRepository.findFirstById(pictureDto.getCar()).get();

                picture.setCar(car);

                this.pictureRepository.save(picture);

                result.append(String.format(VALID_PICTURE_FORMAT,
                        pictureDto.getName()));


            } else {
                result.append(INVALID_PICTURE).append(System.lineSeparator());
            }
        }
        return result.toString();
    }
}

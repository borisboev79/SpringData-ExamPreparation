package softuni.exam.instagraphlite.service.impl;

import com.google.gson.Gson;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import softuni.exam.instagraphlite.models.Picture;
import softuni.exam.instagraphlite.models.dto.PictureDTO;
import softuni.exam.instagraphlite.repository.PictureRepository;
import softuni.exam.instagraphlite.service.PictureService;
import softuni.exam.instagraphlite.util.ValidationUtils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;

import static softuni.exam.instagraphlite.util.constants.Messages.INVALID_PICTURE;
import static softuni.exam.instagraphlite.util.constants.Messages.VALID_PICTURE_FORMAT;
import static softuni.exam.instagraphlite.util.constants.Paths.PICTURES_JSON_PATH;

@Service
public class PictureServiceImpl implements PictureService {
    private final PictureRepository pictureRepository;
    private final Gson gson;
    private final ValidationUtils validator;
    private final ModelMapper mapper;
    private final StringBuilder result;

    @Autowired
    public PictureServiceImpl(PictureRepository pictureRepository, Gson gson, ValidationUtils validator, ModelMapper mapper, StringBuilder result) {
        this.pictureRepository = pictureRepository;
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
    public String readFromFileContent() throws IOException {
        return Files.readString(Path.of(PICTURES_JSON_PATH));
    }

    @Override
    public String importPictures() throws IOException {
        List<PictureDTO> pictureDtos = Arrays.stream(gson.fromJson(readFromFileContent(), PictureDTO[].class)).toList();

        for (PictureDTO pictureDto : pictureDtos) {

            boolean isValid = this.validator.isValid(pictureDto);

            if (this.pictureRepository.findFirstByPath(pictureDto.getPath()).isPresent()) {
                isValid = false;
            }

            if (isValid) {

                Picture picture = this.mapper.map(pictureDto, Picture.class);

                this.pictureRepository.save(picture);

                result.append(String.format(VALID_PICTURE_FORMAT,
                        pictureDto.getSize()));


            } else {
                result.append(INVALID_PICTURE).append(System.lineSeparator());
            }
        }
        return result.toString();
    }

    @Override
    public String exportPictures() {

        List<Picture> pictures = this.pictureRepository.findAllBySizeGreaterThanOrderBySizeAsc(30000d).get();

        for (Picture picture : pictures) {
            result.append(picture.toString()).append(System.lineSeparator());
        }

        return result.toString();
    }
}

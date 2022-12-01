package softuni.exam.service;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import softuni.exam.domain.dtos.PictureDTO;
import softuni.exam.domain.dtos.wrappers.PicturesWrapperDTO;
import softuni.exam.domain.entities.Picture;
import softuni.exam.repository.PictureRepository;
import softuni.exam.util.FileUtil;
import softuni.exam.util.ValidatorUtil;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import static softuni.exam.util.constants.Messages.INVALID_PICTURE;
import static softuni.exam.util.constants.Messages.VALID_PICTURE_FORMAT;
import static softuni.exam.util.constants.Paths.PICTURES_XML_PATH;

@Service
public class PictureServiceImpl implements PictureService {
    private final PictureRepository pictureRepository;
    private final JAXBContext context;
    private final ValidatorUtil validator;
    private final ModelMapper mapper;
    private final StringBuilder result;
    private final FileUtil fileReader;

    @Autowired
    public PictureServiceImpl(PictureRepository pictureRepository, @Qualifier("picturesContext") JAXBContext context, ValidatorUtil validator, ModelMapper mapper, StringBuilder result, FileUtil fileReader) {
        this.pictureRepository = pictureRepository;
        this.context = context;
        this.validator = validator;
        this.mapper = mapper;
        this.result = result;
        this.fileReader = fileReader;
    }


    @Override
    public String importPictures() throws JAXBException, FileNotFoundException {
        Unmarshaller unmarshaller = this.context.createUnmarshaller();

        PicturesWrapperDTO pictures = (PicturesWrapperDTO) unmarshaller
                .unmarshal(new FileReader(PICTURES_XML_PATH));


        for (PictureDTO pictureDto : pictures.getPictures()) {

            boolean isValid = this.validator.isValid(pictureDto);

            if (this.pictureRepository.findFirstByUrl(pictureDto.getUrl()).isPresent()) {
                isValid = false;
            }

            if (isValid) {

                Picture picture = this.mapper.map(pictureDto, Picture.class);

                this.pictureRepository.save(picture);

                result.append(String.format(VALID_PICTURE_FORMAT,
                        pictureDto.getUrl()));

            } else {
                result.append(INVALID_PICTURE).append(System.lineSeparator());
            }
        }
        return result.toString();
    }

    @Override
    public boolean areImported() {
        return this.pictureRepository.count() > 0;
    }

    @Override
    public String readPicturesXmlFile() throws IOException {
        return this.fileReader.readFile(PICTURES_XML_PATH);
    }

}

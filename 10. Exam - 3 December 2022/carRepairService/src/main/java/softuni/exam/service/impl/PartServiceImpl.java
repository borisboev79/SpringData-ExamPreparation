package softuni.exam.service.impl;

import com.google.gson.Gson;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import softuni.exam.models.dto.PartDTO;
import softuni.exam.models.entity.Part;
import softuni.exam.repository.PartRepository;
import softuni.exam.service.PartService;
import softuni.exam.util.ValidationUtils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;

import static softuni.exam.constants.Messages.INVALID_PART;
import static softuni.exam.constants.Messages.VALID_PART_FORMAT;
import static softuni.exam.constants.Paths.PARTS_JSON_PATH;

@Service
public class PartServiceImpl implements PartService {
    private final PartRepository partRepository;
    private final Gson gson;
    private final ValidationUtils validator;
    private final ModelMapper mapper;
    private final StringBuilder result;

    @Autowired
    public PartServiceImpl(PartRepository partRepository, Gson gson, ValidationUtils validator, ModelMapper mapper, StringBuilder result) {
        this.partRepository = partRepository;
        this.gson = gson;
        this.validator = validator;
        this.mapper = mapper;
        this.result = result;
    }

    @Override
    public boolean areImported() {
        return this.partRepository.count() > 0;
    }

    @Override
    public String readPartsFileContent() throws IOException {
        return Files.readString(Path.of(PARTS_JSON_PATH));
    }

    @Override
    public String importParts() throws IOException {
        List<PartDTO> parts = Arrays.stream(gson.fromJson(readPartsFileContent(), PartDTO[].class)).toList();

        for (PartDTO partDto : parts) {
            boolean isValid = this.validator.isValid(partDto);

            if (this.partRepository.findFirstByPartName(partDto.getPartName()).isPresent()) {
                isValid = false;
            }

            if (isValid) {

                Part part = this.mapper.map(partDto, Part.class);

                this.partRepository.save(part);

                result.append(String.format(VALID_PART_FORMAT,
                        partDto.getPartName(),
                        partDto.getPrice()));


            } else {
                result.append(INVALID_PART).append(System.lineSeparator());
            }
        }
        return result.toString();
    }
}

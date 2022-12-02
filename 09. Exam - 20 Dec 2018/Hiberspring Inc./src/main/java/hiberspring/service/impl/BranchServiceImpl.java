package hiberspring.service.impl;

import com.google.gson.Gson;
import hiberspring.domain.dtos.BranchDTO;
import hiberspring.domain.entities.Branch;
import hiberspring.domain.entities.Town;
import hiberspring.repository.BranchRepository;
import hiberspring.repository.TownRepository;
import hiberspring.service.BranchService;
import hiberspring.util.FileUtil;
import hiberspring.util.ValidationUtil;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import static hiberspring.common.Constants.*;

@Service
public class BranchServiceImpl implements BranchService {
    private final BranchRepository branchRepository;
    private final TownRepository townRepository;
    private final FileUtil fileReader;
    private final Gson gson;
    private final ValidationUtil validator;
    private final ModelMapper mapper;
    private final StringBuilder result;

    @Autowired
    public BranchServiceImpl(BranchRepository branchRepository, TownRepository townRepository, FileUtil fileReader, Gson gson, ValidationUtil validator, ModelMapper mapper, StringBuilder result) {
        this.branchRepository = branchRepository;
        this.townRepository = townRepository;
        this.fileReader = fileReader;
        this.gson = gson;
        this.validator = validator;
        this.mapper = mapper;
        this.result = result;
    }

    @Override
    public Boolean branchesAreImported() {
        return this.branchRepository.count() > 0;
    }

    @Override
    public String readBranchesJsonFile() throws IOException {
        return this.fileReader.readFile(PATH_TO_FILES + "branches.json");
    }

    @Override
    public String importBranches(String branchesFileContent) throws IOException {
        List<BranchDTO> branches = Arrays.stream(gson.fromJson(readBranchesJsonFile(), BranchDTO[].class)).toList();

        for (BranchDTO branchDto : branches) {
            boolean isValid = this.validator.isValid(branchDto);

            if (this.branchRepository.findFirstByName(branchDto.getName()).isPresent()) {
                isValid = false;
            }

            if (isValid) {

                Branch branch = this.mapper.map(branchDto, Branch.class);

                Town town = this.townRepository.findFirstByName(branchDto.getTown()).get();

                branch.setTown(town);

                this.branchRepository.save(branch);

                result.append(String.format(SUCCESSFUL_IMPORT_MESSAGE,
                        branch.getClass().getSimpleName(),
                        branch.getName()));
                result.append(System.lineSeparator());


            } else {
                result.append(INCORRECT_DATA_MESSAGE).append(System.lineSeparator());
            }
        }
        return result.toString();
    }
}

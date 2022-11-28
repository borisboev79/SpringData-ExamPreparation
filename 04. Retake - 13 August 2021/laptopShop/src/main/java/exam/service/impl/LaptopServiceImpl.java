package exam.service.impl;

import com.google.gson.Gson;
import exam.model.dto.LaptopDTO;
import exam.model.entity.Laptop;
import exam.model.entity.Shop;
import exam.repository.LaptopRepository;
import exam.repository.ShopRepository;
import exam.service.LaptopService;
import exam.util.ValidationUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import javax.xml.bind.JAXBContext;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;

import static exam.util.constants.Messages.INVALID_LAPTOP;
import static exam.util.constants.Messages.VALID_LAPTOP_FORMAT;
import static exam.util.constants.Paths.LAPTOPS_JSON_PATH;

@Service
public class LaptopServiceImpl implements LaptopService {
    private final LaptopRepository laptopRepository;
    private final ShopRepository shopRepository;
    private final Gson gson;
    private final ValidationUtils validator;
    private final ModelMapper mapper;
    private final StringBuilder result;

    @Autowired
    public LaptopServiceImpl(LaptopRepository laptopRepository, ShopRepository shopRepository,
                             @Qualifier("basicGson") Gson gson, ValidationUtils validator,
                             ModelMapper mapper, StringBuilder result) {

        this.laptopRepository = laptopRepository;
        this.shopRepository = shopRepository;
        this.gson = gson;

        this.validator = validator;
        this.mapper = mapper;
        this.result = result;
    }

    @Override
    public boolean areImported() {
        return this.laptopRepository.count() > 0;
    }

    @Override
    public String readLaptopsFileContent() throws IOException {
        return Files.readString(Path.of(LAPTOPS_JSON_PATH));
    }

    @Override
    public String importLaptops() throws IOException {
        List<LaptopDTO> laptopDtos = Arrays.stream(gson.fromJson(readLaptopsFileContent(), LaptopDTO[].class)).toList();

        for (LaptopDTO laptopDto : laptopDtos) {
            boolean isValid = this.validator.isValid(laptopDto);

            if (this.laptopRepository.findFirstByMacAddress(laptopDto.getMacAddress()).isPresent()) {
                isValid = false;
            }

            if (isValid) {

                Laptop laptop = this.mapper.map(laptopDto, Laptop.class);

                Shop shop = this.shopRepository.findFirstByName(laptopDto.getShop().getName()).get();

                laptop.setShop(shop);

                this.laptopRepository.save(laptop);

                result.append(String.format(VALID_LAPTOP_FORMAT,
                        laptopDto.getMacAddress(),
                        laptopDto.getCpuSpeed(),
                        laptopDto.getRam(),
                        laptopDto.getStorage()));


            } else {
                result.append(INVALID_LAPTOP).append(System.lineSeparator());
            }
        }
        return result.toString();
    }

    @Override
    public String exportBestLaptops() {

        List<Laptop> laptops = this.laptopRepository.findAllByOrderByCpuSpeedDescRamDescStorageDescMacAddressAsc().get();

        for (Laptop laptop : laptops) {
            result.append(laptop.toString()).append(System.lineSeparator());
        }

        return result.toString();
    }
}

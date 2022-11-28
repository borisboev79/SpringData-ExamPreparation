package exam.service.impl;

import exam.model.dto.ShopDTO;
import exam.model.dto.wrapper.ShopsWrapperDTO;
import exam.model.entity.Shop;
import exam.model.entity.Town;
import exam.repository.ShopRepository;
import exam.repository.TownRepository;
import exam.service.ShopService;
import exam.util.ValidationUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static exam.util.constants.Messages.INVALID_SHOP;
import static exam.util.constants.Messages.VALID_SHOP_FORMAT;
import static exam.util.constants.Paths.SHOPS_XML_PATH;

@Service
public class ShopServiceImpl implements ShopService {
    private final ShopRepository shopRepository;
    private final TownRepository townRepository;
    private final JAXBContext context;
    private final ValidationUtils validator;
    private final ModelMapper mapper;
    private final StringBuilder result;

    @Autowired
    public ShopServiceImpl(ShopRepository shopRepository, TownRepository townRepository, @Qualifier("shopsContext") JAXBContext context, ValidationUtils validator, ModelMapper mapper, StringBuilder result) {
        this.shopRepository = shopRepository;
        this.townRepository = townRepository;

        this.context = context;
        this.validator = validator;
        this.mapper = mapper;
        this.result = result;
    }

    @Override
    public boolean areImported() {
        return this.shopRepository.count() > 0;
    }

    @Override
    public String readShopsFileContent() throws IOException {
        return Files.readString(Path.of(SHOPS_XML_PATH));
    }

    @Override
    public String importShops() throws JAXBException, FileNotFoundException {
        Unmarshaller unmarshaller = this.context.createUnmarshaller();

        ShopsWrapperDTO shopsDto = (ShopsWrapperDTO) unmarshaller
                .unmarshal(new FileReader(SHOPS_XML_PATH));




        for (ShopDTO shopDto : shopsDto.getShops()) {

            boolean isValid = this.validator.isValid(shopDto);

            if (this.shopRepository.findFirstByName(shopDto.getName()).isPresent()) {
                isValid = false;
            }

            if (isValid) {

                Shop shop = this.mapper.map(shopDto, Shop.class);

                Town town = this.townRepository.findFirstByName(shopDto.getTown().getName()).get();

                shop.setTown(town);

                this.shopRepository.save(shop);

                result.append(String.format(VALID_SHOP_FORMAT,
                        shop.getClass().getSimpleName(),
                        shopDto.getName(),
                        shopDto.getIncome()));

            } else {
                result.append(INVALID_SHOP).append(System.lineSeparator());
            }
        }
        return result.toString();
    }
}

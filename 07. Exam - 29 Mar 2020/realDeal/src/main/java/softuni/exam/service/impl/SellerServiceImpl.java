package softuni.exam.service.impl;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import softuni.exam.models.dto.SellerDTO;
import softuni.exam.models.dto.wrapper.SellersWrapperDTO;
import softuni.exam.models.entity.Seller;
import softuni.exam.repository.SellerRepository;
import softuni.exam.service.SellerService;
import softuni.exam.util.ValidationUtil;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static softuni.exam.util.constants.Messages.INVALID_SELLER;
import static softuni.exam.util.constants.Messages.VALID_SELLER_FORMAT;
import static softuni.exam.util.constants.Paths.SELLERS_XML_PATH;

@Service
public class SellerServiceImpl implements SellerService {
    private final SellerRepository sellerRepository;
    private final JAXBContext context;
    private final ValidationUtil validator;
    private final ModelMapper mapper;
    private final StringBuilder result;

    @Autowired
    public SellerServiceImpl(SellerRepository sellerRepository, @Qualifier("sellersContext") JAXBContext context, ValidationUtil validator, ModelMapper mapper, StringBuilder result) {
        this.sellerRepository = sellerRepository;
        this.context = context;
        this.validator = validator;
        this.mapper = mapper;
        this.result = result;
    }

    @Override
    public boolean areImported() {
        return this.sellerRepository.count() > 0;
    }

    @Override
    public String readSellersFromFile() throws IOException {
        return Files.readString(Path.of(SELLERS_XML_PATH));
    }

    @Override
    public String importSellers() throws IOException, JAXBException {
        Unmarshaller unmarshaller = this.context.createUnmarshaller();

        SellersWrapperDTO sellers = (SellersWrapperDTO) unmarshaller
                .unmarshal(new FileReader(SELLERS_XML_PATH));


        for (SellerDTO sellerDto : sellers.getSellers()) {

            boolean isValid = this.validator.isValid(sellerDto);

            if (this.sellerRepository.findFirstByEmail(sellerDto.getEmail()).isPresent()) {
                isValid = false;
            }

            if (isValid) {

                Seller seller = this.mapper.map(sellerDto, Seller.class);

                this.sellerRepository.save(seller);

                result.append(String.format(VALID_SELLER_FORMAT,
                        sellerDto.getLastName(),
                        sellerDto.getEmail()));

            } else {
                result.append(INVALID_SELLER).append(System.lineSeparator());
            }
        }
        return result.toString();
    }
}

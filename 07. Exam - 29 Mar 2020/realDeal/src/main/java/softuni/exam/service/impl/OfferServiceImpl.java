package softuni.exam.service.impl;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import softuni.exam.models.dto.OfferDTO;
import softuni.exam.models.dto.wrapper.OffersWrapperDTO;
import softuni.exam.models.entity.Car;
import softuni.exam.models.entity.Offer;
import softuni.exam.models.entity.Seller;
import softuni.exam.repository.CarRepository;
import softuni.exam.repository.OfferRepository;
import softuni.exam.repository.SellerRepository;
import softuni.exam.service.OfferService;
import softuni.exam.util.ValidationUtil;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static softuni.exam.util.constants.Messages.INVALID_OFFER;
import static softuni.exam.util.constants.Messages.VALID_OFFER_FORMAT;
import static softuni.exam.util.constants.Paths.OFFERS_XML_PATH;

@Service
public class OfferServiceImpl implements OfferService {
    private final SellerRepository sellerRepository;
    private final CarRepository carRepository;
    private final OfferRepository offerRepository;
    private final JAXBContext context;
    private final ValidationUtil validator;
    private final ModelMapper mapper;
    private final StringBuilder result;

    @Autowired
    public OfferServiceImpl(SellerRepository sellerRepository, CarRepository carRepository, OfferRepository offerRepository, @Qualifier("offersContext") JAXBContext context, ValidationUtil validator, ModelMapper mapper, StringBuilder result) {
        this.sellerRepository = sellerRepository;
        this.carRepository = carRepository;
        this.offerRepository = offerRepository;
        this.context = context;
        this.validator = validator;
        this.mapper = mapper;
        this.result = result;
    }

    @Override
    public boolean areImported() {
        return this.offerRepository.count() > 0;
    }

    @Override
    public String readOffersFileContent() throws IOException {
        return Files.readString(Path.of(OFFERS_XML_PATH));
    }

    @Override
    public String importOffers() throws IOException, JAXBException {
        Unmarshaller unmarshaller = this.context.createUnmarshaller();

        OffersWrapperDTO offers = (OffersWrapperDTO) unmarshaller
                .unmarshal(new FileReader(OFFERS_XML_PATH));


        for (OfferDTO offerDto : offers.getOffers()) {

            boolean isValid = this.validator.isValid(offerDto);

            if (this.offerRepository.findFirstByDescriptionAndAddedOn(offerDto.getDescription(), offerDto.getAddedOn()).isPresent()) {
                isValid = false;
            }

            if (isValid) {

                Offer offer = this.mapper.map(offerDto, Offer.class);

                Car car = this.carRepository.findFirstById(offerDto.getCar().getId()).get();
                Seller seller = this.sellerRepository.findFirstById(offerDto.getSeller().getId()).get();

                offer.setCar(car);
                offer.setSeller(seller);

                this.offerRepository.save(offer);

                result.append(String.format(VALID_OFFER_FORMAT,
                        offerDto.getAddedOn(),
                        String.valueOf(offer.isHasGoldStatus()).toLowerCase()));

            } else {
                result.append(INVALID_OFFER).append(System.lineSeparator());
            }
        }
        return result.toString();
    }
}

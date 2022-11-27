package softuni.exam.service.impl;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import softuni.exam.models.dto.OfferDTO;
import softuni.exam.models.dto.wrapper.OffersWrapperDTO;
import softuni.exam.models.entity.Agent;
import softuni.exam.models.entity.Apartment;
import softuni.exam.models.entity.Offer;
import softuni.exam.repository.AgentRepository;
import softuni.exam.repository.ApartmentRepository;
import softuni.exam.repository.OfferRepository;
import softuni.exam.service.OfferService;
import softuni.exam.util.ValidationUtils;
import softuni.exam.util.constants.ApartmentType;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;

import static softuni.exam.util.constants.Messages.*;
import static softuni.exam.util.constants.Paths.OFFERS_XML_PATH;

@Service
public class OfferServiceImpl implements OfferService {
    private final OfferRepository offerRepository;
    private final AgentRepository agentRepository;
    private final ApartmentRepository apartmentRepository;
    private final JAXBContext context;
    private final ValidationUtils validator;
    private final ModelMapper mapper;
    private final StringBuilder result;

    @Autowired
    public OfferServiceImpl(OfferRepository offerRepository, AgentRepository agentRepository,
                            ApartmentRepository apartmentRepository, @Qualifier("offersContext") JAXBContext context, ValidationUtils validator, ModelMapper mapper, StringBuilder result) {
        this.offerRepository = offerRepository;
        this.agentRepository = agentRepository;
        this.apartmentRepository = apartmentRepository;
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

        OffersWrapperDTO offersDto = (OffersWrapperDTO) unmarshaller
                .unmarshal(new FileReader(OFFERS_XML_PATH));


        for (OfferDTO offerDto : offersDto.getOffers()) {

            boolean isValid = this.validator.isValid(offerDto);

            if (this.agentRepository.findFirstByFirstName(offerDto.getAgent().getName()).isEmpty()) {
                isValid = false;
            }

            if (isValid) {

                Offer offer = this.mapper.map(offerDto, Offer.class);

                final Agent agent = this.agentRepository.findFirstByFirstName(offerDto.getAgent().getName()).get();
                final Apartment apartment = this.apartmentRepository.findFirstById(offerDto.getApartment().getId()).get();

                offer.setAgent(agent);
                offer.setApartment(apartment);

                this.offerRepository.save(offer);

                result.append(String.format(VALID_OFFER_FORMAT,
                        offerDto.getPrice()));


            } else {
                result.append(INVALID_OFFER).append(System.lineSeparator());
            }
        }
        return result.toString();
    }

    @Override
    public String exportOffers() {
        ApartmentType type = ApartmentType.three_rooms;
        List<Offer> agents = this.offerRepository.findAllByApartment_ApartmentTypeOrderByApartment_AreaDescPriceAsc(type).get();

        for (Offer agent : agents) {
            result.append(agent.toString()).append(System.lineSeparator());
        }

        return result.toString();
    }
}

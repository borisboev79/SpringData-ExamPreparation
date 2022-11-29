package softuni.exam.service.impl;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import softuni.exam.models.dto.TicketDTO;
import softuni.exam.models.dto.wrapper.TicketsWrapperDTO;
import softuni.exam.models.entity.Passenger;
import softuni.exam.models.entity.Plane;
import softuni.exam.models.entity.Ticket;
import softuni.exam.models.entity.Town;
import softuni.exam.repository.PassengerRepository;
import softuni.exam.repository.PlaneRepository;
import softuni.exam.repository.TicketRepository;
import softuni.exam.repository.TownRepository;
import softuni.exam.service.TicketService;
import softuni.exam.util.ValidationUtil;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static softuni.exam.util.constants.Messages.*;
import static softuni.exam.util.constants.Paths.PLANES_XML_PATH;
import static softuni.exam.util.constants.Paths.TICKETS_XML_PATH;

@Service
class TicketServiceImpl implements TicketService {
    private final TicketRepository ticketRepository;
    private final TownRepository townRepository;
    private final PassengerRepository passengerRepository;
    private final PlaneRepository planeRepository;
    private final JAXBContext context;
    private final ValidationUtil validator;
    private final ModelMapper mapper;
    private final StringBuilder result;

    @Autowired
    TicketServiceImpl(TicketRepository ticketRepository, TownRepository townRepository, PassengerRepository passengerRepository, PlaneRepository planeRepository, @Qualifier("ticketsContext") JAXBContext context, ValidationUtil validator, ModelMapper mapper, StringBuilder result) {
        this.ticketRepository = ticketRepository;
        this.townRepository = townRepository;
        this.passengerRepository = passengerRepository;
        this.planeRepository = planeRepository;

        this.context = context;
        this.validator = validator;
        this.mapper = mapper;
        this.result = result;
    }

    @Override
    public boolean areImported() {
        return this.ticketRepository.count() > 0;
    }

    @Override
    public String readTicketsFileContent() throws IOException {
        return Files.readString(Path.of(TICKETS_XML_PATH));
    }

    @Override
    public String importTickets() throws FileNotFoundException, JAXBException {
        Unmarshaller unmarshaller = this.context.createUnmarshaller();

        TicketsWrapperDTO ticketsDto = (TicketsWrapperDTO) unmarshaller
                .unmarshal(new FileReader(TICKETS_XML_PATH));


        for (TicketDTO ticketDto : ticketsDto.getTickets()) {

            boolean isValid = this.validator.isValid(ticketDto);

            if (this.ticketRepository.findFirstBySerialNumber(ticketDto.getSerialNumber()).isPresent()) {
                isValid = false;
            }

            if (isValid) {

                Ticket ticket = this.mapper.map(ticketDto, Ticket.class);

                Town fromTown = this.townRepository.findFirstByName(ticketDto.getFromTown().getName()).get();
                Town toTown = this.townRepository.findFirstByName(ticketDto.getToTown().getName()).get();
                Passenger passenger = this.passengerRepository.findFirstByEmail(ticketDto.getPassenger().getEmail()).get();
                Plane plane = this.planeRepository.findFirstByRegisterNumber(ticketDto.getPlane().getRegisterNumber()).get();

               ticket.setFromTown(fromTown);
               ticket.setToTown(toTown);
               ticket.setPassenger(passenger);
               ticket.setPlane(plane);

                this.ticketRepository.save(ticket);

                result.append(String.format(VALID_TICKET_FORMAT,
                        ticket.getClass().getSimpleName(),
                        ticketDto.getFromTown().getName(),
                        ticketDto.getToTown().getName()));

            } else {
                result.append(String.format(INVALID_MESSAGE_FORMAT, "Ticket")).append(System.lineSeparator());
            }
        }
        return result.toString();
    }
}

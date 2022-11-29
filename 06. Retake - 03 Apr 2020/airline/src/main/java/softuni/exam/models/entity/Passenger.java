package softuni.exam.models.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "passengers")
public class Passenger extends AutoId{

    @Column(name = "first_name", nullable = false)
    private String firstName;

    @Column(name = "last_name", nullable = false)
    private String lastName;

    @Column
    private int age;

    @Column(name = "phone_number", nullable = false)
    private String phoneNumber;

    @Column(nullable = false)
    private String email;

    @ManyToOne(optional = false)
    @JoinColumn(name = "town_id", referencedColumnName = "id")
    @Fetch(FetchMode.JOIN)
    private Town town;

    @OneToMany(mappedBy = "passenger", targetEntity = Ticket.class)
    private List<Ticket> tickets;

    public Passenger(){
        this.tickets = new ArrayList<>();
    }

    public Passenger(String firstName, String lastName, int age, String phoneNumber, String email, Town town){
        this();
        this.firstName = firstName;
        this.lastName = lastName;
        this.age = age;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.town = town;
    }

    @Override
    public String toString() {
        return "Passenger " + firstName + " " + lastName + System.lineSeparator() +
                "   Email - " + email + System.lineSeparator() +
                "   Phone - " + phoneNumber + System.lineSeparator() +
                String.format("   Number of tickets - %d%n",tickets.size());
    }
}

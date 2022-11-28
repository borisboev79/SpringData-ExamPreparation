package softuni.exam.instagraphlite.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "posts")
public class Post extends AutoId{

    @Column(nullable = false)
    private String caption;

    @ManyToOne(optional = false)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    @Fetch(FetchMode.JOIN)
    private User user;

    @ManyToOne(optional = false)
    @JoinColumn(name = "picture_id", referencedColumnName = "id")
    @Fetch(FetchMode.JOIN)
    private Picture picture;

    @Override
    public String toString() {
        return "==Post Details:" + System.lineSeparator() +
                "----Caption: " + caption + System.lineSeparator() +
                String.format("----Picture Size: %.2f", picture.getSize());
    }
}

package softuni.exam.instagraphlite.models;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@Entity
@Table(name = "users")
public class User extends AutoId{

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private String password;

    @ManyToOne(optional = false)
    @JoinColumn(name = "profile_picture_id", referencedColumnName = "id")
    @Fetch(FetchMode.JOIN)
    private Picture profilePicture;

    @OneToMany(mappedBy = "user", targetEntity = Post.class, fetch = FetchType.EAGER)
    private List<Post> posts;

    public User() {
        this.posts = new ArrayList<>();
    }

    public User(String username, String password, Picture profilePicture) {
        this();
        this.username = username;
        this.password = password;
        this.profilePicture = profilePicture;
    }
        @Override
        public String toString() {

            return "User: " + username + System.lineSeparator() +
                    "Post count: " + posts.size() + System.lineSeparator() +
                    posts
                            .stream()
                            .sorted(Comparator.comparingDouble(p -> p.getPicture().getSize()))
                            .map(Post::toString)
                            .collect(Collectors.joining(System.lineSeparator()));
    }
}

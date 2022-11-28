package exam.model.entity;

import javax.persistence.*;

@MappedSuperclass
public abstract class AutoId {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Long id;

}

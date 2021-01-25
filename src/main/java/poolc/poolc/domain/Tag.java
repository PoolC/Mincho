package poolc.poolc.domain;

import lombok.Getter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity(name = "Tag")
@Getter
public class Tag {
    @Id
    @GeneratedValue
    @Column(name = "ID")
    private Long id;

    @Column(name = "name", columnDefinition = "varchar(1024)", nullable = false)
    private String name;

}
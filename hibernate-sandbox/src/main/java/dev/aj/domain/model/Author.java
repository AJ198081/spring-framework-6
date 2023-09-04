package dev.aj.domain.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

@Entity(name = "author")
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public final class Author {

    @Id
//    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "hilo-demo")
    @GenericGenerator(name = "hilo-demo", type = org.hibernate.id.enhanced.SequenceStyleGenerator.class,
    parameters = {
            @Parameter(name = "sequence_name", value = "author_sequence2"),
            @Parameter(name = "initial_value", value = "4"),
            @Parameter(name = "increment_size", value = "4"),
//            @Parameter(name = "optimizer", value = "hilo")
            @Parameter(name = "optimizer", value = "pooled-lo")
    })
    private Long id;

    private String name;

}

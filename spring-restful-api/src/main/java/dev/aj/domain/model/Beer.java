package dev.aj.domain.model;

import dev.aj.domain.enums.BeerStyle;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.UuidGenerator;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Beer {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(length = 36, nullable = false, updatable = false, columnDefinition = "varchar")
    private UUID id;

    private Integer version;

    private String beerName;

    private BeerStyle beerStyle;

    private String upc;

    private Integer quantityOnHand;

    private BigDecimal price;

    private LocalDateTime createdDate;

    private LocalDateTime updatedDate;
}

package dev.aj.domain.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Customer {
    private Long id;
    private Integer version;
    private String firstName;
    private String lastName;
    private LocalDateTime createdDate;
    private LocalDateTime lastModifiedDate;
}

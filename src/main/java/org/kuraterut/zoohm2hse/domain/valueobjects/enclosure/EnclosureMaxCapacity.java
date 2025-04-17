package org.kuraterut.zoohm2hse.domain.valueobjects.enclosure;


import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.Positive;
import lombok.Value;

@Embeddable
@Value
public class EnclosureMaxCapacity {
    @Positive
    @Column(name = "max_capacity")
    int value;

    public EnclosureMaxCapacity(int value) {
        if (value <= 0) {
            throw new IllegalArgumentException("Вместимость должна быть положительной");
        }
        this.value = value;
    }

    protected EnclosureMaxCapacity() { // Для JPA
        this.value = 0;
    }
}
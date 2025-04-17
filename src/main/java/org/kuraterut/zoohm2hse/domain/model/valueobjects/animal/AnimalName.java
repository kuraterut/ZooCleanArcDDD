package org.kuraterut.zoohm2hse.domain.model.valueobjects.animal;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Value;
import org.kuraterut.zoohm2hse.application.exceptions.InvalidAnimalNameException;

@Embeddable
@Value
public class AnimalName {
    @Column(name = "name")
    String value;

    public AnimalName(String value) {
        if (value == null || value.isBlank()) {
            throw new InvalidAnimalNameException("Имя животного не может быть пустым");
        }
        if (value.length() > 100) {
            throw new InvalidAnimalNameException("Имя животного слишком длинное");
        }
        this.value = value;
    }

    protected AnimalName() {
        this.value = null;
    }
}

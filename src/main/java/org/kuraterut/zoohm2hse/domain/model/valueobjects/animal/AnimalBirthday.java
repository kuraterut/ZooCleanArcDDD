package org.kuraterut.zoohm2hse.domain.model.valueobjects.animal;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Value;
import java.time.LocalDate;
import java.time.Period;

@Embeddable
@Value
public class AnimalBirthday {
    @Column(name = "birth_date")
    LocalDate value;

    public AnimalBirthday(LocalDate value) {
        if (value == null || value.isAfter(LocalDate.now())) {
            throw new IllegalArgumentException("Дата рождения должна быть в прошлом");
        }
        this.value = value;
    }

    protected AnimalBirthday() {
        this.value = null;
    }

    public int getAge() {
        return Period.between(value, LocalDate.now()).getYears();
    }
}

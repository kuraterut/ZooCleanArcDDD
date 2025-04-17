package org.kuraterut.zoohm2hse.domain.model.valueobjects.animal;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Value;

@Embeddable
@Value
public class AnimalHealthFlag {
    @Column(name = "healthy")
    boolean value;

    public AnimalHealthFlag(boolean value) {
        this.value = value;
    }

    protected AnimalHealthFlag() {
        this.value = false;
    }

    public String toStatus() {
        return value ? "HEALTHY" : "SICK";
    }
}
package org.kuraterut.zoohm2hse.presentation.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.kuraterut.zoohm2hse.domain.valueobjects.enclosure.EnclosureType;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CreateEnclosureRequest {
    private EnclosureType type;
    private int maxCapacity;
}

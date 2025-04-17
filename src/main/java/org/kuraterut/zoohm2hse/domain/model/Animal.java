package org.kuraterut.zoohm2hse.domain.model;

import jakarta.persistence.*;
import lombok.*;
import org.kuraterut.zoohm2hse.domain.model.valueobjects.animal.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Animal {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Embedded
    @Column(nullable = false)
    private AnimalName name;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AnimalType type;

    @Embedded
    @Column(nullable = false)
    private AnimalBirthday birthDate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Gender gender;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private FoodType favoriteFood;

    @Embedded
    @Column(nullable = false)
    private AnimalHealthFlag healthy;

    @ManyToOne
    @JoinColumn(name = "enclosure_id")
    private Enclosure enclosure;


    public void feed() {
        System.out.println(name + " покормлен(а) " + favoriteFood);
    }

    public void heal() {
        this.healthy = new AnimalHealthFlag(true);
        System.out.println("Животное " + name + " вылечено");
    }

    public void markAsSick() {
        this.healthy = new AnimalHealthFlag(false);
        System.out.println("Животное " + name + " помечено как больное");
    }

}
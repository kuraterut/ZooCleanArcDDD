package org.kuraterut.zoohm2hse.domain;

import jakarta.persistence.*;
import lombok.*;
import org.kuraterut.zoohm2hse.domain.valueobjects.AnimalType;
import org.kuraterut.zoohm2hse.domain.valueobjects.FoodType;
import org.kuraterut.zoohm2hse.domain.valueobjects.Gender;

import java.time.LocalDate;

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

    @Column(nullable = false)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AnimalType type;

    @Column(nullable = false)
    private LocalDate birthDate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Gender gender;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private FoodType favoriteFood;

    @Column(nullable = false)
    private boolean healthy = true;

    @ManyToOne
    @JoinColumn(name = "enclosure_id")
    private Enclosure enclosure;


    // Методы доменной логики
    public void feed() {
        System.out.println(name + " покормлен(а) " + favoriteFood);
    }

    public void heal() {
        this.healthy = true;
        System.out.println("Животное " + name + " вылечено");
    }

    public void markAsSick() {
        this.healthy = false;
        System.out.println("Животное " + name + " помечено как больное");
    }

}
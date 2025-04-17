-- Вольеры
CREATE TABLE IF NOT EXISTS enclosure (
                                         id BIGINT PRIMARY KEY AUTO_INCREMENT,
                                         type VARCHAR(20) NOT NULL CHECK (type IN ('PREDATOR_ENCLOSURE', 'HERBIVORE_ENCLOSURE', 'AVIARY', 'AQUARIUM')),
    max_capacity INT NOT NULL CHECK (max_capacity > 0),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
    );

-- Животные
CREATE TABLE IF NOT EXISTS animal (
                                      id BIGINT PRIMARY KEY AUTO_INCREMENT,
                                      name VARCHAR(100) NOT NULL,
    type VARCHAR(10) NOT NULL CHECK (type IN ('PREDATOR', 'HERBIVORE', 'BIRD', 'AQUATIC')),
    birth_date DATE NOT NULL,
    gender VARCHAR(7) NOT NULL CHECK (gender IN ('MALE', 'FEMALE', 'UNKNOWN')),
    favorite_food VARCHAR(10) NOT NULL CHECK (favorite_food IN ('MEAT', 'FISH', 'FRUIT', 'VEGETABLES', 'GRAIN')),
    healthy BOOLEAN NOT NULL DEFAULT true,
    enclosure_id BIGINT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (enclosure_id) REFERENCES enclosure(id) ON DELETE SET NULL
    );

-- Расписание кормлений
CREATE TABLE IF NOT EXISTS feeding_schedule (
                                                id BIGINT PRIMARY KEY AUTO_INCREMENT,
                                                animal_id BIGINT NOT NULL,
                                                feeding_time TIME NOT NULL,
                                                food_type VARCHAR(10) NOT NULL CHECK (food_type IN ('MEAT', 'FISH', 'FRUIT', 'VEGETABLES', 'GRAIN')),
    is_completed BOOLEAN NOT NULL DEFAULT false,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (animal_id) REFERENCES animal(id) ON DELETE CASCADE
    );

-- Индексы для ускорения поиска
CREATE INDEX idx_animal_enclosure ON animal(enclosure_id);
CREATE INDEX idx_feeding_animal ON feeding_schedule(animal_id);
CREATE INDEX idx_feeding_time ON feeding_schedule(feeding_time);
DELETE FROM feeding_schedule;
DELETE FROM animal;
DELETE FROM enclosure;
ALTER TABLE enclosure ALTER COLUMN id RESTART WITH 1;
ALTER TABLE animal ALTER COLUMN id RESTART WITH 1;
ALTER TABLE feeding_schedule ALTER COLUMN id RESTART WITH 1;

INSERT INTO enclosure (type, max_capacity) VALUES
('PREDATOR_ENCLOSURE', 3),
('HERBIVORE_ENCLOSURE', 5),
('AVIARY', 4),
('AQUARIUM', 2);


INSERT INTO animal (name, type, birth_date, gender, favorite_food, healthy, enclosure_id) VALUES
('Симба', 'PREDATOR', '2019-05-15', 'MALE', 'MEAT', true, 1),
('Шерхан', 'PREDATOR', '2017-11-03', 'MALE', 'MEAT', false, 1),
('Зебра', 'HERBIVORE', '2020-03-10', 'FEMALE', 'VEGETABLES', true, 2),
('Жираф', 'HERBIVORE', '2018-07-21', 'MALE', 'FRUIT', true, 2),
('Кеша', 'BIRD', '2021-01-12', 'MALE', 'GRAIN', true, 3),
('Немо', 'AQUATIC', '2022-02-28', 'MALE', 'FISH', true, 4);


INSERT INTO feeding_schedule (animal_id, feeding_time, food_type) VALUES
(1, '09:00:00', 'MEAT'),    -- Симба утром
(1, '18:00:00', 'MEAT'),    -- Симба вечером
(2, '10:00:00', 'MEAT'),    -- Шерхан
(3, '08:30:00', 'VEGETABLES'), -- Зебра
(4, '11:00:00', 'FRUIT'),   -- Жираф
(5, '07:00:00', 'GRAIN'),   -- Кеша
(6, '12:00:00', 'FISH');    -- Немо
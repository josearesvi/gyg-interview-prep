create schema if not exists getyourguide;

CREATE TABLE IF NOT EXISTS getyourguide.activity (
    id            BIGINT PRIMARY KEY,
    title         VARCHAR(255),
    price         FLOAT,
    currency      VARCHAR(16),
    rating        FLOAT,
    special_offer BOOLEAN,
    supplier_id   BIGINT
);

INSERT INTO getyourguide.activity (id, title, price, currency, rating, special_offer, supplier_id) VALUES
(25651, 'Reichstag: Parliament Quarter Tour & Glass Dome', 14, '$', 4.8, FALSE, 1),
(6960,  'Pergamon Museum: Skip-the-Line Entry Ticket', 21, '$', 4.8, FALSE, 2),
(80426, 'Berlin City Pass: Public Transport & Discounts', 10, '$', 4.6, FALSE, 2),
(23113, 'Berlin TV Tower: Fast-Track Ticket', 143, '$', 4.6, FALSE, 2),
(26093, 'Spree River: 1-Hour Sightseeing Cruise', 14, '$', 4.5, FALSE, 1),
(10871, 'Berlin Wall & Cold War Walking Tour', 12, '$', 4.7, TRUE, 3),
(63071, 'Potsdam Palaces Half-Day Trip from Berlin', 45, '$', 4.4, FALSE, 3),
(51932, 'Berlin: Street Art & Graffiti Workshop', 29, '$', 4.9, TRUE, 200),
(44580, 'Sachsenhausen Memorial Guided Tour', 23, '$', 4.8, FALSE, 1),
(37201, 'Berlin Hop-On Hop-Off Bus: 24-Hour Ticket', 25, '$', 4.1, TRUE, 2),
(90213, 'Kreuzberg Food Tour with Tastings', 59, '$', 4.9, FALSE, 250),
(18034, 'Berlin Zoo: Entry Ticket', 19, '$', 4.3, FALSE, 3),
(72290, 'Berlin by Bike: Highlights Tour', 28, '$', 4.7, FALSE, 200),
(66514, 'Museum Island: 3-Museum Pass', 34, '$', 4.6, TRUE, 2),
(58820, 'Berlin Underworlds: Bunker Tour', 18, '$', 4.8, FALSE, 5),
(47715, 'Charlottenburg Palace: Entry Ticket', 17, '$', 4.2, FALSE, 3),
(33408, 'Berlin Evening Comedy Show', 22, '$', 4.0, FALSE, 250);

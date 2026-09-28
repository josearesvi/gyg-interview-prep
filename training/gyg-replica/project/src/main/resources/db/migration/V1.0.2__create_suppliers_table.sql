CREATE TABLE getyourguide.supplier (
    id      INT PRIMARY KEY,
    name    VARCHAR(255),
    address VARCHAR(255),
    zip     VARCHAR(20),
    city    VARCHAR(100),
    country VARCHAR(100)
);

INSERT INTO getyourguide.supplier (id, name, address, zip, city, country) VALUES
(1,   'Spree Tours GmbH',     'Unter den Linden 12',  '10117',  'Berlin',    'Germany'),
(2,   'City Pass Berlin',     'Alexanderplatz 3',     '10178',  'Berlin',    'Germany'),
(3,   'Hauptstadt Guides',    'Friedrichstrasse 101', '10117',  'Berlin',    'Germany'),
(200, 'Urban Wheels',         '88 Harbour Road',      '999077', 'Hong Kong', 'China'),
(250, 'Taste of Kreuzberg',   'Oranienstrasse 25',    '10999',  'Berlin',    'Germany');

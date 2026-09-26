INSERT INTO activity (id, title, supplier) VALUES (1, 'Colosseum skip-the-line tour', 'Roma Tours');
INSERT INTO activity (id, title, supplier) VALUES (2, 'Berlin Wall bike tour', 'Bike Berlin');
INSERT INTO activity (id, title, supplier) VALUES (3, 'Sagrada Familia guided visit', 'BCN Guides');

INSERT INTO review (activity_id, author, rating, comment, flagged, created_at) VALUES (1, 'Ana',   5, 'Amazing guide, skip the line really worked', FALSE, TIMESTAMP '2026-05-01 10:00:00');
INSERT INTO review (activity_id, author, rating, comment, flagged, created_at) VALUES (1, 'Ben',   4, 'Great but very crowded',                    FALSE, TIMESTAMP '2026-05-03 10:00:00');
INSERT INTO review (activity_id, author, rating, comment, flagged, created_at) VALUES (1, 'Chen',  3, 'Our guide was 20 minutes late',             FALSE, TIMESTAMP '2026-05-02 10:00:00');
INSERT INTO review (activity_id, author, rating, comment, flagged, created_at) VALUES (1, 'Troll', 1, 'total scam do not book',                    TRUE,  TIMESTAMP '2026-05-04 10:00:00');
INSERT INTO review (activity_id, author, rating, comment, flagged, created_at) VALUES (2, 'Dora',  5, 'Best bike tour in Berlin',                  FALSE, TIMESTAMP '2026-06-01 10:00:00');

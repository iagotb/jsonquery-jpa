--
-- Dumping data for table thing
--

INSERT INTO thing (id, name) VALUES
(1, 'Paper'),
(2, 'Pen'),
(3, 'Pencil'),
(4, 'Ballpen');

--
-- Dumping data for table parent
--

INSERT INTO parent (id, store, thing_id) VALUES
(1, 'StoreA', 1),
(2, 'StoreB', 2),
(3, 'StoreC', 3),
(4, 'StoreD', 4);

--
-- Dumping data for table child
--

INSERT INTO child (id, age, birthDate, creationDate, money, name, parent_id, married) VALUES
(1, 10, '2001-10-01 00:00:00', '2011-10-01 00:00:00', 2000.75, 'John Smith', 1, b'1'),
(2, 20, '1991-12-25 00:00:00', '2011-12-25 00:00:00', 1500.5, 'Jane Adams', 2, b'0'),
(3, 15, '1996-01-01 00:00:00', '2011-01-01 00:00:00', 3000.75, 'Mike Myers', 3, b'1'),
(4, 30, '1981-11-15 00:00:00', '2011-11-15 00:00:00', 2500.5, 'Mark Johnson', 4, NULL);

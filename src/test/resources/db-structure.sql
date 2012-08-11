--
-- Database: jsonquery
--

-- --------------------------------------------------------

--
-- Table structure for table thing
--

CREATE TABLE thing (
  id bigint generated by default as identity not null primary key,
  name varchar(255),
);


--
-- Table structure for table parent
--

CREATE TABLE parent (
  id bigint generated by default as identity not null primary key,
  store varchar(255),
  thing_id bigint,
  CONSTRAINT FK8E0FF4CA27417BEC FOREIGN KEY (thing_id) REFERENCES thing (id)
);

--
-- Table structure for table child
--

CREATE TABLE child (
  id bigint generated by default as identity not null primary key,
  age int,
  birthDate datetime,
  creationDate datetime,
  money double,
  name varchar(255),
  parent_id bigint,
  married bit,
  constraint FK3E104FC5EAA7EC8 FOREIGN KEY (parent_id) REFERENCES parent (id)
);

--
-- Table structure for table grandparent
--

CREATE TABLE grandparent (
  id bigint generated by default as identity not null primary key,
  DTYPE varchar(31),
  age varchar(255)
);

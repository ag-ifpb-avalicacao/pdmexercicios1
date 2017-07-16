CREATE TABLE message(
  id  serial not NULL,
  content character varying(255) NOT NULL,
  is_person_1 boolean,
  PRIMARY KEY (id)
)

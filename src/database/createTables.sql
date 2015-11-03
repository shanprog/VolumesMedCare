CREATE TABLE IF NOT EXISTS mo_list (
  id_mo    INT AUTO_INCREMENT,
  nomer_mo INT NOT NULL,
  name     TINYTEXT,
  sort     INT,
  actual   BOOLEAN,
  PRIMARY KEY (id_mo)
);

CREATE TABLE IF NOT EXISTS profiles (
  id_profile INT AUTO_INCREMENT,
  name       CHAR(200),
  PRIMARY KEY (id_profile)
);

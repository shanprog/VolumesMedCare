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

CREATE TABLE IF NOT EXISTS roles (
  id_role INT AUTO_INCREMENT,
  name    CHAR(50),
  PRIMARY KEY (id_role)
) ;

CREATE TABLE IF NOT EXISTS users (
  id_user  INT     AUTO_INCREMENT,
  login    CHAR(50),
  password CHAR(50),
  name     CHAR(50),
  role     INT,
  active   BOOLEAN DEFAULT 1,
  PRIMARY KEY (id_user),
  FOREIGN KEY (role) REFERENCES roles (id_role)
);


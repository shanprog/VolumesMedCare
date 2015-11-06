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
);

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

CREATE TABLE IF NOT EXISTS offers_hours_24 (
  id_h24     INT AUTO_INCREMENT,
  id_mo      INT,
  id_profile INT,
  bed        INT,
  hosp       INT,
  kdays      INT,
  year       INT,
  PRIMARY KEY (id_h24),
  FOREIGN KEY (id_mo) REFERENCES mo_list (id_mo),
  FOREIGN KEY (id_profile) REFERENCES profiles (id_profile)
);

CREATE TABLE IF NOT EXISTS offers_hours_8 (
  id_h8      INT AUTO_INCREMENT,
  id_mo      INT,
  id_profile INT,
  out_p      INT,
  kdays      INT,
  year       INT,
  PRIMARY KEY (id_h8),
  FOREIGN KEY (id_mo) REFERENCES mo_list (id_mo),
  FOREIGN KEY (id_profile) REFERENCES profiles (id_profile)
);

CREATE TABLE IF NOT EXISTS offers_ambul (
  id_ambul   INT AUTO_INCREMENT,
  id_mo      INT,
  id_profile INT,
  prof       INT,
  neot       INT,
  zab        INT,
  year       INT,
  PRIMARY KEY (id_ambul),
  FOREIGN KEY (id_mo) REFERENCES mo_list (id_mo),
  FOREIGN KEY (id_profile) REFERENCES profiles (id_profile)
);

CREATE TABLE IF NOT EXISTS offers_ambul_uet (
  id_ambul_uet INT AUTO_INCREMENT,
  id_mo        INT,
  id_profile   INT,
  prof         INT,
  neot         INT,
  zab          INT,
  year         INT,
  PRIMARY KEY (id_ambul_uet),
  FOREIGN KEY (id_mo) REFERENCES mo_list (id_mo),
  FOREIGN KEY (id_profile) REFERENCES profiles (id_profile)
);

CREATE TABLE IF NOT EXISTS offers_smp (
  id_smp     INT AUTO_INCREMENT,
  id_mo      INT,
  id_profile INT,
  offer      INT,
  year       INT,
  PRIMARY KEY (id_smp),
  FOREIGN KEY (id_mo) REFERENCES mo_list (id_mo),
  FOREIGN KEY (id_profile) REFERENCES profiles (id_profile)
);
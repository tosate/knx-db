DROP TABLE IF EXISTS groupaddress;
DROP TABLE IF EXISTS device;
DROP TABLE IF EXISTS room;
DROP TABLE IF EXISTS project;

CREATE TABLE IF NOT EXISTS project (
  projectid INTEGER PRIMARY KEY AUTOINCREMENT,
  name TEXT NOT NULL
);

CREATE TABLE IF NOT EXISTS room (
  roomid INTEGER  PRIMARY KEY AUTOINCREMENT,
  name TEXT NOT NULL,
  label TEXT NOT NULL,
  floor TEXT,
  roomproject INTEGER -- foreign key
);

CREATE TABLE IF NOT EXISTS device (
  deviceid INTEGER  PRIMARY KEY AUTOINCREMENT,
  label TEXT NOT NULL,
  nameaffix TEXT,
  devicetype Text NOT NULL,
  deviceroom INTEGER
);

CREATE TABLE IF NOT EXISTS groupaddress (
  groupaddressid INTEGER  PRIMARY KEY AUTOINCREMENT,
  maingroup INTEGER NOT NULL,
  middlegroup INTEGER NOT NULL,
  subgroup INTEGER NOT NULL,
  datatype TEXT,
  function TEXT,
  groupaddressdevice INTEGER -- foreign key
);

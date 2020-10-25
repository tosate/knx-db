DROP TABLE IF EXISTS groupaddress;
DROP TABLE IF EXISTS device;
DROP TABLE IF EXISTS type;
DROP TABLE IF EXISTS room;
DROP TABLE IF EXISTS project;

CREATE TABLE IF NOT EXISTS project (
  projectid INTEGER,
  name TEXT NOT NULL,
  PRIMARY KEY (projectid ASC)
);

CREATE TABLE IF NOT EXISTS room (
  roomid INTEGER NOT NULL,
  name TEXT NOT NULL,
  label TEXT NOT NULL,
  floor TEXT,
  roomproject INTEGER NOT NULL, -- foreign key
  PRIMARY KEY (roomid ASC)
);

CREATE TABLE IF NOT EXISTS device (
  deviceid INTEGER NOT NULL,
  label TEXT NOT NULL,
  devicetype INTEGER NOT NULL, -- foreign key
  deviceroom INTEGER,
  PRIMARY KEY (deviceid ASC)
);

CREATE TABLE IF NOT EXISTS type (
  typeid INTEGER NOT NULL,
  label TEXT NOT NULL,
  PRIMARY KEY (typeid ASC)
);

CREATE TABLE IF NOT EXISTS groupaddress (
  groupaddressid INTEGER NOT NULL,
  maingroup INTEGER NOT NULL,
  middlegroup INTEGER NOT NULL,
  subgroup INTEGER NOT NULL,
  datatype TEXT,
  function TEXT,
  addressdevice INTEGER NOT NULL, -- foreign key
  PRIMARY KEY (groupaddressid ASC)
);

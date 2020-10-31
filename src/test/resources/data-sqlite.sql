INSERT INTO project (name) VALUES ('Salm Saarburg');

INSERT INTO room (name,label,floor,roomproject) VALUES ('Living Room','Wohnzimmer','GF',1);
INSERT INTO room (name,label,floor, roomproject) VALUES ('Kitchen','Küche','GF',1);

INSERT INTO device (label,devicetype,deviceroom) VALUES ('Wohnzimmer','Lightbulb',1);

INSERT INTO groupaddress (maingroup,middlegroup,subgroup,groupaddressdevice) VALUES (1,1,99,1);
INSERT INTO groupaddress (maingroup,middlegroup,subgroup,groupaddressdevice) VALUES (4,1,99,1);

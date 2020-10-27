INSERT INTO type (label) VALUES ('Lightbulb');
INSERT INTO type (label) VALUES ('Dimmer');
INSERT INTO type (label) VALUES ('Rollershutter');
INSERT INTO type (label) VALUES ('ContactSensor');
INSERT INTO type (label) VALUES ('MotionSensor');
INSERT INTO type (label) VALUES ('Thermostat');
INSERT INTO type (label) VALUES ('PowerOutlet');
INSERT INTO type (label) VALUES ('StopMoveSwitch');
INSERT INTO type (label) VALUES ('SmokeSensor');
INSERT INTO type (label) VALUES ('ContactSensor');

INSERT INTO project (name) VALUES ('Salm Saarburg');

INSERT INTO room (name,label,floor,roomproject) VALUES ('Living Room','Wohnzimmer','GF',1);
INSERT INTO room (name,label,floor, roomproject) VALUES ('Kitchen','Küche','GF',1);

INSERT INTO device (label,devicetype,deviceroom) VALUES ('Wohnzimmer',2,1);

INSERT INTO groupaddress (maingroup,middlegroup,subgroup,groupaddressdevice) VALUES (1,1,99,1);
INSERT INTO groupaddress (maingroup,middlegroup,subgroup,groupaddressdevice) VALUES (4,1,99,1);

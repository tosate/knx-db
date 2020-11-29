package de.devtom.java.output.csv;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.devtom.java.entities.Device;
import de.devtom.java.entities.GroupAddress;
import de.devtom.java.entities.Room;

public class HomebridgeCsvGenerator extends AbstractCsvGenerator {
	private static final Logger LOGGER = LoggerFactory.getLogger(HomebridgeCsvGenerator.class);
	private static final Map<String, String[]> DEVICE_TYPE_MAP = new HashMap<>();
	static {
		DEVICE_TYPE_MAP.put(Device.TYPE_LIGHTBULB, new String[] {"Lightbulb", "Licht"});
		DEVICE_TYPE_MAP.put(Device.TYPE_DIMMER, new String[] {"Lightbulb", "Licht"});
		DEVICE_TYPE_MAP.put(Device.TYPE_CONTACT_SENSOR, new String[] {"ContactSensor", "Fensterkontakt"});
		DEVICE_TYPE_MAP.put(Device.TYPE_JALOUSIE, new String[] {"WindowCovering", "Jalousie"});
		DEVICE_TYPE_MAP.put(Device.TYPE_MOTION_SENSOR, new String[] {"OccupancySensor", "Präsenzmelder"});
		DEVICE_TYPE_MAP.put(Device.TYPE_POWER_OUTLET, new String[] {"Outlet", "Steckdose"});
		DEVICE_TYPE_MAP.put(Device.TYPE_ROLLERSHUTTER, new String[] {"WindowCovering", "Rollade"});
		DEVICE_TYPE_MAP.put(Device.TYPE_THERMOSTAT, new String[] {"Thermostat", "Thermostat"});
		DEVICE_TYPE_MAP.put(Device.TYPE_SMOKE_SENSOR, new String[] {"SmokeSensor", "Rauchmelder"});
	}

	@Override
	protected String[] getHeaders() {
		return new String[] {"Instance", "Homekit Type", "Grp-Adr 1", "Grp-Adr 2", "Grp-Adr3", "Grp-Adr4", "Grp-Adr5"};
	}

	@Override
	protected List<String> deviceToValue(Room room, Device device, List<GroupAddress> deviceGroupAddresses) {
		List<String> row = new ArrayList<>();
		row.add(getDeviceName(room, device));
		row.add(DEVICE_TYPE_MAP.get(device.getDeviceType())[0]);
		addGroupAddresses(row, device, deviceGroupAddresses);
		
		return row;
	}

	private void addGroupAddresses(List<String> row, Device device, List<GroupAddress> deviceGroupAddresses) {
		switch(device.getDeviceType()) {
		case Device.TYPE_LIGHTBULB:
			generateGroupAddressColumns(row, deviceGroupAddresses, 1, 4, null, null, null);
			break;
		case Device.TYPE_DIMMER:
			generateGroupAddressColumns(row, deviceGroupAddresses, 1, 4, 3, 5, null);
			row.add("");
			break;
		case Device.TYPE_CONTACT_SENSOR:
			generateGroupAddressColumns(row, deviceGroupAddresses, 5, null, null, null, null);
			break;
		case Device.TYPE_JALOUSIE:
			generateGroupAddressColumns(row, deviceGroupAddresses, 4, 3, 6, 5, null);
			break;
		case Device.TYPE_MOTION_SENSOR:
			generateGroupAddressColumns(row, deviceGroupAddresses, 5, null, null, null, null);
			break;
		case Device.TYPE_POWER_OUTLET:
			generateGroupAddressColumns(row, deviceGroupAddresses, 1, 4, null, null, null);
			break;
		case Device.TYPE_ROLLERSHUTTER:
			generateGroupAddressColumns(row, deviceGroupAddresses, 4, 3, null, null, null);
			break;
		case Device.TYPE_THERMOSTAT:
			generateGroupAddressColumns(row, deviceGroupAddresses, 1, 2, 6, 3, 4);
			break;
		case Device.TYPE_SMOKE_SENSOR:
			generateGroupAddressColumns(row, deviceGroupAddresses, 1, null, null, null, null);
			break;
		default:
			LOGGER.warn("Unknown device type [{}].", device.getDeviceType());
		}
	}
	
	private void generateGroupAddressColumns(List<String> row, List<GroupAddress> groupAddresses, Integer middleGroup1, Integer middleGroup2, Integer middleGroup3, Integer middleGroup4, Integer middleGroup5) {
		if(middleGroup1 != null) {
			row.add(getGroupAddressByMiddleGroup(groupAddresses, middleGroup1));
		} else {
			row.add("");
		}
		if(middleGroup2 != null) {
			row.add(getGroupAddressByMiddleGroup(groupAddresses, middleGroup2));
		} else {
			row.add("");
		}
		if(middleGroup3 != null) {
			row.add(getGroupAddressByMiddleGroup(groupAddresses, middleGroup3));
		} else {
			row.add("");
		}
		if(middleGroup4 != null) {
			row.add(getGroupAddressByMiddleGroup(groupAddresses, middleGroup4));
		} else {
			row.add("");
		}
		if(middleGroup5 != null) {
			row.add(getGroupAddressByMiddleGroup(groupAddresses, middleGroup5));
		} else {
			row.add("");
		}
	}

	private String getDeviceName(Room room, Device device) {
		StringBuilder sb = new StringBuilder();
		sb.append(DEVICE_TYPE_MAP.get(device.getDeviceType())[1]);
		sb.append(" ");
		sb.append(device.getLabel());
		
		return sb.toString();
	}
}

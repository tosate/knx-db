package de.devtom.java.output.csv;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import de.devtom.java.entities.Device;
import de.devtom.java.entities.GroupAddress;
import de.devtom.java.entities.Room;

public class HomeAssistantCsvGenerator extends AbstractCsvGenerator {
	private static final Logger LOGGER = LoggerFactory.getLogger(HomeAssistantCsvGenerator.class);
	
	@Override
	protected List<String> deviceToValue(Room room, Device device, List<GroupAddress> deviceGroupAddresses) {
		List<String> row = new ArrayList<>();
		row.add(getDeviceName(room, device));
		row.add(device.getLabel());
		row.add(room.getFloor());
		row.add(getRoomName(room));
		row.add(room.getLabel());
		row.add(device.getDeviceType());
		addGroupAddresses(row, device, deviceGroupAddresses);
		
		return row;
	}
	
	private void addGroupAddresses(List<String> row, Device device, List<GroupAddress> deviceGroupAddresses) {
		switch(device.getDeviceType()) {
		case Device.TYPE_LIGHTBULB:
			generateGroupAddressColumns(row, deviceGroupAddresses, 1, 4, null, null, null, null);
			break;
		case Device.TYPE_DIMMER:
			generateGroupAddressColumns(row, deviceGroupAddresses, 1, 4, 2, 3, 5, null);
			row.add("");
			break;
		case Device.TYPE_CONTACT_SENSOR:
			generateGroupAddressColumns(row, deviceGroupAddresses, 5, null, null, null, null, null);
			break;
		case Device.TYPE_JALOUSIE:
			generateGroupAddressColumns(row, deviceGroupAddresses, 1, 2, 4, 3, 6, 5);
			break;
		case Device.TYPE_MOTION_SENSOR:
			generateGroupAddressColumns(row, deviceGroupAddresses, 5, null, null, null, null, null);
			break;
		case Device.TYPE_POWER_OUTLET:
			generateGroupAddressColumns(row, deviceGroupAddresses, 1, 4, null, null, null, null);
			break;
		case Device.TYPE_ROLLERSHUTTER:
			generateGroupAddressColumns(row, deviceGroupAddresses, 1, 2, 4, 3, null, null);
			break;
		case Device.TYPE_THERMOSTAT:
			generateGroupAddressColumns(row, deviceGroupAddresses, 1, 2, 6, 4, 7, null);
			break;
		case Device.TYPE_SMOKE_SENSOR:
			generateGroupAddressColumns(row, deviceGroupAddresses, 1, null, null, null, null, null);
			break;
		default:
			LOGGER.warn("Unknown device type [{}].", device.getDeviceType());
		}
	}
	
	private void generateGroupAddressColumns(List<String> row, List<GroupAddress> groupAddresses, Integer middleGroup1, Integer middleGroup2, Integer middleGroup3, Integer middleGroup4, Integer middleGroup5, Integer middleGroup6) {
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
		if(middleGroup6 != null) {
			row.add(getGroupAddressByMiddleGroup(groupAddresses, middleGroup6));
		} else {
			row.add("");
		}
	}

	private static String getRoomName(Room room) {
		StringBuilder sb = new StringBuilder();
		sb.append(room.getFloor());
		sb.append('_');
		sb.append(room.getName());
		
		return sb.toString();
	}

	private String getDeviceName(Room room, Device device) {
		StringBuilder sb = new StringBuilder();
		sb.append(room.getFloor());
		sb.append('-');
		sb.append(room.getName());
		sb.append('.');
		sb.append(device.getDeviceType());
		if(!StringUtils.isEmpty(device.getNameAffix())) {
			sb.append('_');
			sb.append(device.getNameAffix());
		}
		
		return sb.toString();
	}

	@Override
	protected String[] getHeaders() {
		return new String[] {"NAME","LABEL","FLOOR","ROOM_NAME","ROOM_LABEL","TYPE","GA1","GA2","GA3","GA4","GA5","GA6"};
	}
}

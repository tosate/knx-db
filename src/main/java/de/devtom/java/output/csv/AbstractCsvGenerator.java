package de.devtom.java.output.csv;

import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.devtom.java.entities.Device;
import de.devtom.java.entities.GroupAddress;
import de.devtom.java.entities.Project;
import de.devtom.java.entities.Room;

public abstract class AbstractCsvGenerator {
	private static final Logger LOGGER = LoggerFactory.getLogger(AbstractCsvGenerator.class);
	
	public String generateCsv(Project project, List<Room> rooms, List<Device> devices, List<GroupAddress> groupAddresses) throws IOException {
		StringWriter out = new StringWriter();
		try (CSVPrinter printer = new CSVPrinter(out, CSVFormat.DEFAULT.withHeader(getHeaders()))) {
			for(Room room : rooms) {
				for(Device device : devices) {
					if(device.getRoom().getRoomid().equals(room.getRoomid())) {
						List<GroupAddress> deviceGroupAddresses = new ArrayList<>();
						for(GroupAddress g : groupAddresses) {
							if(g.getDevice().getDeviceid().equals(device.getDeviceid())) {
								deviceGroupAddresses.add(g);
							}
						}
						printer.printRecord(deviceToValue(room, device, deviceGroupAddresses));
					}
				}
			}
		}
		return out.toString();
	}
	
	protected String getGroupAddressByMiddleGroup(List<GroupAddress> groupAddresses, int middleGroup) {
		for(GroupAddress address : groupAddresses) {
			if(address.getMiddleGroup() == middleGroup) {
				return address.toString();
			}
		}
		LOGGER.warn("Group address list does not include address with middle group [{}].", middleGroup);
		return "";
	}
	
	abstract protected String[] getHeaders();
	abstract protected List<String> deviceToValue(Room room, Device device, List<GroupAddress> deviceGroupAddresses);
}

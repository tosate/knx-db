package de.devtom.java.services;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import de.devtom.java.entities.Device;
import de.devtom.java.entities.Room;

@SpringBootTest
public class DeviceServiceUnitTest {
	private static final String NAME_AFFIX = "GarageBack";
	@Autowired
	private DeviceService deviceService;
	@Autowired
	private RoomService roomService;
	
	@Test
	public void testList() {
		List<Device> devices = deviceService.list();
		
		assertEquals(1, devices.size());
	}
	
	@Test
	public void testCreateUpdateDelete() {
		Room room = roomService.findById(1l);
		Device device = new Device("device-label", "Lightbulb");
		device.setNameAffix(NAME_AFFIX);
		device.setRoom(room);
		long count = deviceService.getNumberOfEntities();
		Device savedDevice = deviceService.createDevice(device);
		assertEquals(NAME_AFFIX, savedDevice.getNameAffix());
		Device updatedDevice = deviceService.updateDevice(savedDevice);
		assertEquals(count + 1, deviceService.getNumberOfEntities());
		
		// Delete
		deviceService.delete(updatedDevice);
		assertEquals(count, deviceService.getNumberOfEntities());
	}
}

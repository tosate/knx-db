package de.devtom.java.services;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import de.devtom.java.entities.Device;

@SpringBootTest
public class DeviceServiceUnitTest {
//	private static final String NAME_AFFIX = "GarageBack";
	@Autowired
	private DeviceService deviceService;
//	@Autowired
//	private RoomService roomService;
//	@Autowired
//	private ProjectService projectService;
	
	@Test
	public void testList() {
		List<Device> devices = deviceService.list();
		
		assertEquals(1, devices.size());
	}
	
//	@Test
//	public void testCreateUpdateDelete() {
//		Optional<Project> project = projectService.findById(1l);
//		if(!project.isPresent()) {
//			fail("Project not found!");
//		}
//		
//		if(project.get().getRooms().size() == 0) {
//			fail(String.format("Project [%s] has no room", project.get().getName()));
//		}
//		Room room = project.get().getRooms().get(0);
//		Device device = new Device("device-label", "Lightbulb");
//		device.setNameAffix(NAME_AFFIX);
//		long count = deviceService.getNumberOfEntities();
//		Device savedDevice = deviceService.save(room, device);
//		assertEquals(NAME_AFFIX, savedDevice.getNameAffix());
//		roomService.save(project.get(), room);
//		assertEquals(count + 1, deviceService.getNumberOfEntities());
//		
//		// Delete
//		deviceService.delete(room, savedDevice);
//		assertEquals(count, deviceService.getNumberOfEntities());
//	}
}

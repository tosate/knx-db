package de.devtom.java.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import de.devtom.java.entities.Device;
import de.devtom.java.entities.Project;
import de.devtom.java.entities.Room;

@SpringBootTest
public class DeviceServiceUnitTest {
	private static final String NAME_AFFIX = "GarageBack";
	@Autowired
	private DeviceService deviceService;
	@Autowired
	private RoomService roomService;
	@Autowired
	private ProjectService projectService;
	
	@Test
	public void testList() {
		List<Device> devices = deviceService.list();
		
		assertEquals(1, devices.size());
	}
	
	@Test
	public void testCreateUpdateDelete() {
		Optional<Project> project = projectService.findById(1l);
		if(!project.isPresent()) {
			fail("Project not found!");
		}
		
		if(project.get().getRooms().size() == 0) {
			fail(String.format("Project [%s] has no room", project.get().getName()));
		}
		Room room = project.get().getRooms().get(0);
		Device device = new Device("device-label", "Lightbulb");
		device.setNameAffix(NAME_AFFIX);
		int count = deviceService.list().size();
		Device savedDevice = deviceService.save(room, device);
		assertEquals(NAME_AFFIX, savedDevice.getNameAffix());
		roomService.save(project.get(), room);
		assertEquals(count + 1, deviceService.list().size());
		
		// Delete
		deviceService.delete(room, savedDevice);
		assertEquals(count, deviceService.list().size());
	}
}

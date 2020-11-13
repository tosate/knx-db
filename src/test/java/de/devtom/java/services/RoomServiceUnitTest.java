package de.devtom.java.services;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import de.devtom.java.entities.Room;

@SpringBootTest
public class RoomServiceUnitTest {
	@Autowired
	private RoomService roomService;
//	@Autowired
//	private ProjectService projectService;
	
	@Test
	public void testList() {
		List<Room> rooms = roomService.list();
		
		assertEquals(2, rooms.size());
	}
//	
//	@Test
//	public void testCreateUpdateDelete() {
//		Optional<Project> parentProject = projectService.findById(1l);
//		Project project = null;
//		if(parentProject.isPresent()) {
//			project = parentProject.get();
//		} else {
//			fail("Parent project not found!");
//		}
//		
//		// Create
//		Room room = new Room("name", "label");
//		long count = roomService.getNumberOfEntities();
//		project.addRoom(room);
//		room = roomService.save(project, room);
//		assertEquals(count + 1, roomService.getNumberOfEntities());
//		
//		// Delete
//		roomService.delete(project, room);
//		assertEquals(count, roomService.getNumberOfEntities());
//	}
}

package de.devtom.java.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import de.devtom.java.entities.Project;
import de.devtom.java.entities.Room;

@SpringBootTest
public class RoomServiceUnitTest {
	@Autowired
	private RoomService roomService;
	@Autowired
	private ProjectService projectService;
	
	@Test
	public void testList() {
		List<Room> rooms = roomService.list();
		
		assertEquals(2, rooms.size());
	}
	
	@Test
	public void testCreateUpdateDelete() {
		Optional<Project> parentProject = projectService.findById(1l);
		if(!parentProject.isPresent()) {
			fail("Parent project not found!");
		}
		
		// Create
		Room room = new Room("name", "label");
		int count = roomService.list().size();
		Room savedRoom = roomService.save(parentProject.get(), room);
		projectService.save(parentProject.get());
		assertEquals(count + 1, roomService.list().size());
		
		// Delete
		roomService.delete(savedRoom);
		assertEquals(count, roomService.list().size());
	}
}

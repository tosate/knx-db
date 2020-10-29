package de.devtom.java.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import de.devtom.java.entities.Device;
import de.devtom.java.entities.GroupAddress;

@SpringBootTest
public class GroupAddressServiceUnitTest {
	@Autowired
	private GroupAddressService groupAddressSerice;
	@Autowired
	private DeviceService deviceService;
	
	@Test
	public void testList() {
		List<GroupAddress> addresses = groupAddressSerice.list();
		
		assertEquals(2, addresses.size());
	}
	
	@Test
	public void testCresteUpdateDelete() {
		Optional<Device> device = deviceService.findById(1l);
		if(!device.isPresent()) {
			fail("Device not found");
		}
		GroupAddress address = new GroupAddress(1,1,1);
		int count = groupAddressSerice.list().size();
		GroupAddress savedAddress = groupAddressSerice.save(device.get(), address);
		assertEquals(count + 1, groupAddressSerice.list().size());
		
		// Delete
		groupAddressSerice.delete(savedAddress);
		assertEquals(count, groupAddressSerice.list().size());
	}
}

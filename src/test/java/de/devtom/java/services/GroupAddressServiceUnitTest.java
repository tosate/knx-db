package de.devtom.java.services;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

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
		Device device = deviceService.findById(1l);
		GroupAddress address = new GroupAddress(1,1,1);
		address.setDevice(device);
		int count = groupAddressSerice.list().size();
		GroupAddress savedAddress = groupAddressSerice.createGroupAddress(address);
		assertEquals(count + 1, groupAddressSerice.list().size());
		
		// Delete
		groupAddressSerice.delete(savedAddress);
		assertEquals(count, groupAddressSerice.list().size());
	}
}

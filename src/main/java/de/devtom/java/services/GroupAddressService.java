package de.devtom.java.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import de.devtom.java.entities.Device;
import de.devtom.java.entities.GroupAddress;
import de.devtom.java.repositories.GroupAddressRepository;

@Service
public class GroupAddressService {
	@Autowired
	private GroupAddressRepository groupAddressRepository;
	
	public List<GroupAddress> list() {
		return groupAddressRepository.findAll();
	}

	public GroupAddress save(Device parentDevice, GroupAddress address) {
		parentDevice.addAddress(address);
		return groupAddressRepository.save(address);
	}
	
	public GroupAddress update(GroupAddress address) {
		return groupAddressRepository.save(address);
	}

	public void delete(GroupAddress savedAddress) {
		savedAddress.getDevice().deleteAddress(savedAddress);
		groupAddressRepository.deleteById(savedAddress.getGroupAddressId());
	}
}

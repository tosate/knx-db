package de.devtom.java.services;

import java.util.List;
import java.util.Optional;

import javax.persistence.EntityNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import de.devtom.java.entities.GroupAddress;
import de.devtom.java.repositories.GroupAddressRepository;

@Service
public class GroupAddressService extends AbstractService<GroupAddress> {
	@Autowired
	private GroupAddressRepository groupAddressRepository;
	
	public List<GroupAddress> list() {
		return groupAddressRepository.findAll();
	}

	public GroupAddress createGroupAddress(GroupAddress address) {
		return groupAddressRepository.save(address);
	}
	
	public GroupAddress updateGroupAddress(GroupAddress address) {
		return groupAddressRepository.findById(address.getGroupAddressId()).map(a -> {
			a.setMainGroup(address.getMainGroup());
			a.setMiddleGroup(address.getMiddleGroup());
			a.setSubGroup(address.getSubGroup());
			a.setDataType(address.getDataType());
			a.setFunction(address.getFunction());
			a.setDevice(address.getDevice());
			return groupAddressRepository.save(a);
		}).orElseThrow(() -> new EntityNotFoundException(String.format("Group address ID [%d] not found", address.getGroupAddressId())));
	}
	
	public GroupAddress findById(Long groupAddressId) {
		Optional<GroupAddress> address = groupAddressRepository.findById(groupAddressId);
		return handleOptional(address, String.format("Group address ID [%s] not found", groupAddressId));
	}

	public boolean delete(GroupAddress address) {
		return groupAddressRepository.findById(address.getGroupAddressId()).map(a -> {
			groupAddressRepository.deleteById(address.getGroupAddressId());
			return true;
		}).orElseThrow(() -> new EntityNotFoundException(String.format("Group address ID [%d] not found", address.getGroupAddressId())));
	}
	
	public List<GroupAddress> findByDeviceId(Long deviceId) {
		return groupAddressRepository.findByDeviceDeviceid(deviceId);
	}
}

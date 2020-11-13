package de.devtom.java.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import de.devtom.java.entities.Device;
import de.devtom.java.entities.Room;
import de.devtom.java.repositories.DeviceRepository;

@Service
public class DeviceService {
	@Autowired
	private DeviceRepository deviceRepository;
	
	public List<Device> list() {
		return deviceRepository.findAll();
	}
	
	public Device save(Room parentRoom, Device device) {
		parentRoom.addDevice(device);
		return deviceRepository.save(device);
	}
	
	public Device update(Device device) {
		return deviceRepository.save(device);
	}
	
	public Optional<Device> findById(Long deviceid) {
		return deviceRepository.findById(deviceid);
	}
	
	public void delete(Room room, Device device) {
		room.deleteDevice(device);
		deviceRepository.deleteById(device.getDeviceid());
	}
	
	public long getNumberOfEntities() {
		return deviceRepository.count();
	}
}

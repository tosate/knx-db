package de.devtom.java.services;

import java.util.List;
import java.util.Optional;

import javax.persistence.EntityNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import de.devtom.java.entities.Device;
import de.devtom.java.repositories.DeviceRepository;

@Service
public class DeviceService {
	@Autowired
	private DeviceRepository deviceRepository;
	
	public List<Device> list() {
		return deviceRepository.findAll();
	}
	
	public Device createDevice(Device device) {
		return deviceRepository.save(device);
	}
	
	public Device updateDevice(Device device) {
		return deviceRepository.findById(device.getDeviceid()).map(d -> {
			d.setLabel(device.getLabel());
			d.setNameAffix(device.getNameAffix());
			d.setDeviceType(device.getDeviceType());
			d.setRoom(device.getRoom());
			return deviceRepository.save(d);
		}).orElseThrow(() -> new EntityNotFoundException(String.format("Device ID [%d] not found", device.getDeviceid())));
	}
	
	public Device findById(Long deviceid) {
		Optional<Device> device = deviceRepository.findById(deviceid);
		return handleOptional(device, String.format("Device ID [%d] not found", deviceid));
	}
	
	public boolean delete(Device device) {
		return deviceRepository.findById(device.getDeviceid()).map(d -> {
			deviceRepository.deleteById(device.getDeviceid());
			return true;
		}).orElseThrow(() -> new EntityNotFoundException(String.format("Device ID [%d] not found", device.getDeviceid())));
	}
	
	public long getNumberOfEntities() {
		return deviceRepository.count();
	}
	
	private Device handleOptional(Optional<Device> device, String errorMessage) {
		if(device.isPresent()) {
			return device.get();
		} else {
			throw new EntityNotFoundException(errorMessage);
		}
	}
	
	public List<Device> findByRoomId(Long roomId) {
		return deviceRepository.findByRoomRoomid(roomId);
	}
}

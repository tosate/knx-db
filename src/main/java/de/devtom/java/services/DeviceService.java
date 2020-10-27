package de.devtom.java.services;

import java.util.List;

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
}

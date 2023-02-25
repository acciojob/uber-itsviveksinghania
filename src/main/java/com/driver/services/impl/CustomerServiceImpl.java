package com.driver.services.impl;

import com.driver.model.TripBooking;
import com.driver.services.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.driver.model.Customer;
import com.driver.model.Driver;
import com.driver.repository.CustomerRepository;
import com.driver.repository.DriverRepository;
import com.driver.repository.TripBookingRepository;
import com.driver.model.TripStatus;

import java.util.*;

@Service
public class CustomerServiceImpl implements CustomerService {

	@Autowired
	CustomerRepository customerRepository2;

	@Autowired
	DriverRepository driverRepository2;

	@Autowired
	TripBookingRepository tripBookingRepository2;

	@Override
	public void register(Customer customer) {
		//Save the customer in database
		customerRepository2.save(customer);
	}

	@Override
	public void deleteCustomer(Integer customerId) {
		// Delete customer without using deleteById function
		customerRepository2.deleteById(customerId);
	}

	@Override
	public TripBooking bookTrip(int customerId, String fromLocation, String toLocation, int distanceInKm) throws Exception{
		//Book the driver with lowest driverId who is free (cab available variable is Boolean.TRUE). If no driver is available, throw "No cab available!" exception
		//Avoid using SQL query
		List<Driver> drivers = driverRepository2.findAll();

		// Find the driver with the lowest ID who is free
		Driver selectedDriver = null;
		for (Driver driver : drivers) {
			if (driver.getCab().getAvailable()) {
				selectedDriver = driver;
				break;
			}
		}

		// If no driver is available, throw an exception
		if (selectedDriver == null) {
			throw new Exception("No cab available!");
		}

		// Create a new trip booking
		TripBooking tripBooking = new TripBooking();
		tripBooking.setCustomer(customerRepository2.getOne(customerId));
		tripBooking.setDriver(selectedDriver);
		tripBooking.setFromLocation(fromLocation);
		tripBooking.setToLocation(toLocation);
		tripBooking.setDistanceInKm(distanceInKm);
		tripBooking.setStatus(TripStatus.CONFIRMED);
		tripBooking.setBill(distanceInKm*selectedDriver.getCab().getPerKmRate());
		tripBookingRepository2.save(tripBooking);

		// Update the driver's availability
		selectedDriver.getCab().setAvailable(false);
		driverRepository2.save(selectedDriver);

		return tripBooking;


	}

	@Override
	public void cancelTrip(Integer tripId){
		//Cancel the trip having given trip Id and update TripBooking attributes accordingly
		Optional<TripBooking> tripBooking = tripBookingRepository2.findById(tripId);

		if(tripBooking.isPresent()) {
			TripBooking curTrip = tripBooking.get();
			curTrip.setStatus(TripStatus.CANCELED);
			tripBookingRepository2.save(curTrip);
			Optional<Driver> driver = driverRepository2.findById(curTrip.getDriver().getDriverId());
			if(driver.isPresent()){
				driver.get().getCab().setAvailable(true);
				driverRepository2.save(driver.get());
			}
		}
	}

	@Override
	public void completeTrip(Integer tripId){
		//Complete the trip having given trip Id and update TripBooking attributes accordingly
		Optional<TripBooking> tripBooking = tripBookingRepository2.findById(tripId);

		if(tripBooking.isPresent()) {
			TripBooking curTrip = tripBooking.get();
			curTrip.setStatus(TripStatus.COMPLETED);
			tripBookingRepository2.save(curTrip);
			Optional<Driver> driver = driverRepository2.findById(curTrip.getDriver().getDriverId());
			if(driver.isPresent()){
				driver.get().getCab().setAvailable(true);
				driverRepository2.save(driver.get());
			}
		}



	}
}

package Amadeus.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import Amadeus.Entity.Flight;
import Amadeus.Repository.FlightRepository;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/flights")
public class FlightController {

    @Autowired
    private FlightRepository flightRepository;

    @GetMapping
    public ResponseEntity<List<Flight>> searchFlights(
            @RequestParam String departure,
            @RequestParam String arrival,
            @RequestParam String departureDate,
            @RequestParam(required = false) String returnDate
    ) {
        if (returnDate != null && !returnDate.isEmpty()) {
            List<Flight> roundTripFlights = flightRepository.findByDepartureAirportAndArrivalAirportAndDepartureDateTimeAndReturnDateTime(
                    departure, arrival, LocalDateTime.parse(departureDate), LocalDateTime.parse(returnDate)
            );
            return new ResponseEntity<>(roundTripFlights, HttpStatus.OK);
        } else {
            List<Flight> oneWayFlights = flightRepository.findByDepartureAirportAndArrivalAirportAndDepartureDateTime(
                    departure, arrival, LocalDateTime.parse(departureDate)
            );
            return new ResponseEntity<>(oneWayFlights, HttpStatus.OK);
        }
    }

    @PostMapping
    public ResponseEntity<Flight> createFlight(@RequestBody Flight flight) {
        try {
            Flight savedFlight = flightRepository.save(flight);
            return new ResponseEntity<>(savedFlight, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Flight> getFlightById(@PathVariable Long id) {
        Flight flight = flightRepository.findById(id).orElse(null);
        if (flight != null) {
            return new ResponseEntity<>(flight, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Flight> updateFlight(@PathVariable Long id, @RequestBody Flight updatedFlight) {
        Flight existingFlight = flightRepository.findById(id).orElse(null);
        if (existingFlight != null) {
            existingFlight.setDepartureAirport(updatedFlight.getDepartureAirport());
            existingFlight.setArrivalAirport(updatedFlight.getArrivalAirport());
            existingFlight.setDepartureDateTime(updatedFlight.getDepartureDateTime());
            existingFlight.setReturnDateTime(updatedFlight.getReturnDateTime());
            existingFlight.setPrice(updatedFlight.getPrice());

            Flight savedFlight = flightRepository.save(existingFlight);
            return new ResponseEntity<>(savedFlight, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFlight(@PathVariable Long id) {
        try {
            flightRepository.deleteById(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}

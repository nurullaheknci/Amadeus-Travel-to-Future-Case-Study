package Amadeus.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import Amadeus.Entity.Airport;

import java.util.List;

@Repository
public interface AirportRepository extends JpaRepository<Airport, Long> {

    List<Airport> findByCity(String city);
}

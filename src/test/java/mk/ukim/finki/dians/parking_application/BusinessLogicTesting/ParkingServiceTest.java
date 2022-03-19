package mk.ukim.finki.dians.parking_application.BusinessLogicTesting;

import mk.ukim.finki.dians.parking_application.model.Parking;
import mk.ukim.finki.dians.parking_application.repository.ParkingRepository;
import mk.ukim.finki.dians.parking_application.service.ParkingService;
import mk.ukim.finki.dians.parking_application.service.implementations.ParkingServiceImpl;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;

import static org.mockito.Mockito.when;

/*
JUST RUN THE TESTS
*/
public class ParkingServiceTest {

    @Mock
    private ParkingRepository parkingRepository;
    private ParkingService service;

    @Before
    public void initData() {
        MockitoAnnotations.initMocks(this);
        this.service = Mockito.spy(new ParkingServiceImpl(this.parkingRepository));
    }

    @Test
    public void testFindAll() {
        //given
        List<Parking> parkings = generateParkingList();

        //when
        when(parkingRepository.findAll()).thenReturn(parkings);

        List<Parking> actual = service.findAll();

        //then
        assertEquals(parkings.size(), actual.size());
        assertEquals(parkings, actual);
    }

    @Test
    public void testFindById() {
        //given
        Parking parking = generateParking();

        //when
        when(parkingRepository.findById(parking.getId())).thenReturn(Optional.of(parking));

        Parking actual = service.findById(parking.getId()).get();

        //then
        assertNotNull(actual);
        assertEquals(parking.getId(), actual.getId());
        assertEquals(parking.getLatitude(), actual.getLatitude());
        assertEquals(parking.getLongitude(), actual.getLongitude());
        assertEquals(parking.getAddress(), actual.getAddress());
        assertEquals(parking.getCity(), actual.getCity());
        assertEquals(parking.getMapUrl(), actual.getMapUrl());
        assertEquals(parking.getName(), actual.getName());
        assertEquals(parking.getRating(), actual.getRating());

    }

    @Test
    public void testDeleteById() {
        //given
        Parking parking = generateParking();

        //when
        when(parkingRepository.save(any(Parking.class))).thenReturn(parking);

        doNothing().when(parkingRepository).deleteById(parking.getId());
        service.deleteById(parking.getId());

        when(parkingRepository.findAll()).thenReturn(Collections.emptyList());
        List<Parking> actual = parkingRepository.findAll();

        //then
        assertEquals(actual.size(), 0);
    }

    @Test
    public void testFindByCurrentAddress() {
        //given
        List<Parking> parkings = generateListForCurrentLocation();
        Double latitude = 41.99473;
        Double longitude = 21.43693;

        //when
        when(parkingRepository.findAll()).thenReturn(parkings);

        List<Parking> actual = service.findByCurrentAddress(latitude, longitude);

        //then
        //distance < 3km
        assertEquals(3, actual.size());

        //sorted by shortest distance
        assertEquals(parkings.get(0), actual.get(0));
        assertEquals(parkings.get(2), actual.get(1));

        //distance > 3km
        assertFalse(actual.contains(parkings.get(3)));
    }


    @Test
    public void testFindAllByAddressAndCityOrderByName() {
        //given
        List<Parking> parkings = new ArrayList<>();
        Parking parking = generateParking();
        parkings.add(parking);

        //when
        when(parkingRepository.findAllByAddressIgnoreCaseContainsAndCityIgnoreCaseContainsOrderByName("Kosturski Heroi", "Skopje"))
                .thenReturn(parkings);
        //then
        List<Parking> actual = service.findAllByCityOrAndAddressSorted("Skopje", "Kosturski Heroi", "name");
        assertEquals(parkings.size(), actual.size());
        assertTrue(actual.contains(parking));
    }

    @Test
    public void testFindAllByAddressAndCityOrderByRating() {
        //given
        List<Parking> parkings = new ArrayList<>();
        Parking parking = generateParking();
        parkings.add(parking);

        //when
        when(parkingRepository.findAllByAddressIgnoreCaseContainsAndCityIgnoreCaseContainsOrderByRatingDesc("Kosturski Heroi", "Skopje"))
                .thenReturn(parkings);
        //then
        List<Parking> actual = service.findAllByCityOrAndAddressSorted("Skopje", "Kosturski Heroi", "rating");
        assertEquals(parkings.size(), actual.size());
        assertTrue(actual.contains(parking));
    }

    @Test
    public void testFindAllByAddressOrderByName() {
        //given
        List<Parking> parkings = new ArrayList<>();
        Parking parking = generateParking();
        parkings.add(parking);

        //when
        when(parkingRepository.findAllByAddressIgnoreCaseContainsOrderByName("Kosturski Heroi")).thenReturn(parkings);

        //then
        List<Parking> actual = service.findAllByCityOrAndAddressSorted("", "Kosturski Heroi", "name");
        assertEquals(parkings.size(), actual.size());
        assertTrue(actual.contains(parking));
    }

    @Test
    public void testFindAllByAddressOrderByRating() {
        //given
        List<Parking> parkings = new ArrayList<>();
        Parking parking = generateParking();
        parkings.add(parking);

        //when
        when(parkingRepository.findAllByAddressIgnoreCaseContainsOrderByRatingDesc("Kosturski Heroi")).thenReturn(parkings);

        //then
        List<Parking> actual = service.findAllByCityOrAndAddressSorted("", "Kosturski Heroi", "rating");
        assertEquals(parkings.size(), actual.size());
        assertTrue(actual.contains(parking));
    }

    @Test
    public void testFindAllByCityOrderByName() {
        //given
        List<Parking> parkings = new ArrayList<>();
        Parking parking = generateParking();
        parkings.add(parking);

        //when
        when(parkingRepository.findAllByCityIgnoreCaseContainsOrderByName("Skopje")).thenReturn(parkings);

        //then
        List<Parking> actual = service.findAllByCityOrAndAddressSorted("Skopje", "", "name");
        assertEquals(parkings.size(), actual.size());
        assertTrue(actual.contains(parking));
    }

    @Test
    public void testFindAllByCityOrderByRating() {
        //given
        List<Parking> parkings = new ArrayList<>();
        Parking parking = generateParking();
        parkings.add(parking);

        //when
        when(parkingRepository.findAllByCityIgnoreCaseContainsOrderByRatingDesc("Skopje")).thenReturn(parkings);

        //then
        List<Parking> actual = service.findAllByCityOrAndAddressSorted("Skopje", "", "rating");
        assertEquals(parkings.size(), actual.size());
        assertTrue(actual.contains(parking));
    }

    @Test // save(Parking p)
    public void testSaveParking() {
        //given
        Parking parking = generateParking();

        //when
        when(parkingRepository.save(any(Parking.class))).thenReturn(parking);
        Parking actual = service.save(parking).get();

        //then
        assertNotNull(actual);
        assertEquals(parking.getId(), actual.getId());
        assertEquals(parking.getLatitude(), actual.getLatitude());
        assertEquals(parking.getLongitude(), actual.getLongitude());
        assertEquals(parking.getAddress(), actual.getAddress());
        assertEquals(parking.getCity(), actual.getCity());
        assertEquals(parking.getMapUrl(), actual.getMapUrl());
        assertEquals(parking.getName(), actual.getName());
        assertEquals(parking.getRating(), actual.getRating());
    }

    @Test // save(String name, String city, String address, Double latitude, Double longitude, String rating)
    public void testSaveParking2() {
        //given
        Parking parking = generateParking();

        //when
        when(parkingRepository.save(any(Parking.class))).thenReturn(parking);

        Parking actual = service.save(parking.getName(), parking.getCity(), parking.getAddress(), parking.getLatitude(), parking.getLongitude(), parking.getRating()).get();

        //then
        assertNotNull(actual);
        assertEquals(parking.getId(), actual.getId());
        assertEquals(parking.getLatitude(), actual.getLatitude());
        assertEquals(parking.getLongitude(), actual.getLongitude());
        assertEquals(parking.getAddress(), actual.getAddress());
        assertEquals(parking.getCity(), actual.getCity());
        assertEquals(parking.getMapUrl(), actual.getMapUrl());
        assertEquals(parking.getName(), actual.getName());
        assertEquals(parking.getRating(), actual.getRating());
    }

    @Test
    public void testEditParking() {
        //given
        Parking parking = generateParking();

        //when
        when(parkingRepository.findById(parking.getId())).thenReturn(Optional.of(parking));
        when(parkingRepository.save(any(Parking.class))).thenReturn(parking);
        Parking actual = service.edit(parking.getId(), parking.getName(), parking.getCity(), parking.getAddress(),
                parking.getLatitude(), parking.getLongitude(), parking.getRating()).get();

        //then
        assertNotNull(actual);
        assertEquals(parking.getId(), actual.getId());
        assertEquals(parking.getLatitude(), actual.getLatitude());
        assertEquals(parking.getLongitude(), actual.getLongitude());
        assertEquals(parking.getAddress(), actual.getAddress());
        assertEquals(parking.getCity(), actual.getCity());
        assertEquals(parking.getMapUrl(), actual.getMapUrl());
        assertEquals(parking.getName(), actual.getName());
        assertEquals(parking.getRating(), actual.getRating());

    }

    private Parking generateParking() {
        Parking parking = new Parking();
        parking.setName("Kosturski Heroi Parking");
        parking.setCity("Skopje");
        parking.setAddress("Kosturski Heroi");
        parking.setLongitude(21.41718);
        parking.setLatitude(41.99715);
        parking.setRating("3");
        return parking;
    }

    private List<Parking> generateParkingList() {
        List<Parking> list = new ArrayList<>();
        Parking parking1 = new Parking("Kosturski Heroi Parking", "Skopje", "Kosturski Heroi",
                41.99715, 21.41718, "Not known rating");
        Parking parking2 = new Parking("Parking 1", "Skopje", "Kosturski Heroi",
                41.99717, 21.41718, "4");
        Parking parking3 = new Parking("Parking Gradezen", "Skopje", "Partizanski Odredi",
                42.00386, 21.41429, "5");
        Parking parking4 = new Parking("Parking", "Ohrid", "Turisticka",
                41.11396, 20.80801, "5");
        list.add(parking1);
        list.add(parking2);
        list.add(parking3);
        list.add(parking4);
        return list;
    }

    private List<Parking> generateListForCurrentLocation() {
        List<Parking> list = new ArrayList<>();
        Parking parking1 = new Parking("Gradski trgovski centar", "Skopje", "Kej 13 Noemvri", 41.99473, 21.43693, "4,1"); //0m
        Parking parking2 = new Parking("Drvodekor", "Skopje", "Kej 13 Noemvri", 41.99585, 21.43279, "4,1"); //350m
        Parking parking3 = new Parking("Parking", "Skopje", "Kej 13 Noemvri", 41.9956, 21.4335, "5,0"); //300m
        Parking parking4 = new Parking("Tinex", "Ohrid", "7mi Noemvri", 41.12261, 20.8096, "Not known rating"); //173km
        list.add(parking1);
        list.add(parking2);
        list.add(parking3);
        list.add(parking4);
        return list;
    }

}


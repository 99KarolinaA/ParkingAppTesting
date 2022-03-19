package mk.ukim.finki.dians.parking_application.IntegrationTesting;

import mk.ukim.finki.dians.parking_application.model.Parking;
import mk.ukim.finki.dians.parking_application.service.ParkingService;
import org.junit.FixMethodOrder;
import org.junit.jupiter.api.Test;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.testng.Assert;

import java.util.List;
import java.util.Optional;

/*
JUST RUN THE TESTS
*/
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class ParkingServiceIntegrationTest {

    @Autowired
    ParkingService parkingService;


    @Test
    public void testFindAll() {
        List<Parking> parkings = parkingService.findAll();

        Assert.assertEquals(parkings.size(), 4);
    }

    @Test
    public void testFindById() {
        Parking parking = parkingService.findById((long) 1).get();

        Assert.assertEquals(parking.getMapUrl(), "https://www.google.com/maps/d/embed?mid=19QU5sn6CbsVvkQQQxm_6RUs7YmbH0P8Z&z=17");
    }

    @Test
    public void testFindAllByCityOrAndAddressSorted() {
        Assert.assertNull(parkingService.findAllByCityOrAndAddressSorted("", "", null));

        //search by address
        Assert.assertEquals(4, parkingService.findAllByCityOrAndAddressSorted("", "Noemvri", null).size());
        Assert.assertEquals(4, parkingService.findAllByCityOrAndAddressSorted("", "NOEMVRI", null).size());
        Assert.assertEquals(4, parkingService.findAllByCityOrAndAddressSorted("", "noemvr", null).size());
        Assert.assertEquals(3, parkingService.findAllByCityOrAndAddressSorted("", "Kej 13 Noemvri", null).size());

        //search by city
        Assert.assertEquals(3, parkingService.findAllByCityOrAndAddressSorted("skopje", "", null).size());

        //sorted by rating
        Parking parking1 = parkingService.findAllByCityOrAndAddressSorted("", "Noemvri", "rating").get(1);
        String mapUrl1 = parking1.getMapUrl();
        Assert.assertEquals("https://www.google.com/maps/d/u/0/embed?mid=1lSMzXTm9-kU-Nak6qE6jBuxNhK0093dO&z=17", mapUrl1);

        //sorted by default (name)
        Parking parking2 = parkingService.findAllByCityOrAndAddressSorted("", "Noemvri", null).get(0);
        String mapUrl2 = parking2.getMapUrl();
        Assert.assertEquals("https://www.google.com/maps/d/embed?mid=1txUPlEdtj6plPAOGkBhTrixwl7UYwXyv&z=17", mapUrl2);
    }

    @Test
    public void testSaveParking() {
        Parking newParking = new Parking("test", "test", "test address", 41.12261, 20.8096, "5");
        Parking parking = parkingService.save(newParking).get();

        Assert.assertNotNull(parkingService.findById(parking.getId()));
    }

    @Test
    public void testSaveParking2() {
        Parking parking = parkingService.save("test2", "test2", "test address2", 41.12261, 20.8096, "5").get();

        Assert.assertNotNull(parkingService.findById(parking.getId()));
    }

    @Test
    public void testEditParking() {
        Parking parking = parkingService.findAll().get(parkingService.findAll().size() - 1);
        String city = "editedCity";
        String name = "editedName";
        String address = parking.getAddress() + "edited";
        parkingService.edit(parking.getId(), name, city, address, parking.getLatitude(), parking.getLongitude(), parking.getRating());
        Parking editedParking = parkingService.findById(parking.getId()).get();

        Assert.assertEquals(editedParking.getCity(), city);
        Assert.assertEquals(editedParking.getName(), name);
        Assert.assertEquals(editedParking.getAddress(), address);

    }

    @Test
    public void testDeleteById() {
        Parking parking = parkingService.findAll().get(parkingService.findAll().size() - 1);
        parkingService.deleteById(parking.getId());

        Assert.assertEquals(parkingService.findById(parking.getId()), Optional.empty());
    }

    @Test
    public void testFindByCurrentAddress() {
        Assert.assertEquals(0, parkingService.findByCurrentAddress(41.9945142, 41.9945142).size());
        Assert.assertEquals(3, parkingService.findByCurrentAddress(41.99473, 21.43693).size());

        //sorted by shortest distance
        Parking parking1 = parkingService.findByCurrentAddress(41.99473, 21.43693).get(0);
        String mapUrl1 = parking1.getMapUrl();
        Assert.assertEquals("https://www.google.com/maps/d/embed?mid=19QU5sn6CbsVvkQQQxm_6RUs7YmbH0P8Z&z=17", mapUrl1);
        Parking parking2 = parkingService.findByCurrentAddress(41.99473, 21.43693).get(1);
        String mapUrl2 = parking2.getMapUrl();
        Assert.assertEquals("https://www.google.com/maps/d/u/0/embed?mid=1lSMzXTm9-kU-Nak6qE6jBuxNhK0093dO&z=17", mapUrl2);

    }
}

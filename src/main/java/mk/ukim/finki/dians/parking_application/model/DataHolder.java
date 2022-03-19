package mk.ukim.finki.dians.parking_application.model;

import lombok.Getter;
import mk.ukim.finki.dians.parking_application.model.enumeration.Role;
import mk.ukim.finki.dians.parking_application.service.ParkingService;
import mk.ukim.finki.dians.parking_application.service.UserService;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
@Getter
public class DataHolder {

    private final ParkingService parkingService;
    private final UserService userService;

    public DataHolder(ParkingService parkingService, UserService userService) {
        this.parkingService = parkingService;
        this.userService = userService;
    }

    @PostConstruct
    public void initData() {
        Parking p1 = new Parking("Gradski trgovski centar","Skopje","Kej 13 Noemvri",41.99473,21.43693,"4,1");
        p1.setMapUrl("https://www.google.com/maps/d/embed?mid=19QU5sn6CbsVvkQQQxm_6RUs7YmbH0P8Z&z=17");
        Parking p2 = new Parking("Drvodekor","Skopje","Kej 13 Noemvri",41.99585,21.43279,"4,1");
        p2.setMapUrl("https://www.google.com/maps/d/embed?mid=1txUPlEdtj6plPAOGkBhTrixwl7UYwXyv&z=17");
        Parking p3 = new Parking("Parking","Skopje","Kej 13 Noemvri",41.9956,21.4335,"5,0");
        p3.setMapUrl("https://www.google.com/maps/d/u/0/embed?mid=1lSMzXTm9-kU-Nak6qE6jBuxNhK0093dO&z=17");
        Parking p4= new Parking("Tinex","Ohrid","7mi Noemvri",41.12261,20.8096,"Not known rating");
        p4.setMapUrl("https://www.google.com/maps/d/embed?mid=1apNNFxkVFvoNGbep-VjkFJykFcjbV2QF&z=17");
        parkingService.save(p1);
        parkingService.save(p2);
        parkingService.save(p3);
        parkingService.save(p4);

        userService.register("user","user","user","user","user",Role.ROLE_USER);
        userService.register("admin","admin","admin","admin", "admin", Role.ROLE_ADMIN);

    }

}

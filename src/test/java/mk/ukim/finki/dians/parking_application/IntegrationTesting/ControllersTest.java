package mk.ukim.finki.dians.parking_application.IntegrationTesting;

import lombok.SneakyThrows;
import mk.ukim.finki.dians.parking_application.model.Parking;
import mk.ukim.finki.dians.parking_application.service.ParkingService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/*
JUST RUN THE TESTS
*/
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class ControllersTest {

    MockMvc mockMvc;

    @Autowired
    ParkingService parkingService;


    @BeforeEach
    public void setup(WebApplicationContext wac) {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(wac)
                .build();
    }

/*
   HOME CONTROLLER
*/

    @Test
    public void testGetHomePage() throws Exception {
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.get("/");

        this.mockMvc.perform(request)
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.model().attribute("bodyContent", "home"))
                .andExpect(MockMvcResultMatchers.view().name("master-template"));

    }

/*
   LOGIN CONTROLLER
*/

    @Test
    public void testGetLoginPage() throws Exception {
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.get("/login");

        this.mockMvc.perform(request)
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.model().attribute("bodyContent", "login"))
                .andExpect(MockMvcResultMatchers.view().name("master-template"));

    }

    @Test
    public void testGetErrorLoginPage() throws Exception {
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.get("/login/error");

        this.mockMvc.perform(request)
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.model().attributeExists("hasError"))
                .andExpect(MockMvcResultMatchers.model().attributeExists("error"))
                .andExpect(MockMvcResultMatchers.model().attribute("bodyContent", "login"))
                .andExpect(MockMvcResultMatchers.view().name("master-template"));

    }

/*
     PARKING CONTROLLER
*/

    @Test
    public void testGetLocatePage() throws Exception {
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.get("/parking/locate");

        this.mockMvc.perform(request)
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.model().attribute("bodyContent", "locate"))
                .andExpect(MockMvcResultMatchers.view().name("master-template"));
    }

    @Test
    public void testSearchParkingSuccessful() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders
                .post("/parking/result")
                .param("city", "Skopje")
                .param("address", ""))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.model().attributeExists("parking"))
                .andExpect(MockMvcResultMatchers.model().attributeExists("currentLocation"))
                .andExpect(MockMvcResultMatchers.model().attribute("bodyContent", "results"))
                .andExpect(MockMvcResultMatchers.view().name("master-template"));

    }

    @Test
    public void testSearchParkingEmptyAddressAndCity() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders
                .post("/parking/result")
                .param("city", "")
                .param("address", ""))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.model().attributeExists("hasError"))
                .andExpect(MockMvcResultMatchers.model().attributeExists("currentLocation"))
                .andExpect(MockMvcResultMatchers.model().attributeExists("error"))
                .andExpect(MockMvcResultMatchers.model().attribute("bodyContent", "locate"))
                .andExpect(MockMvcResultMatchers.view().name("master-template"));
    }

    @Test
    public void testSearchParkingNoResult() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders
                .post("/parking/result")
                .param("city", "ffdgfd")
                .param("address", ""))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("notfoundparking"));
    }

    @Test
    public void testGetCurrentLocationPageSuccessful() throws Exception {
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.get("/parking/current-location");

        this.mockMvc.perform(request.param("coordinates", "41.99473 21.43693"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.model().attributeExists("parking"))
                .andExpect(MockMvcResultMatchers.model().attributeExists("currentLocation"))
                .andExpect(MockMvcResultMatchers.model().attribute("bodyContent", "results"))
                .andExpect(MockMvcResultMatchers.view().name("master-template"));
    }

    @Test
    public void testGetCurrentLocationPageEmptyCoordinates() throws Exception {
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.get("/parking/current-location");

        this.mockMvc.perform(request.param("coordinates", ""))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.model().attribute("bodyContent", "locate"))
                .andExpect(MockMvcResultMatchers.view().name("master-template"));
    }

    @Test
    public void testGetCurrentLocationPageNoResult() throws Exception {
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.get("/parking/current-location");

        this.mockMvc.perform(request.param("coordinates", "41.9784924 21.4760118"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("notfoundparking"));
    }


    @Test
    public void testGetParkingPage() throws Exception {
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.get("/parking/all-parkings");

        this.mockMvc.perform(request)
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.model().attributeExists("parking"))
                .andExpect(MockMvcResultMatchers.model().attribute("bodyContent", "all-parkings"))
                .andExpect(MockMvcResultMatchers.view().name("master-template"));

    }

    @Test
    public void testDeleteParking() throws Exception {
        Parking parking
                = this.parkingService.save("Parking komercijalna banka", "Bitola",
                "General Vasko Karangjelevski", 41.02706, 21.31453, "4,3").get();
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .get("/parking/delete/" + parking.getId());

        this.mockMvc.perform(request)
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.redirectedUrl("/parking/all-parkings"));
    }

    @WithMockUser(username = "admin", roles = {"USER", "ADMIN"})
    @Test
    public void testEditParkingPage() throws Exception {
        Parking parking
                = this.parkingService.save("Parking komercijalna banka", "Bitola",
                "General Vasko Karangjelevski", 41.02706, 21.31453, "4,3").get();
        MockHttpServletRequestBuilder parkingEditRequest = MockMvcRequestBuilders
                .get("/parking/edit-form/" + parking.getId());

        this.mockMvc.perform(parkingEditRequest)
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.model().attributeExists("parking"))
                .andExpect(MockMvcResultMatchers.model().attribute("bodyContent", "add-parking"))
                .andExpect(MockMvcResultMatchers.view().name("master-template"));
    }

    @WithMockUser(username = "admin", roles = {"USER", "ADMIN"})
    @Test
    public void testEditParkingPageNoResult() throws Exception {
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .get("/parking/edit-form/" + (long) 34935983);

        this.mockMvc.perform(request)
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("notfoundparking"));
    }

    @SneakyThrows
    @WithMockUser(username = "admin", roles = {"USER", "ADMIN"})
    @Test
    public void testAddParkingPage() {
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .get("/parking/add-form");

        this.mockMvc.perform(request)
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.model().attribute("bodyContent", "add-parking"))
                .andExpect(MockMvcResultMatchers.view().name("master-template"));
    }

    @Test
    public void testSaveParking() throws Exception {
        LinkedMultiValueMap<String, String> requestParams = new LinkedMultiValueMap<>();
        requestParams.add("name", "");
        requestParams.add("city", "");
        requestParams.add("address", "");
        requestParams.add("latitude", "12");
        requestParams.add("longitude", "12");
        requestParams.add("rating", "");

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .post("/parking/add").params(requestParams);

        this.mockMvc.perform(request)
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.redirectedUrl("/parking/all-parkings"));
    }

/*
  REGISTER CONTROLLER
*/

    @Test
    public void testGetRegisterPageNoError() throws Exception {
        MockHttpServletRequestBuilder request = (MockMvcRequestBuilders
                .get("/register"));

        this.mockMvc.perform(request)
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.model().attribute("bodyContent", "register"))
                .andExpect(MockMvcResultMatchers.view().name("master-template"));
    }

    @Test
    public void testGetRegisterPageError() throws Exception {
        MockHttpServletRequestBuilder request = (MockMvcRequestBuilders
                .get("/register").param("error", "error"));

        this.mockMvc.perform(request)
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.model().attributeExists("hasError"))
                .andExpect(MockMvcResultMatchers.model().attributeExists("error"))
                .andExpect(MockMvcResultMatchers.model().attribute("bodyContent", "register"))
                .andExpect(MockMvcResultMatchers.view().name("master-template"));
    }

    @Test
    public void testSuccessfulRegister() throws Exception {
        LinkedMultiValueMap<String, String> requestParams = new LinkedMultiValueMap<>();
        requestParams.add("username", "test");
        requestParams.add("password", "test");
        requestParams.add("repeatedPassword", "test");
        requestParams.add("name", "name");
        requestParams.add("surname", "surname");

        MockHttpServletRequestBuilder request = (MockMvcRequestBuilders
                .post("/register").params(requestParams));

        this.mockMvc.perform(request)
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.redirectedUrl("/login"));
    }

    @Test
    public void testRegisterPasswordMismatch() throws Exception {
        LinkedMultiValueMap<String, String> requestParams = new LinkedMultiValueMap<>();
        requestParams.add("username", "user");
        requestParams.add("password", "pass");
        requestParams.add("repeatedPassword", "different");
        requestParams.add("name", "name");
        requestParams.add("surname", "surname");

        MockHttpServletRequestBuilder request = (MockMvcRequestBuilders
                .post("/register").params(requestParams));

        this.mockMvc.perform(request)
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.model().attributeExists("hasError"))
                .andExpect(MockMvcResultMatchers.model().attributeExists("error"))
                .andExpect(MockMvcResultMatchers.model().attribute("bodyContent", "register"))
                .andExpect(MockMvcResultMatchers.view().name("master-template"));
    }
}

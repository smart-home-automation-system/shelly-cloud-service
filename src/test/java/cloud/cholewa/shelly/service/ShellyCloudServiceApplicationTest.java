package cloud.cholewa.shelly.service;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

//the profile is set here as well as in surefire: started from an IDE the class would otherwise
//come up with the home profile and log logstash JSON
@SpringBootTest
@ActiveProfiles("test")
class ShellyCloudServiceApplicationTest {

    @Test
    void contextLoads() {
    }

}

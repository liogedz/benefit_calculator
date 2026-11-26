package ee.lio.birthcalculator;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class BirthCalculatorApplication {

    void main() {
        SpringApplication.run(BirthCalculatorApplication.class);
    }
}

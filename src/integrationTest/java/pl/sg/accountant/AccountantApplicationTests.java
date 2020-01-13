package pl.sg.accountant;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
@TestPropertySource(properties = {"spring.config.location=classpath:application-it.yml"})
public class AccountantApplicationTests {
    @Autowired
    private FooController controller;

    @Test
    public void contextLoads() {
        controller.init();
    }

}

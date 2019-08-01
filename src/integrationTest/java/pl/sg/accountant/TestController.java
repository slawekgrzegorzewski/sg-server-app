package pl.sg.accountant;

import org.springframework.stereotype.Component;

@Component
public class TestController {
    private final TestRepository testRepository;

    public TestController(TestRepository testRepository) {
        this.testRepository = testRepository;
    }

    public void something() {
        testRepository.save(new Test(1L));
        testRepository.save(new Test(2L));
        testRepository.save(new Test(3L));
    }
}

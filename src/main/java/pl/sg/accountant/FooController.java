package pl.sg.accountant;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/foo")
public class FooController {
    private final FooRepository fooRepository;

    public FooController(FooRepository fooRepository) {
        this.fooRepository = fooRepository;
    }

    @RequestMapping(value = "/init", method = RequestMethod.GET)
    public List<Foo> init() {
        if (fooRepository.count() == 0) {
            fooRepository.save(new Foo(1, "a"));
            fooRepository.save(new Foo(2, "b"));
            fooRepository.save(new Foo(3, "c"));
            fooRepository.save(new Foo(4, "d"));
        }
        return fooRepository.findAll();
    }

    @RequestMapping(value = {"/", ""}, method = RequestMethod.GET)
    public List<Foo> allFoo() {
        return fooRepository.findAll();
    }
}

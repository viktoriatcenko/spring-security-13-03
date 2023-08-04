package ru.maxima.springboottest.ProjectSpringBoot1.controllers;

import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.maxima.springboottest.ProjectSpringBoot1.dto.PersonDTO;
import ru.maxima.springboottest.ProjectSpringBoot1.models.Person;
import ru.maxima.springboottest.ProjectSpringBoot1.services.PeopleService;
import ru.maxima.springboottest.ProjectSpringBoot1.services.RegistrationService;
import ru.maxima.springboottest.ProjectSpringBoot1.util.JWTutil;
import ru.maxima.springboottest.ProjectSpringBoot1.validate.PersonValidator;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {
    private final RegistrationService service;
    private final PersonValidator validator;
    private final  PeopleService peopleService;
    private final ModelMapper modelMapper;

    private final JWTutil jwTutil;
    @Autowired
    public AuthController(RegistrationService service, PersonValidator validator, PeopleService peopleService, ModelMapper modelMapper, JWTutil jwTutil) {
        this.service = service;
        this.validator = validator;
        this.peopleService = peopleService;
        this.modelMapper = modelMapper;
        this.jwTutil = jwTutil;
    }

    @GetMapping("/login")
    public String loginPage(Model model) {
        boolean emptyList = false;
        List<Person> people = peopleService.findAll();
        emptyList = (people.size() == 0) ? true : false;
        model.addAttribute("emptyList",emptyList);
        return "auth/login";
    }

    @GetMapping("/registration")
    public String registrationPage(@ModelAttribute("person") Person person) {
        return "auth/registration";
    }

    @GetMapping("/registration/{id}")
    public String registrationPerson(@PathVariable("id") int id,Model model) {
        Person person = peopleService.findOne(id);
        model.addAttribute("person",person);
        return "auth/registration";
    }
    @GetMapping("/unregistration/{id}")
    public String unregistrationPerson(@PathVariable("id") int id,Model model) {
        Person person = peopleService.findOne(id);
        person.setPassword("null");
        peopleService.update(id,person);
        return "redirect:/people";
    }
    @PostMapping("/registration")
    public Map<String, String> performRegistration(@RequestBody @Valid PersonDTO personDTO,
                                      BindingResult bindingResult) {
        Person person = convertToPerson(personDTO);

        validator.validate(person, bindingResult);

        if (bindingResult.hasErrors()) {
            return Map.of("message", "Wrong data from user");
        }

        service.register(person);

        String token = jwTutil.generateToken(person.getName());

        return Map.of("jwt-token", token);
    }



    @GetMapping("/admin")
    public String adminPage() {
        return "auth/admin";
    }

    @GetMapping("/logout")
    public String logOutPage() {
        return "logout";
    }

    @GetMapping("/makeAdmin")
    public String index(Model model) {
        Person person = new Person();
        model.addAttribute("person",person);
        model.addAttribute("peopleWithReg", peopleService.findAllWithMatchingPassword());
        model.addAttribute("people", peopleService.findAll());
        return "/auth/selectAdmin";
    }

    @PostMapping("/addAdmin")
    public String setAdmin(@ModelAttribute("employeeForm") Person getPerson) {
        Person person = peopleService.findOne(getPerson.getId());
        peopleService.save(person);
        return "redirect:/auth/makeAdmin";
    }

    private Person convertToPerson(PersonDTO personDTO) {
        return this.modelMapper.map(personDTO, Person.class);
    }
}


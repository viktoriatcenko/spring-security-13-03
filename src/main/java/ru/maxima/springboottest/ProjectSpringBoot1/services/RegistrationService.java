package ru.maxima.springboottest.ProjectSpringBoot1.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.maxima.springboottest.ProjectSpringBoot1.models.Person;
import ru.maxima.springboottest.ProjectSpringBoot1.repositories.PeopleRepository;

import java.util.Optional;

@Service
public class RegistrationService {
    private final PeopleRepository peopleRepository;
    private final PeopleService peopleService;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public RegistrationService(PeopleRepository peopleRepository, PeopleService peopleService, PasswordEncoder passwordEncoder) {
        this.peopleRepository = peopleRepository;
        this.peopleService = peopleService;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public boolean register(Person person) {
        Person controlPerson = null;
        if(person != null) {
            controlPerson = peopleService.findFirstByNameAndAge(person.getName(), person.getAge());
        }else{
            throw new NullPointerException("Person is null pointer exeption");
        }
        String password = person.getPassword();
        String encodedPassword = passwordEncoder.encode(password);
        person.setPassword(encodedPassword);
        person.setRole("ROLE_USER");
        if(controlPerson == null ){
            peopleRepository.save(person);
        }else{
            if(controlPerson.getPassword().equals("null")){
                peopleService.update(controlPerson.getId(), person);
            }else{
                peopleRepository.save(person);
            }
        }
        return true;
    }
}



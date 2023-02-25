package ru.maxima.springboottest.ProjectSpringBoot1.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.maxima.springboottest.ProjectSpringBoot1.models.Person;
import ru.maxima.springboottest.ProjectSpringBoot1.repositories.PeopleRepository;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class PeopleService {

    private final PeopleRepository peopleRepository;

    @Autowired
    public PeopleService(PeopleRepository peopleRepository) {
        this.peopleRepository = peopleRepository;
    }
    @Transactional
    public List<Person> findAll() {
//        List<Person> byEmail = peopleRepository.findByEmail("mail3@mail.ru");
//        byEmail.forEach(System.out::println);
//        List<Person> alex = peopleRepository.findByName("Alex");
//        alex.forEach(System.out::println);
//        List<Person> n = peopleRepository.findByNameStartingWith("N");
//        n.forEach(System.out::println);
//        List<Person> viktor = peopleRepository.findByNameOrEmail("Viktor", "mail5@mail.ru");
//        viktor.forEach(System.out::println);
//        List<Person> alex1 = peopleRepository.findByNameOrEmail("Alex", "test@maillll,ru");
//        alex1.forEach(System.out::println);
        return peopleRepository.findAll(Sort.by(Sort.Direction.ASC, "id"));
    }
    @Transactional
    public List<Person> findAllWithMatchingPassword(){
        List<Person> people = peopleRepository.findByPasswordNotContainingOrderByIdAsc("null");
        return people;
    }
    @Transactional
    public Person findOne(int id) {
        return peopleRepository.findById(id).orElse(null);
    }

    @Transactional
    public Person findFirstByNameAndAge(String name,int age) {
        return peopleRepository.findFirstByNameAndAge(name,age).orElse(null);
    }

    @Transactional
    public void save(Person person) {
        peopleRepository.save(person);
    }

    @Transactional
    public void update(int id, Person updatedPerson) {
        updatedPerson.setId(id);
        peopleRepository.save(updatedPerson);
    }

    @Transactional
    public void delete(int id) {
        peopleRepository.deleteById(id);
    }
}

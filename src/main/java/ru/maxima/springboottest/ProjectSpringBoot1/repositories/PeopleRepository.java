package ru.maxima.springboottest.ProjectSpringBoot1.repositories;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.maxima.springboottest.ProjectSpringBoot1.models.Person;

@Repository
public interface PeopleRepository extends JpaRepository<Person, Integer> {
}

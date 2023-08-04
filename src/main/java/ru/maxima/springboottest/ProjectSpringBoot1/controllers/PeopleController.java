package ru.maxima.springboottest.ProjectSpringBoot1.controllers;


import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.maxima.springboottest.ProjectSpringBoot1.models.Person;
import ru.maxima.springboottest.ProjectSpringBoot1.security.PersonDetails;
import ru.maxima.springboottest.ProjectSpringBoot1.services.PeopleService;


/**
 * @author Neil Alishev
 */
@Controller
@RequestMapping("/people")
public class PeopleController {

    private final PeopleService peopleService;

    @Autowired
    public PeopleController(PeopleService peopleService) {
        this.peopleService = peopleService;
    }

    @GetMapping()
    public String index(Model model) {
        model.addAttribute("people", peopleService.findAll());
        return "people/index";
    }

    @GetMapping("/{id}")
    public String show(@PathVariable("id") int id, Model model) {
        model.addAttribute("person", peopleService.findOne(id));
        return "people/show";
    }

    @GetMapping("/new")
    public String newPerson(@ModelAttribute("person") Person person) {
        return "people/new";
    }

    @PostMapping()
    public String create(@ModelAttribute("person") @Valid Person person,
                         BindingResult bindingResult) {
        if (bindingResult.hasErrors())
            return "people/new";
        person.setPassword("null");
        peopleService.save(person);
        return "redirect:/people";
    }

    @GetMapping("/{id}/edit")
    public String edit(Model model, @PathVariable("id") int id) {
        model.addAttribute("person", peopleService.findOne(id));
        return "people/edit";
    }

    @PostMapping("/{id}")
    public String update(@ModelAttribute("person") @Valid Person person, BindingResult bindingResult,
                         @PathVariable("id") int id) {
        if (bindingResult.hasErrors())
            return "people/edit";
        Person controlPerson = peopleService.findOne(id);
        if(controlPerson != null) {
            person.setPassword(controlPerson.getPassword());
            peopleService.update(id, person);
        }else{
            throw new NullPointerException("Response the returned null pointer exeption");
        }
        return "redirect:/people";
    }

    @PostMapping("/delete/{id}")
    public String delete(@PathVariable("id") int id) {
        peopleService.delete(id);
        return "redirect:/people";
    }

    @GetMapping("/show")
    @ResponseBody
    public String showUserInfo() {
        return SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
    }
}

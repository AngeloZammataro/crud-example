package com.example.crudexample.controllers;

import com.example.crudexample.entities.User;
import com.example.crudexample.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

import javax.websocket.server.PathParam;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    //adesso facciamo le operazioni CRUD
    //CREATE
    @PostMapping()
    public User create(@RequestBody User user){
        User userSaved = userRepository.saveAndFlush(user);
        return userSaved;
    }

    //READ
    @GetMapping
    public List<User> getAllUsers(){
        List<User> user = userRepository.findAll();
        return user;
    }

    //READ con pageble, ci permette di creare delle pagine che contengono gli user.Possiamo definire la dimensione della pagina.
    @GetMapping("/page_users")
    public Page<User> getPageUsers(@RequestParam(required = false) Optional<Integer> page, @RequestParam(required = false) Optional<Integer> size){
        if(page.isPresent() && size.isPresent()){
            Pageable pageable = PageRequest.of(page.get(), size.get());
            Page<User> user = userRepository.findAll(pageable);
            return user;
        }else {
            Page<User> userPage = Page.empty();
            return  userPage;
        }
    }

    //READ con PAGEBLE e SORT, ci permette di creare delle pagine che contengono gli user.Possiamo definire la dimensione della pagina.
    //facciamo il SORT per lastName e poi anche per firstName
    @GetMapping("/page_sort_users")
    public Page<User> getPageSortUsers(@RequestParam(required = false) Optional<Integer> page, @RequestParam(required = false) Optional<Integer> size){
        if(page.isPresent() && size.isPresent()){
            Sort sort = Sort.by(new Sort.Order(Sort.Direction.ASC,"lastName"),new Sort.Order(Sort.Direction.ASC,"firstName"));
            Pageable pageable = PageRequest.of(page.get(), size.get(), sort);
            Page<User> user = userRepository.findAll(pageable);
            return user;
        }else {
            Page<User> userPage = Page.empty();
            return  userPage;
        }
    }

    //READ by id
    @GetMapping("/{id}")
    public User getSingleUser(@PathVariable long id){
        User user = userRepository.getReferenceById(id);
        return user;
    }

    //UPDATE for id
    @PutMapping("/{id}")
    public User updateSingleUser(@PathVariable long id, @RequestBody User user){
        user.setId(id);
        User newUser = userRepository.saveAndFlush(user);
        return newUser;
    }

    //DELETE for id
    @DeleteMapping("/{id}")
    public void deleteSingleUser(@PathVariable long id){
        userRepository.deleteById(id);
    }

    //DELETE multiplo
    @DeleteMapping("")
    public void deleteMultiple(@RequestParam() List<Long> ids){
        userRepository.deleteAllById(ids);
    }
}

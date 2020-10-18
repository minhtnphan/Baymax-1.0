/**
 * Controller for users of Baymax 1.0
 * @Author Huyen Nguyen
 * @Author Minh Phan
 */
package com.baymax.baymax.controller;

import com.baymax.baymax.entity.Access;
import com.baymax.baymax.entity.User;
import com.baymax.baymax.repository.AccessRepository;
import com.baymax.baymax.repository.DepartmentRepository;
import com.baymax.baymax.repository.UserRepository;
import com.baymax.baymax.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;


@RestController
public class UserController {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private DepartmentRepository departmentRepository;
    @Autowired
    private AccessRepository accessRepository;

    /**
     * Register new user
     * @param user user's information
     * @return String denoting if the user has successfully registered their accounts
     */
    @PostMapping("/user")
    public String registerUser(@RequestBody User user) {
        if (user.getRole() == null
                ||user.getPassword() == null
                ||user.getUsername() == null) {
            return "Missing information";
        }
        if (userRepository.findByUsername(user.getUsername()).size() > 0) {
            return "User already exist";
        }
        user.setRole(user.getRole().toLowerCase());
        if (user.getRole().equals("admin")) {
            Access adminAccess = new Access(user.getRole());
            user.addAccess(adminAccess);
        }
        if (user.getRole().equals("user")) {
            Access userAccess = new Access(user.getRole());
            user.addAccess(userAccess);
        }
        String hashedPass = SecurityUtils.hashPassword(user.getPassword());
        user.setPassword(hashedPass);
        userRepository.save(user);
        return "Success";
    }

    /**
     * Log in to the application
     * @param user user's information
     * @return user's information if successfully logged in
     */
    @PostMapping("/user/login")
    public String login(@RequestBody User user) {
        if (user.getUsername() == null || user.getPassword() == null) {
            return "Missing login information";
        }
        User ret = new User();
        final String hashed = SecurityUtils.hashPassword(user.getPassword());
        final List<User> users = userRepository.
                findByUsernameAndPassword(user.getUsername(), hashed);
        if (users.size() != 1) {
            return "Log in failed";
        }
        return "Welcome to Baymax 1.0!";
    }

    /**
     * For admins to get all users' information from database
     * @param username to check if the user has permission to view all information
     * @return list of registered users in the application
     */
    @GetMapping("/{username}/user")
    public List<User> getAllUsers(@PathVariable(name = "username") String username) {
        if (userRepository.findByUsername(username).isEmpty()) {
            return new ArrayList<>();
        }
        Access access = accessRepository.findByUsers(
                userRepository.findByUsername(username).get(0)).get(0);
        if (access.getUser.equals("no")) {
            return new ArrayList<>();

        }
        return userRepository.findAll();
    }

    /**
     * Get a user's information
     * @param userId id to find the needed user
     * @param username to check if this person has permission to get a user's information
     * @return the information of the needed user
     */
    @GetMapping("/{username}/{userId}")
    public User getUser(@PathVariable(name = "userId") Long userId,
                        @PathVariable(name = "username") String username) {
        if (userRepository.findByUsername(username).isEmpty()) {
            return new User();
        }
        Access access = accessRepository.findByUsers
                (userRepository.findByUsername(username).get(0)).get(0);
        if (access.getUser.equals("no")) {
            return new User();
        }
        return userRepository.findById(userId).get();
    }

    /**
     * Delete a user from repository
     * @param id the id of the user that will be deleted
     * @param username to check if this person has permission to delete user
     */
    @DeleteMapping("/{username}/{id}")
    void deleteUser(@PathVariable(name = "id") long id,
                    @PathVariable(name = "username") String username) {

        if (userRepository.findByUsername(username).isEmpty()) {
            return;
        }
        Access access = accessRepository.
                findByUsers(userRepository.findByUsername(username).get(0)).get(0);
        if (access.getDelete().equals("no")) {
            return;
        }
        userRepository.delete(userRepository.getOne(id));
    }

    /**
     * Update a users' information
     * @param newUser the updated information of the user
     * @param id the id of the user whose information will be updated
     * @param username to check if this person has permission to update user's information
     * @return the new updated information of the user
     */
    @PutMapping("/{username}/{id}")
    User updateUser(@RequestBody User newUser, @PathVariable(name = "id") long id,
                    @PathVariable(name = "username") String username) {
        if (userRepository.findByUsername(username).isEmpty()) {
            return new User();
        }
        Access access = accessRepository.
                findByUsers(userRepository.findByUsername(username).get(0)).get(0);
        if (access.getUpdate().equals("no")) {
            return new User();
        }
        User terUser = userRepository.findById(id).get();
        Access newAc = terUser.getAccess().iterator().next();
        if (newUser.getRole().equals("admin")) {
            newAc.setRole("admin");
            newAc.setGetUser("yes");
            newAc.setUpdate("yes");
            newAc.setDelete("yes");
            newAc.setSave("yes");
        }
        else if (newUser.getRole().equals("user")) {
            newAc.setRole("user");
            newAc.setGetUser("no");
            newAc.setUpdate("no");
            newAc.setDelete("no");
            newAc.setSave("no");
        }
        return userRepository.findById(id).map(user -> {
            user.setUsername(newUser.getUsername());
            user.setPassword(SecurityUtils.hashPassword(newUser.getPassword()));
            user.setRole(newUser.getRole());

            return userRepository.save(user);
        })
                .orElseGet(()-> {
                    newUser.setId(id);
                    return userRepository.save(newUser);
                });
    }



}

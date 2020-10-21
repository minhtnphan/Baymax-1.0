/**
 * Deparment controller of Baymax 1.0
 * @Author Huyen Nguyen
 * @Author Minh Phan
 */
package com.baymax.baymax.controller;

import com.baymax.baymax.entity.Access;
import com.baymax.baymax.entity.Department;
import com.baymax.baymax.repository.AccessRepository;
import com.baymax.baymax.repository.DepartmentRepository;
import com.baymax.baymax.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;


@RestController
public class DepartmentController {

    @Autowired
    DepartmentRepository departmentRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    AccessRepository accessRepository;


    /**
     * Method for admins to insert new departments into database
     * @Param department includes all information of the department
     * @Param username to check if the user have permission to insert new department
     * @Return String denoting if the department is saved in database
     */
    @PostMapping("{username}/department")
    public String insertDepartment(@RequestBody Department department,
                                 @PathVariable(name = "username") String username) {
        if (userRepository.findByUsername(username).isEmpty()) {
            return "This username is not valid";
        }
        Access access = accessRepository.findByUsers
                    (userRepository.findByUsername(username).get(0)).get(0);
        if (access.getSave().equals("no")) {
            return "You are not allowed to save new hospital department";
        }
        if (department.getSymptom() == null
            || department.getInsuranceStatus() == null
            || department.getLocation() == null) {
            return "Missing information";
        }
        department.setDepartment(department.getDepartment().toLowerCase());
        department.setSymptom(department.getSymptom().toLowerCase());
        departmentRepository.save(department);
        return "Success";
    }

    /**
     * Delete a department from database
     * @param id department id that needs to be deleted
     * @param username to check if the user have permission to delete department
     */
    @DeleteMapping("/{username}/department/{id}")
    void deleteDepartment(@PathVariable(name = "id") long id,
                    @PathVariable(name = "username") String username) {

        if (userRepository.findByUsername(username).isEmpty()) {
            return;
        }
        Access access = accessRepository.
                findByUsers(userRepository.findByUsername(username).get(0)).get(0);
        if (access.getDelete().equals("no")) {
            return;
        }
        departmentRepository.delete(departmentRepository.getOne(id));
    }

    /**
     * For admins to update information of the department
     * @param newDepartment contains all information that admin wants to update
     * @param id id of the department that admin wants to update
     * @param username check if the user has permission to update informtion
     * @return new update Department
     */
    @PutMapping("/{username}/department/{id}")
    String updateDepartment(@RequestBody Department newDepartment,
                                @PathVariable(name = "id") long id,
                                @PathVariable(name = "username") String username) {
        if (userRepository.findByUsername(username).isEmpty()) {
            return "This username is not valid";
        }
        Access access = accessRepository.findByUsers
                (userRepository.findByUsername(username).get(0)).get(0);
        if (access.getUpdate().equals("no")) {
            return "You have no permission to update departments";
        }
        if (newDepartment.getDepartment() == null
            || newDepartment.getHospital() == null
            || newDepartment.getSymptom() == null
            || newDepartment.getLocation() == null) {
            return "Missing information";
        }
        if (!departmentRepository.existsById(id)) {
            newDepartment.setId(id);
            departmentRepository.save(newDepartment);
        }
        Department department = departmentRepository.findById(id).get();
        department.setDepartment(newDepartment.getDepartment());
        department.setHospital(newDepartment.getHospital());
        department.setSymptom(newDepartment.getSymptom());
        department.setRanking(newDepartment.getRanking());
        department.setInsuranceStatus(newDepartment.getInsuranceStatus());
        department.setPrice(newDepartment.getPrice());
        department.setLocation(newDepartment.getLocation());
        departmentRepository.save(department);
        return "department saved";
    }

    /**
     * Return all existing departments in database
     * @param username the user who has registered in Baymax
     * @return the list of all departments in database
     */
    @GetMapping("/{username}/department")
    public List<Department> getAllDepartments(@PathVariable(name = "username") String username) {
        if (userRepository.findByUsername(username).isEmpty()) {
            return new ArrayList<>();
        }
        return departmentRepository.findAll();
    }

    /**
     * Recommend users with departments that have highest ranking to cure their input symptoms
     * @param username the user who has registered in Baymax
     * @param department containing information from user to receive recommendation
     * @return departments that have the highest ranking from 1 to 5
     * for curing the symptom input by users
     */
    @GetMapping("/{username}/department/recommender")
    public List<Department> findDepartmentsBySymptoms(@PathVariable(name = "username") String username,
                                            @RequestBody Department department){
        if (userRepository.findByUsername(username).isEmpty() || department.getSymptom().isEmpty()) {
            return new ArrayList<>();
        }
        List<Department> recommendedDepartments = new ArrayList<Department>();
        recommendedDepartments = departmentRepository.
                findBySymptomAndRanking(department.getSymptom(),
                        Long.valueOf(5));
        return recommendedDepartments;
    }

    /**
     * Recommend users with departments that accepts using Bao Minh insurance to cure their input symptoms
     * @param department information of users' symptoms
     * @param username user who has access to this functionality
     * @return the list of departments that cure the input symptoms and accept paying by Bao Minh insurance
     */
    @GetMapping("/{username}/department/recommender/insurance") // recommend hospitals filtering by Bao Minh Insurance
    public List<Department> findDepartmentsByInsurance(@RequestBody Department department,
                                                       @PathVariable(name = "username") String username){
        if (userRepository.findByUsername(username).isEmpty()
            ||department.getSymptom().isEmpty()) {
            return new ArrayList<>();
        }
        List<Department> recommendedDepartmentsBySymptoms = findDepartmentsBySymptoms(username, department);
        List<Department> recommendedDepartmentsByInsurance = new ArrayList<>();
        for (int i = 0; i < recommendedDepartmentsBySymptoms.size(); i++){
            if (recommendedDepartmentsBySymptoms.get(i).getInsuranceStatus() == 1) {
                recommendedDepartmentsByInsurance.add(recommendedDepartmentsBySymptoms.get(i));
            }
        }
        return recommendedDepartmentsByInsurance;
    }

    /**
     * Recommend users with departments that cure their input symptoms with price lower than their input
     * @param department information of users' symptoms
     * @param username user who has access to this functionality
     * @param price the price threshold by the user
     * @return list of departments that cure the symptoms with price lower than the specified threshold
     */

    @GetMapping("/{username}/department/recommender/price/{price}") // recommend hospitals filtering by price
    public List<Department> findDepartmentsByPrice(@RequestBody Department department,
                                                   @PathVariable(name = "username") String username,
                                                    @PathVariable(name = "price") long price) {
        if (userRepository.findByUsername(username).isEmpty()
            || department.getSymptom().isEmpty()) {
            return new ArrayList<>();
        }
        List<Department> recommendedDepartmentsBySymptoms = findDepartmentsBySymptoms(username, department);
        List<Department> recommendedDepartmentsByPrice = new ArrayList<Department>();
        for (int i = 0; i < recommendedDepartmentsBySymptoms.size(); i++) {
            if (recommendedDepartmentsBySymptoms.get(i).getPrice() <= price) {
                recommendedDepartmentsByPrice.add(recommendedDepartmentsBySymptoms.get(i));
            }
        }
        return recommendedDepartmentsByPrice;
    }

    /**
     * Recommend users with departments that cure their input symptoms with within the location of the user
     * @param department information of users' symptoms
     * @param username user who has access to this functionality
     * @param location the location input by the user
     * @return list of departments that cure the symptoms with location similar to user's input
     */

    @GetMapping("/{username}/department/recommender/{location}") // recommend hospitals filtering by location
    public List<Department> findDepartmentsByLocation(@RequestBody Department department,
                                                      @PathVariable(name = "username") String username,
                                                      @PathVariable(name = "location") String location) {
        if (userRepository.findByUsername(username).isEmpty()
            ||department.getSymptom().isEmpty()) {
            return new ArrayList<>();
        }
        List<Department> recommendedDepartments = new ArrayList<Department>();
        recommendedDepartments = departmentRepository.findBySymptomAndLocation(department.getSymptom(), location);
        return recommendedDepartments;
    }

}

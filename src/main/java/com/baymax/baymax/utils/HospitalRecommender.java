//package com.baymax.baymax.utils;
//
//import com.baymax.baymax.entity.Department;
//import com.baymax.baymax.repository.DepartmentRepository;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PathVariable;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestBody;
//
//import java.util.ArrayList;
//import java.util.List;
//
//public class HospitalRecommender {
//    @Autowired
//    private DepartmentRepository departmentRepository;
//
//    @GetMapping("/department/recommender") // recommends the highest-ranking departments
//    public List<Department> findDepartmentsBySymptoms(@RequestBody Department department){
//        List<Department> recommendedDepartments = new ArrayList<Department>();
//        recommendedDepartments = departmentRepository.findBySymptomAndRanking(department.getSymptom(),
//                Long.valueOf(5));
//        return recommendedDepartments;
//    }
//
//    @GetMapping("/department/recommender/insurance") // recommend hospitals filtering by Bao Minh Insurance
//    public List<Department> findDepartmentsByInsurance(@RequestBody Department department){
//        List<Department> recommendedDepartmentsBySymptoms = findDepartmentsBySymptoms(department);
//        List<Department> recommendedDepartmentsByInsurance = new ArrayList<Department>();
//        for (int i = 0; i < recommendedDepartmentsBySymptoms.size(); i++){
//            if (recommendedDepartmentsBySymptoms.get(i).getInsuranceStatus() == 1){
//                recommendedDepartmentsByInsurance.add(recommendedDepartmentsBySymptoms.get(i));
//            }
//        }
//        return recommendedDepartmentsByInsurance;
//    }
//
//    @GetMapping("/department/recommender/{price}") // recommend hospitals filtering by price
//    public List<Department> findDepartmentsByPrice(@RequestBody Department department, @PathVariable long price) {
//        List<Department> recommendedDepartmentsBySymptoms = findDepartmentsBySymptoms(department);
//        List<Department> recommendedDepartmentsByPrice = new ArrayList<Department>();
//        for (int i = 0; i < recommendedDepartmentsBySymptoms.size(); i++) {
//            if (recommendedDepartmentsBySymptoms.get(i).getPrice() <= price) {
//                recommendedDepartmentsByPrice.add(recommendedDepartmentsBySymptoms.get(i));
//            }
//        }
//        return recommendedDepartmentsByPrice;
//    }
//}

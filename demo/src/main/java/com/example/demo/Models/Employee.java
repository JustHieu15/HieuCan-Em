package com.example.demo.Models;

import jakarta.persistence.*;
import lombok.Getter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;


@Getter
@Entity
@Table(name = "employees")
public class Employee {

    private static List<Employee> employeeList = new ArrayList<>();
    private static Long idCounter = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String name;

    private Integer age;

    private Double salary;

    public Employee() {}

    public Employee(String name, Integer age, Double salary) {
        this.name = name;
        this.age = age;
        this.salary = salary;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public void setSalary(Double salary) {
        this.salary = salary;
    }

    @Override
    public String toString() {
        return "Employee{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", age=" + age +
                ", salary=" + salary +
                '}';
    }


    public static List<Employee> findAll() {
        return new ArrayList<>(employeeList);
    }

    public static Optional<Employee> findById(Long id) {
        return employeeList.stream()
                .filter(e -> Objects.equals(e.getId(), id))
                .findFirst();
    }

    public static Optional<Employee> findByName(String name) {
        return employeeList.stream()
                .filter(e -> e.getName().equals(name))
                .findFirst();
    }

    public static boolean existsByName(String name) {
        return employeeList.stream()
                .anyMatch(e -> e.getName().equals(name));
    }

    public static Employee save(Employee employee) throws Exception {
        // For new employee
        if (employee.getId() == null) {
            // Check if name exists
            if (existsByName(employee.getName())) {
                throw new Exception("Error while creating User. Unable to create. A User with name " +
                        employee.getName() + " already exist.");
            }

            // Create new employee
            employee.setId(idCounter++);
            employeeList.add(employee);
            return employee;
        }
        else {
            Optional<Employee> existingEmp = findById(employee.getId());

            if (existingEmp.isPresent()) {
                Optional<Employee> empWithSameName = findByName(employee.getName());
                if (empWithSameName.isPresent() && !Objects.equals(empWithSameName.get().getId(), employee.getId())) {
                    throw new Exception("Error while updating User. A User with name " +
                            employee.getName() + " already exist.");
                }

                employeeList.remove(existingEmp.get());

                employeeList.add(employee);

                return employee;
            } else {
                throw new Exception("User not found with id: " + employee.getId());
            }
        }
    }

    public static void deleteById(Long id) {
        employeeList.removeIf(e -> Objects.equals(e.getId(), id));
    }

    public static List<Employee> search(String name, Integer minAge, Integer maxAge,
                                        Double minSalary, Double maxSalary) {
        return employeeList.stream()
                .filter(e -> name == null || e.getName().toLowerCase().contains(name.toLowerCase()))
                .filter(e -> minAge == null || e.getAge() >= minAge)
                .filter(e -> maxAge == null || e.getAge() <= maxAge)
                .filter(e -> minSalary == null || e.getSalary() >= minSalary)
                .filter(e -> maxSalary == null || e.getSalary() <= maxSalary)
                .collect(Collectors.toList());
    }

    public static void initSampleData() {
        if (employeeList.isEmpty()) {
            try {
                Employee e1 = new Employee("Sam", 30, 70000.0);
                Employee e2 = new Employee("Tom", 40, 50000.0);
                save(e1);
                save(e2);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}

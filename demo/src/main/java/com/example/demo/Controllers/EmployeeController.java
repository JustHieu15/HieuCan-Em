package com.example.demo.Controllers;

import com.example.demo.Models.Employee;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.validation.Valid;
import java.util.Optional;

@Controller
public class EmployeeController {

    public void init() {
        Employee.initSampleData();
    }

    @GetMapping("/")
    public String index(Model model) {
        model.addAttribute("employees", Employee.findAll());
        model.addAttribute("employee", new Employee());
        return "index";
    }

    @PostMapping("/employee/add")
    public String addEmployee(@Valid @ModelAttribute("employee") Employee employee,
                              BindingResult result,
                              RedirectAttributes redirectAttributes,
                              Model model) {
        if (result.hasErrors()) {
            model.addAttribute("employees", Employee.findAll());
            return "index";
        }

        try {
            Employee.save(employee);
            redirectAttributes.addFlashAttribute("successMessage", "User created successfully");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }

        return "redirect:/";
    }

    @GetMapping("/employee/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        Optional<Employee> employee = Employee.findById(id);
        if (employee.isPresent()) {
            model.addAttribute("employee", employee.get());
            model.addAttribute("employees", Employee.findAll());
            model.addAttribute("isEdit", true);
            return "index";
        } else {
            return "redirect:/";
        }
    }

    @PostMapping("/employee/update")
    public String updateEmployee(@Valid @ModelAttribute("employee") Employee employee,
                                 BindingResult result,
                                 RedirectAttributes redirectAttributes,
                                 Model model) {
        if (result.hasErrors()) {
            model.addAttribute("employees", Employee.findAll());
            model.addAttribute("isEdit", true);
            return "index";
        }

        try {
            Employee.save(employee);
            redirectAttributes.addFlashAttribute("successMessage", "User updated successfully");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }

        return "redirect:/";
    }

    @GetMapping("/employee/delete/{id}")
    public String deleteEmployee(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        Employee.deleteById(id);
        redirectAttributes.addFlashAttribute("successMessage", "User removed successfully");
        return "redirect:/";
    }

    @GetMapping("/employee/search")
    public String searchEmployees(@RequestParam(required = false) String name,
                                  @RequestParam(required = false) Integer minAge,
                                  @RequestParam(required = false) Integer maxAge,
                                  @RequestParam(required = false) Double minSalary,
                                  @RequestParam(required = false) Double maxSalary,
                                  Model model) {
        model.addAttribute("employees", Employee.search(name, minAge, maxAge, minSalary, maxSalary));
        model.addAttribute("employee", new Employee());
        return "index";
    }
}

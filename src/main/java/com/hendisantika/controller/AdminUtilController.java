package com.hendisantika.controller;

import com.hendisantika.entity.User;
import com.hendisantika.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * Admin utility controller for managing test users
 * This endpoint initializes default test users in the database
 */
@Controller
public class AdminUtilController {

    @Autowired
    private UserRepository userRepository;

    /**
     * Initialize test users in MongoDB
     * Access via: http://localhost:8080/admin/init-users
     */
    @GetMapping("/admin/init-users")
    @ResponseBody
    public String initializeUsers() {
        try {
            // Delete existing users to avoid duplicates
            userRepository.deleteAll();

            // Create test users with plaintext passwords
            User admin = new User();
            admin.setUsername("admin");
            admin.setPassword("admin");
            admin.setEmail("admin@example.com");

            User user = new User();
            user.setUsername("user");
            user.setPassword("user");
            user.setEmail("user@example.com");

            User manager = new User();
            manager.setUsername("manager");
            manager.setPassword("manager");
            manager.setEmail("manager@example.com");

            userRepository.save(admin);
            userRepository.save(user);
            userRepository.save(manager);

            return "✓ Test users initialized successfully!<br>" +
                    "Username: admin / Password: admin<br>" +
                    "Username: user / Password: user<br>" +
                    "Username: manager / Password: manager<br><br>" +
                    "<a href='/login'>Go to Login</a>";
        } catch (Exception e) {
            return "Error initializing users: " + e.getMessage();
        }
    }

    /**
     * Debug endpoint to check stored users in database
     * Access via: http://localhost:8080/admin/check-users
     */
    @GetMapping("/admin/check-users")
    @ResponseBody
    public String checkUsers() {
        try {
            List<User> users = userRepository.findAll();
            StringBuilder sb = new StringBuilder();
            sb.append("<h2>Users in Database (").append(users.size()).append(")</h2>");
            sb.append("<table border='1' cellpadding='10'>");
            sb.append("<tr><th>ID</th><th>Username</th><th>Password</th><th>Email</th></tr>");
            
            for (User u : users) {
                sb.append("<tr>");
                sb.append("<td>").append(u.getId()).append("</td>");
                sb.append("<td>").append(u.getUsername()).append("</td>");
                sb.append("<td><code>").append(u.getPassword()).append("</code></td>");
                sb.append("<td>").append(u.getEmail()).append("</td>");
                sb.append("</tr>");
            }
            sb.append("</table>");
            sb.append("<br><a href='/admin/init-users'>Reinitialize Users</a>");
            return sb.toString();
        } catch (Exception e) {
            return "Error checking users: " + e.getMessage();
        }
    }
}

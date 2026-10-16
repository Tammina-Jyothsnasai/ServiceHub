package com.servicehub.service;

import com.servicehub.entity.Feedback;
import com.servicehub.entity.ServiceRequest;
import com.servicehub.entity.Staff;
import com.servicehub.entity.User;
import com.servicehub.enums.*;
import com.servicehub.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.UUID;

@Component
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final StaffRepository staffRepository;
    private final ServiceRequestRepository requestRepository;
    private final FeedbackRepository feedbackRepository;
    private final PasswordEncoder passwordEncoder;
    private final NotificationService notificationService;

    @Autowired
    public DataInitializer(UserRepository userRepository,
                           StaffRepository staffRepository,
                           ServiceRequestRepository requestRepository,
                           FeedbackRepository feedbackRepository,
                           PasswordEncoder passwordEncoder,
                           NotificationService notificationService) {
        this.userRepository = userRepository;
        this.staffRepository = staffRepository;
        this.requestRepository = requestRepository;
        this.feedbackRepository = feedbackRepository;
        this.passwordEncoder = passwordEncoder;
        this.notificationService = notificationService;
    }

    @Override
    public void run(String... args) throws Exception {
        if (userRepository.count() == 0) {
            System.out.println(">>> Initializing ServiceHub Demo Data in Embedded Database...");

            // 1. Create Default Admin
            User admin = new User();
            admin.setName("System Admin");
            admin.setEmail("admin@servicehub.com");
            admin.setPassword(passwordEncoder.encode("Admin@123"));
            admin.setRole(Role.ADMIN);
            admin = userRepository.save(admin);

            // 2. Create Default Users
            User user1 = new User();
            user1.setName("Rahul Sharma");
            user1.setEmail("rahul@servicehub.com");
            user1.setPassword(passwordEncoder.encode("User@123"));
            user1.setRole(Role.USER);
            user1 = userRepository.save(user1);

            User user2 = new User();
            user2.setName("Priya Patel");
            user2.setEmail("priya@servicehub.com");
            user2.setPassword(passwordEncoder.encode("User@123"));
            user2.setRole(Role.USER);
            user2 = userRepository.save(user2);

            // 3. Create Default Staff Members
            Staff staff1 = new Staff(null, "John Kumar", "john.kumar@servicehub.com", Department.IT, "+91 9876543210", StaffStatus.ACTIVE);
            Staff staff2 = new Staff(null, "Ramesh Electrician", "ramesh.elec@servicehub.com", Department.ELECTRICAL, "+91 9876543211", StaffStatus.ACTIVE);
            Staff staff3 = new Staff(null, "Suresh Plumber", "suresh.plumb@servicehub.com", Department.PLUMBING, "+91 9876543212", StaffStatus.ACTIVE);
            Staff staff4 = new Staff(null, "Anita Maintenance", "anita.maint@servicehub.com", Department.MAINTENANCE, "+91 9876543213", StaffStatus.ACTIVE);

            staff1 = staffRepository.save(staff1);
            staff2 = staffRepository.save(staff2);
            staff3 = staffRepository.save(staff3);
            staff4 = staffRepository.save(staff4);

            // 4. Create Sample Service Requests
            // Ticket 1: Pending
            ServiceRequest req1 = new ServiceRequest();
            req1.setUser(user1);
            req1.setTicketId("SR1001");
            req1.setTitle("AC in Room 204 is not cooling");
            req1.setDescription("The air conditioner in Room 204 is emitting warm air and making a loud clicking noise.");
            req1.setCategory(Category.MAINTENANCE);
            req1.setPriority(Priority.HIGH);
            req1.setStatus(RequestStatus.PENDING);
            req1.setLocation("Building A, Room 204");
            req1 = requestRepository.save(req1);

            // Ticket 2: Assigned & In Progress
            ServiceRequest req2 = new ServiceRequest();
            req2.setUser(user1);
            req2.setTicketId("SR1002");
            req2.setTitle("WiFi router disconnected on 3rd Floor");
            req2.setDescription("Internet is completely down for the entire 3rd floor office section.");
            req2.setCategory(Category.INTERNET);
            req2.setPriority(Priority.URGENT);
            req2.setStatus(RequestStatus.IN_PROGRESS);
            req2.setLocation("Block B, 3rd Floor");
            req2.setAssignedStaff(staff1);
            req2 = requestRepository.save(req2);

            // Ticket 3: Resolved with Feedback
            ServiceRequest req3 = new ServiceRequest();
            req3.setUser(user2);
            req3.setTicketId("SR1003");
            req3.setTitle("Water pipe leaking under Lab sink");
            req3.setDescription("Water is leaking steadily under the Chemistry Lab 1 main sink.");
            req3.setCategory(Category.PLUMBING);
            req3.setPriority(Priority.HIGH);
            req3.setStatus(RequestStatus.RESOLVED);
            req3.setLocation("Science Wing, Lab 1");
            req3.setAssignedStaff(staff3);
            req3.setResolutionNotes("Replaced broken washer and sealed drainage pipe connection.");
            req3.setResolvedAt(LocalDateTime.now().minusHours(2));
            req3 = requestRepository.save(req3);

            // Seed Feedback for req3
            Feedback fb = new Feedback();
            fb.setServiceRequest(req3);
            fb.setUser(user2);
            fb.setRating(5);
            fb.setComment("Problem was resolved quickly and cleanly by Suresh!");
            feedbackRepository.save(fb);

            // Seed Notifications
            notificationService.createNotification(user1, req1, "Your request SR1001 has been registered.");
            notificationService.createNotification(user1, req2, "SR1002 assigned to John Kumar (IT) and is In Progress.");
            notificationService.createNotification(user2, req3, "SR1003 has been RESOLVED. Thank you for your feedback!");

            System.out.println("==========================================================");
            System.out.println(">>> SERVICEHUB BACKEND IS RUNNING SUCCESSFULLY! <<<");
            System.out.println("==========================================================");
            System.out.println(">>> Default Admin: admin@servicehub.com / Admin@123");
            System.out.println(">>> Default User:  rahul@servicehub.com / User@123");
            System.out.println("==========================================================");
        }
    }
}

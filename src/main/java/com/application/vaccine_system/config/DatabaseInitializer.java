package com.application.vaccine_system.config;


import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.application.vaccine_system.model.User;
import com.application.vaccine_system.model.User.UserRole;
import com.application.vaccine_system.repository.RoomRepository;
import com.application.vaccine_system.repository.UserRepository;
// import com.application.vaccine_system.util.RoomDataGenerator;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DatabaseInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoomRepository roomRepository;

    @Override
    public void run(String... args) throws Exception {
        System.out.println(">>> START INIT DATABASE");
        long countUsers = this.userRepository.count();
        // long countRooms = this.roomRepository.count();
        if (countUsers == 0) {
            User adminUser = new User();
            adminUser.setEmail("admin@gmail.com");
            adminUser.setFullname("I'm admin");
            adminUser.setPassword(this.passwordEncoder.encode("123456"));
            adminUser.setPhone("0123456789");
            adminUser.setAddress("Hà Nội");
            adminUser.setRole(UserRole.ADMIN);
            this.userRepository.save(adminUser);  
        }
        // if (countRooms == 0) {
        //     roomRepository.saveAll(RoomDataGenerator.generateSampleRooms());
        // }
    }
}

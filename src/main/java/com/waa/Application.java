package com.waa;

import java.util.List;
import com.waa.domain.*;
import java.util.ArrayList;
import java.time.LocalDateTime;
import com.waa.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.password.PasswordEncoder;


@SpringBootApplication
public class Application implements CommandLineRunner {

    @Autowired
    UserRepository userRepository;
    @Autowired
    PasswordEncoder passwordEncoder;

    public static void main(String[] args) {
        System.out.println("===Start===");
        SpringApplication.run(Application.class, args);
        System.out.println("===End===");
    }

    @Override
    public void run(String... args) throws Exception {
        //saveData();
    }
    private void saveData(){
        var user1 = User.builder()
                .firstName("Romeo")
                .lastName("Mugwaneza")
                .email("romeo.mugwa@miu.edu")
                .isApproved(true)
                .role(Role.ADMIN)
                .password(passwordEncoder.encode("123"))
                .properties(new ArrayList<>())
                .build();
        var user2 = User.builder()
                .firstName("Martin")
                .lastName("Mugisha")
                .email("matt.mugisha@miu.edu")
                .isApproved(true)
                .role(Role.OWNER)
                .password(passwordEncoder.encode("123"))
                .properties(new ArrayList<>())
                .build();
        var user3 = User.builder()
                .firstName("Amani")
                .lastName("Jean")
                .email("amani.jean@miu.edu")
                .isApproved(false)
                .role(Role.OWNER)
                .password(passwordEncoder.encode("123"))
                .properties(new ArrayList<>())
                .build();
        var user4 = User.builder()
                .firstName("Patrick")
                .lastName("Bihizi")
                .email("patrick.bihizi@miu.edu")
                .isApproved(true)
                .role(Role.CUSTOMER)
                .password(passwordEncoder.encode("123"))
                .properties(new ArrayList<>())
                .build();

        var address1 = Address.builder()
                .street("110 E Broadway")
                .city("Fairfield")
                .state("IA")
                .zipCode("52556")
                .build();

        var property1 = Property.builder()
                .owner(user2)
                .address(address1)
                .propertyStatus(PropertyStatus.AVAILABLE)
                .imageUrl("")
                .title("Family Home")
                .numberOfBedRooms(3)
                .numberOfBathRooms(2)
                .price(350000)
                .postedDate(LocalDateTime.now())
                .build();

        user2.addProperty(property1);

        var users = List.of(user1, user2, user3, user4);
        userRepository.saveAll(users);

    }
}

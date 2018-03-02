package com.example.demo.utils;

import com.example.demo.model.Role;
import com.example.demo.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataLoader implements CommandLineRunner{
    @Autowired
    RoleRepository roleRepository;

    @Override
    public void run(String... strings) throws Exception{
        Role r = new Role();
        r.setRole("USER");
        roleRepository.save(r);

        r = new Role();
        r.setRole("ADMIN");
        roleRepository.save(r);
    }
}

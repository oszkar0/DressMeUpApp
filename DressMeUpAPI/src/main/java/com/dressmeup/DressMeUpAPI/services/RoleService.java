package com.dressmeup.DressMeUpAPI.services;

import com.dressmeup.DressMeUpAPI.entities.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.dressmeup.DressMeUpAPI.repositories.RoleRepository;
@Service
public class RoleService {
    @Autowired
    private RoleRepository roleRepository;

    public Role getByName(String name)
    {
        return roleRepository.findByName(name);
    }

    public Role save(Role role)
    {
        return roleRepository.save(role);
    }
}

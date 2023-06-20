package com.revature.yolp.roles;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.revature.yolp.utils.custom_exceptions.RoleNotFoundException;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class RoleService {
    private final RoleRepository roleRepository;
    private final static Logger logger = LoggerFactory.getLogger(RoleService.class);

    public Role findByName(String name) {
        logger.debug("Attempting to find role with name: {}", name);
        Optional<Role> roleOpt = roleRepository.findByName(name.toUpperCase());
        if (roleOpt.isPresent()) {
            Role role = roleOpt.get();
            logger.info("Role {} found in database.", role.toString());
            return role;
        }
        logger.warn("Role with name \"{}\" not found.", name);
        throw new RoleNotFoundException("Role with name \"" + name + "\" not found.");
    }
}
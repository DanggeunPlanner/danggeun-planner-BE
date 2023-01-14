package com.finalteam4.danggeunplanner.group.repository;

import com.finalteam4.danggeunplanner.group.entity.Group;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface GroupRepository extends JpaRepository<Group,Long> {
    Optional<Group> findByAdmin(String admin);
    boolean existsByAdmin(String username);
}

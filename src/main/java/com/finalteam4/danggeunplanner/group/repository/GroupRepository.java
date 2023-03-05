package com.finalteam4.danggeunplanner.group.repository;

import com.finalteam4.danggeunplanner.group.entity.Group;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface GroupRepository extends JpaRepository<Group,Long> {
    Optional<Group> findByAdmin(String admin);
    boolean existsByAdmin(String username);
    @Modifying
    @Query("update groups g set g.admin = :request where g.admin = :username")
    void updateGroupAdmin(@Param("request") String request, @Param("username") String username);
}

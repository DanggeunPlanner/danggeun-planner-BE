package com.finalteam4.danggeunplanner.group.controller;

import com.finalteam4.danggeunplanner.group.service.GroupService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/group")
public class GroupController {
    private final GroupService groupService;
}

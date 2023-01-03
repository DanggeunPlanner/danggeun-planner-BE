package com.finalteam4.danggeunplanner.plan.service;

import com.finalteam4.danggeunplanner.plan.repository.PlanRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PlanService {
    private final PlanRepository planRepository;
}

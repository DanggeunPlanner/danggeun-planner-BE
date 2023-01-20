package com.finalteam4.danggeunplanner.timer.dto.request;

import com.finalteam4.danggeunplanner.common.exception.ValidationGroups;
import lombok.Getter;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Getter
public class TimerFinishRequest {

    @NotNull(message = "종료 시간은 Null이 될 수 없습니다.", groups = ValidationGroups.FirstNotNullGroup.class)
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime endTime;

    @NotNull(message = "연속 개수는 Null이 될 수 없습니다.", groups = ValidationGroups.SecondNotNullGroup.class)
    private Integer count;
}

package com.finalteam4.danggeunplanner.common.exception;

import javax.validation.GroupSequence;
import javax.validation.groups.Default;

@GroupSequence({Default.class,
        ValidationGroups.EmailPatternGroup.class,
        ValidationGroups.PasswordPatternGroup.class,
        ValidationGroups.FirstNotNullGroup.class,
        ValidationGroups.SecondNotNullGroup.class,
        ValidationGroups.ThirdNotNullGroup.class,
        ValidationGroups.FirstSizeGroup.class,
        ValidationGroups.SecondSizeGroup.class})
public interface ValidationSequence {
}

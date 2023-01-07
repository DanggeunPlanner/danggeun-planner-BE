package com.finalteam4.danggeunplanner.group.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Random;

@Getter
@AllArgsConstructor
public enum GroupImageEnum {
    RANDOM_IMAGE1("랜덤이미지1"),
    RANDOM_IMAGE2("랜덤이미지2"),
    RANDOM_IMAGE3("랜덤이미지3"),
    RANDOM_IMAGE4("랜덤이미지4"),
    RANDOM_IMAGE5("랜덤이미지5");

    private final String image;

    private static final Random random = new Random();

    public static GroupImageEnum pickRandomImage() {
        GroupImageEnum[] groupImageEnums = values();
        return groupImageEnums[random.nextInt(groupImageEnums.length)];
    }
}

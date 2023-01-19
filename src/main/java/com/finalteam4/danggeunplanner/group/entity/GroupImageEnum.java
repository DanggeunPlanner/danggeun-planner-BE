package com.finalteam4.danggeunplanner.group.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Random;

@Getter
@AllArgsConstructor
public enum GroupImageEnum {
    RANDOM_IMAGE1("https://danggeunplanner-bucket.s3.ap-northeast-2.amazonaws.com/images/7495d94d-a1d7-45dd-ad33-3e06df57c40arabbit_icon.png"),
    RANDOM_IMAGE2("https://danggeunplanner-bucket.s3.ap-northeast-2.amazonaws.com/images/62da0643-c3e9-4c06-b254-f6755b4b1cd4monkey_icon.png"),
    RANDOM_IMAGE3("https://danggeunplanner-bucket.s3.ap-northeast-2.amazonaws.com/images/15c4b14c-f50d-4356-95cd-84aff144c890dog_icon.png"),
    RANDOM_IMAGE4("https://danggeunplanner-bucket.s3.ap-northeast-2.amazonaws.com/images/7d046e6e-1ce7-4506-9484-65a0dc6e8fbdbird_icon.png");

    private final String image;

    private static final Random random = new Random();

    public static GroupImageEnum pickRandomImage() {
        GroupImageEnum[] groupImageEnums = values();
        return groupImageEnums[random.nextInt(groupImageEnums.length)];
    }
}

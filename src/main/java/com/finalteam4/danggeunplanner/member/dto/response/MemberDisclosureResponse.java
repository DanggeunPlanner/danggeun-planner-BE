package com.finalteam4.danggeunplanner.member.dto.response;

import lombok.Getter;

@Getter
public class MemberDisclosureResponse {
        private Boolean isPlannerOpened;

        public MemberDisclosureResponse(Boolean isPlannerOpened){
            this.isPlannerOpened = isPlannerOpened;
        }
}

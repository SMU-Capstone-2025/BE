package com.capstone.domain.project.dto.response;

public record InviteCheckResult(boolean available, String message) {

    public static InviteCheckResult toAvailable() {
        return new InviteCheckResult(true, "초대 가능한 사용자 입니다.");
    }

    public static InviteCheckResult toAlreadyMember() {
        return new InviteCheckResult(false, "이미 프로젝트에 존재하는 사용자입니다.");
    }
}
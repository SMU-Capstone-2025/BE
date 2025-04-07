package com.capstone.domain.user.entity;

public enum Role {
    ROLE_MEMBER, ROLE_MANAGER;

    public static boolean isValid(String value) {
        try {
            Role.valueOf(value);
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }
}
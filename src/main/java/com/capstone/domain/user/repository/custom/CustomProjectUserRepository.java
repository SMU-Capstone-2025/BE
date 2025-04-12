package com.capstone.domain.user.repository.custom;

import java.util.List;

public interface CustomProjectUserRepository {
    List<String> findUserIdByProjectId(String projectId);
}

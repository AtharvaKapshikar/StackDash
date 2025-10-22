package com.StackDash.DTOs;

import java.util.ArrayList;
import java.util.List;

public class UserPageResponse {
    private List<GetAllUserGTO> users;
    private long totalElements;
    private int totalPages;

    public UserPageResponse(List<GetAllUserGTO> users, long totalElements, int totalPages) {
        this.users = users != null ? users : new ArrayList<>(); // ✅ null-safe
        this.totalElements = totalElements;
        this.totalPages = totalPages;
    }



    public List<GetAllUserGTO> getUsers() {
        return users;
    }

    public void setUsers(List<GetAllUserGTO> users) {
        this.users = users;
    }

    public long getTotalElements() {
        return totalElements;
    }

    public void setTotalElements(long totalElements) {
        this.totalElements = totalElements;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }
}

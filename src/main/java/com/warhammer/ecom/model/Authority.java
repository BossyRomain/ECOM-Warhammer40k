package com.warhammer.ecom.model;

import org.springframework.security.core.GrantedAuthority;

public enum Authority implements GrantedAuthority {
    CLIENT("CLIENT"),
    ADMIN("ADMIN");

    private String authority;

    Authority(String authority) {
        this.authority = authority;
    }

    @Override
    public String getAuthority() {
        return authority;
    }
}

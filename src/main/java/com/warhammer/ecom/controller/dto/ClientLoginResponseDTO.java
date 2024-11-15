package com.warhammer.ecom.controller.dto;

import com.warhammer.ecom.model.Cart;
import com.warhammer.ecom.model.User;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class ClientLoginResponseDTO {

    private Long id;

    private User user;

    private Cart currentCart;

    private String firstName;

    private String lastName;

    private LocalDate birthday;

    private boolean newsletter;

    private String authToken;
}

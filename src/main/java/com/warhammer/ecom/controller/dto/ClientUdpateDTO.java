package com.warhammer.ecom.controller.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class ClientUdpateDTO {

    private String lastName;

    private String firstName;

    private LocalDate birthday;
}

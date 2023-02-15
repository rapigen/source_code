package com.visit.program.ReservationProgram.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
/**
 * 직원정보 수정
 * */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UpdateEmployeeDTO {
    @NotNull
    private Long id;
    @NotEmpty
    private String loginId;
    @NotEmpty
    private String employee_name;
    @NotEmpty
    private String beforePassword;

    @NotEmpty
    private String password;
    @NotEmpty
    private String passwordCheck;
}

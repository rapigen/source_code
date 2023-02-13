package com.visit.program.ReservationProgram.domain.dto;

import lombok.*;

import javax.validation.constraints.NotEmpty;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Login {
    @NotEmpty(message="id가 누락되었습니다.")
    private String loginId;
    @NotEmpty(message="패스워드가 누락되었습니다.")
    private String password;


}

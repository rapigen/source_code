package com.visit.program.ReservationProgram.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.lang.Nullable;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ReservationDTO {
    @Nullable
    private String date1 = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
    @Nullable
    private String date2;
    @Nullable
    private Boolean is_checked;
    @Nullable
    private String name;


}

package com.visit.program.ReservationProgram.domain.repository;

import com.visit.program.ReservationProgram.domain.dao.*;
import com.visit.program.ReservationProgram.domain.dto.Login;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface EmployeeRepository {

    void updatePassword(@Param("password")String password, @Param("id")Long id);
    String findByName(@Param("id")Long id);

    List<Employee> findAll();

    Employee findById(@Param("id")Long id);

    Employee findByLoginId(String loginId);

}

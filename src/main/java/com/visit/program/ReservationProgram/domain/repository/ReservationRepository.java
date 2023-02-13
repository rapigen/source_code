package com.visit.program.ReservationProgram.domain.repository;

import com.visit.program.ReservationProgram.domain.dao.Reservation;
import com.visit.program.ReservationProgram.domain.dao.ReservationInfo;
import com.visit.program.ReservationProgram.domain.dao.SaveReservationInfo;
import com.visit.program.ReservationProgram.domain.dto.ReservationDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Mapper
@Repository
public interface ReservationRepository {
    List<Reservation> findAll();
    void saveInfo(SaveReservationInfo reservation);

    void deleteInfo(Long id);
    void updateCheckedInfo(@Param("is_checked")boolean is_checked, @Param("id") Long id);
    List<Reservation> findAllPages(@Param("start")int start, @Param("end")int end);

    List<Reservation> findAllFromTodayTo7DaysReservations(ReservationDTO reservationDTO);

    Reservation findOne(Long id);

    ReservationInfo findInfoOne(Long id);

    List<Reservation> findAllDTO(ReservationDTO reservationDTO);
    List<Reservation> findAllDTO2(ReservationDTO reservationDTO);


}

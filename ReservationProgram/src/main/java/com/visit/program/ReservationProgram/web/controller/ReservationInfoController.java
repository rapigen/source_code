package com.visit.program.ReservationProgram.web.controller;

import com.visit.program.ReservationProgram.domain.dao.Reservation;
import com.visit.program.ReservationProgram.domain.dao.session.SessionConst;
import com.visit.program.ReservationProgram.domain.dto.ReservationDTO;
import com.visit.program.ReservationProgram.domain.service.ReservationService;
import com.visit.program.ReservationProgram.web.controller.path.PathChange;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import javax.servlet.http.HttpSession;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;

@Controller
@Slf4j
@RequiredArgsConstructor
public class HomeController {
    private final ReservationService reservationService;
    @GetMapping("/")
    @ResponseBody
    public String home(){return "해당 페이지에 접근할 수 없습니다. 다시 접속해주세요";}

    @ModelAttribute(name="renewDate")
    public String renewDate(){
        return  LocalDateTime.now().format(DateTimeFormatter.ofPattern("yy/MM/dd hh:mm:ss"));
    }
    @GetMapping("/reservation/info/all/rapigen_employee")
    public String viewEmployees(HttpSession session) {
        String uri = "redirect:/reservation/info/save";
        if(session.getAttribute(SessionConst.ACCESS_ID)==null){
            String uuid = UUID.randomUUID().toString();
            session.setAttribute(SessionConst.ACCESS_ID, uuid);
        }
        return uri;
    }


    @GetMapping("/reservation/info/all/rapigen_security")
    public String viewSecurity(@ModelAttribute("reservationDTO")ReservationDTO reservationDTO,HttpSession session,Model model) {
      String url =   "redirect:/reservation/info/all";
        List<Reservation> reservations = reservationService.findAllDTO(reservationDTO);
        if(session.getAttribute(SessionConst.ACCESS_ID)==null){
            String uuId = UUID.randomUUID().toString()+"security";
            session.setAttribute(SessionConst.ACCESS_ID,uuId);
        }
        model.addAttribute("reservations",reservations);
        return url;
    }

    @GetMapping("/reservation")
    public String redirectReservation(RedirectAttributes redirectAttributes){
//        String renewDate = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yy/MM/dd hh:mm:ss"));
//        redirectAttributes.addAttribute("renewDate",renewDate);
        return "redirect:/reservation/info/all";
    }


    @GetMapping("/reservation/info/all")
    public String viewAll(Model model,@ModelAttribute("reservationDTO")ReservationDTO reservationDTO,HttpSession session) {
        List<Reservation> reservations = null;
        String url = "view/All1";
        session.removeAttribute(SessionConst.LOGIN_SUCCESS);
        if(session.getAttribute(SessionConst.ACCESS_ID).toString().contains("security")){
            url = "view/All2";
            reservations = reservationService.findAllDTO2(reservationDTO);
        }
        else{
            reservations = reservationService.findAllDTO(reservationDTO);
        }
        model.addAttribute("reservations",reservations);
        url=PathChange.setAccessMethod(session.getAttribute(SessionConst.ACCESS_METHOD).toString(),url);
        return url;
    }














}

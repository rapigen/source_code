package com.visit.program.ReservationProgram.web.controller.mobile;

import com.visit.program.ReservationProgram.domain.dao.Reservation;
import com.visit.program.ReservationProgram.domain.dao.session.SessionConst;
import com.visit.program.ReservationProgram.domain.dto.ReservationDTO;
import com.visit.program.ReservationProgram.domain.service.ReservationService;
import com.visit.program.ReservationProgram.web.controller.path.AbstractPath;
import com.visit.program.ReservationProgram.web.controller.path.PathChange;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;

@Controller
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/m")
public class MobileHomeController {
    private final ReservationService reservationService;
    @ModelAttribute(name="renewDate")
    public String renewDate(){
        return  LocalDateTime.now().format(DateTimeFormatter.ofPattern("yy/MM/dd hh:mm:ss"));
    }
    @GetMapping
    @ResponseBody
    public String home(){return "해당 페이지에 접근할 수 없습니다. 다시 접속해주세요";}

    @GetMapping("/reservation")
    public String redirectReservation(RedirectAttributes redirectAttributes){
//        String renewDate = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yy/MM/dd hh:mm:ss"));
//        redirectAttributes.addAttribute("renewDate",renewDate);
        return "redirect:/m/reservation/info/all";
    }


    @GetMapping("/reservation/info/all")
    public String viewAll(Model model,@ModelAttribute("reservationDTO")ReservationDTO reservationDTO,HttpSession session) {
        String url = "mobile/view/All1";
//        model.addAttribute("renewDate",LocalDateTime.now().format(DateTimeFormatter.ofPattern("yy/MM/dd hh:mm:ss")));
        session.removeAttribute(SessionConst.LOGIN_SUCCESS);
        if(session.getAttribute(SessionConst.ACCESS_ID).toString().contains("security")){
            url = "mobile/view/All2";
            List<Reservation> reservations = reservationService.findAllDTO2(reservationDTO);
            model.addAttribute("reservations",reservations);
             //경비실
        }
        else{
            List<Reservation> reservations = reservationService.findAllDTO(reservationDTO);
            model.addAttribute("reservations",reservations);
        }
        return url;
    }














}
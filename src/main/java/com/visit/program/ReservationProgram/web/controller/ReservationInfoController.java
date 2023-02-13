package com.visit.program.ReservationProgram.web.controller;
import com.visit.program.ReservationProgram.domain.dao.*;
import com.visit.program.ReservationProgram.domain.dao.session.SessionConst;
import com.visit.program.ReservationProgram.domain.dto.ReservationDTO;
import com.visit.program.ReservationProgram.domain.ex.AlreadyCheckedEx;
import com.visit.program.ReservationProgram.domain.ex.ErrorMessage;
import com.visit.program.ReservationProgram.domain.ex.NoModificationsEx;
import com.visit.program.ReservationProgram.domain.ex.ReviseCountExcess;
import com.visit.program.ReservationProgram.domain.service.EmployeeService;
import com.visit.program.ReservationProgram.domain.service.ReservationService;
import com.visit.program.ReservationProgram.domain.service.VisitorService;
import com.visit.program.ReservationProgram.web.controller.path.AbstractPath;
import com.visit.program.ReservationProgram.web.controller.path.PathChange;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.regex.Pattern;

import static com.visit.program.ReservationProgram.domain.dao.session.SessionConst.ACCESS_METHOD;

@Controller
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/reservation/info")
public class ReservationInfoController {
    private final ReservationService reservationService;
    private final EmployeeService employeeService;
    private final VisitorService visitorService;


    @GetMapping("/save")
    public String saveInfo(@ModelAttribute("visitor")SaveVisitor visitor,Model model,HttpSession session){
        String path = "view/SaveForm";
        visitor.setVisit_date1(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        visitor.setVisit_date2(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        if(session.getAttribute(SessionConst.EMPLOYEE_ID)!=null) {
            long empId = Long.parseLong(session.getAttribute(SessionConst.EMPLOYEE_ID).toString());
            Employee employee = employeeService.findById(empId);
            visitor.setEmployee_name(employee.getEmployee_name());
            visitor.setLoginId(employee.getLoginId());
        }
        List<Employee> employees = employeeService.findAll();
        path = PathChange.setAccessMethod(session.getAttribute(ACCESS_METHOD).toString(),path);

        model.addAttribute("employees",employees);
        return path;
    }


    @PostMapping("/save")
    public String saveInfo(@Valid @ModelAttribute(name = "visitor") SaveVisitor visitor, BindingResult bindingResult,Model model,HttpSession session
    ) {
        String path = "view/SaveForm";

        String wrongPhoneNumber = wrongPhoneNumber(visitor.getPhone_number());
        if(wrongPhoneNumber!=null){
            model.addAttribute("wrongPhoneNumber",wrongPhoneNumber);
        }
        if (bindingResult.hasErrors()) {
            String errorMsg = setErrorMsg(visitor,bindingResult);
            model.addAttribute("errorMsg",errorMsg);
        }
        else {
                path="redirect:/reservation/info/all";
                Long visitorId = visitorService.saveInfo(visitor);
                Visitor visitor1 = visitorService.findOne(visitorId);
                Employee employee = employeeService.findByLoginId(visitor.getLoginId());
                session.setAttribute(SessionConst.EMPLOYEE_ID,employee.getId());
                reservationService.saveInfo(new SaveReservationInfo(visitor1.getId(), employee.getId(), visitor1.getIs_checked()));
                log.info("session empId={}",session.getAttribute(SessionConst.EMPLOYEE_ID));

            }
//        path = PathChange.setAccessMethod(session.getAttribute(ACCESS_METHOD).toString(),path);

            return path;
        }





    private String setErrorMsg(SaveVisitor visitor,BindingResult bindingResult) {
        int cnt = 0;
        StringBuilder builder = new StringBuilder();
        if (!StringUtils.hasText(visitor.getLoginId())) {
            cnt++;
            builder.append("아이디 ");
        }
        if (!StringUtils.hasText(visitor.getPassword())) {
            cnt++;
            builder.append("비밀번호 ");
        }
        if (!StringUtils.hasText(visitor.getPasswordCheck())) {
            cnt++;
            builder.append("비밀번호(확인) ");
        }
        if (!StringUtils.hasText(visitor.getEmployee_name())) {
            cnt++;
            builder.append("이름 ");
        }

        if (!StringUtils.hasText(visitor.getName())) {
            cnt++;
            builder.append("방문자 ");
        }
        if (!StringUtils.hasText(visitor.getPhone_number())) {
            cnt++;
            builder.append("연락처 ");
        }
        if (!StringUtils.hasText(visitor.getPurpose())) {
            cnt++;
            builder.append("방문목적 ");
        }
        if (!StringUtils.hasText(visitor.getVisit_date1().toString())) {
            cnt++;
            builder.append("방문일자 ");
        }
        if(cnt==0){
            return null;
        }
        else{
            String str = "다음 표시된 항목을 확인 후 다시 입력 해주세요 : "+builder.toString();
            bindingResult.reject("globalError",str);
            return str;
        }
    }


    private String wrongPhoneNumber(String phoneNumber){
        String regExp = "^0([0-9]{1,2})([0-9]{7,8})$";
        if(!Pattern.matches(regExp, phoneNumber)){
            return "형식 오류 : '-'을 제외한 형식으로 입력해주세요";
        }
        return null;
    }

    @RequestMapping("/click/{id}")
    public void clickReservation(@ModelAttribute("reservationDTO") ReservationDTO reservationDTO, @PathVariable(name = "id") Long id,
                                 HttpServletRequest request, HttpServletResponse response) throws IOException {
        Reservation reservation = reservationService.findOne(id);
        Long visitor_id = reservation.getVisitor_id();
        Visitor visitor = visitorService.findOne(visitor_id);
        if(visitor.getIs_checked()){
            throw new AlreadyCheckedEx();
        }

        visitorService.updateCheckedInfo(visitor_id);
        reservationService.updateCheckedInfo(id);
        response.sendRedirect(redirectURL(request,response));
    }

    private String redirectURL(HttpServletRequest request, HttpServletResponse response){
        String referURL = request.getHeader("REFERER");
        response.setContentType("text/html; charset=UTF-8");
        referURL=referURL.substring(referURL.indexOf("r")-1);
        return referURL;
    }


    @GetMapping("/{id}")
    public String viewReservationOne(@PathVariable(name = "id") Long id, Model model,HttpSession session) {
        ReservationInfo reservationInfo = reservationService.findInfo(id);
        Long visitorId = reservationInfo.getVisitor_id();
        Visitor visitor = visitorService.findOne(visitorId);
        model.addAttribute("visitor",visitor);
        model.addAttribute("reservation", reservationInfo);
        session.removeAttribute(SessionConst.LOGIN_SUCCESS);

        return "view/ViewOne";
    }

    @GetMapping("/update/{reservationId}")
    public String updateInfo(@PathVariable("reservationId") Long reservationId,@SessionAttribute(ACCESS_METHOD)String access, Model model) {
        String url = "view/UpdateForm";

        Reservation reservation = reservationService.findOne(reservationId);
        Visitor beforeVisitor = visitorService.findOne(reservation.getVisitor_id());
        UpdateVisitor updateVisitor = updateVisitor(beforeVisitor);
        model.addAttribute("reservationId",reservationId);
        model.addAttribute("visitor",updateVisitor);
        AbstractPath path = new AbstractPath() {
            @Override
            protected String call() {
                return "redirect:/m/reservation/info/update/{reservationId}";
            }
        };
        url = path.change(access,url);
        return  url;
    }

    @PostMapping("/update/{reservationId}")
    public String updateInfo(@PathVariable("reservationId") Long reservationId, @Valid @ModelAttribute(name = "visitor") UpdateVisitor updateVisitor, BindingResult bindingResult, HttpSession session){
        int count = updateVisitor.getCount();
        ReviseCountEx(count);
        NoModificationEx(reservationId,updateVisitor);
        String url = "view/UpdateForm";
        if (bindingResult.hasErrors()) {
            url = PathChange.setAccessMethod(session.getAttribute(ACCESS_METHOD).toString(),"view/UpdateForm");
            log.info("url={}",url);
            return "view/UpdateForm";
        }
            visitorService.updateInfo(updateVisitor);
            session.removeAttribute(SessionConst.LOGIN_SUCCESS);
        log.info("url={}",url);

        return  "redirect:/reservation/info/{reservationId}";
    }

    private boolean equalOrNotVisitorObj(Visitor visitor,UpdateVisitor updateVisitor) {
        if(     visitor.getEmployee_name().equals(updateVisitor.getEmployee_name()) &&
                visitor.getPhone_number().equals(updateVisitor.getPhone_number()) &&
                visitor.getName().equals(updateVisitor.getName()) &&
                visitor.getCompany().equals(updateVisitor.getCompany()) &&
                visitor.getWithPerson().equals(updateVisitor.getWithPerson()) &&
                visitor.getPurpose().equals(updateVisitor.getPurpose()) &&
                visitor.getVisit_date1().equals(updateVisitor.getVisit_date1()) &&
                visitor.getVisit_date2().equals(updateVisitor.getVisit_date2())) {
            return true;
        }
        return false;
    }

    private void NoModificationEx(Long id,UpdateVisitor updateVisitor) {
        Reservation reservation=reservationService.findOne(id);
        Visitor visitor = visitorService.findOne(reservation.getVisitor_id());
        if(equalOrNotVisitorObj(visitor, updateVisitor)){
            throw new NoModificationsEx(ErrorMessage.NO_REVISE_MSG);
        }
    }


    private void ReviseCountEx(int count){
        if(count>=5){
            throw new ReviseCountExcess(ErrorMessage.REVISE_COUNT_EXCESS);
        }
    }

    private UpdateVisitor updateVisitor(Visitor visitor) {
        log.info("visitor id={}",visitor.getId());
        return new UpdateVisitor(visitor.getLoginId(),visitor.getPassword(),visitor.getEmployee_name(),visitor.getName(),visitor.getPhone_number(),visitor.getCompany(),visitor.getVisit_date1(),visitor.getVisit_date2()
                ,visitor.getWithPerson(),visitor.getPurpose(),visitor.getWrite_date(),visitor.getCount(),visitor.getIs_checked(),visitor.getChecked_date(),visitor.getId());}


    @GetMapping("/delete/{reservationId}")
    public String deleteInfo(@PathVariable("reservationId") Long reservationId,HttpSession session){
        Long visitorId = reservationService.findOne(reservationId).getVisitor_id();
        reservationService.deleteInfo(reservationId);
        visitorService.deleteInfo(visitorId);
        session.removeAttribute(SessionConst.LOGIN_SUCCESS);

        return "redirect:/reservation/info/all";
    }


}
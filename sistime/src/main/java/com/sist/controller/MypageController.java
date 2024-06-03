package com.sist.controller;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import com.sist.annotation.Controller;
import com.sist.annotation.RequestMapping;
import com.sist.annotation.RequestMethod;
import com.sist.dao.MypageDAO;

import com.sist.domain.MemberDTO;
import com.sist.domain.MypageDTO;
import com.sist.domain.SessionInfo;
import com.sist.servlet.ModelAndView;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;


@Controller
public class MypageController {
	@RequestMapping(value = "/member/pwd", method = RequestMethod.GET)
	public ModelAndView pwdForm(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// 패스워드 확인 폼
		ModelAndView mav = new ModelAndView("member/pwd");
		
		String mode = req.getParameter("mode");
		mav.addObject("mode", mode);

		return mav;
	}
	
	
	@RequestMapping(value = "/member/pwd", method = RequestMethod.POST)
	public ModelAndView pwdSubmit(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// 패스워드 확인
		MypageDAO dao = new MypageDAO();
		HttpSession session = req.getSession();

		try {
			SessionInfo info = (SessionInfo) session.getAttribute("member");

			// DB에서 해당 회원 정보 가져오기
			MemberDTO dto = dao.findByEmail(info.getEmail());
			if (dto == null) {
				session.invalidate();
				return new ModelAndView("redirect:/");
			}

			String userPwd = req.getParameter("userPwd");
			String mode = req.getParameter("mode");
			//System.out.println(userPwd);
			if (! dto.getUserPwd().equals(userPwd)) {
				ModelAndView mav = new ModelAndView("member/pwd");
				
				mav.addObject("mode", mode);
				mav.addObject("message", "패스워드가 일치하지 않습니다.");
				
				return mav;
			}
			//System.out.println("Mode: " + mode);
			//System.out.println("UserPwd from form: " + userPwd);
			//System.out.println("UserPwd from DB: " + dto.getUserPwd());

//			if (mode.equals("delete")) {
//				// 회원탈퇴
//				dao.deleteMember(info.getEmail());
//
//				session.removeAttribute("member");
//				session.invalidate();
//
//				return new ModelAndView("redirect:/");
//			}

			// 회원정보수정 - 마이페이지 폼
			ModelAndView mav = new ModelAndView("member/mypage");
			
			mav.addObject("title", "회원 정보 수정");
			mav.addObject("dto", dto);
			mav.addObject("mode", "update");
			
			return mav;
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
		return new ModelAndView("redirect:/");
	}
	
	// 비밀번호 확인후 마이페이지로 넘어감
	@RequestMapping(value = "/member/mypage", method = RequestMethod.GET)
	public ModelAndView mypageForm(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

	    HttpSession session = req.getSession();
	    SessionInfo info = (SessionInfo) session.getAttribute("member");
	    if (info == null) { // 로그아웃 된 경우
	        return new ModelAndView("redirect:/member/login");
	    }

	    // 회원 정보를 다시 가져옴
	    MypageDAO dao = new MypageDAO();
	    MemberDTO dto = dao.findByEmail(info.getEmail());
    	List<MypageDTO> list = dao.myListpage(info.getEmail());
    	
	    // ModelAndView에 회원 정보를 담아서 화면에 전달
	    ModelAndView mav = new ModelAndView("member/mypage");
	    mav.addObject("dto", dto);
	    mav.addObject("dto2", list);
	    System.out.println(list);
	    
	    String message = (String) session.getAttribute("message");
	    
        if (message != null) {
            mav.addObject("message", message);
            session.removeAttribute("message");
        }
	    
	    return mav;
	}
	
	@RequestMapping(value = "/member/updateName", method = RequestMethod.POST)
	public ModelAndView updateName(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

	    
	    HttpSession session = req.getSession();
	    SessionInfo info = (SessionInfo) session.getAttribute("member");

	    try {
	        // 세션이 없으면 로그인 페이지로 리다이렉트
	        if (info == null) {
	            return new ModelAndView("redirect:/member/login");
	        }

	        // 사용자가 입력한 새 이름 가져와서 DTO에 설정
	        MemberDTO dto = new MemberDTO();
	        dto.setEmail(info.getEmail());
	        dto.setUserName(req.getParameter("newName")); // 새로운 이름 설정

	        
	        MypageDAO dao = new MypageDAO();
	        dao.updateName(dto); // 

	        
	        session.setAttribute("message", "이름이 성공적으로 변경되었습니다.");
	        return new ModelAndView("redirect:/member/mypage");
	    } catch (SQLException e) {
	        e.printStackTrace();
	        session.setAttribute("message", "이름 업데이트 중 오류가 발생했습니다.");
	    }

	    
	    return new ModelAndView("redirect:/member/mypage");
	}

	
	
	// 닉네임 수정
	@RequestMapping(value = "/member/updateNickname", method = RequestMethod.POST)
	public ModelAndView updateNickname(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

	    HttpSession session = req.getSession();
	    try {
	    	SessionInfo info = (SessionInfo) session.getAttribute("member");
			if (info == null) { // 로그아웃 된 경우
				return new ModelAndView("redirect:/member/login");
			}
			MemberDTO dto = new MemberDTO();
			MypageDAO dao = new MypageDAO();
			dto.setEmail(info.getEmail());
		    dto.setNickname(req.getParameter("newNickname"));
		    dao.updateNickname(dto);
		    
		    
		} catch (SQLException e) {
			e.printStackTrace();
		}

	    // 다시 마이페이지로 리다이렉트
	    ModelAndView mav = new ModelAndView("redirect:/member/mypage");
	    return mav;
	}
	
	// 비밀번호 변경
	@RequestMapping(value = "/member/updatePassword", method = RequestMethod.POST)
	public ModelAndView updatePassword(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
	    HttpSession session = req.getSession();
	    SessionInfo info = (SessionInfo) session.getAttribute("member");

	    if (info == null) { // 로그아웃 된 경우
	        return new ModelAndView("redirect:/member/login");
	    }

	    try {
	        String currentPassword = req.getParameter("currentPassword");
	        String newPassword = req.getParameter("newPassword");
	        String confirmPassword = req.getParameter("confirmPassword");

	        if (!newPassword.equals(confirmPassword)) {
	            // 새 비밀번호와 확인 비밀번호가 일치하지 않을 경우 처리
	            session.setAttribute("message", "새 비밀번호가 일치하지 않습니다.");
	            return new ModelAndView("redirect:/member/mypage");
	        }

	        MypageDAO dao = new MypageDAO();
	        MemberDTO dto = dao.findByEmail(info.getEmail());

	        // 현재 비밀번호 확인
	        if (!dto.getUserPwd().equals(currentPassword)) {
	            // 현재 비밀번호가 일치하지 않을 경우 처리
	            session.setAttribute("message", "현재 비밀번호가 일치하지 않습니다.");
	            return new ModelAndView("redirect:/member/mypage");
	        }

	        // 비밀번호 업데이트
	        dto.setUserPwd(newPassword);
	        dao.updatePwd(dto);
	        session.setAttribute("message", "비밀번호가 성공적으로 변경되었습니다.");

	    } catch (SQLException e) {
	        e.printStackTrace();
	        session.setAttribute("message", "비밀번호 변경 중 오류가 발생했습니다.");
	    }

	    return new ModelAndView("redirect:/member/mypage");
	}
	// 내정보 수정
	@RequestMapping(value = "/member/updateMember", method = RequestMethod.POST)
	public ModelAndView updateMember(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

	    // 세션에서 회원 정보 가져오기
	    HttpSession session = req.getSession();
	    SessionInfo info = (SessionInfo) session.getAttribute("member");

	    try {
	    	// 세션이 없으면 로그인 페이지로 리다이렉트
	    	if (info == null) {
	        return new ModelAndView("redirect:/member/login");
	    	}

	    	// 사용자가 입력한 내용을 가져와서 DTO에 설정
		    MemberDTO dto = new MemberDTO();
		    dto.setEmail(info.getEmail());
		    dto.setBirth(req.getParameter("birth"));
		    dto.setTel(req.getParameter("tel"));
		    dto.setZip(req.getParameter("zip"));
		    dto.setAddr1(req.getParameter("addr1"));
		    dto.setAddr2(req.getParameter("addr2"));

	    
		    MypageDAO dao = new MypageDAO();
		    
		    dao.updateMember(dto); // member2 테이블 업데이트
		    session.setAttribute("message", "업데이트 성공.");
		    return new ModelAndView("redirect:/member/mypage");
	    
	    } catch (SQLException e) {
	        e.printStackTrace();
	        session.setAttribute("message", "사용자 업데이트중 오류가 발생했습니다.");
	    }
	    return new ModelAndView("redirect:/member/mypage");
	}
	
	 @RequestMapping(value = "/member/deletemember", method = RequestMethod.POST)
	    public ModelAndView deleteMember(HttpServletRequest req, HttpServletResponse resp) {
	        // 세션에서 회원 정보 가져오기
	        HttpSession session = req.getSession();
	        SessionInfo info = (SessionInfo) session.getAttribute("member");

	        try {
	            // 세션이 없으면 로그인 페이지로 리다이렉트
	            if (info == null) {
	                return new ModelAndView("redirect:/member/login");
	            }

	            // 회원 정보 삭제
	            String email = info.getEmail();
	            MypageDAO dao = new MypageDAO();
	            dao.deleteMember(email);

	           
	            session.invalidate();

	            // 회원탈퇴 완료 메시지를 세션에 저장
	            session.setAttribute("message", "회원탈퇴가 완료되었습니다.");

	            // 홈 화면으로 리다이렉트
	            return new ModelAndView("redirect:/");
	        } catch (SQLException e) {
	            e.printStackTrace();
	            // 오류 메시지를 세션에 저장
	            session.setAttribute("message", "회원탈퇴 중 오류가 발생했습니다.");
	        }
	        // 홈 화면으로 리다이렉트
	        return new ModelAndView("redirect:/");
	    }
}
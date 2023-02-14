package com.hosinsa.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.hosinsa.domain.MemberVO;
import com.hosinsa.service.MemberService;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j;

@Controller
@Log4j
@SessionAttributes("member")
@RequestMapping("/member/*")
@AllArgsConstructor
public class MemberController {

	private MemberService memberService;
	
	@GetMapping("/agree")
	public void agreeGET( ) {
		log.info("Agree====>");
	}
	
	// 로그인
	@GetMapping("/login")
	public void login() {
		log.info("login/get====");
	}
	
	@PostMapping("/login")
	public String logPOST(MemberVO member, RedirectAttributes rttr, Model model) throws Exception {
		log.info("loginPOST===");
		log.info("loginPOST : " + member);
		
		MemberVO vo = memberService.memberLogin(member);
		
		if (vo == null) {
			int result = 0;
			rttr.addFlashAttribute("result", result);
			return "redirect:/member/login";
		} else {
			model.addAttribute("member", vo);
			return "redirect:/";
		}
		
	} // 로그인 끝
	
	// 로그아웃
	@GetMapping("/logout")
	public String logoutGet(SessionStatus session) {
		log.info("logout====");
		session.setComplete();
        
        return "redirect:/";     
	}
	
	@PostMapping("/modify")
	public String modifyPOST(MemberVO member, RedirectAttributes rttr) {
		log.info("modifyPOST : " + member);
		
		if (memberService.modify(member)) {
			rttr.addFlashAttribute("result", "success");
		} 
		return "redirect:/member/manager";
	}
	
	@PostMapping("/remove")
	public String remove(@RequestParam("id") String id, RedirectAttributes rttr) {
		log.info("remove----" + id);
		
		if (memberService.remove(id)) {
			rttr.addFlashAttribute("result", "seccess");
		}
		return "redirect:/member/manager";
	}
	
	// 회원가입
	@GetMapping("/join")
	public void join() {
		log.info("join====");
	}
	// 회원가입
	@PostMapping("/join")
	public String joinPOST(MemberVO member, RedirectAttributes rttr) {
		log.info("joinPOST====" + member);
		memberService.join(member);
		rttr.addFlashAttribute("result", member.getId());
		return "redirect:/";
	}
	// 아이디 중복체크
	@PostMapping("/memberIdChk")
	@ResponseBody // join.jsp로 메서드의 결과 반환
	public String memberIdChkPOST(String memberId) {
		log.info("memberIdChk====");
		int result = memberService.idCheck(memberId);
		
		log.info("결과값 = " + result);
		if(result != 0) {
			return "fail";	// 중복 아이디가 존재
		} else {
			return "success";	// 중복 아이디 x
		}
	}
	// 닉네임 중복체크
	@PostMapping("/memberNickChk")
	@ResponseBody
	public String memberNicknamePOST(String memberNickname) {
		log.info("memberNickChk====");
		
		int result = memberService.nickCheck(memberNickname);
		log.info("결과값 : " + result);
		if (result != 0) {
			return "fail";	// 중복 닉네임이 존재
		} else {
			return "success";	// 중복 닉네임 x
		}
	}
	// 추천인 확인
	@PostMapping("/memberIntroChk")
	@ResponseBody
	public String memberIntroducerPOST(String memberIntroducer) {
		log.info("memberIntroChk");
		
		int result = memberService.introCheck(memberIntroducer);
		log.info("결과값 : " + result);
		if (result != 0) {
			return "success"; // 추천인 있음
		} else {
			return "fail";	// 추천인 없음 X
		}
	}
}

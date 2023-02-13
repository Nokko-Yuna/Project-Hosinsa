package com.hosinsa.controller;

import java.io.File;

import javax.servlet.ServletContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.hosinsa.domain.Criteria;
import com.hosinsa.domain.MemberVO;
import com.hosinsa.domain.PageDTO;
import com.hosinsa.domain.ProductVO;
import com.hosinsa.service.AdminService;
import com.hosinsa.service.MainService;

@Controller
@RequestMapping("/admin/*")
public class AdminController {
	
	@Autowired
	private MainService mainService;
	
	@Autowired
	private AdminService adminService;
	
	@GetMapping("/member")
	public void adminMemberList(Criteria cri, Model model) {
		int total = adminService.getTotal(cri);
		model.addAttribute("list", adminService.getList(cri));
		model.addAttribute("pageMaker", new PageDTO(cri, total));
	}
	
	@GetMapping("/memberModify")
	public void memberModifyGET(@RequestParam("id") String id, @ModelAttribute("cri") Criteria cri, Model model) {
		model.addAttribute("member", adminService.get(id)); 	
	}
	
	@PostMapping("/memberModify")
	public String memberModifyPOST(MemberVO member, @ModelAttribute("cri") Criteria cri, RedirectAttributes rttr){
		if (adminService.memberModify(member)) {
			rttr.addFlashAttribute("result", "success");
		}
		
		rttr.addAttribute("pageNum", cri.getPageNum());
		rttr.addAttribute("amount", cri.getAmount());
		
		return "redirect:/admin/member";
	}
	
	/*
	 * @GetMapping(value = "/search/member") public String searchMember(String
	 * keyword, Model model, Criteria cri) { int total = 0; total =
	 * adminService.searchMember(cri); }
	 */
	
	@GetMapping("/product")
	public void adminProList(Model model,Criteria cri){
		int total = mainService.getTotalCountView(cri);
		model.addAttribute("pageMaker", new PageDTO(cri, total));
		cri.setAmount(10);
		model.addAttribute("product",mainService.getListProview(cri));
	}
	
	@GetMapping("/category")
	public String adminCategory(Model model,Criteria cri,@RequestParam("category") String category) {
		model.addAttribute("category",category);
		cri.setAmount(10);
		int total = mainService.getTotalCount(cri);
		model.addAttribute("pageMaker", new PageDTO(cri, total));
		model.addAttribute("product",mainService.getListCategory(cri));
		
		return "admin/product";
	}
	
	@GetMapping("/modify")
	public void adminModifyForm(Model model,int pronum) {
		model.addAttribute("product",mainService.getProductByPronum(pronum));		
	}
	
	@PostMapping("/modify")
	public String adminModify(ProductVO vo,MultipartFile uploadFile,RedirectAttributes rttr) {
		
		//==========================배포 전 경로 Works3로 수정해 주세요.
		File saveFile = new File("C:\\Works3\\Project-Hosinsa\\Hosinsa\\src\\main\\webapp\\resources\\productImg\\"+vo.getCategory(),vo.getProimg().substring(vo.getProimg().lastIndexOf("/")+1));
		
		try {
			uploadFile.transferTo(saveFile);
		}catch(Exception e){
			e.printStackTrace();
		}
		
		if(adminService.modify(vo)) {
			rttr.addFlashAttribute("modify","success");
		}		
		return "redirect:/admin/product"; 
	}
	
	@GetMapping("/delete")
	public String adminRemove(int pronum,RedirectAttributes rttr) {
		
		//파일 삭제
		//==========================배포 전 경로 Works3로 수정해 주세요.
		ProductVO vo = mainService.getProductByPronum(pronum);
		String path = "C:\\Works3\\Project-Hosinsa\\Hosinsa\\src\\main\\webapp\\resources\\productImg\\"+vo.getCategory();
		
		//현재 게시판에 존재하는 파일객체를 만듬
		File file = new File(path + "\\" + vo.getProimg().substring(vo.getProimg().lastIndexOf("/")+1));
				
		if(file.exists()) { // 파일이 존재하면
			file.delete(); // 파일 삭제	
		}
		
		if(adminService.remove(pronum)) {
			rttr.addFlashAttribute("remove","success");
		}	
		return "redirect:/admin/product";
	}
	
	@GetMapping("/register")
	public void adminRegisterForm() {
	}
	
	@PostMapping("/register")
	public String adminResister(MultipartFile uploadFile,ProductVO vo,RedirectAttributes rttr) {
		
		String category = vo.getCategory();
		
		//==========================배포 전 경로 Works3로 수정해 주세요.
		String uploadFolder = "C:\\Works3\\Project-Hosinsa\\Hosinsa\\src\\main\\webapp\\resources\\productImg\\"+category;
		String catecode="";
		switch(category) {
			case "상의" : catecode = "001";
					break;
			case "아우터" : catecode = "002";
					break;
			case "바지" : catecode = "003";
					break;
			case "가방" : catecode = "004";
					break;
			case "원피스" : catecode = "020";
					break;
			case "스커트" : catecode = "022";
					break;
			case "신발" : catecode = "005";
					break;
			case "시계" : catecode = "006";
					break;
			case "모자" : catecode = "007";
					break;
			case "액세서리" : catecode = "011";
					break;
		}
		String fileName = catecode+"_"+vo.getPronum()+".jpg";
		File saveFile = new File(uploadFolder,fileName);
		
		try {
			uploadFile.transferTo(saveFile);
		}catch(Exception e){
			e.printStackTrace();
		}
		
		vo.setProimg("../../resources/productImg/"+category+"/"+fileName);
		
		if(adminService.register(vo)) {
			rttr.addFlashAttribute("register","success");
			rttr.addFlashAttribute("newPronum",vo.getPronum());
		}
		return "redirect:/admin/product";
	}
	
	@ResponseBody
	@GetMapping("/register/checkPronum")	
	public int checkPronum(@RequestParam("pronum") int pronum) {		
		return adminService.checkPronum(pronum);
	}
	
}

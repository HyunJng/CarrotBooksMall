package com.carrot.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.carrot.domain.MemberVO;
import com.carrot.domain.OrderPageVO;
import com.carrot.domain.OrderVO;
import com.carrot.service.MemberService;
import com.carrot.service.OrderService;

@Controller
public class OrderController {
	private static final Logger logger = LoggerFactory.getLogger(OrderController.class);

	@Autowired private OrderService orderService;
	@Autowired private MemberService memberService;
	
	@GetMapping("/order/{memberId}")
	public String orderPageGet(@PathVariable("memberId") int memberId, OrderPageVO order, Model model) {
		logger.info("order페이지 진입");
		MemberVO member = new MemberVO();
		member.setMemberId(memberId);
		
		model.addAttribute("orderList", orderService.getGoodsInfo(order.getOrders()));
		model.addAttribute("memberInfo", memberService.findMemberbyId(member));
		return "/order";
	}
	
	@PostMapping("/order")
	public String orderPagePost(OrderVO order, HttpServletRequest req) {
		logger.info("orderPagePost 진입");

		orderService.order(order);
		
		MemberVO member = new MemberVO();
		member.setMemberId(order.getMemberId());
		
		HttpSession session = req.getSession();
		
		try {
			MemberVO memberLogin = memberService.findMemberbyId(member);
			session.setAttribute("member", memberLogin);
		} catch(Exception e) {
			e.printStackTrace();
		}
		
		return "redirect:/main";
	}
}

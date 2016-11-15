package org.launchcode.blogz.controllers;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.launchcode.blogz.models.User;
import org.launchcode.blogz.models.dao.PostDao;
import org.launchcode.blogz.models.dao.UserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class AuthenticationController extends AbstractController {

	
	@RequestMapping(value = "/signup", method = RequestMethod.GET)
	public String signupForm() {
		return "signup";
	}
	
	@RequestMapping(value = "/signup", method = RequestMethod.POST)
	public String signup(HttpServletRequest request, Model model) {
		
		// TODO - implement signup
		//get parameters
		String username = request.getParameter("username");
		String password = request.getParameter("password");
		String verify = request.getParameter("verify");
		
		//validate parameters
		if (!password.equals(verify))
		{
			String verify_error = "The passwords don't match";
			model.addAttribute("verify_error", verify_error);
			return "signup";
		}
		if (!User.isValidUsername(username))
		{
			String username_error = "Invalid username.";
			model.addAttribute("username_error", username_error);
			return "signup";
		}
		if (userDao.findByUsername(username) != null)
		{
			String username_error = "That username already exists.";
			model.addAttribute("username_error", username_error);
			return "signup";
		}
		if (!User.isValidPassword(password))
		{
			String password_error = "Invalid password.";
			model.addAttribute("password_error", password_error);
			return "signup";
		}

		User newUser = new User(username, password);
		
		userDao.save(newUser);
				
		//insert user into session --
		HttpSession thisSession = request.getSession();
		this.setUserInSession(thisSession, newUser);
		return "redirect:blog/newpost";
	}
	
	@RequestMapping(value = "/login", method = RequestMethod.GET)
	public String loginForm() {
		return "login";
	}
	
	@RequestMapping(value = "/login", method = RequestMethod.POST)
	public String login(HttpServletRequest request, Model model) {

		// TODO - implement login
		// get input
		String username = request.getParameter("username");
		String password = request.getParameter("password");
		//validate
		
		User newUser = userDao.findByUsername(username);

		if (userDao.findByUsername(username) == null)
		{
			String error = "Invalid username.";
			model.addAttribute("error", error);
			return "login";	
		}
		//How does the encoder work? How can I compare the PWs if they hash different every time?
		if (newUser.isMatchingPassword(password))
		{
			HttpSession thisSession = request.getSession();
			this.setUserInSession(thisSession, newUser);
			return "redirect:blog/newpost";
		}
		else
		{
			String error = "Invalid password.";
			model.addAttribute("error", error);
			return "login";
		}

		
	}
	
	@RequestMapping(value = "/logout", method = RequestMethod.GET)
	public String logout(HttpServletRequest request){
        request.getSession().invalidate();
		return "redirect:/";
	}
	
	
}

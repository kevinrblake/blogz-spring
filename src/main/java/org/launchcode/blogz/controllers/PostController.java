package org.launchcode.blogz.controllers;

import java.io.File;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.launchcode.blogz.models.Post;
import org.launchcode.blogz.models.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class PostController extends AbstractController {

	@RequestMapping(value = "/blog/newpost", method = RequestMethod.GET)
	public String newPostForm() {
		return "newpost";
	}
	
	@RequestMapping(value = "/blog/newpost", method = RequestMethod.POST)
	public String newPost(HttpServletRequest request, Model model) {
		
		// TODO - implement newPost
		HttpSession thisSession = request.getSession();
		String title = request.getParameter("title");
		String body = request.getParameter("body");
		User user = this.getUserFromSession(thisSession);
			if (title == null)
			{
				String error = "Posts require a title";
				model.addAttribute("error", error);
				return "newpost";
			}
			if (body == null)
			{
				String error = "Posts require a body";
				model.addAttribute("error", error);
				return "newpost";
			}
		Post newPost = new Post(title, body, user);
		postDao.save(newPost);
		String thisNewPost = "redirect:" + user.getUsername() + "/" + newPost.getUid(); 

		return thisNewPost; // TODO - this redirect should go to the new post's page  		
	}
	
	@RequestMapping(value = "/blog/{username}/{uid}", method = RequestMethod.GET)
	public String singlePost(@PathVariable String username, @PathVariable int uid, Model model) {
		
		Post post = postDao.findByUid(uid);
		model.addAttribute("post", post);
		return "post";
	}
	
	@RequestMapping(value = "/blog/{username}", method = RequestMethod.GET)
	public String userPosts(@PathVariable String username, Model model) {
		
		// TODO - implement userPosts
		User user = userDao.findByUsername(username);
		List<Post> posts = user.getPosts();
		model.addAttribute("posts", posts);
		
		return "blog";
	}
	
}

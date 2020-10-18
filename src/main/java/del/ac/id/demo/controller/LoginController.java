package del.ac.id.demo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import del.ac.id.demo.jpa.*;

@RestController
public class LoginController {
	@Autowired UserRepository userRepository;
	@Autowired ItemRepository itemRepository;

	public LoginController(UserRepository userRepository) {
		super();
		this.userRepository = userRepository;
	}
	
	@GetMapping("/login")
	public ModelAndView login()
	{
		ModelAndView mv=new ModelAndView("login");
		mv.addObject("user",new User());
		return mv;
	}
	
	@RequestMapping(value="/login" ,method=RequestMethod.POST)
	public ModelAndView loginSubmit(@ModelAttribute User user,BindingResult bindingResult)
	{
		ModelAndView mv = new ModelAndView();
		User exist = userRepository.findByUsername(user.getUsername());
		
		if(exist == null) {
			bindingResult.rejectValue("email", "Username not found");
		}
		
		else {
			if(exist.getPassword().equals(user.getPassword())) {
				if(exist.getRole().equals("admin") ) {
					mv=new ModelAndView("redirect:/admin");
				}
				
				else if(exist.getRole().equals("user")) {
					mv.setViewName("redirect:/item");
				}
			}
			
			else {
				bindingResult.rejectValue("password", "Password incorrect");
			}
		}
		
		return mv;
	}
	
	@RequestMapping("/admin")
	public ModelAndView item() {
		List<Item> items = itemRepository.findAll();
		ModelAndView mv = new ModelAndView("admin");
		mv.addObject("items",items);
		return mv;
		}

	
	@GetMapping("/registration")
	public ModelAndView registration()
	{
		ModelAndView mv=new ModelAndView("registration");
		mv.addObject("user",new User());
		return mv;
	}
	@RequestMapping(value="/registration" ,method=RequestMethod.POST)
	public ModelAndView registrationSubmit(@ModelAttribute User user,BindingResult bindingResult)
	{
		if(bindingResult.hasErrors())
		{
			System.out.println("Error");
		}
		userRepository.save(user);
		return new ModelAndView("redirect:/login");
	}
	
	
}

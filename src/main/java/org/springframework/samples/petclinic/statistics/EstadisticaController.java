package org.springframework.samples.petclinic.statistics;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.user.UserService;
import org.springframework.samples.petclinic.web.LoggedUserController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class EstadisticaController {

    @Autowired
    private final EstadisticaService estadisticaService;

    @Autowired
    LoggedUserController currentUser;

    @Autowired
    UserService userService;


    @Autowired
    public EstadisticaController(EstadisticaService estadisticaService) {
        this.estadisticaService = estadisticaService;
    }

    @InitBinder
	public void setAllowedFields(WebDataBinder dataBinder) {
		dataBinder.setDisallowedFields("id");
	}

    @GetMapping(value = "perfil/estadistica/{username}")
    public ModelAndView getStatistics(@RequestParam("username") String username) {
        ModelAndView mav = new ModelAndView("estadistica");
		Estadistica estadisticaToShow = this.estadisticaService.findStatisticsByUsername(username).get();
		mav.addObject("estadistica", estadisticaToShow);
		return mav;
    }

    @GetMapping(value = "**/estadistica")
    public ModelAndView getStatistics() {
        Authentication auth =SecurityContextHolder.getContext().getAuthentication();
		String username = "";
		if (auth!=null) {
			if (auth.isAuthenticated() && auth.getPrincipal() instanceof org.springframework.security.core.userdetails.User) {
				org.springframework.security.core.userdetails.User userLogged = (org.springframework.security.core.userdetails.User) auth.getPrincipal();
				username = userLogged.getUsername();
			}
		}
        
		ModelAndView mav = new ModelAndView("estadisticas/estadisticasUser");
		Estadistica estadisticaToShow = new Estadistica();
        if(!this.estadisticaService.findStatisticsByUsername(username).isPresent())
        estadisticaService.newStatistics(userService.findUser(username).get());
        
        estadisticaToShow = this.estadisticaService.findStatisticsByUsername(username).get();
        mav.addObject("estadistica", estadisticaToShow);
		return mav;
    }

    
}

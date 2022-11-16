/*
 * Copyright 2002-2013 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.springframework.samples.petclinic.user;

import java.util.Collection;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.jugador.Jugador;
import org.springframework.samples.petclinic.jugador.JugadorService;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

/**
 * @author Ignacio Warleta
 * @author Gabriel Vacaro
 */
@Controller
public class UserController {

	private static final String USUARIOS_LISTING_VIEW = "/users/UsersListing";

	private UserService userService;

	@Autowired
	public UserController(UserService userService){
		this.userService = userService;
	}

	@GetMapping(value = "/users/find")
	public String initFindForm(Map<String, Object> model) {
		model.put("user", new User());
		return "users/findUsers";
	}

	@GetMapping(value = "/users")
	public String processFindForm(User user, BindingResult result, Map<String, Object> model) {

		// allow parameterless GET request for /owners to return all records
		if (user.getNombre() == null) {
			user.setNombre(""); // empty string signifies broadest possible search
		}

		// find users by last name
		Collection<User> results = this.userService.findOwnerByName(user.getNombre());
		if (results.isEmpty()) {
			// no users found
			result.rejectValue("nombre", "notFound", "not found");
			return "users/findUsers";
		}
		else if (results.size() == 1) {
			// 1 owner found
			user = results.iterator().next();
			return "redirect:/users/" + user.getUsername();
		}
		else {
			// multiple owners found
			model.put("selections", results);
			return "users/UsersListing";
		}
	}

	@GetMapping("/users/{userId}")
	public ModelAndView showOwner(@PathVariable("username") int ownerId) {
		ModelAndView mav = new ModelAndView("users/");
		mav.addObject(this.userService.findUser("username"));
		return mav;
	}

	@GetMapping("/users/")
    public ModelAndView showPartidas(){
        ModelAndView result = new ModelAndView(USUARIOS_LISTING_VIEW);
        result.addObject("usuarios", userService.getUsuarios());
        return result;
    }

	private static final String VIEWS_JUGADOR_CREATE_FORM = "jugadores/createOrUpdateJugadorForm";


	@InitBinder
	public void setAllowedFields(WebDataBinder dataBinder) {
		dataBinder.setDisallowedFields("id");
	}

	@GetMapping(value = "/users/new")
	public String initCreationForm(Map<String, Object> model) {
		Jugador jugador = new Jugador();
		model.put("jugador", jugador);
		return VIEWS_JUGADOR_CREATE_FORM;
	}

	@PostMapping(value = "/users/new")
	public String processCreationForm(@Valid User user, BindingResult result) {
		if (result.hasErrors()) {
			return VIEWS_JUGADOR_CREATE_FORM;
		}
		else {
			//creating owner, user, and authority
			this.userService.saveUser(user);
			return "redirect:/";
		}
	}


}
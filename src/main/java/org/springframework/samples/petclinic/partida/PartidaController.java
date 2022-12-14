package org.springframework.samples.petclinic.partida;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.celda.CeldaEspecialService;
import org.springframework.samples.petclinic.celda.CeldaService;
import org.springframework.samples.petclinic.jugador.Jugador;
import org.springframework.samples.petclinic.jugador.JugadorService;
import org.springframework.samples.petclinic.partida.enums.Fase;
import org.springframework.samples.petclinic.partida.enums.NumRondas;
import org.springframework.samples.petclinic.tablero.Tablero;
import org.springframework.samples.petclinic.tablero.TableroService;
import org.springframework.samples.petclinic.user.User;
import org.springframework.samples.petclinic.user.UserService;
import org.springframework.samples.petclinic.web.LoggedUserController;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/partida")
public class PartidaController {

    PartidaService partidaService;
    UserService userService;
    JugadorService jugadorService;
    TableroService tableroService;
    CeldaService celdaService;
    CeldaEspecialService celdaEspecialService;

    private final String MIS_PARTIDAS_LISTING_VIEW = "/partidas/MisPartidasListing";
    private final String PARTIDAS_LISTING_VIEW = "/partidas/PartidasListing";
    private final String PARTIDAS_FORM = "/partidas/createOrUpdatePartidaForm";
    private final String PARTIDAS_ACTIVAS_VIEW = "partidas/partidasActivasListing";
    private final String LOBBY_VIEW = "partidas/Lobby";
    private final String VIEWS_TABLERO = "tablero/showTablero";

    @Autowired
    LoggedUserController currentUser;

    @Autowired
    public PartidaController(PartidaService partidaService, UserService userService, JugadorService jugadorService,
            TableroService tableroService, CeldaService celdaService, CeldaEspecialService celdaEspecialService) {
        this.partidaService = partidaService;
        this.userService = userService;
        this.jugadorService = jugadorService;
        this.tableroService = tableroService;
        this.celdaService = celdaService;
        this.celdaEspecialService = celdaEspecialService;
    }

    @GetMapping("/partidas")
    public ModelAndView showPartidas(){
        ModelAndView res = new ModelAndView(PARTIDAS_LISTING_VIEW);
        res.addObject("partidas", partidaService.getPartidas());
        return res;
    }


    @Transactional(readOnly = true)
    @GetMapping("/misPartidas")
    public ModelAndView showMisPartidas(){

        ModelAndView res = new ModelAndView(MIS_PARTIDAS_LISTING_VIEW);
        String currentUsername = currentUser.returnLoggedUserName();
        User userLogged = userService.findUser(currentUsername).get();
        List<Partida> partidas = new ArrayList<Partida>();

        for (Partida partida: partidaService.getPartidas()) {
            if (partida.getUsersOnTheGame().contains(userLogged)) partidas.add(partida);
        }

        res.addObject("partidas", partidas);
        return res;
    }

    @Transactional(readOnly = true)
    @GetMapping("/partidasActivas")
    public ModelAndView partidasActivas(){
        
        ModelAndView res = new ModelAndView(PARTIDAS_ACTIVAS_VIEW);
        List<Partida> partidas = new ArrayList<Partida>();

        for (Partida partida: partidaService.getPartidas()){
            if (partida.getFaseActual() == Fase.INICIANDO) {
                if (partida.getUsersOnTheGame().size() < 3) partidas.add(partida);
            }
        }

        res.addObject("partidas", partidas);
        return res;
    }

    @Transactional()
    @GetMapping("/{id}/delete")
    public ModelAndView deletePartida(@PathVariable int id){
        partidaService.deletePartidaById(id);        
        return showPartidas();
    }

    @Transactional()
    @GetMapping("/delete/{id}")
    public String deletePartidaDesdeLobby(@PathVariable int id) {
        List<Jugador> jugadoresInGame = partidaService.getPlayersInAGame(id);
        for (Jugador j: jugadoresInGame) {
            jugadorService.deleteJugadorById(j.getId());
        }
        partidaService.deletePartidaById(id);
        return "redirect:/";
    }

    @Transactional()
    @GetMapping("/leave/{id}")
    public String leaveGame(@PathVariable int id) {
        List<Jugador> jugadoresInGame = partidaService.getPlayersInAGame(id);
        Partida game = partidaService.findPartidaById(id);
        if (jugadoresInGame.size() == 1) {
            for (Jugador j: jugadoresInGame) {
                jugadorService.deleteJugadorById(j.getId());
            }
            partidaService.deletePartidaById(id);
        } else if (jugadoresInGame.size() == 2) {
            if (game.getUser0().getUsername() == partidaService.getUserLogged().getUsername()) {
                User user1 = userService.findUser(game.getUser1().getUsername()).get();
                game.setUser0(user1);
                game.setUser1(null);
                game.setUser2(null);
                Integer idUser0 = jugadoresInGame.get(0).getId();
                jugadoresInGame.remove(0);
                jugadorService.deleteJugadorById(idUser0);
            } else {
                game.setUser1(null);
                Integer idUser1 = jugadoresInGame.get(1).getId();
                jugadoresInGame.remove(1);
                jugadorService.deleteJugadorById(idUser1);
            }
        } else if (jugadoresInGame.size() == 3) {
            if (game.getUser0().getUsername() == partidaService.getUserLogged().getUsername()) {
                User user1 = userService.findUser(game.getUser1().getUsername()).get();
                User user2 = userService.findUser(game.getUser2().getUsername()).get();
                game.setUser0(user1);
                game.setUser1(user2);
                game.setUser2(null);
                Integer idUser0 = jugadoresInGame.get(0).getId();
                jugadoresInGame.remove(0);
                jugadorService.deleteJugadorById(idUser0);
            } else if (game.getUser1().getUsername() == partidaService.getUserLogged().getUsername()) {
                User user2 = userService.findUser(game.getUser2().getUsername()).get();
                game.setUser1(user2);
                game.setUser2(null);
                Integer idUser1 = jugadoresInGame.get(1).getId();
                jugadoresInGame.remove(1);
                jugadorService.deleteJugadorById(idUser1);
            } else {
                game.setUser2(null);
                Integer idUser2 = jugadoresInGame.get(2).getId();
                jugadoresInGame.remove(2);
                jugadorService.deleteJugadorById(idUser2);
            }
        }
        return "redirect:/";
    }

    @Transactional(readOnly = true)
    @GetMapping(value = "/create")
	public String initCreationForm(Map<String, Object> model) {
		Partida partida = new Partida();
		model.put("partida", partida);
        model.put("numRondas", Arrays.asList(NumRondas.values()));
		return PARTIDAS_FORM;
	}

    @Transactional()
	@PostMapping(value = "/create")
	public String processCreationForm(@Valid Partida partida, BindingResult result, Map<String, Object> model) throws Exception{
		if (result.hasErrors()) {
			return PARTIDAS_FORM;
		}
		else {
			this.partidaService.save(partida);
            this.jugadorService.save(partidaService.getUserLogged(), partida);
            Integer id_sala = partida.getId();
			return "redirect:../lobby/" + id_sala;
		}
	}

    @Transactional()
    @GetMapping("lobby/{id_sala}")
    public String goToLobby(@PathVariable("id_sala") Integer id_sala, Map<String, Object> model, 
            HttpServletResponse response) throws Exception{
        
        // Refresco de p??gina.
        response.addHeader("Refresh", "5");

        //ModelAndView res = new ModelAndView(LOBBY_VIEW); // Muestra el lobby de la partida.
        Partida partida = partidaService.findPartidaById(id_sala); // Obtiene la partida que se acaba de crear.

        // Para los usuarios que deseen unirse a una partida.
        // Si el usuario no se encuentra en la partida...
        if (!partida.getUsersOnTheGame().contains(partidaService.getUserLogged())) {
            jugadorService.save(partidaService.getUserLogged(), partida); // Se guarda un nuevo jugador relacionado al usuario que se une a la partida.

            // Si la partida tiene 1 solo jugador entonces el nuevo usuario ser?? el usuario 2 de la partida.
            if (partida.getUser1() == null) partida.setUser1(partidaService.getUserLogged());
            // Si la partida tiene 2 jugadores entonces el nuevo usuario ser?? el usuario 3 de la partida.
            else {
                if (partida.getUser2() == null) partida.setUser2(partidaService.getUserLogged());
            } 
        }

        model.put("partida", partida);
        model.put("logged", partidaService.getUserLogged().getUsername());
         
        return LOBBY_VIEW;
    }

    @Transactional()
    @GetMapping(value = "tablero/create/{id}")
    public String iniciarPartida(@PathVariable int id, Map<String, Object> model) {
        Partida partida = partidaService.findPartidaById(id);
        tableroService.save(partida);
        Tablero tablero = tableroService.findAll().stream().filter(x -> x.getPartida().equals(partida)).findFirst().get();
        partidaService.iniciarPartida(id, tablero);
        return "redirect:/partida/tablero/" + partida.getId();
    }

    @Transactional()
    @GetMapping(value = "tablero/{id}")
    public String juego(@PathVariable int id, Map<String,Object> model, HttpServletResponse response) {

        response.addHeader("Refresh", "5");

        Partida partida = partidaService.findPartidaById(id);
        Tablero tablero = tableroService.findAll().stream().filter(x -> x.getPartida().equals(partida)).findFirst().get();

        Jugador jugador1 = jugadorService.findJugadorInAGame(partida.getUser0().getUsername(), partida);
        model.put("jugador1", jugador1);
        Jugador jugador2 = jugadorService.findJugadorInAGame(partida.getUser1().getUsername(), partida);
        model.put("jugador2", jugador2);

        if (partida.getUser2() != null) {
            Jugador jugador3 = jugadorService.findJugadorInAGame(partida.getUser2().getUsername(), partida);
            model.put("jugador3", jugador3);
        }

        model.put("partida", partida);
        model.put("tablero", tablero);
        model.put("actual", partidaService.getUserLogged().getUsername());
        model.put("fase1", Fase.EXTRACCION);
        model.put("fase2", Fase.SELECCION);
        model.put("fase3", Fase.RESOLUCION);
        return VIEWS_TABLERO;
    }

    @Transactional()
    @GetMapping(value = "tablero/celda1/{id}")
    public String celda1(@PathVariable int id) {
        Partida partida = partidaService.findPartidaById(id);
        Tablero tablero = tableroService.findAll().stream().filter(x -> x.getPartida().equals(partida)).findFirst().get();
        String username = partidaService.getUserLogged().getUsername();
        
        // Colocar ficha en la celda 1.
        celdaService.colocarFicha(partida, tablero, username, 0);
        // Actualizar el turno de los jugadores despu??s de que el que tenga el turno haya colocado ficha.
        partidaService.actualizarTurno(partida);

        return "redirect:/partida/tablero/" + partida.getId();
    }

    @Transactional()
    @GetMapping(value = "tablero/celda2/{id}")
    public String celda2(@PathVariable int id) {
        Partida partida = partidaService.findPartidaById(id);
        Tablero tablero = tableroService.findAll().stream().filter(x -> x.getPartida().equals(partida)).findFirst().get();
        String username = partidaService.getUserLogged().getUsername();
        
        // Colocar ficha en la celda 2.
        celdaService.colocarFicha(partida, tablero, username, 1);
        // Actualizar el turno de los jugadores despu??s de que el que tenga el turno haya colocado ficha.
        partidaService.actualizarTurno(partida);

        return "redirect:/partida/tablero/" + partida.getId();
    }

    @Transactional()
    @GetMapping(value = "tablero/celda3/{id}")
    public String celda3(@PathVariable int id) {
        Partida partida = partidaService.findPartidaById(id);
        Tablero tablero = tableroService.findAll().stream().filter(x -> x.getPartida().equals(partida)).findFirst().get();
        String username = partidaService.getUserLogged().getUsername();
        
        // Colocar ficha en la celda 3.
        celdaService.colocarFicha(partida, tablero, username, 2);
        // Actualizar el turno de los jugadores despu??s de que el que tenga el turno haya colocado ficha.
        partidaService.actualizarTurno(partida);

        return "redirect:/partida/tablero/" + partida.getId();
    }

    @Transactional()
    @GetMapping(value = "tablero/celda4/{id}")
    public String celda4(@PathVariable int id) {
        Partida partida = partidaService.findPartidaById(id);
        Tablero tablero = tableroService.findAll().stream().filter(x -> x.getPartida().equals(partida)).findFirst().get();
        String username = partidaService.getUserLogged().getUsername();
        
        // Colocar ficha en la celda 4.
        celdaService.colocarFicha(partida, tablero, username, 3);
        // Actualizar el turno de los jugadores despu??s de que el que tenga el turno haya colocado ficha.
        partidaService.actualizarTurno(partida);

        return "redirect:/partida/tablero/" + partida.getId();
    }

    @Transactional()
    @GetMapping(value = "tablero/celda5/{id}")
    public String celda5(@PathVariable int id) {
        Partida partida = partidaService.findPartidaById(id);
        Tablero tablero = tableroService.findAll().stream().filter(x -> x.getPartida().equals(partida)).findFirst().get();
        String username = partidaService.getUserLogged().getUsername();
        
        // Colocar ficha en la celda 5.
        celdaService.colocarFicha(partida, tablero, username, 4);
        // Actualizar el turno de los jugadores despu??s de que el que tenga el turno haya colocado ficha.
        partidaService.actualizarTurno(partida);

        return "redirect:/partida/tablero/" + partida.getId();
    }

    @Transactional()
    @GetMapping(value = "tablero/celda6/{id}")
    public String celda6(@PathVariable int id) {
        Partida partida = partidaService.findPartidaById(id);
        Tablero tablero = tableroService.findAll().stream().filter(x -> x.getPartida().equals(partida)).findFirst().get();
        String username = partidaService.getUserLogged().getUsername();
        
        // Colocar ficha en la celda 6.
        celdaService.colocarFicha(partida, tablero, username, 5);
        // Actualizar el turno de los jugadores despu??s de que el que tenga el turno haya colocado ficha.
        partidaService.actualizarTurno(partida);

        return "redirect:/partida/tablero/" + partida.getId();
    }

    @Transactional()
    @GetMapping(value = "tablero/celda7/{id}")
    public String celda7(@PathVariable int id) {
        Partida partida = partidaService.findPartidaById(id);
        Tablero tablero = tableroService.findAll().stream().filter(x -> x.getPartida().equals(partida)).findFirst().get();
        String username = partidaService.getUserLogged().getUsername();
        
        // Colocar ficha en la celda 7.
        celdaService.colocarFicha(partida, tablero, username, 6);
        // Actualizar el turno de los jugadores despu??s de que el que tenga el turno haya colocado ficha.
        partidaService.actualizarTurno(partida);

        return "redirect:/partida/tablero/" + partida.getId();
    }


    @Transactional()
    @GetMapping(value = "tablero/celda8/{id}")
    public String celda8(@PathVariable int id) {
        Partida partida = partidaService.findPartidaById(id);
        Tablero tablero = tableroService.findAll().stream().filter(x -> x.getPartida().equals(partida)).findFirst().get();
        String username = partidaService.getUserLogged().getUsername();
        
        // Colocar ficha en la celda 8.
        celdaService.colocarFicha(partida, tablero, username, 7);
        // Actualizar el turno de los jugadores despu??s de que el que tenga el turno haya colocado ficha.
        partidaService.actualizarTurno(partida);

        return "redirect:/partida/tablero/" + partida.getId();
    }

    @Transactional()
    @GetMapping(value = "tablero/celda9/{id}")
    public String celda9(@PathVariable int id) {
        Partida partida = partidaService.findPartidaById(id);
        Tablero tablero = tableroService.findAll().stream().filter(x -> x.getPartida().equals(partida)).findFirst().get();
        String username = partidaService.getUserLogged().getUsername();
        
        // Colocar ficha en la celda 9.
        celdaService.colocarFicha(partida, tablero, username, 8);
        // Actualizar el turno de los jugadores despu??s de que el que tenga el turno haya colocado ficha.
        partidaService.actualizarTurno(partida);

        return "redirect:/partida/tablero/" + partida.getId();
    }

    @Transactional()
    @GetMapping(value = "tablero/celda_especial1/{id}")
    public String celdaEspecial1(@PathVariable int id) {
        Partida partida = partidaService.findPartidaById(id);
        Tablero tablero = tableroService.findAll().stream().filter(x -> x.getPartida().equals(partida)).findFirst().get();
        String username = partidaService.getUserLogged().getUsername();
        
        // Colocar ficha en la celda 9.
        celdaEspecialService.colocarFicha(partida, tablero, username, 0);
        // Actualizar el turno de los jugadores despu??s de que el que tenga el turno haya colocado ficha.
        partidaService.actualizarTurno(partida);

        return "redirect:/partida/tablero/" + partida.getId();
    }

    @Transactional()
    @GetMapping(value = "tablero/celda_especial2/{id}")
    public String celdaEspecial2(@PathVariable int id) {
        Partida partida = partidaService.findPartidaById(id);
        Tablero tablero = tableroService.findAll().stream().filter(x -> x.getPartida().equals(partida)).findFirst().get();
        String username = partidaService.getUserLogged().getUsername();
        
        // Colocar ficha en la celda 9.
        celdaEspecialService.colocarFicha(partida, tablero, username, 1);
        // Actualizar el turno de los jugadores despu??s de que el que tenga el turno haya colocado ficha.
        partidaService.actualizarTurno(partida);

        return "redirect:/partida/tablero/" + partida.getId();
    }

    @Transactional()
    @GetMapping(value = "tablero/celda_especial3/{id}")
    public String celdaEspecial3(@PathVariable int id) {
        Partida partida = partidaService.findPartidaById(id);
        Tablero tablero = tableroService.findAll().stream().filter(x -> x.getPartida().equals(partida)).findFirst().get();
        String username = partidaService.getUserLogged().getUsername();
        
        // Colocar ficha en la celda 9.
        celdaEspecialService.colocarFicha(partida, tablero, username, 2);
        // Actualizar el turno de los jugadores despu??s de que el que tenga el turno haya colocado ficha.
        partidaService.actualizarTurno(partida);

        return "redirect:/partida/tablero/" + partida.getId();
    }

    @Transactional()
    @GetMapping(value = "tablero/celda_montana/{id}")
    public String celdaMontana(@PathVariable int id) {
        Partida partida = partidaService.findPartidaById(id);
        Tablero tablero = tableroService.findAll().stream().filter(x -> x.getPartida().equals(partida)).findFirst().get();
        
        // El jugador que tiene el turno toma una carta de la monta??a y esta se coloca autom??ticamente en la rejilla de 3x3.
        partidaService.faseExtraccion(partida, tablero);

        return "redirect:/partida/tablero/" + partida.getId();
    }
 
}
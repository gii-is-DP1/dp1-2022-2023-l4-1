package org.springframework.samples.petclinic.jugador;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

@Service
public class JugadorService {

    private JugadorRepository repo;

    @Autowired
    public JugadorService(JugadorRepository repo) {
        this.repo = repo;
    }

    @Transactional(readOnly = true)
	public Jugador findPlayerByUserName(String username) throws DataAccessException {
		Jugador jugador = repo.findByUsername(username);
		return jugador;
	}

    
}
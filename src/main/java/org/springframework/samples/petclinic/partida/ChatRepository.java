package org.springframework.samples.petclinic.partida;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChatRepository extends CrudRepository<Chat,Integer>{
    
}

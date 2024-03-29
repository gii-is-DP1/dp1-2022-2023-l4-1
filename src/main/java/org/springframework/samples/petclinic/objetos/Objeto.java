package org.springframework.samples.petclinic.objetos;

import javax.persistence.Entity;
import javax.validation.constraints.NotNull;

import org.springframework.samples.petclinic.model.BaseEntity;

import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Objeto extends BaseEntity {

    @NotNull
    private String nombre;

}

package org.springframework.samples.petclinic.statistics;


import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface AchievementRepository extends CrudRepository<Achievement, Integer>{
    List<Achievement> findAll();

    @Query("SELECT a FROM Achievement a WHERE a.name=:name")
    List<Achievement> findByName(@Param("name")String name);
}
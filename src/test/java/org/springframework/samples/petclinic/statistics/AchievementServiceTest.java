package org.springframework.samples.petclinic.statistics;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class AchievementServiceTest {
    
    @Mock
    AchievementRepository achievementRepository;

    @BeforeEach
    public void config(){
        Achievement achievement1 = new Achievement();
        achievement1.setName("Acaparador");
        List<Achievement> achievements = new ArrayList<Achievement>();
        achievements.add(achievement1);
        when(achievementRepository.findByName(any(String.class))).thenReturn(achievements);
    }

    @Test
    public void saveUnsuccessfulTest(){
        Achievement achievement2 = new Achievement();
        achievement2.setName("Acaparador");

        AchievementService achievementService = new AchievementService(achievementRepository);

        assertThrows(Exception.class, () -> achievementService.save(achievement2));
    }

    @Test
    public void saveSuccessfulTest(){
        Achievement achievement3 = new Achievement();
        achievement3.setName("Prueba de achievement");
        achievement3.setDescription("Prueba de descripcion");
        AchievementService achievementService = new AchievementService(achievementRepository);
        try{
            achievementService.save(achievement3);
        } catch(Exception e){
            fail("Esta excepcion no debería ser lanzada");
        }
    }
}

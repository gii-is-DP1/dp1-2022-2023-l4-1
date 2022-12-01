package org.springframework.samples.petclinic.statistics;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.samples.petclinic.configuration.SecurityConfiguration;
import org.springframework.samples.petclinic.user.UserService;
import org.springframework.samples.petclinic.web.LoggedUserController;
import org.springframework.security.config.annotation.web.WebSecurityConfigurer;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;

@WebMvcTest(controllers=AchievementController.class,
    excludeFilters=@ComponentScan.Filter(type=FilterType.ASSIGNABLE_TYPE, classes=WebSecurityConfigurer.class),
    excludeAutoConfiguration=SecurityConfiguration.class)
public class AchievementControllerTest {
    
    @Autowired
    private MockMvc mockMvc;
    
    @MockBean
    private AchievementService achievementService;
    @MockBean
    private UserService userService;
    @MockBean
    private LoggedUserController currentUser;
    
    @WithMockUser
    @Test
    public void testAchievementListing() throws Exception{
        mockMvc.perform(get("/statistics/achievements/")).
                andExpect(status().isOk()).
                andExpect(view().name("/achievements/AchievementsListing")).
                andExpect(model().attributeExists("achievements"));
    }
}

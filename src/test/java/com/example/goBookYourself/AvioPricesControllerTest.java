package com.example.goBookYourself;

import com.example.goBookYourself.DTO.AvioPricesDTO;
import com.example.goBookYourself.security.TokenUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import java.nio.charset.Charset;

import static junit.framework.TestCase.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@TestPropertySource(locations = "classpath:test-application.properties")
@SpringBootTest
public class AvioPricesControllerTest {

    private static final String URL_PREFIX = "/api/avioprices";

    private MockMvc mockMvc;

    private MediaType contentType = new MediaType(MediaType.APPLICATION_JSON.getType(),
            MediaType.APPLICATION_JSON.getSubtype(), Charset.forName("utf8"));

    @Autowired
    private WebApplicationContext webApplicationContext;

    private String token;

    @Autowired
    private TokenUtils tokenUtils;

    @Before
    public void setUp() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        this.token = tokenUtils.generateToken("user3");
    }

    @Test
    @Transactional
    @Rollback(true)
    public void removePricingTest() throws Exception {
        assertNotNull(token);
        mockMvc.perform(put(URL_PREFIX + "/remove").header("Authorization", "Bearer " + this.token).contentType(contentType)
                .content(TestUtil.json(new AvioPricesDTO(1, "", 0, (long) 0))))
                .andExpect(status().isOk());

        mockMvc.perform(put(URL_PREFIX + "/remove").contentType(contentType)
                .content(TestUtil.json(new AvioPricesDTO(1, "", 0, (long) 0))))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @Transactional
    @Rollback(true)
    public void changePricingTest() throws Exception {
        assertNotNull(token);
        mockMvc.perform(put(URL_PREFIX + "/change").header("Authorization", "Bearer " + this.token).contentType(contentType)
                .content(TestUtil.json(new AvioPricesDTO(1, "Brt", 150, (long) 0))))
                .andExpect(status().isOk());

        mockMvc.perform(put(URL_PREFIX + "/remove").contentType(contentType)
                .content(TestUtil.json(new AvioPricesDTO(1, "", 0, (long) 0))))
                .andExpect(status().isUnauthorized());
    }
}

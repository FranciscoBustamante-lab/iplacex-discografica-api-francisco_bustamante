package org.iplacex.proyectos.discografia;

import org.iplacex.proyectos.discografia.artistas.IArtistaRepository;
import org.iplacex.proyectos.discografia.discos.IDiscoRepository;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

@SpringBootTest
class ApiApplicationTests {

    @MockBean
    private IArtistaRepository artistaRepo;

    @MockBean
    private IDiscoRepository discoRepo;

    @Test
    void contextLoads() {
    }

}

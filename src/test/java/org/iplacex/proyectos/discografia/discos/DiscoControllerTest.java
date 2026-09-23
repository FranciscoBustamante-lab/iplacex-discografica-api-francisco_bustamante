package org.iplacex.proyectos.discografia.discos;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Arrays;
import java.util.Optional;
import org.iplacex.proyectos.discografia.artistas.IArtistaRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(DiscoController.class)
public class DiscoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private IDiscoRepository discoRepo;

    @MockBean
    private IArtistaRepository artistaRepo;

    @Test
    public void testHandlePostDiscoRequest_ArtistExists() throws Exception {
        when(artistaRepo.existsById("art1")).thenReturn(true);
        Disco disco = new Disco("d1", "art1", "Alturas de Machu Picchu", 1981, Arrays.asList("Alturas de Machu Picchu", "La poderosa muerte"));
        when(discoRepo.save(any(Disco.class))).thenReturn(disco);

        String json = "{\"idArtista\":\"art1\",\"nombre\":\"Alturas de Machu Picchu\",\"anioLanzamiento\":1981,\"canciones\":[\"Alturas de Machu Picchu\",\"La poderosa muerte\"]}";

        mockMvc.perform(post("/api/disco")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.nombre").value("Alturas de Machu Picchu"))
                .andExpect(jsonPath("$.idArtista").value("art1"));
    }

    @Test
    public void testHandlePostDiscoRequest_ArtistNotFound() throws Exception {
        when(artistaRepo.existsById("art99")).thenReturn(false);

        String json = "{\"idArtista\":\"art99\",\"nombre\":\"Alturas de Machu Picchu\",\"anioLanzamiento\":1981,\"canciones\":[]}";

        mockMvc.perform(post("/api/disco")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testHandleGetDiscosRequest() throws Exception {
        Disco d1 = new Disco("d1", "art1", "Alturas de Machu Picchu", 1981, Arrays.asList("Cancion 1"));
        Disco d2 = new Disco("d2", "art2", "La Voz de los '80", 1984, Arrays.asList("La voz de los 80"));
        when(discoRepo.findAll()).thenReturn(Arrays.asList(d1, d2));

        mockMvc.perform(get("/api/discos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].nombre").value("Alturas de Machu Picchu"));
    }

    @Test
    public void testHandleGetDiscoRequest_Found() throws Exception {
        Disco d1 = new Disco("d1", "art1", "Alturas de Machu Picchu", 1981, Arrays.asList("Cancion 1"));
        when(discoRepo.findById("d1")).thenReturn(Optional.of(d1));

        mockMvc.perform(get("/api/disco/d1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre").value("Alturas de Machu Picchu"));
    }

    @Test
    public void testHandleGetDiscoRequest_NotFound() throws Exception {
        when(discoRepo.findById("d99")).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/disco/d99"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testHandleGetDiscosByArtistaRequest() throws Exception {
        Disco d1 = new Disco("d1", "art1", "Alturas de Machu Picchu", 1981, Arrays.asList("Cancion 1"));
        when(discoRepo.findDiscosByIdArtista("art1")).thenReturn(Arrays.asList(d1));

        mockMvc.perform(get("/api/artista/art1/discos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].nombre").value("Alturas de Machu Picchu"));
    }
}

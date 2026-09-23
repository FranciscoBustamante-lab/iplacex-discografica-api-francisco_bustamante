package org.iplacex.proyectos.discografia.artistas;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Arrays;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(ArtistaController.class)
public class ArtistaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private IArtistaRepository artistaRepo;

    @Test
    public void testHandleInsertArtistaRequest() throws Exception {
        Artista artista = new Artista("1", "Los Jaivas", Arrays.asList("Rock Progresivo", "Folk"), 1963, true);
        when(artistaRepo.save(any(Artista.class))).thenReturn(artista);

        String json = "{\"nombre\":\"Los Jaivas\",\"estilos\":[\"Rock Progresivo\",\"Folk\"],\"anioFundacion\":1963,\"estaActivo\":true}";

        mockMvc.perform(post("/api/artista")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.nombre").value("Los Jaivas"));
    }

    @Test
    public void testHandleGetAristasRequest() throws Exception {
        Artista a1 = new Artista("1", "Los Jaivas", Arrays.asList("Rock"), 1963, true);
        Artista a2 = new Artista("2", "Los Prisioneros", Arrays.asList("Rock", "Punk"), 1983, false);
        when(artistaRepo.findAll()).thenReturn(Arrays.asList(a1, a2));

        mockMvc.perform(get("/api/artistas"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].nombre").value("Los Jaivas"));
    }

    @Test
    public void testHandleGetArtistaRequest_Found() throws Exception {
        Artista artista = new Artista("1", "Los Jaivas", Arrays.asList("Rock"), 1963, true);
        when(artistaRepo.findById("1")).thenReturn(Optional.of(artista));

        mockMvc.perform(get("/api/artista/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre").value("Los Jaivas"));
    }

    @Test
    public void testHandleGetArtistaRequest_NotFound() throws Exception {
        when(artistaRepo.findById("99")).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/artista/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testHandleUpdateArtistaRequest_Success() throws Exception {
        when(artistaRepo.existsById("1")).thenReturn(true);
        Artista updated = new Artista("1", "Los Jaivas Actualizado", Arrays.asList("Rock"), 1963, true);
        when(artistaRepo.save(any(Artista.class))).thenReturn(updated);

        String json = "{\"nombre\":\"Los Jaivas Actualizado\",\"estilos\":[\"Rock\"],\"anioFundacion\":1963,\"estaActivo\":true}";

        mockMvc.perform(put("/api/artista/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre").value("Los Jaivas Actualizado"));
    }

    @Test
    public void testHandleUpdateArtistaRequest_NotFound() throws Exception {
        when(artistaRepo.existsById("99")).thenReturn(false);

        String json = "{\"nombre\":\"Los Jaivas\",\"estilos\":[\"Rock\"],\"anioFundacion\":1963,\"estaActivo\":true}";

        mockMvc.perform(put("/api/artista/99")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testHandleDeleteArtistaRequest_Success() throws Exception {
        when(artistaRepo.existsById("1")).thenReturn(true);
        doNothing().when(artistaRepo).deleteById("1");

        mockMvc.perform(delete("/api/artista/1"))
                .andExpect(status().isOk());
    }

    @Test
    public void testHandleDeleteArtistaRequest_NotFound() throws Exception {
        when(artistaRepo.existsById("99")).thenReturn(false);

        mockMvc.perform(delete("/api/artista/99"))
                .andExpect(status().isNotFound());
    }
}

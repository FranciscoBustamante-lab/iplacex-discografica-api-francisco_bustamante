package org.iplacex.proyectos.discografia.artistas;

import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin
@RequestMapping("/api")
public class ArtistaController {

    @Autowired
    private IArtistaRepository artistaRepo;

    @PostMapping(value = "/artista", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> HandleInsertArtistaRequest(@RequestBody Artista artista) {
        Artista res = artistaRepo.save(artista);
        return new ResponseEntity<>(res, HttpStatus.CREATED);
    }

    @GetMapping(value = "/artistas", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Artista>> HandleGetAristasRequest() {
        List<Artista> artistas = artistaRepo.findAll();
        return new ResponseEntity<>(artistas, HttpStatus.OK);
    }

    @GetMapping(value = "/artista/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> HandleGetArtistaRequest(@PathVariable("id") String id) {
        Optional<Artista> artista = artistaRepo.findById(id);
        if (artista.isPresent()) {
            return new ResponseEntity<>(artista.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Artista no encontrado", HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping(value = "/artista/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> HandleUpdateArtistaRequest(@PathVariable("id") String id, @RequestBody Artista artista) {
        if (!artistaRepo.existsById(id)) {
            return new ResponseEntity<>("Artista no encontrado", HttpStatus.NOT_FOUND);
        }
        artista._id = id;
        Artista updated = artistaRepo.save(artista);
        return new ResponseEntity<>(updated, HttpStatus.OK);
    }

    @DeleteMapping(value = "/artista/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> HandleDeleteArtistaRequest(@PathVariable("id") String id) {
        if (!artistaRepo.existsById(id)) {
            return new ResponseEntity<>("Artista no encontrado", HttpStatus.NOT_FOUND);
        }
        artistaRepo.deleteById(id);
        return new ResponseEntity<>("Artista eliminado exitosamente", HttpStatus.OK);
    }
}

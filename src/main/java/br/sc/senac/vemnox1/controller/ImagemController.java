package br.sc.senac.vemnox1.controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import br.sc.senac.vemnox1.service.ImagemService;

@RestController
@RequestMapping("/api/imagem")
public class ImagemController {

    @Autowired
    private ImagemService imagemService;

    @PostMapping("/upload")
    public ResponseEntity<String> uploadImagem(@RequestParam("file") MultipartFile file) {
        try {
        	//TODO limitar a 10mb no CartaController
            String base64 = imagemService.processarImagem(file);
            return ResponseEntity.ok(base64);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Erro ao processar imagem: " + e.getMessage());
        }
    }
}

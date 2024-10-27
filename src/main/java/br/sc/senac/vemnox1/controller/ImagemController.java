package br.sc.senac.vemnox1.controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import br.sc.senac.vemnox1.service.ImagemService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;

@RestController
@RequestMapping("/api/imagem")
public class ImagemController {

    @Autowired
    private ImagemService imagemService;
    
	@Operation(
		summary = "Upload de Imagem (genérico sem salvar no banco)",
		requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
			description = "Arquivo de imagem a ser enviado",
			required = true,
			content = @Content(
				mediaType = "multipart/form-data",
				schema = @Schema(type = "string", format = "binary")
			)
		),
		description = "Simula o upload de uma imagem, retornando uma string com a sua representação no formato base64."
	)
    @PostMapping("/upload")
    public ResponseEntity<String> uploadImagem(@RequestParam("file") MultipartFile file) {
        try {
            String base64 = imagemService.processarImagem(file);
            return ResponseEntity.ok(base64);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Erro ao processar imagem: " + e.getMessage());
        }
    }
}

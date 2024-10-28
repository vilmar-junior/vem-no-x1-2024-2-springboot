package br.sc.senac.vemnox1.service;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import br.sc.senac.vemnox1.exception.VemNoX1Exception;

import java.io.IOException;
import java.util.Base64;

@Service
public class ImagemService {

    public String processarImagem(MultipartFile file) throws VemNoX1Exception {
        // Converte MultipartFile em byte[]
        byte[] imagemBytes;
		try {
			imagemBytes = file.getBytes();
		} catch (IOException e) {
			throw new VemNoX1Exception("Erro ao processar arquivo");
		}
        
        // Converte byte[] para Base64
        String base64Imagem = Base64.getEncoder().encodeToString(imagemBytes);
        
        return base64Imagem;
    }
}

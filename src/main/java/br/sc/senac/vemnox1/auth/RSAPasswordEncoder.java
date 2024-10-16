package br.sc.senac.vemnox1.auth;
import java.nio.charset.StandardCharsets;
import java.security.PrivateKey;
import java.security.PublicKey;
import javax.crypto.Cipher;
import org.springframework.security.crypto.password.PasswordEncoder;
import java.util.Base64;

public class RSAPasswordEncoder implements PasswordEncoder {

    private final PublicKey publicKey;
    private final PrivateKey privateKey;

    public RSAPasswordEncoder(PublicKey publicKey, PrivateKey privateKey) {
        this.publicKey = publicKey;
        this.privateKey = privateKey;
    }

    @Override
    public String encode(CharSequence rawPassword) {
        try {
            // Converter a senha bruta em bytes
            byte[] passwordBytes = rawPassword.toString().getBytes(StandardCharsets.UTF_8);

            // Criptografar a senha com a chave pública
            Cipher cipher = Cipher.getInstance("RSA");
            cipher.init(Cipher.ENCRYPT_MODE, publicKey);
            byte[] encryptedBytes = cipher.doFinal(passwordBytes);

            // Retornar o texto criptografado em Base64
            return Base64.getEncoder().encodeToString(encryptedBytes);
        } catch (Exception e) {
            throw new RuntimeException("Erro ao criptografar a senha com RSA", e);
        }
    }

    @Override
    public boolean matches(CharSequence rawPassword, String encodedPassword) {
        try {
            // Decodificar o texto criptografado em Base64
            byte[] encryptedBytes = Base64.getDecoder().decode(encodedPassword);

            // Decifrar a senha com a chave privada
            Cipher cipher = Cipher.getInstance("RSA");
            cipher.init(Cipher.DECRYPT_MODE, privateKey);
            byte[] decryptedBytes = cipher.doFinal(encryptedBytes);

            // Comparar a senha original com a decifrada
            String decryptedPassword = new String(decryptedBytes, StandardCharsets.UTF_8);
            return rawPassword.toString().equals(decryptedPassword);
        } catch (Exception e) {
            throw new RuntimeException("Erro ao decifrar a senha com RSA", e);
        }
    }
}

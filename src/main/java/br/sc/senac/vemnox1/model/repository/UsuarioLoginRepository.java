package br.sc.senac.vemnox1.model.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import br.sc.senac.vemnox1.model.entity.UsuarioLogin;

public interface UsuarioLoginRepository extends JpaRepository<UsuarioLogin, Long> {
    Optional<UsuarioLogin> findByLogin(String login);
}

package com.autobots.automanager.repositorios;

import com.autobots.automanager.entidades.CredencialAcesso;
import com.autobots.automanager.entidades.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UsuarioRepositorio extends JpaRepository<Usuario, Long> {
    Optional<Usuario> findByCredencial(CredencialAcesso credencial);
}

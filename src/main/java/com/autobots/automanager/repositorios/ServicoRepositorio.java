package com.autobots.automanager.repositorios;

import com.autobots.automanager.entidades.Servico;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ServicoRepositorio extends JpaRepository<Servico, Long> {
}

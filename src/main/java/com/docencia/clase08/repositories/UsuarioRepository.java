package com.docencia.clase08.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.docencia.clase08.models.Usuario;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    Usuario findByUsername(String username);
}

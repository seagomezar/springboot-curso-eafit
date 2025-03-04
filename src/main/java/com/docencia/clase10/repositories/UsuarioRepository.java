package com.docencia.clase10.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.docencia.clase10.models.Usuario;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    Usuario findByUsername(String username);
}

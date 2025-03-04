package com.docencia.clase08.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.docencia.clase08.models.Curso;

@Repository
public interface CursoRepository extends JpaRepository<Curso, Long> {
}
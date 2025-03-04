package com.docencia.clase10.services;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.docencia.clase10.models.Alumno;
import com.docencia.clase10.models.Curso;
import com.docencia.clase10.repositories.AlumnoRepository;
import com.docencia.clase10.repositories.CursoRepository;

@Service
public class GestorCursosService {

  @Autowired
  AlumnoRepository repoAlumno;
  @Autowired
  CursoRepository repoCurso;
  
  
  public void insertarAlumno(Alumno alumno) {
    repoAlumno.save(alumno);
  }
  public List<Alumno> buscarTodosAlumnos() {
    return repoAlumno.findAll();
  }
  
  
  public List<Curso> buscarTodosCursos() {
    return repoCurso.findAll();
  }
  
}
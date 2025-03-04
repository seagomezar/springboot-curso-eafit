package com.docencia.clase10.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.docencia.clase10.models.Curso;
import com.docencia.clase10.services.GestorCursosService;

@Controller
public class CursoController {

    @Autowired
    GestorCursosService servicio;

    @GetMapping("/cursos")
    public String listarCursos(Model model) {
        List<Curso> listaCursos = servicio.buscarTodosCursos();
        model.addAttribute("listacursos", listaCursos);
        return "cursos/index";
    }
}

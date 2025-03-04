
package com.docencia.clase10.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.docencia.clase10.services.GestorCursosService;



@Controller
public class AlumnoController {

  @Autowired
  GestorCursosService servicio;

  @RequestMapping("/alumnos")
  public String listaAlumnos(Model modelo) {
    
    modelo.addAttribute("listaalumnos", servicio.buscarTodosAlumnos());
    return "alumnos/index";
  }
  
}
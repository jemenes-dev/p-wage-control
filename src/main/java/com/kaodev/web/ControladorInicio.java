package com.kaodev.web;

import javax.validation.Valid;

import com.kaodev.domain.Persona;
import com.kaodev.servicio.PersonaService;
import com.kaodev.servicio.UsuarioService;
import com.kaodev.util.MockData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Controller
@Slf4j
public class ControladorInicio {

    @Autowired
    private PersonaService personaService;

    @Autowired
    private UsuarioService usuarioService;

    private List<Persona> mockList = MockData.getMockData();
    private boolean isRunning = false;

    @GetMapping("/")
    public String inicio(Model model, @AuthenticationPrincipal User user){
        if (user.getUsername().equals("admin")) {
           if (!isRunning) {
               isRunning = true;
               final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
               final Runnable runnable = new Runnable() {
                   int countdown = 20;
                   @Override
                   public void run() {
                       countdown--;
                       if (countdown < 0) {
                           var personas = personaService.listarPersonas();
                           if (personas.size() > 0) {
                               for(var p: personas){
                                   personaService.eliminar(p);
                               }
                           }
                           for(var p: mockList){
                               personaService.guardar(p);
                           }
                           isRunning = false;
                           scheduler.shutdown();
                       }
                   }
               };
               scheduler.scheduleAtFixedRate(runnable,0,1, TimeUnit.MINUTES);
           }
        }
        var personas = personaService.listarPersonas();
        model.addAttribute("personas", personas);
        var saldoTotal = 0D;
        for(var p: personas){
            saldoTotal += p.getSaldo();
        }
        model.addAttribute("saldoTotal", saldoTotal);
        model.addAttribute("totalClientes", personas.size());
        return "index";
    }

    @GetMapping("/agregar")
    public String agregar(Persona persona){
        return "modificar";
    }

    @PostMapping("/guardar")
    public String guardar(@Valid Persona persona, Errors errores){
        if(errores.hasErrors()){
            return "modificar";
        }
        personaService.guardar(persona);
        return "redirect:/";
    }

    @GetMapping("/editar/{idPersona}")
    public String editar(Persona persona, Model model){
        persona = personaService.encontrarPersona(persona);
        model.addAttribute("persona", persona);
        return "modificar";
    }

    @GetMapping("/eliminar")
    public String eliminar(Persona persona){
        personaService.eliminar(persona);
        return "redirect:/";
    }
}
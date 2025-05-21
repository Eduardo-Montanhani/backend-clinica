package com.clinica.gestao_consultas.controller;


import com.clinica.gestao_consultas.model.Consulta;
import com.clinica.gestao_consultas.repository.ConsultaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/consultas")
@RequiredArgsConstructor
public class ConsultaController {

    private final ConsultaRepository consultaRepository;

    @GetMapping
    public List<Consulta> listar() {
        return consultaRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Consulta> buscar(@PathVariable Long id) {
        return consultaRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<?> criar(@RequestBody @Valid Consulta consulta) {
        return ResponseEntity.ok(consultaRepository.save(consulta));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> atualizar(@PathVariable Long id, @RequestBody @Valid Consulta dados) {
        return consultaRepository.findById(id)
                .map(c -> {
                    c.setDataHora(dados.getDataHora());
                    c.setMedico(dados.getMedico());
                    c.setPaciente(dados.getPaciente());
                    c.setObservacoes(dados.getObservacoes());
                    return ResponseEntity.ok(consultaRepository.save(c));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        if (consultaRepository.existsById(id)) {
            consultaRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}
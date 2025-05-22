package com.clinica.gestao_consultas.controller;

import com.clinica.gestao_consultas.model.Paciente;
import com.clinica.gestao_consultas.repository.ConsultaRepository;
import com.clinica.gestao_consultas.repository.PacienteRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/pacientes")
public class PacienteController {

    @Autowired
    private PacienteRepository pacienteRepository;
    private final ConsultaRepository consultaRepository;

    public PacienteController(ConsultaRepository consultaRepository) {
        this.consultaRepository = consultaRepository;
    }

    //Get api/pacientes
    @GetMapping
    public List<Paciente> listarTodos(){
        return pacienteRepository.findAll();
    }

    // Get /api/pacientes/{id}
    @GetMapping("/{id}")
    public ResponseEntity<Paciente> buscarPorId(@PathVariable Long id){
        Optional<Paciente> paciente = pacienteRepository.findById(id);
        return paciente.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    //Post /api/pacientes
    @PostMapping
    public ResponseEntity<?> criar(@RequestBody @Valid Paciente paciente) {
        return ResponseEntity.ok(pacienteRepository.save(paciente));
    }

    // PUT /api/pacientes/{id}
    @PutMapping("/{id}")
    public ResponseEntity<?> atualizar(@PathVariable Long id, @RequestBody @Valid Paciente dados) {
        return pacienteRepository.findById(id)
                .map(paciente -> {
                    paciente.setNome(dados.getNome());
                    paciente.setCpf(dados.getCpf());
                    paciente.setTelefone(dados.getTelefone());
                    paciente.setEmail(dados.getEmail());
                    return ResponseEntity.ok(pacienteRepository.save(paciente));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    //DELETE /api/pacientes/{id}
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletar(@PathVariable Long id) {
        if (!pacienteRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }

        // Verifica se paciente tem consultas pendentes (futuras)
        var consultasPendentes = consultaRepository.findByPacienteIdAndDataHoraAfter(id, LocalDateTime.now());

        if (!consultasPendentes.isEmpty()) {
            return ResponseEntity.badRequest()
                    .body("Não é possível excluir paciente: existe(m) consulta(s) pendente(s).");
        }

        pacienteRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}

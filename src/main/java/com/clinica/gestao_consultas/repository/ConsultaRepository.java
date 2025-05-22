package com.clinica.gestao_consultas.repository;


import com.clinica.gestao_consultas.model.Consulta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.awt.*;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ConsultaRepository extends JpaRepository<Consulta, Long> {
    // Consultas futuras do paciente
    List<Consulta> findByPacienteIdAndDataHoraAfter(Long pacienteId, LocalDateTime now);
}
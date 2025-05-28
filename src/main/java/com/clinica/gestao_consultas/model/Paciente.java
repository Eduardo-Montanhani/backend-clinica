package com.clinica.gestao_consultas.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;


@Entity
@Table(name = "pacientes")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Paciente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "O nome é obrigatório")
    private String nome;

    @NotBlank(message = "O CPF é obrigatório")
    @Size(min = 14, max = 14, message = "O CPF deve ter exatamente 11 dígitos")
    private String cpf;

    @NotBlank(message = "O telefone é obrigatório")
    private String telefone;

    @Email(message = "Formato de e-mail inválido")
    private String email;
}
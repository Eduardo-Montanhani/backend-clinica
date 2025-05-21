package com.clinica.gestao_consultas.controller;


import com.clinica.gestao_consultas.model.Usuario;
import com.clinica.gestao_consultas.repository.UsuarioRepository;
import com.clinica.gestao_consultas.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> dados) {
        try {
            String username = dados.get("username");
            String senha = dados.get("senha");

            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(username, senha)
            );

            Usuario usuario = usuarioRepository.findByUsername(username).get();

            String token = jwtUtil.gerarToken(username);

            return ResponseEntity.ok(Map.of(
                    "token", token,
                    "username", username,
                    "role", usuario.getRole()
            ));
        } catch (BadCredentialsException e) {
            return ResponseEntity.status(401).body(Map.of("erro", "Usuário ou senha inválidos"));
        }
    }

    @PostMapping("/register")
    public ResponseEntity<?> registrar(@RequestBody Map<String, String> dados) {
        String username = dados.get("username");
        String senha = dados.get("senha");
        String role = dados.getOrDefault("role", "ROLE_USER");

        if (usuarioRepository.findByUsername(username).isPresent()) {
            return ResponseEntity.badRequest().body(Map.of("erro", "Usuário já existe"));
        }

        Usuario novoUsuario = Usuario.builder()
                .username(username)
                .senha(passwordEncoder.encode(senha))
                .role(role)
                .build();

        usuarioRepository.save(novoUsuario);

        return ResponseEntity.ok(Map.of("msg", "Usuário registrado com sucesso"));
    }
}

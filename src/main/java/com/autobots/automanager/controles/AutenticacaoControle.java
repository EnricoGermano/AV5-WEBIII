package com.autobots.automanager.controles;

import com.autobots.automanager.entidades.CredencialAcesso;
import com.autobots.automanager.entidades.Usuario;
import com.autobots.automanager.repositorios.CredencialAcessoRepositorio;
import com.autobots.automanager.repositorios.UsuarioRepositorio;
import com.autobots.automanager.seguranca.JwtProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/auth")
public class AutenticacaoControle {

    @Autowired
    private CredencialAcessoRepositorio credencialRepositorio;

    @Autowired
    private UsuarioRepositorio usuarioRepositorio;

    @Autowired
    private JwtProvider jwtProvider;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        if (loginRequest.getNomeUsuario() == null || loginRequest.getNomeUsuario().isEmpty() ||
            loginRequest.getSenha() == null || loginRequest.getSenha().isEmpty()) {
            return ResponseEntity.badRequest().body(new ErrorResponse("Username e senha são obrigatórios"));
        }

        Optional<CredencialAcesso> optCredencial = credencialRepositorio.findByNomeUsuario(loginRequest.getNomeUsuario());

        if (optCredencial.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new ErrorResponse("Credenciais inválidas"));
        }

        CredencialAcesso credencial = optCredencial.get();

        if (!validarSenha(loginRequest.getSenha(), credencial.getSenha())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new ErrorResponse("Credenciais inválidas"));
        }

        Optional<Usuario> optUsuario = usuarioRepositorio.findByCredencial(credencial);

        if (optUsuario.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new ErrorResponse("Usuário não encontrado"));
        }

        Usuario usuario = optUsuario.get();

        if (usuario.getPerfil() == null || usuario.getPerfil().isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new ErrorResponse("Usuário não possui perfis atribuídos"));
        }

        List<String> roles = usuario.getPerfil().stream()
                .map(Enum::toString)
                .collect(Collectors.toList());

        String token = jwtProvider.generateToken(usuario.getId(), usuario.getEmail(), roles);
        String refreshToken = jwtProvider.generateRefreshToken(usuario.getId());

        Map<String, Object> response = new HashMap<>();
        response.put("access_token", token);
        response.put("refresh_token", refreshToken);
        response.put("token_type", "Bearer");
        response.put("usuario_id", usuario.getId());
        response.put("email", usuario.getEmail());
        response.put("perfis", roles);

        return ResponseEntity.ok(response);
    }

    @PostMapping("/refresh")
    public ResponseEntity<?> refreshToken(@RequestBody RefreshTokenRequest refreshTokenRequest) {
        if (refreshTokenRequest.getRefreshToken() == null || refreshTokenRequest.getRefreshToken().isEmpty()) {
            return ResponseEntity.badRequest().body(new ErrorResponse("Refresh token é obrigatório"));
        }

        String token = jwtProvider.getTokenFromBearerToken(refreshTokenRequest.getRefreshToken());

        if (!jwtProvider.validateToken(token)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new ErrorResponse("Refresh token inválido ou expirado"));
        }

        Long userId = jwtProvider.getUserIdFromToken(token);

        Optional<Usuario> optUsuario = usuarioRepositorio.findById(userId);

        if (optUsuario.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new ErrorResponse("Usuário não encontrado"));
        }

        Usuario usuario = optUsuario.get();

        List<String> roles = usuario.getPerfil().stream()
                .map(Enum::toString)
                .collect(Collectors.toList());

        String newAccessToken = jwtProvider.generateToken(usuario.getId(), usuario.getEmail(), roles);

        Map<String, String> response = new HashMap<>();
        response.put("access_token", newAccessToken);
        response.put("token_type", "Bearer");

        return ResponseEntity.ok(response);
    }

    private boolean validarSenha(String senhaFornecida, String senhaArmazenada) {
        if (senhaArmazenada.startsWith("$2a$") || senhaArmazenada.startsWith("$2b$") || senhaArmazenada.startsWith("$2y$")) {
            return passwordEncoder.matches(senhaFornecida, senhaArmazenada);
        }
        return senhaFornecida.equals(senhaArmazenada);
    }

    public static class LoginRequest {
        private String nomeUsuario;
        private String senha;

        public String getNomeUsuario() {
            return nomeUsuario;
        }

        public void setNomeUsuario(String nomeUsuario) {
            this.nomeUsuario = nomeUsuario;
        }

        public String getSenha() {
            return senha;
        }

        public void setSenha(String senha) {
            this.senha = senha;
        }
    }

    public static class RefreshTokenRequest {
        private String refreshToken;

        public String getRefreshToken() {
            return refreshToken;
        }

        public void setRefreshToken(String refreshToken) {
            this.refreshToken = refreshToken;
        }
    }

    public static class ErrorResponse {
        private String message;

        public ErrorResponse(String message) {
            this.message = message;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }
    }
}

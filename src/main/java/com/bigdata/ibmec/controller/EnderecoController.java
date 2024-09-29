package com.bigdata.ibmec.controller;

import com.bigdata.ibmec.model.Endereco;
import com.bigdata.ibmec.service.EnderecoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/enderecos")
public class EnderecoController {

    @Autowired
    private EnderecoService enderecoService;

    // Rota para buscar endereços de um cliente específico
    @GetMapping("/cliente/{clienteId}")
    public ResponseEntity<List<Endereco>> listarEnderecosPorCliente(@PathVariable Long clienteId) {
        List<Endereco> enderecos = enderecoService.listarEnderecosPorCliente(clienteId);
        return ResponseEntity.ok(enderecos);
    }

    // Adiciona um novo endereço a um cliente existente
    @PostMapping("/cliente/{clienteId}")
    public ResponseEntity<Endereco> adicionarEndereco(@PathVariable Long clienteId, @Valid @RequestBody Endereco endereco) {
        Endereco novoEndereco = enderecoService.adicionarEndereco(clienteId, endereco);
        return ResponseEntity.ok(novoEndereco);
    }

    // Lista todos os endereços
    @GetMapping
    public ResponseEntity<List<Endereco>> listarTodos() {
        return ResponseEntity.ok(enderecoService.listarTodos());
    }

    // Atualiza um endereço específico
    @PutMapping("/{id}")
    public ResponseEntity<Endereco> atualizarEndereco(@PathVariable Long id, @Valid @RequestBody Endereco enderecoAtualizado) {
        Endereco endereco = enderecoService.atualizarEndereco(id, enderecoAtualizado);
        return ResponseEntity.ok(endereco);
    }

    // Deleta um endereço específico
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarEndereco(@PathVariable Long id) {
        enderecoService.deletarEndereco(id);
        return ResponseEntity.noContent().build();
    }
}

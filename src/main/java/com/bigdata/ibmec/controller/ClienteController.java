package com.bigdata.ibmec.controller;

import com.bigdata.ibmec.DTO.ClienteComEnderecoDTO;
import com.bigdata.ibmec.model.Cliente;
import com.bigdata.ibmec.service.ClienteService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/clientes")
public class ClienteController {

    @Autowired
    private ClienteService clienteService;

    // Cria um cliente junto com o primeiro endereço usando o DTO
    @PostMapping
    public ResponseEntity<Cliente> adicionarCliente(@Valid @RequestBody ClienteComEnderecoDTO clienteComEnderecoDTO) {
        Cliente novoCliente = clienteService.adicionarClienteComEndereco(clienteComEnderecoDTO);
        return ResponseEntity.ok(novoCliente);
    }

    // Lista todos os clientes
    @GetMapping
    public ResponseEntity<List<Cliente>> listarTodos() {
        List<Cliente> clientes = clienteService.listarTodos();
        return ResponseEntity.ok(clientes);
    }

    // Busca cliente por ID
    @GetMapping("/{id}")
    public ResponseEntity<Cliente> buscarClientePorId(@PathVariable Long id) {
        Cliente cliente = clienteService.buscarClientePorId(id);
        return ResponseEntity.ok(cliente);
    }

    // Atualiza dados do cliente
    @PutMapping("/{id}")
    public ResponseEntity<Cliente> atualizarCliente(@PathVariable Long id, @Valid @RequestBody Cliente clienteAtualizado) {
        Cliente cliente = clienteService.atualizarDadosCliente(id, clienteAtualizado);
        return ResponseEntity.ok(cliente);
    }

    // Deleta um cliente
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarCliente(@PathVariable Long id) {
        clienteService.deletarCliente(id);
        return ResponseEntity.noContent().build();
    }
}

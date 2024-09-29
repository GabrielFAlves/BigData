package com.bigdata.ibmec.service;

import com.bigdata.ibmec.model.Cliente;
import com.bigdata.ibmec.model.Endereco;
import com.bigdata.ibmec.repository.ClienteRepository;
import com.bigdata.ibmec.repository.EnderecoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EnderecoService {

    @Autowired
    private EnderecoRepository enderecoRepository;

    @Autowired
    private ClienteRepository clienteRepository;

    // Busca todos os endereços de um cliente específico
    public List<Endereco> listarEnderecosPorCliente(Long clienteId) {
        Cliente cliente = clienteRepository.findById(clienteId)
                .orElseThrow(() -> new RuntimeException("Cliente não encontrado"));

        // Retorna a lista de endereços do cliente
        return cliente.getEnderecos();
    }

    // Adiciona um novo endereço a um cliente existente
    public Endereco adicionarEndereco(Long clienteId, Endereco endereco) {
        Cliente cliente = clienteRepository.findById(clienteId)
                .orElseThrow(() -> new RuntimeException("Cliente não encontrado"));

        endereco.setCliente(cliente);
        cliente.getEnderecos().add(endereco);

        return enderecoRepository.save(endereco);
    }

    // Lista todos os endereços
    public List<Endereco> listarTodos() {
        return enderecoRepository.findAll();
    }

    // Atualiza um endereço específico
    public Endereco atualizarEndereco(Long id, Endereco enderecoAtualizado) {
        Endereco enderecoExistente = enderecoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Endereço não encontrado"));

        // Atualiza apenas os dados do endereço, sem mexer no cliente
        enderecoExistente.setRua(enderecoAtualizado.getRua());
        enderecoExistente.setNumero(enderecoAtualizado.getNumero());
        enderecoExistente.setBairro(enderecoAtualizado.getBairro());
        enderecoExistente.setCidade(enderecoAtualizado.getCidade());
        enderecoExistente.setEstado(enderecoAtualizado.getEstado());
        enderecoExistente.setCep(enderecoAtualizado.getCep());

        return enderecoRepository.save(enderecoExistente);
    }

    // Deleta um endereço específico
    public void deletarEndereco(Long id) {
        Endereco endereco = enderecoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Endereço não encontrado"));
        enderecoRepository.delete(endereco);
    }
}

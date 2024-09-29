package com.bigdata.ibmec.service;

import com.bigdata.ibmec.DTO.ClienteComEnderecoDTO;
import com.bigdata.ibmec.exceptions.IdadeMinimaException;
import com.bigdata.ibmec.model.Cliente;
import com.bigdata.ibmec.model.Endereco;
import com.bigdata.ibmec.repository.ClienteRepository;
import com.bigdata.ibmec.repository.EnderecoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Period;
import java.util.List;

@Service
public class ClienteService {

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private EnderecoRepository enderecoRepository;

    // Adiciona um cliente com endereço inicial usando DTO
    public Cliente adicionarClienteComEndereco(ClienteComEnderecoDTO clienteComEnderecoDTO) {
        Cliente cliente = clienteComEnderecoDTO.getCliente();
        Endereco endereco = clienteComEnderecoDTO.getEndereco();

        // Validação: Verifica se o cliente tem pelo menos 18 anos
        if (calcularIdade(cliente.getDataNascimento()) < 18) {
            throw new IdadeMinimaException();
        }

        // Adiciona o cliente ao endereço
        endereco.setCliente(cliente);
        cliente.getEnderecos().add(endereco);

        // Salva o cliente e endereço
        clienteRepository.save(cliente);
        enderecoRepository.save(endereco);

        return cliente;
    }

    // Método auxiliar para calcular a idade com base na data de nascimento
    private int calcularIdade(LocalDate dataNascimento) {
        return Period.between(dataNascimento, LocalDate.now()).getYears();
    }

    public List<Cliente> listarTodos() {
        return clienteRepository.findAll();
    }

    public Cliente buscarClientePorId(Long id) {
        return clienteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cliente não encontrado"));
    }

    public Cliente atualizarDadosCliente(Long id, Cliente clienteAtualizado) {
        Cliente clienteExistente = clienteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cliente não encontrado"));

        clienteExistente.setNome(clienteAtualizado.getNome());
        clienteExistente.setEmail(clienteAtualizado.getEmail());
        clienteExistente.setCpf(clienteAtualizado.getCpf());
        clienteExistente.setDataNascimento(clienteAtualizado.getDataNascimento());
        clienteExistente.setTelefone(clienteAtualizado.getTelefone());

        return clienteRepository.save(clienteExistente);
    }

    public void deletarCliente(Long id) {
        Cliente cliente = clienteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cliente não encontrado"));
        clienteRepository.delete(cliente);
    }
}

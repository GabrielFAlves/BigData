package com.bigdata.ibmec.DTO;

import com.bigdata.ibmec.model.Cliente;
import com.bigdata.ibmec.model.Endereco;
import jakarta.validation.Valid;
import lombok.Data;

@Data
public class ClienteComEnderecoDTO {

    @Valid
    private Cliente cliente;

    @Valid
    private Endereco endereco;
}

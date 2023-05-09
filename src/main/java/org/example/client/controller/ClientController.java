package org.example.client.controller;

import org.example.client.model.ClientEntity;
import org.example.client.service.ClientService;

import java.sql.SQLException;
import java.util.List;

public class ClientController {
    private final ClientService clientService;

    public ClientController( final ClientService clientService) {
        this.clientService = clientService;
    }

    public ClientEntity create(ClientEntity clientEntity) throws SQLException {
        return clientService.create(clientEntity);
    }

    public ClientEntity update(ClientEntity clientEntity) throws SQLException{
        return clientService.update(clientEntity);
    }

    public void delete(Long id) throws SQLException{
        clientService.delete(id);
    }

    public List<ClientEntity> findAll() throws SQLException{
        return clientService.findAll();
    }
    public ClientEntity findById(Long id) throws SQLException{
        return clientService.findById(id);
    }
}

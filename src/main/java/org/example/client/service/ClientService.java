package org.example.client.service;


import org.example.client.model.ClientEntity;
import org.example.client.repository.ClientRepository;

import java.sql.SQLException;
import java.util.List;

public class ClientService {
    private final ClientRepository clientRepository ;

    public ClientService( final ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    public ClientEntity create(ClientEntity clientEntity) throws SQLException {
        return clientRepository.create(clientEntity);
    }

    public ClientEntity update(ClientEntity clientEntity) throws SQLException{
        return clientRepository.update(clientEntity);
    }

    public void delete(Long id) throws SQLException{
        clientRepository.delete(id);
    }

    public List<ClientEntity> findAll() throws SQLException{
        return clientRepository.findAll();
    }
    public ClientEntity findById(Long id) throws SQLException{
        return clientRepository.findById(id);
    }

}

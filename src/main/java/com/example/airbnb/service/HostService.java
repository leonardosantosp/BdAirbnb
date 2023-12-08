package com.example.airbnb.service;

import com.example.airbnb.entity.Host;
import com.example.airbnb.entity.User;
import com.example.airbnb.exception.HostNotFoundException;
import com.example.airbnb.exception.InvalidHostException;
import com.example.airbnb.exception.UserNotFoundException;
import com.example.airbnb.repository.HostRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@AllArgsConstructor
@Service
public class HostService {

    public final HostRepository repository;

    public List<Host> getAllHosts(){
        return repository.findAll();
    }

    public Host addHost(Host host){
        host.setAvailabilityStatus(false);
        host.setAverageRating(0.0);
        host.setNPlaceToBook(0);
        if(Objects.isNull(host) || Objects.isNull(host.getNPlaceToBook())
                || Objects.isNull(host.getAvailabilityStatus())
                || Objects.isNull(host.getAverageRating())

                || Objects.isNull(host.getUser())){
            throw new InvalidHostException("Erro ao cadastrar o host!");
        }
        if(repository.isUserReferenced(host.getUser().getIdUser())){
            throw new InvalidHostException("Chave duplicada! o usuário " + host.getUser().getName() + " já é um host!");
        }
        return repository.save(host);
    }

    public Host hostFindById(Integer id){
        if (Objects.isNull(id)){
            throw new IllegalArgumentException("Id null when fetching for an host.");
        }
        return repository.findById(id).orElseThrow(() ->
                new InvalidHostException(
                        String.format("No host found for id %d",id))
        );

    }

    public Host editHost(Integer id, Host newHost ){
        Host existingHost = repository.findById(id).
                orElseThrow(() -> new HostNotFoundException("Host não encontrado!"));

        existingHost.setNPlaceToBook(newHost.getNPlaceToBook());
        existingHost.setAvailabilityStatus(newHost.getAvailabilityStatus());
        existingHost.setAverageRating(newHost.getAverageRating());
        return repository.save(existingHost);
    }

}

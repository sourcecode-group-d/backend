package com.sourcecode.infrastructure.services;

import com.sourcecode.infrastructure.RequestRepository;
import com.sourcecode.models.Request;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
public class RequestServiceImp implements RequestService {
    @Autowired
    private RequestRepository requestRepository ;


    @Override
    public Request createRequest(Request request) {
        return requestRepository.save(request);
    }

    @Override
    public Request findRequest(Long id) {
        return requestRepository.findById(id).orElseThrow();
    }

    @Override
    public List<Request> getAllRequest() {
        return  requestRepository.findAll();
    }

    @Override
    public Request deleteRequest(Long id) {
        Request request = requestRepository.findById(id).orElseThrow();
        requestRepository.deleteById(id);
        return request;
    }
}

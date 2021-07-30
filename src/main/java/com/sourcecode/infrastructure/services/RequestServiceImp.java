package com.sourcecode.infrastructure.services;

import com.sourcecode.infrastructure.RequestRepository;
import com.sourcecode.models.Request;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
}

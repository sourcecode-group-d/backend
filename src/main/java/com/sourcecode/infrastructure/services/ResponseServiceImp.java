package com.sourcecode.infrastructure.services;

import com.sourcecode.infrastructure.ResponseRepository;
import com.sourcecode.models.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ResponseServiceImp implements ResponseService {

    @Autowired
    private ResponseRepository responseRepository;

    @Override
    public Response createResponse(Response response) {
        return responseRepository.save(response);
    }

    @Override
    public Response deleteResponse(Long id) {
        Response response = responseRepository.findById(id).orElseThrow();
        responseRepository.deleteById(id);
        return response;
    }
}

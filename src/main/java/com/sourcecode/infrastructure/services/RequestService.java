package com.sourcecode.infrastructure.services;

import com.sourcecode.models.Request;

import java.util.List;
import java.util.Set;

public interface RequestService {
    Request createRequest(Request request);

    Request findRequest(Long id);

    List<Request> getAllRequest();

    Request deleteRequest(Long id);

//    Request findAllByOrderByLikesCounterDesc();
}

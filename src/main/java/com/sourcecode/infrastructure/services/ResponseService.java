package com.sourcecode.infrastructure.services;

import com.sourcecode.models.Response;

public interface ResponseService {

    Response createResponse(Response response);

    Response deleteResponse(Long id);

    Response findResponse(Long id);
}

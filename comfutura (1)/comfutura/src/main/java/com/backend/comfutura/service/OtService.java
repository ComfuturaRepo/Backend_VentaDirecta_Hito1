package com.backend.comfutura.service;

import com.backend.comfutura.dto.request.OtCreateRequest;
import com.backend.comfutura.dto.response.OtResponse;

public interface OtService {
    OtResponse createOt(OtCreateRequest request);
}
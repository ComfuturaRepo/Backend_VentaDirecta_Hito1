package com.backend.comfutura.service.serviceImpl;

import com.backend.comfutura.dto.response.MaestroCodigoComboDTO;
import com.backend.comfutura.repository.MaestroCodigoRepository;
import com.backend.comfutura.service.MaestroCodigoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MaestroCodigoServiceImpl implements MaestroCodigoService {

    private final MaestroCodigoRepository repository;

    @Override
    public List<MaestroCodigoComboDTO> listarParaCombo() {
        return repository.listarParaCombo();
    }
}

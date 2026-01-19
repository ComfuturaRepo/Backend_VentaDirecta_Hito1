package com.backend.comfutura.service.serviceImpl;
import com.backend.comfutura.Exceptions.ResourceNotFoundException;
import com.backend.comfutura.dto.request.OtCreateRequest;
import com.backend.comfutura.dto.response.OtResponse;
import com.backend.comfutura.model.Area;
import com.backend.comfutura.model.Cliente;
import com.backend.comfutura.model.Ots;
import com.backend.comfutura.repository.AreaRepository;
import com.backend.comfutura.repository.ClienteRepository;
import com.backend.comfutura.repository.OtsRepository;
import com.backend.comfutura.service.OtService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class OtServiceImpl implements OtService {

    private final OtsRepository otsRepository;
    private final ClienteRepository clienteRepository;
    private final AreaRepository areaRepository;

    @Override
    @Transactional
    public OtResponse createOt(OtCreateRequest request) {

        // 1. Validar unicidad del número de OT
        if (otsRepository.existsByOt(request.getOt())) {
            throw new IllegalArgumentException("Ya existe una OT con el número: " + request.getOt());
        }

        // 2. Buscar cliente
        Cliente cliente = clienteRepository.findById(request.getIdCliente())
                .orElseThrow(() -> new ResourceNotFoundException("Cliente no encontrado con id: " + request.getIdCliente()));

        // 3. Buscar área
        Area area = areaRepository.findById(request.getIdArea())
                .orElseThrow(() -> new ResourceNotFoundException("Área no encontrada con id: " + request.getIdArea()));

        // 4. Crear entidad
        Ots ots = new Ots();
        ots.setOt(request.getOt());
        ots.setCeco(request.getCeco());
        ots.setCliente(cliente);
        ots.setArea(area);
        ots.setDescripcion(request.getDescripcion());
        ots.setFechaApertura(request.getFechaApertura());
        ots.setActivo(true);

        Ots saved = otsRepository.save(ots);

        // 5. Construir response
        return OtResponse.builder()
                .id(saved.getId())
                .ot(saved.getOt())
                .ceco(saved.getCeco())
                .idCliente(cliente.getId())
                .nombreCliente(cliente.getRazonSocial())
                .idArea(area.getId())
                .nombreArea(area.getNombre())
                .descripcion(saved.getDescripcion())
                .fechaApertura(saved.getFechaApertura())
                .activo(saved.getActivo())
                .fechaCreacion(saved.getFechaCreacion())
                .build();
    }
}
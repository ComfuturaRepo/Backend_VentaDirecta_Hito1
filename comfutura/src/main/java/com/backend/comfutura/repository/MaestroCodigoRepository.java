package com.backend.comfutura.repository;

import com.backend.comfutura.dto.response.MaestroCodigoComboDTO;
import com.backend.comfutura.model.MaestroCodigo;
import com.backend.comfutura.model.Trabajador;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface MaestroCodigoRepository extends JpaRepository<MaestroCodigo,Integer> {
    List<MaestroCodigo> findByActivoTrueOrderByCodigoAsc();
    @Query("""
        SELECT new com.backend.comfutura.dto.response.MaestroCodigoComboDTO(
            m.id,
            m.codigo,
            m.descripcion
        )
        FROM MaestroCodigo m
        WHERE m.activo = true
        ORDER BY m.codigo
    """)
    List<MaestroCodigoComboDTO> listarParaCombo();
}

package com.backend.comfutura.repository;

import com.backend.comfutura.dto.response.OcDetalleResponseDTO;
import com.backend.comfutura.model.OcDetalle;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface OcDetalleRepository extends JpaRepository<OcDetalle, Integer> {

    // 🔹 Paginado
    Page<OcDetalle> findByOrdenCompra_IdOc(Integer idOc, Pageable pageable);

    // 🔹 No paginado (opcional, útil para cálculos)
    List<OcDetalle> findByOrdenCompra_IdOc(Integer idOc);

    // 🔹 Borrado por OC
    @Query("""
    SELECT new com.backend.comfutura.dto.response.OcDetalleResponseDTO(
        d.idOcDetalle,
        m.id,
        m.codigo,
        m.descripcion,
          um.descripcion,
        d.cantidad,
        d.precioUnitario,
        d.subtotal,
        d.igv,
        d.total,
        d.ordenCompra.idOc
    )
    FROM OcDetalle d
    JOIN MaestroCodigo m ON m.id = d.idMaestro
    LEFT JOIN m.unidadMedida um
    WHERE d.ordenCompra.idOc = :idOc
""")
    Page<OcDetalleResponseDTO> listarPorOcDTO(
            @Param("idOc") Integer idOc,
            Pageable pageable
    );

}
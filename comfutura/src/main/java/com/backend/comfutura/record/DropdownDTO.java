package com.backend.comfutura.record;

public record DropdownDTO(
        Integer id,
        String label,
        String adicional,
        Boolean estado
) {
    // Constructor secundario (adicional opcional)
    public DropdownDTO(Integer id, String label) {
        this(id, label, null,null);
    }

    }


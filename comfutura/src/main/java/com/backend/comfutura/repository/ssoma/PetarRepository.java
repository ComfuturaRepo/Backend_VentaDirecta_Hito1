package com.backend.comfutura.repository.ssoma;

import com.backend.comfutura.model.ssoma.petar.Petar;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface PetarRepository extends JpaRepository<Petar, Integer> {

    @Query("SELECT p FROM Petar p")
    List<Petar> findAllPetar();

    List<Petar> findByNumeroRegistro(String numeroRegistro);
}
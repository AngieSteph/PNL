package com.pnl.municipio.service;

import com.pnl.municipio.entity.Denuncia;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface DenunciaService {
    List<Denuncia> findAll(Pageable page);
    List<Denuncia> findAll();
    List<Denuncia> findByTitulo(String titulo, Pageable page);
    Denuncia findById(int id);
    Denuncia save(Denuncia denuncia);
    void delete(int id);
}
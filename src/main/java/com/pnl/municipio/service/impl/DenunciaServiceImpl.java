package com.pnl.municipio.service.impl;

import com.pnl.municipio.entity.Denuncia;
import com.pnl.municipio.exception.GeneralException;
import com.pnl.municipio.exception.NoDataFoundException;
import com.pnl.municipio.exception.ValidateException;
import com.pnl.municipio.repository.DenunciaRepository;
import com.pnl.municipio.service.DenunciaService;
import com.pnl.municipio.validator.DenunciaValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class DenunciaServiceImpl implements DenunciaService {

    @Autowired
    private DenunciaRepository repository;

    @Override
    @Transactional(readOnly = true)
    public List<Denuncia> findAll(Pageable page) {
        try {
            return repository.findAll(page).toList();
        } catch (ValidateException | NoDataFoundException e) {
            throw e;
        } catch (Exception e) {
            throw new GeneralException("Error del servidor");
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<Denuncia> findByTitulo(String titulo, Pageable page) {
        try {
            return repository.findByTituloContaining(titulo, page);
        } catch (ValidateException | NoDataFoundException e) {
            throw e;
        } catch (Exception e) {
            throw new GeneralException("Error del servidor");
        }
    }

    @Override
    @Transactional(readOnly = true)
    public Denuncia findById(int id) {
        try {
            return repository.findById(id)
                    .orElseThrow(() -> new NoDataFoundException("No existe una denuncia con ese ID"));
        } catch (ValidateException | NoDataFoundException e) {
            throw e;
        } catch (Exception e) {
            throw new GeneralException("Error del servidor");
        }
    }

    @Override
    @Transactional
    public Denuncia save(Denuncia denuncia) {
        try {
            DenunciaValidator.validate(denuncia);
    
            // Si el id es null o 0, es una nueva denuncia
            if (denuncia.getId() == null || denuncia.getId() == 0) {
                return repository.save(denuncia);  // Crear nueva denuncia
            }
    
            // Verificar si existe el registro antes de actualizar
            Denuncia registroExistente = repository.findById(denuncia.getId())
                    .orElseThrow(() -> new NoDataFoundException("No existe una denuncia con ese ID"));
    
            // Actualizar los campos
            registroExistente.setTitulo(denuncia.getTitulo());
            registroExistente.setDescripcion(denuncia.getDescripcion());
            registroExistente.setUbicacion(denuncia.getUbicacion());
            registroExistente.setEstado(denuncia.getEstado());
            registroExistente.setCiudadano(denuncia.getCiudadano());
            registroExistente.setTelefonoCiudadano(denuncia.getTelefonoCiudadano());
    
            return registroExistente;  // Sin `save`, solo devolvemos el registro actualizado
    
        } catch (ValidateException | NoDataFoundException e) {
            throw e;
        } catch (Exception e) {
            throw new GeneralException("Error del servidor");
        }
    }    
    @Override
    @Transactional
    public void delete(int id) {
        try {
            Denuncia registro = repository.findById(id)
                    .orElseThrow(() -> new NoDataFoundException("No existe una denuncia con ese ID"));
            repository.delete(registro);
        } catch (ValidateException | NoDataFoundException e) {
            throw e;
        } catch (Exception e) {
            throw new GeneralException("Error del servidor");
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<Denuncia> findAll() {
        try {
            return repository.findAll();
        } catch (ValidateException | NoDataFoundException e) {
            throw e;
        } catch (Exception e) {
            throw new GeneralException("Error del servidor");
        }
    }
}
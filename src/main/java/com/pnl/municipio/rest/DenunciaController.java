package com.pnl.municipio.rest;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.pnl.municipio.converter.DenunciaConverter;
import com.pnl.municipio.dto.DenunciaDto;
import com.pnl.municipio.entity.Denuncia;
import com.pnl.municipio.service.DenunciaService;
import com.pnl.municipio.util.WrapperResponse;

@RestController
@RequestMapping("/v1/denuncias")
//localhost:8090/v1/denuncias versionado en la URI
public class DenunciaController {
	@Autowired
	private DenunciaService service;
	
	@Autowired
	private DenunciaConverter converter;
	
	@GetMapping
	public ResponseEntity<List<DenunciaDto>> findAll(
			@RequestParam(value = "offset", required = false, defaultValue = "0") int pageNumber,
			@RequestParam(value = "limit", required = false, defaultValue = "5") int pageSize
			){
		Pageable page = PageRequest.of(pageNumber, pageSize);				
		List<DenunciaDto> denuncias = converter.fromEntity(service.findAll());
		
		return new WrapperResponse(true, "success", denuncias).createResponse(HttpStatus.OK);
	}
	
	@PostMapping
	public ResponseEntity<DenunciaDto> create (@RequestBody DenunciaDto denuncia){
		Denuncia denunciaEntity=converter.fromDTO(denuncia);
		DenunciaDto registro = converter.fromEntity(service.save(denunciaEntity));
		return new WrapperResponse(true, "success", registro).createResponse(HttpStatus.CREATED);
	}
	
	@PutMapping("/{id}")
	public ResponseEntity<DenunciaDto> update(@PathVariable("id") int id, @RequestBody DenunciaDto denuncia) {
		// Convierte el DTO en entidad y asegura que el id está configurado para la actualización
		Denuncia denunciaEntity = converter.fromDTO(denuncia);
		denunciaEntity.setId(id);  // Asigna el id de la ruta al objeto de denuncia para evitar creación
	
		// Guarda el registro actualizado
		DenunciaDto registro = converter.fromEntity(service.save(denunciaEntity));
		return new WrapperResponse(true, "success", registro).createResponse(HttpStatus.OK);
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity delete(@PathVariable("id") int id){
		service.delete(id);
		return new WrapperResponse(true, "success", null).createResponse(HttpStatus.OK);
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<DenunciaDto> findById(@PathVariable("id") int id){
		DenunciaDto registro=converter.fromEntity(service.findById(id));
		return new WrapperResponse(true, "success", registro).createResponse(HttpStatus.OK);
	}
	
}

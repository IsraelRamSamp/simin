package mx.dgtic.unam.simin.service.dto.impl;

import mx.dgtic.unam.simin.dto.DetalleSimulacionDto;
import mx.dgtic.unam.simin.entity.DetalleSimulacion;
import mx.dgtic.unam.simin.repository.DetalleSimulacionRepository;
import mx.dgtic.unam.simin.service.dto.DetalleSimulacionDtoService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;
import jakarta.annotation.PostConstruct;

@Service
public class DetalleSimulacionDtoServiceImpl implements DetalleSimulacionDtoService {

    @Autowired
    private DetalleSimulacionRepository detalleSimulacionRepository;

    @Autowired
    private ModelMapper modelMapper;

    @PostConstruct
    public void configurarModelMapper() {
        modelMapper.typeMap(DetalleSimulacion.class, DetalleSimulacionDto.class)
                .addMappings(mapper -> {
                    mapper.map(DetalleSimulacion::getPlazo, DetalleSimulacionDto::setPlazoTexto);
                    mapper.map(DetalleSimulacion::getInversionBonddia, DetalleSimulacionDto::setInversionBonddia);
                    mapper.map(DetalleSimulacion::getInteresBonddia, DetalleSimulacionDto::setInteresBonddia);
                    mapper.map(DetalleSimulacion::getTitulosBonddia, DetalleSimulacionDto::setTitulosBonddia);
                    mapper.map(DetalleSimulacion::getValorFinal, DetalleSimulacionDto::setValorFinal);
                });

        modelMapper.typeMap(DetalleSimulacionDto.class, DetalleSimulacion.class)
                .addMappings(mapper -> {
                    mapper.map(DetalleSimulacionDto::getPlazoTexto, DetalleSimulacion::setPlazo);
                    mapper.map(DetalleSimulacionDto::getInversionBonddia, DetalleSimulacion::setInversionBonddia);
                    mapper.map(DetalleSimulacionDto::getInteresBonddia, DetalleSimulacion::setInteresBonddia);
                    mapper.map(DetalleSimulacionDto::getTitulosBonddia, DetalleSimulacion::setTitulosBonddia);
                    mapper.map(DetalleSimulacionDto::getValorFinal, DetalleSimulacion::setValorFinal);
                });
    }

    @Override
    public List<DetalleSimulacionDto> obtenerPorIdSimulacion(Integer idSimulacion) {
        List<DetalleSimulacion> detalles = detalleSimulacionRepository.findBySimulacion_IdSimulacion(idSimulacion);
        return detalles.stream()
                .map(detalle -> modelMapper.map(detalle, DetalleSimulacionDto.class))
                .collect(Collectors.toList());
    }
}
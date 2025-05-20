package mx.dgtic.unam.simin.controller;

import mx.dgtic.unam.simin.entity.Instrumento;
import mx.dgtic.unam.simin.service.InstrumentoService;
import mx.dgtic.unam.simin.service.TipoInstrumentoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/instrumentos/consulta")
public class InstrumentosConsultaController {

    @Autowired
    private InstrumentoService instrumentoService;

    @Autowired
    private TipoInstrumentoService tipoInstrumentoService;

    @GetMapping("/list-public")
    public String listarInstrumentosPublicos(
            @RequestParam(defaultValue = "") String nombre,
            @RequestParam(required = false) Integer idTipoInstrumento,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "nombre") String sortField,
            @RequestParam(defaultValue = "asc") String sortDir,
            Model model) {

        Page<Instrumento> pageInstrumentos = instrumentoService.buscarInstrumentos(nombre, idTipoInstrumento, sortField, sortDir, PageRequest.of(page, 5));

        model.addAttribute("instrumentos", pageInstrumentos.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", pageInstrumentos.getTotalPages());
        model.addAttribute("sortField", sortField);
        model.addAttribute("sortDir", sortDir);
        model.addAttribute("nombre", nombre);
        model.addAttribute("selectedTipoInstrumento", idTipoInstrumento);
        model.addAttribute("tiposInstrumento", tipoInstrumentoService.findAll());

        return "instrumentos/consulta/list-public";
    }

    @GetMapping("/search")
    public String buscarInstrumentosPublicos(
            @RequestParam(value = "nombre", required = false) String nombre,
            @RequestParam(value = "idTipoInstrumento", required = false) Integer idTipoInstrumento,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "sortField", defaultValue = "nombre") String sortField,
            @RequestParam(value = "sortDir", defaultValue = "asc") String sortDir,
            Model model
    ) {
        Pageable pageable = PageRequest.of(page, 5); // cambia el tamaño si lo deseas
        Page<Instrumento> pageResult = instrumentoService.buscarInstrumentos(
                nombre, idTipoInstrumento, sortField, sortDir, pageable);

        model.addAttribute("instrumentos", pageResult.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", pageResult.getTotalPages());
        model.addAttribute("nombre", nombre);
        model.addAttribute("selectedTipoInstrumento", idTipoInstrumento);
        model.addAttribute("sortField", sortField);
        model.addAttribute("sortDir", sortDir);
        model.addAttribute("tiposInstrumento", tipoInstrumentoService.findAll());

        return "instrumentos/consulta/list-public";
    }
}

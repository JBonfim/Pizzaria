package br.com.jabes.pizzaria.controladores;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import br.com.jabes.pizzaria.excecoes.IngredienteInvalidoException;
import br.com.jabes.pizzaria.modelo.entidades.Ingrediente;
import br.com.jabes.pizzaria.modelo.enumeracoes.CategoriaDeIngrediente;
import br.com.jabes.pizzaria.modelo.repositorios.IngredienteRepositorio;

@Controller
@RequestMapping("/ingredientes")
public class IngredienteController {
	
	@Autowired private IngredienteRepositorio ingredientesRepositorio;

	@RequestMapping(method=RequestMethod.GET)
	public String listarIngredientes(Model model) {
		Iterable<Ingrediente> ingredientes = ingredientesRepositorio.findAll();
		
		model.addAttribute("titulo", "Meu titulo");
		model.addAttribute("ingredientes", ingredientes);
		model.addAttribute("categorias", CategoriaDeIngrediente.values());
		return "ingrediente/listagem";
	}
	
	@RequestMapping(method=RequestMethod.POST)
	public String salvarIgrediente(@Valid @ModelAttribute Ingrediente ingrediente,BindingResult bindResult,Model model) {
		if(bindResult.hasErrors()) {
			throw new IngredienteInvalidoException();
		}else {
			ingredientesRepositorio.save(ingrediente);
			
			
		}
		
		model.addAttribute("titulo", "Meu titulo");
		model.addAttribute("ingredientes", ingredientesRepositorio.findAll());
		model.addAttribute("categorias", CategoriaDeIngrediente.values());
		
		
		return "ingrediente/tabela-ingredientes";
	}
	
	@RequestMapping(method=RequestMethod.DELETE, value="/{id}")
	public ResponseEntity<String> deletarIngrediente(@PathVariable Long id) {
		try {
			ingredientesRepositorio.delete(id);
			return new ResponseEntity<String>(HttpStatus.OK);
		}catch(Exception ex) {
			return new ResponseEntity<String>(HttpStatus.BAD_REQUEST);
		}
		
	}
	
	@RequestMapping(method=RequestMethod.GET, value="/{id}")
	@ResponseBody
	public Ingrediente buscarIngrediente(@PathVariable Long id) {
		Ingrediente ingrediente = ingredientesRepositorio.findOne(id);
		return ingrediente;
	}
}

package br.com.jabes.pizzaria.controladores;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import br.com.jabes.pizzaria.excecoes.IngredienteInvalidoException;
import br.com.jabes.pizzaria.modelo.entidades.Ingrediente;
import br.com.jabes.pizzaria.modelo.entidades.Pizza;
import br.com.jabes.pizzaria.modelo.enumeracoes.CategoriaDeIngrediente;
import br.com.jabes.pizzaria.modelo.enumeracoes.CategoriaDePizza;
import br.com.jabes.pizzaria.modelo.repositorios.IngredienteRepositorio;
import br.com.jabes.pizzaria.modelo.repositorios.PizzaRepositorio;
import br.com.jabes.pizzaria.propertyeditors.IngredientePropertyEditor;

@Controller
@RequestMapping("/pizzas")
public class PizzaController {
	
	@Autowired private IngredientePropertyEditor ingredientePropertyEditor;
	

	@Autowired private PizzaRepositorio pizzaRepositorio;
	@Autowired private IngredienteRepositorio ingredienteRepositorio;
	
	@RequestMapping("/quantas")
	@ResponseBody
	public String quantasPizzas() {
		return "Atualmente há " + pizzaRepositorio.count() + " Cadastradas";
	}
	
	@RequestMapping(method=RequestMethod.GET)
	public String listarPizzas(Model model) {
		model.addAttribute("pizzas", pizzaRepositorio.findAll());
		model.addAttribute("categorias", CategoriaDePizza.values());
		model.addAttribute("ingredientes", ingredienteRepositorio.findAll());
		
		return "pizza/listagem";
	}
	
	@RequestMapping(method=RequestMethod.POST)
	public String salvarIgrediente(@Valid @ModelAttribute Pizza pizza,BindingResult bindResult,Model model) {
		if(bindResult.hasErrors()) {
			throw new IngredienteInvalidoException();
		}else {
			pizzaRepositorio.save(pizza);
			
			
		}
		
		 
		model.addAttribute("pizzas", pizzaRepositorio.findAll());
		//model.addAttribute("categorias", CategoriaDeIngrediente.values());
		
		
		return "pizza/tabela-pizzas";
	}
	
	@RequestMapping(method=RequestMethod.DELETE, value="/{pizzaid}")
	public ResponseEntity<String> deletarIngrediente(@PathVariable Long pizzaid) {
		try {
			pizzaRepositorio.delete(pizzaid);
			return new ResponseEntity<String>(HttpStatus.OK);
		}catch(Exception ex) {
			return new ResponseEntity<String>(HttpStatus.BAD_REQUEST);
		}
		
	}
	
	@InitBinder
	public void initBinder(WebDataBinder webDataBinder){
		webDataBinder.registerCustomEditor(Ingrediente.class, ingredientePropertyEditor);
	}
	
	@RequestMapping(method=RequestMethod.GET, value="/{id}")
	@ResponseBody
	public Pizza buscarPizzas(@PathVariable Long id) {
		Pizza pizza = pizzaRepositorio.findOne(id);
		return pizza;
	}
	
}

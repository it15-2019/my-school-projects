package rva.ctrls;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import rva.jpa.Departman;
import rva.jpa.Departman;

@CrossOrigin
@RestController
@Api(tags = {"Departman CRUD operacije"})
public class DepartmanRestController {
	
	@Autowired
	private rva.repositores.DepartmanRepository departmanRepository;
	
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	@GetMapping("departman")
	@ApiOperation(value = "Vraca kolekciju svih departmana iz baze podataka")
	public Collection<Departman> getAllDepartman() {
		return departmanRepository.findAll();
	}
	
	@GetMapping("departmanId/{id}") 
	@ApiOperation(value = "Vraca departman u odnosu na posledjenu vrednost path varijable id")
	public Departman getDepartmanById(@PathVariable("id") Integer id) {
		return departmanRepository.getOne(id);
	}
	
	@GetMapping("departmanNaziv/{naziv}")
	@ApiOperation(value = "Vraca kolekciju departmana koji imaju naziv koje sadrzi vrednost prosledjenu u okviru path varijable naziv")
	public Collection<Departman> getDepartmanByNaziv(@PathVariable("naziv") String naziv) {
		return departmanRepository.findByNazivContainingIgnoreCase(naziv);
	}
	
	@PostMapping("departman")
	@ApiOperation(value = "Dodaje novi departman u bazu podataka.")
	public ResponseEntity<Departman> insertDepartman(@RequestBody Departman departman) {
		if(!departmanRepository.existsById(departman.getId())) {
			departmanRepository.save(departman);
			return new ResponseEntity<Departman>(HttpStatus.OK);
		}
		return new ResponseEntity<Departman>(HttpStatus.CONFLICT);
	}
	
	@PutMapping("departman")
	@ApiOperation(value = "Azurira postojeci departman.")
	public ResponseEntity<Departman> updateDepartman(@RequestBody Departman departman) {
		if(departmanRepository.existsById(departman.getId())) {
			departmanRepository.save(departman);
			return new ResponseEntity<Departman>(HttpStatus.OK);
		}
		return new ResponseEntity<Departman>(HttpStatus.NO_CONTENT);	
	}
	
	@DeleteMapping("departman/{id}")
	@ApiOperation(value = "Brise departman u odnosu na vrednost prosledjene path varijable id.")
	public ResponseEntity<Departman> deleteDepartman(@PathVariable("id") Integer id) {
		if(departmanRepository.existsById(id)) {
			departmanRepository.deleteById(id);
			
			if(id == -100) {
				jdbcTemplate.execute("insert into departman (id, naziv, oznaka, fakultet) values (-100, 'Test naziv', 'Test', -100)");
			}
			return new ResponseEntity<Departman>(HttpStatus.OK);
		}
		return new ResponseEntity<Departman>(HttpStatus.NO_CONTENT);
	}
}

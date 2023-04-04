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
import rva.jpa.Fakultet;
import rva.jpa.Status;
import rva.repositores.StatusRepository;

@CrossOrigin
@RestController
@Api(tags = {"Status CRUD operacije"})
public class StatusRestController {

	@Autowired
	private StatusRepository statusRepository;
	
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	@GetMapping("status")
	@ApiOperation(value = "Vraca kolekciju svih statusa iz baze podataka")
	public Collection<Status> getAllStatus() {
		return statusRepository.findAll();
	}
	
	@GetMapping("statusId/{id}")
	@ApiOperation(value = "Vraca status u odnosu na posledjenu vrednost path varijable id")
	public Status getStatusById(@PathVariable("id") Integer id) {
		return statusRepository.getOne(id); 
	}
	
	@GetMapping("statusOznaka/{oznaka}")
	@ApiOperation(value = "Vraca kolekciju statusa koji imaju oznaku koji sadrzi vrednost prosledjenu u okviru path varijable oznaka")
	public Collection<Status> getStatusByOznaka(@PathVariable("oznaka") String oznaka) {
		return statusRepository.findByOznakaContainingIgnoreCase(oznaka);
	}
	
	@PostMapping("status")
	@ApiOperation(value = "Dodaje novi status u bazu podataka.")
	public ResponseEntity<Status> insertStatus(@RequestBody Status status) {
		if(!statusRepository.existsById(status.getId())) {
			statusRepository.save(status);
			return new ResponseEntity<Status>(HttpStatus.OK);
		}
		return new ResponseEntity<Status>(HttpStatus.CONFLICT);
	}
	
	@PutMapping("status")
	@ApiOperation(value = "Azurira postojeci status.")
	public ResponseEntity<Status> updateStatus(@RequestBody Status status) {
		if(statusRepository.existsById(status.getId())) {
			statusRepository.save(status);
			
			return new ResponseEntity<Status>(HttpStatus.OK);
		}
		return new ResponseEntity<Status>(HttpStatus.NO_CONTENT);	
	}
	
	@DeleteMapping("status/{id}")
	@ApiOperation(value = "Brise status u odnosu na vrednost prosledjene path varijable id.")
	public ResponseEntity<Status> deleteStatus(@PathVariable("id") Integer id) {
		if(statusRepository.existsById(id)) {
			statusRepository.deleteById(id);
			
			if (id == -100) 
			{
				jdbcTemplate.execute("insert into status (id, naziv, oznaka) values (-100, 'Test naziv', 'Test')");
			}
			return new ResponseEntity<Status>(HttpStatus.OK);
		}
		return new ResponseEntity<Status>(HttpStatus.NO_CONTENT);
	}
}

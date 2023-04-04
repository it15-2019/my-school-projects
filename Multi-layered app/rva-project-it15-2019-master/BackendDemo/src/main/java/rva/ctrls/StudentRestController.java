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
import rva.jpa.Status;
import rva.jpa.Student;
import rva.repositores.DepartmanRepository;

@CrossOrigin
@RestController
@Api(tags = {"Student CRUD operacije"})
public class StudentRestController {
	
	@Autowired
	private rva.repositores.StudentRepository studentRepository;
	
	@Autowired
	private rva.repositores.DepartmanRepository departmanRepository;
	
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	@GetMapping("student")
	@ApiOperation(value = "Vraca kolekciju svih studenata iz baze podataka")
	public Collection<Student> getStudent() {
		return studentRepository.findAll();
	}
	
	@GetMapping("studentId/{id}")
	@ApiOperation(value = "Vraca studenta u odnosu na posledjenu vrednost path varijable id")
	public Student getStudentById(@PathVariable("id") Integer id) {
		return studentRepository.getOne(id); 
	}
	
	@GetMapping("studentNaDepartmanuId/{id}")
	@ApiOperation(value = "Vraca studenta u odnosu na id departmana")
	public Collection<Student> getStudentPoDepartmanu(@PathVariable("id") Integer id) {
		Departman d = departmanRepository.getOne(id);
		return studentRepository.findByDepartman(d);
	}
	
	@GetMapping("studentIme/{ime}")
	@ApiOperation(value = "Vraca kolekciju studenata koji imaju ime koje sadrzi vrednost prosledjenu u okviru path varijable ime")
	public Collection<Student> getStudentByIme(@PathVariable("ime") String ime) {
		return studentRepository.findByImeContainingIgnoreCase(ime);
	}
	
	@PostMapping("student")
	@ApiOperation(value = "Dodaje novog studenta u bazu podataka.")
	public ResponseEntity<Student> insertStudent(@RequestBody Student student) {
		if(!studentRepository.existsById(student.getId())) {
			studentRepository.save(student);
			return new ResponseEntity<Student>(HttpStatus.OK);
		}
		return new ResponseEntity<Student>(HttpStatus.CONFLICT);
	}
	
	@PutMapping("student")
	@ApiOperation(value = "Azurira postojeceg studenta.")
	public ResponseEntity<Student> updateStudent(@RequestBody Student student) {
		if(studentRepository.existsById(student.getId())) {
			studentRepository.save(student);
			return new ResponseEntity<Student>(HttpStatus.OK);
		}
		return new ResponseEntity<Student>(HttpStatus.NO_CONTENT);	
	}
	
	@DeleteMapping("student/{id}")
	@ApiOperation(value = "Brise studenta u odnosu na vrednost prosledjene path varijable id.")
	public ResponseEntity<Student> deleteStudent(@PathVariable("id") Integer id) {
		if(!studentRepository.existsById(id)) {
			new ResponseEntity<Student>(HttpStatus.NO_CONTENT);
		}
		studentRepository.deleteById(id);
		if(id == -100) {
			jdbcTemplate.execute("insert into student (id, ime, prezime, broj_indeksa, status, departman) "
					+ "values (-100, 'Test ime', 'Test prezime', 'Test broj indeksa', -100, -100)");
		}
		return new ResponseEntity<Student>(HttpStatus.OK);

	}
}

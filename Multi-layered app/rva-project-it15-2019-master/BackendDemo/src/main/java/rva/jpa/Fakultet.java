package rva.jpa;

import java.io.Serializable;
import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;


@JsonIgnoreProperties({"hibernateLazyInitialize", "handler"})
@Entity
@Table(name = "fakultet")
@NamedQuery(name = "Fakultet.findAll", query = "SELECT f FROM Fakultet f")
public class Fakultet implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name = "FAKULTET_ID_GENERATOR", sequenceName = "SEQ_FAKULTET", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "FAKULTET_ID_GENERATOR")
	private Integer id;

	private String naziv;

	private String sediste;

	@JsonIgnore
	@OneToMany(mappedBy = "fakultet", cascade = {CascadeType.DETACH, CascadeType.REMOVE})
	private List<Departman> departmans;

	public Fakultet() {
	}

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getNaziv() {
		return this.naziv;
	}

	public void setNaziv(String naziv) {
		this.naziv = naziv;
	}

	public String getSediste() {
		return this.sediste;
	}

	public void setSediste(String sediste) {
		this.sediste = sediste;
	}

	public List<Departman> getDepartmans() {
		return this.departmans;
	}

	public void setDepartman(List<Departman> departmans) {
		this.departmans = departmans;
	}

	public Departman addDepartman(Departman departman) {
		getDepartmans().add(departman);
		departman.setFakultet(this);

		return departman;
	}

	public Departman removeDepartman(Departman departman) {
		getDepartmans().remove(departman);
		departman.setFakultet(null);

		return departman;
	}

}
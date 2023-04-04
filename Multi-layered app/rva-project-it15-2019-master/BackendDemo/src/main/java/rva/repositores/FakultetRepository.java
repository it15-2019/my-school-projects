package rva.repositores;

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;

import rva.jpa.Fakultet;

public interface FakultetRepository extends JpaRepository <Fakultet, Integer> {

	Collection<Fakultet> findBySedisteContainingIgnoreCase(String sediste);

}

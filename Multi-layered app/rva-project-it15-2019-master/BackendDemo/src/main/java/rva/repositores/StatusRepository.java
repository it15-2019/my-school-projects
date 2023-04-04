package rva.repositores;

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;

import rva.jpa.Status;

public interface StatusRepository extends JpaRepository <Status, Integer>{

	Collection<Status> findByOznakaContainingIgnoreCase(String oznaka);
}

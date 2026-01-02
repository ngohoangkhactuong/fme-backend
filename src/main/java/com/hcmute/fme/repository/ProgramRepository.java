package com.hcmute.fme.repository;

import com.hcmute.fme.entity.Program;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProgramRepository extends JpaRepository<Program, Long> {
    
    Optional<Program> findByCode(String code);
    
    List<Program> findByTypeAndIsActiveTrueOrderByNameAsc(Program.ProgramType type);
    
    List<Program> findByIsActiveTrueOrderByTypeAscNameAsc();
    
    boolean existsByCode(String code);
}

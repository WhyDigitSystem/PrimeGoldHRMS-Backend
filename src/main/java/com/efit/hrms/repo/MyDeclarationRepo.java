package com.efit.hrms.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.efit.hrms.entity.MyDeclarationVO;

@Repository
public interface MyDeclarationRepo extends JpaRepository<MyDeclarationVO, Long>{

	@Query(value = "SELECT 1 AS id, 'otherdeclaration' AS screenname, COUNT(*) AS recordcount, SUM(maxlimit) AS totalmaxlimit FROM otherdeclaration WHERE declarationid = ?1 " +
            "UNION ALL " +
            "SELECT 2 AS id, 'onecrorefivelacdeduction' AS screenname, COUNT(*) AS recordcount, SUM(maxlimit) AS totalmaxlimit FROM onecrorefivelacdeduction WHERE declarationid = ?1 " +
            "UNION ALL " +
            "SELECT 3 AS id, 'taxsavingallowances' AS screenname, COUNT(*) AS recordcount, SUM(maxexemptionlimit) AS totalmaxlimit FROM taxsavingallowances WHERE declarationid = ?1", 
    nativeQuery = true)
List<Object[]> getMyDeclarations(Long declarationId);


}

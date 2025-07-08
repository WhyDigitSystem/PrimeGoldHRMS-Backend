package com.efit.hrms.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.efit.hrms.entity.DeclarationVO;
import com.efit.hrms.entity.TaxSavingAllowancesVO;

@Repository
public interface TaxSavingAllowancesRepo extends JpaRepository< TaxSavingAllowancesVO, Long>{

	List<TaxSavingAllowancesVO> findByDeclarationVO(DeclarationVO declaration);

}

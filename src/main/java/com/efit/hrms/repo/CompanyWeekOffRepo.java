package com.efit.hrms.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.efit.hrms.entity.CompanyVO;
import com.efit.hrms.entity.CompanyWeekOffVO;

@Repository
public interface CompanyWeekOffRepo extends JpaRepository<CompanyWeekOffVO, Long>{

	List<CompanyWeekOffVO> findByCompanyVO(CompanyVO companyVO);
    

    @Query(value = "SELECT * FROM companyweekoff WHERE companyid = :companyId", nativeQuery = true)
    List<CompanyWeekOffVO> findByCompanyId(@Param("companyId") Long companyId);

    @Query(value = " SELECT \r\n"
    		+ "        w.weekoffdays, \r\n"
    		+ "        p.weeknumber \r\n"
    		+ "    FROM \r\n"
    		+ "        companyweekoff w\r\n"
    		+ "    JOIN \r\n"
    		+ "        weekoffoccurrences p ON w.companyweekoffid = p.companyweekoffid\r\n"
    		+ "    JOIN \r\n"
    		+ "        company a ON a.companyid = w.companyid\r\n"
    		+ "    JOIN \r\n"
    		+ "        branch b ON a.companyid = b.orgid\r\n"
    		+ "    WHERE \r\n"
    		+ "        a.companyid = ?1 \r\n"
    		+ "        AND b.branchcode = ?2", nativeQuery = true)
	List<Object[]> findWeekOffDaysAndWeeks(Long orgId, String branchCode);


	@Query(value = "SELECT cw.weekoffdays, cwk.weeknumber " +
            "FROM companyweekoff cw " +
            "JOIN weekoffoccurrences cwk ON cw.companyweekoffid = cwk.companyweekoffid " +
            "WHERE cw.companyid = ?1", nativeQuery = true)
List<Object[]> findWeekOffOccurrencesByCompanyId(Long companyId);
}


	
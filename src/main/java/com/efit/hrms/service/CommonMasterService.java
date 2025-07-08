package com.efit.hrms.service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.efit.hrms.dto.CityDTO;
import com.efit.hrms.dto.CompanyDTO;
import com.efit.hrms.dto.CountryDTO;
import com.efit.hrms.dto.CurrencyDTO;
import com.efit.hrms.dto.DepartmentDTO;
import com.efit.hrms.dto.DesignationDTO;
import com.efit.hrms.dto.FinScreenDTO;
import com.efit.hrms.dto.FinancialYearDTO;
import com.efit.hrms.dto.LeaveRequestDTO;
import com.efit.hrms.dto.LeaveTypeDTO;
import com.efit.hrms.dto.RegionDTO;
import com.efit.hrms.dto.RolesDTO;
import com.efit.hrms.dto.ScreenNamesDTO;
import com.efit.hrms.dto.StateDTO;
import com.efit.hrms.entity.CityVO;
import com.efit.hrms.entity.CompanyVO;
import com.efit.hrms.entity.CountryVO;
import com.efit.hrms.entity.CurrencyVO;
import com.efit.hrms.entity.DepartmentVO;
import com.efit.hrms.entity.DesignationVO;
import com.efit.hrms.entity.EmployeeVO;
import com.efit.hrms.entity.FinancialYearVO;
import com.efit.hrms.entity.LeaveRequestVO;
import com.efit.hrms.entity.LeaveTypeVO;
import com.efit.hrms.entity.RegionVO;
import com.efit.hrms.entity.RolesVO;
import com.efit.hrms.entity.ScreenNamesVO;
import com.efit.hrms.entity.StateVO;
import com.efit.hrms.exception.ApplicationException;

import io.jsonwebtoken.io.IOException;

@Service
public interface CommonMasterService {

	// Country

	List<CountryVO> getAllCountry(Long orgid); // Method names should be in camelCase

	Optional<CountryVO> getCountryById(Long countryid);

	Map<String, Object> createUpdateCountry(CountryDTO countryDTO) throws ApplicationException; // Return the created
																								// entity

	void deleteCountry(Long countryid);

	// State

	List<StateVO> getAllgetAllStates(Long orgid);

	Optional<StateVO> getStateById(Long stateid);

	List<StateVO> getStatesByCountry(Long orgid, String country);

	Map<String, Object> createUpdateState(StateDTO stateDTO) throws ApplicationException;

	void deleteState(Long stateid);

	// city

	List<CityVO> getAllgetAllCities(Long orgid);

	List<CityVO> getAllCitiesByState(Long orgid, String state);

	Optional<CityVO> getCityById(Long cityid);

	Map<String, Object> createUpdateCity(CityDTO cityDTO) throws ApplicationException;

	void deleteCity(Long cityid);

	// Currency

	List<CurrencyVO> getAllCurrency(Long orgid);

	Optional<CurrencyVO> getCurrencyById(Long currencyid);

	Map<String, Object> createUpdateCurrency(CurrencyDTO currencyDTO) throws ApplicationException;

	void deleteCurrency(Long currencyid);

	// region

	List<RegionVO> getAllRegios();

	List<RegionVO> getAllRegionsByOrgId(Long orgId);

	Optional<RegionVO> getRegionById(Long regionid);

	Map<String, Object> createUpdateRegion(RegionDTO regionDTO) throws ApplicationException;

	void deleteRegion(Long regionid);

	// Company

	List<CompanyVO> getAllCompany();

	List<CompanyVO> getCompanyById(Long companyid);

	CompanyVO createCompany(CompanyDTO companyDTO) throws Exception;

	CompanyVO updateCompany(CompanyDTO companyDTO) throws ApplicationException;
	
	CompanyVO uploadCompanyLogoInBloob(MultipartFile file, Long id) throws IOException, java.io.IOException;
	
	//CompanyVO createUpdateCompany(CompanyDTO companyDTO) throws ApplicationException;
	
	

	void deleteCompany(Long companyid);

	// FINANCIAL YEAR

	Map<String, Object> createUpdateFinYear(FinancialYearDTO financialYearDTO) throws ApplicationException;

	List<FinancialYearVO> getAllActiveFInYear(Long orgId);

	List<FinancialYearVO> getAllFInYearByOrgId(Long orgId);

	Optional<FinancialYearVO> getAllFInYearById(Long id);

//	FinScreen
	List<ScreenNamesVO> getFinScreenById(Long id);

	ScreenNamesVO updateCreateFinScreen(@Valid FinScreenDTO finScreenDTO) throws ApplicationException;

	List<Map<String, Object>> getAllScreenCode(Long orgId);

	// Screen Names
	Map<String, Object> createUpdateScreenNames(ScreenNamesDTO screenNamesDTO) throws ApplicationException;

	List<ScreenNamesVO> getAllScreenNames();

	ScreenNamesVO getScreenNamesById(Long id) throws ApplicationException;

	List<Map<String, Object>> getAllCurrencyForExRate(Long orgId);

	// Department
	Map<String, Object> createUpdateDepartment(DepartmentDTO departmentDTO) throws ApplicationException;

	Optional<DepartmentVO> getDepartmentById(Long id);

	List<DepartmentVO> getDepartmentByOrgId(Long orgId);

	// Designation
	Map<String, Object> createUpdateDesignation(DesignationDTO designationDTO) throws ApplicationException;

	Optional<DesignationVO> getDesignationById(Long id);

	List<DesignationVO> getDesignationByOrgId(Long orgId);
	
	//Roles
	
	RolesVO getRolesById(Long id);
	
	Map<String, Object> createUpdateRoles(RolesDTO rolesDTO) throws ApplicationException;
	
	List<Map<String, Object>> getAllActiveRolesByOrgId(Long orgId);
	
	List<RolesVO> getRolesByOrgId(Long orgId);
	
	

}

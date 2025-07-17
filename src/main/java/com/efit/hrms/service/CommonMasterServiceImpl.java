package com.efit.hrms.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import javax.transaction.Transactional;
import javax.validation.Valid;

import org.apache.commons.lang3.ObjectUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContextException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.efit.hrms.dto.CityDTO;
import com.efit.hrms.dto.CompanyDTO;
import com.efit.hrms.dto.CompanyWeekOffDTO;
import com.efit.hrms.dto.CountryDTO;
import com.efit.hrms.dto.CurrencyDTO;
import com.efit.hrms.dto.DepartmentDTO;
import com.efit.hrms.dto.DesignationDTO;
import com.efit.hrms.dto.FinScreenDTO;
import com.efit.hrms.dto.FinancialYearDTO;
import com.efit.hrms.dto.RegionDTO;
import com.efit.hrms.dto.RolesDTO;
import com.efit.hrms.dto.ScreenNamesDTO;
import com.efit.hrms.dto.StateDTO;
import com.efit.hrms.entity.CityVO;
import com.efit.hrms.entity.CompanyVO;
import com.efit.hrms.entity.CompanyWeekOffVO;
import com.efit.hrms.entity.CountryVO;
import com.efit.hrms.entity.CurrencyVO;
import com.efit.hrms.entity.DepartmentVO;
import com.efit.hrms.entity.DesignationVO;
import com.efit.hrms.entity.EmployeeVO;
import com.efit.hrms.entity.FinancialYearVO;
import com.efit.hrms.entity.RegionVO;
import com.efit.hrms.entity.RolesVO;
import com.efit.hrms.entity.ScreenNamesVO;
import com.efit.hrms.entity.StateVO;
import com.efit.hrms.entity.UserVO;
import com.efit.hrms.exception.ApplicationException;
import com.efit.hrms.repo.CityRepo;
import com.efit.hrms.repo.CompanyRepo;
import com.efit.hrms.repo.CompanyWeekOffRepo;
import com.efit.hrms.repo.CountryRepo;
import com.efit.hrms.repo.CurrencyRepo;
import com.efit.hrms.repo.DepartmentRepo;
import com.efit.hrms.repo.DesignationRepo;
import com.efit.hrms.repo.EmployeeRepo;
import com.efit.hrms.repo.FinScreenRepo;
import com.efit.hrms.repo.FinancialYearRepo;
import com.efit.hrms.repo.RegionRepo;
import com.efit.hrms.repo.ResponsibilitiesRepo;
import com.efit.hrms.repo.RoleRepo;
import com.efit.hrms.repo.RolesRepo;
import com.efit.hrms.repo.ScreenNamesRepo;
import com.efit.hrms.repo.StateRepo;
import com.efit.hrms.repo.UserRepo;
import com.efit.hrms.util.CryptoUtils;

import io.jsonwebtoken.io.IOException;

@Service
public class CommonMasterServiceImpl implements CommonMasterService {

	public static final Logger LOGGER = LoggerFactory.getLogger(CommonMasterServiceImpl.class);

	@Autowired
	CountryRepo countryRepo;

	@Autowired
	RolesRepo rolesRepo;

	@Autowired
	CurrencyRepo currencyRepo;

	@Autowired
	StateRepo stateRepo;

	@Autowired
	PasswordEncoder encoder;

	@Autowired
	CityRepo cityRepo;

	@Autowired
	RegionRepo regionRepo;

	@Autowired
	CompanyRepo companyRepo;

	@Autowired
	EmployeeRepo employeeRepo;

	@Autowired
	UserRepo userRepo;

	@Autowired
	FinancialYearRepo financialYearRepo;

	@Autowired
	RoleRepo roleRepo;

	@Autowired
	ResponsibilitiesRepo responsibilitiesRepo;

	@Autowired
	FinScreenRepo finScreenRepo;

	@Autowired
	ScreenNamesRepo screenNamesRepo;

	@Autowired
	DesignationRepo designationRepo;

	@Autowired
	DepartmentRepo departmentRepo;

	@Autowired
	CompanyWeekOffRepo companyWeekOffRepo;

	// Company

	@Override
	public List<CompanyVO> getAllCompany() {
		return companyRepo.findAll();
	}

	@Override
	public 		List<CompanyVO>getCompanyById(Long companyid) {
		return companyRepo.findByCompany(companyid);
	}

	@Override
	@Transactional
	public CompanyVO createCompany(CompanyDTO companyDTO) throws Exception {

			if (companyDTO.getId() == null) {
				
				if (companyRepo.existsByCompanyName(companyDTO.getCompanyName())) {
					String errorMessage = String.format("The CompanyName: %s already exists ",
							companyDTO.getCompanyName());
					throw new ApplicationException(errorMessage);
				}
				
				if (companyRepo.existsByCompanyCode(companyDTO.getCompanyCode())) {
					String errorMessage = String.format("The CompanyCode: %s already exists ",
							companyDTO.getCompanyCode());
					throw new ApplicationException(errorMessage);
				}
				
				if (companyDTO.getPhone() != null && !companyDTO.getPhone().trim().isEmpty() && companyRepo.existsByPhone(companyDTO.getPhone())) {
				    String errorMessage = String.format("The Phone: %s already exists ", companyDTO.getPhone());
				    throw new ApplicationException(errorMessage);
				}
				
				if (companyRepo.existsByEmail(companyDTO.getEmail())) {
					String errorMessage = String.format("The Email: %s already exists ",
							companyDTO.getEmail());
					throw new ApplicationException(errorMessage);
				}
			}
			
		CompanyVO companyVO = new CompanyVO();
		getCompanyVOFromCompanyDTO(companyVO, companyDTO);
		companyRepo.save(companyVO);

		EmployeeVO employeeVO = new EmployeeVO();
		employeeVO.setEmployeeName(companyVO.getEmployeeName());
		employeeVO.setEmployeeCode(companyVO.getEmployeeCode());
		employeeVO.setActive(true);
		employeeVO.setOrgId(companyVO.getId());
		employeeRepo.save(employeeVO);

		UserVO userVO = new UserVO();
		userVO.setUserName(companyVO.getEmployeeCode());
		userVO.setEmployeeName(companyVO.getEmployeeName());
		userVO.setEmployeeCode(companyVO.getEmployeeCode());
		userVO.setEmail(companyVO.getEmail());
		userVO.setMobileNo(companyVO.getPhone());
//		userVO.setRole(Role.ROLE_USER);
		userVO.setUserType("ADMIN");
		userVO.setOrgId(companyVO.getId());
		userVO.setCreatedby(companyVO.getCreatedBy());
		userVO.setUpdatedby(companyVO.getCreatedBy());
		userVO.setActive(true);
		userVO.setLoginStatus(false);
		userVO.setCompanyVO(companyVO);

		try {
			userVO.setPassword(encoder.encode(CryptoUtils.getDecrypt(companyDTO.getPassword())));
		} catch (Exception e) {
			LOGGER.error(e.getMessage());
			throw new ApplicationContextException("Unable To Encode Password");
		}
		userRepo.save(userVO);

		return companyVO;
	}

	private void getCompanyVOFromCompanyDTO(CompanyVO companyVO, CompanyDTO companyDTO) {
		companyVO.setCompanyCode(companyDTO.getCompanyCode());
		companyVO.setCompanyName(companyDTO.getCompanyName());
		companyVO.setCountry(companyDTO.getCountry());
		companyVO.setCurrency(companyDTO.getCurrency());
//		companyVO.setMainCurrency(companyDTO.getMainCurrency());
		companyVO.setAddress(companyDTO.getAddress());
		companyVO.setZip(companyDTO.getZip());
		companyVO.setCity(companyDTO.getCity());
		companyVO.setState(companyDTO.getState());
		companyVO.setPhone(companyDTO.getPhone());
		companyVO.setLeaveCreditControl(companyDTO.getLeaveCreditControl());
		companyVO.setLeavePolicy(companyDTO.getLeavePolicy());
		companyVO.setAutoCreditDate(companyDTO.getAutoCreditDate());
		companyVO.setPanNo(companyDTO.getPanNo());
		companyVO.setEmail(companyDTO.getEmail());
//		companyVO.setWebSite(companyDTO.getWebSite());
//		companyVO.setNote(companyDTO.getNote());
		companyVO.setEmployeeCode(companyDTO.getEmployeeCode());
		companyVO.setEmployeeName(companyDTO.getEmployeeName());
		companyVO.setCreatedBy(companyDTO.getCreatedBy());
//		companyVO.setUpdatedBy(companyDTO.getCreatedBy());
		companyVO.setActive(companyDTO.isActive());
		companyVO.setCancel(companyDTO.isCancel());
		companyVO.setGstIn(companyDTO.getGstIn());
		companyVO.setGstRegistered(companyDTO.isGstRegistered());
		companyVO.setCeo(companyDTO.getCeo());
		companyVO.setShiftIn(companyDTO.getShiftIn());
		companyVO.setShiftOut(companyDTO.getShiftOut());
		companyVO.setLatitude(companyDTO.getLatitude());
		companyVO.setLongitude(companyDTO.getLongitude());
		companyVO.setHybrid(companyDTO.isHybrid());
		companyVO.setLocationAddress(companyDTO.getLocationAddress());
		if (companyDTO.getAttendanceMode() != null && !companyDTO.getAttendanceMode().isEmpty()) {
		    String modeString = String.join(",", companyDTO.getAttendanceMode());
		    companyVO.setAttendanceMode(modeString);
		} else {
		    companyVO.setAttendanceMode(null); // or "" as default
		}


		if (companyDTO.getCompanyWeekOffDTO() != null) {
			List<CompanyWeekOffVO> companyWeekOffVOList = new ArrayList<>();
			for (CompanyWeekOffDTO companyWeekOffDTO : companyDTO.getCompanyWeekOffDTO()) {
				CompanyWeekOffVO companyWeekOffVO = new CompanyWeekOffVO();
				companyWeekOffVO.setWeekOffDays(companyWeekOffDTO.getWeekOffDays());
				companyWeekOffVO.setWeekNumbers(companyWeekOffDTO.getWeekNumbers());

//				companyWeekOffVO.setOrgId(companyWeekOffDTO.getOrgId());
				companyWeekOffVO.setCompanyVO(companyVO);

				companyWeekOffVOList.add(companyWeekOffVO);
			}
			companyVO.setCompanyWeekOffVO(companyWeekOffVOList);
		}
		
	}

	public CompanyVO updateCompany(CompanyDTO companyDTO) throws ApplicationException {

		if (ObjectUtils.isEmpty(companyDTO.getId())) {
			throw new ApplicationException("Invalid Company Id");
		}

		CompanyVO companyVO = companyRepo.findById(companyDTO.getId())
				.orElseThrow(() -> new ApplicationException("Company not found for Id: " + companyDTO.getId()));

		List<CompanyWeekOffVO> companyWeekOffVOs = companyWeekOffRepo.findByCompanyVO(companyVO);
		companyWeekOffRepo.deleteAll(companyWeekOffVOs);
		mapCompanyDTOToCompanyVO(companyVO, companyDTO);

		return companyRepo.save(companyVO);
	}

	private void mapCompanyDTOToCompanyVO(CompanyVO companyVO, CompanyDTO companyDTO) {
		companyVO.setCompanyCode(companyDTO.getCompanyCode());
		companyVO.setCompanyName(companyDTO.getCompanyName());
		companyVO.setCountry(companyDTO.getCountry());
		companyVO.setCurrency(companyDTO.getCurrency());
//		companyVO.setMainCurrency(companyDTO.getMainCurrency());
		companyVO.setAddress(companyDTO.getAddress());
		companyVO.setZip(companyDTO.getZip());
		companyVO.setCity(companyDTO.getCity());
		companyVO.setState(companyDTO.getState());
		companyVO.setPhone(companyDTO.getPhone());
		companyVO.setLeaveCreditControl(companyDTO.getLeaveCreditControl());
		companyVO.setLeavePolicy(companyDTO.getLeavePolicy());
		companyVO.setAutoCreditDate(companyDTO.getAutoCreditDate());
		companyVO.setGstIn(companyDTO.getGstIn());
		companyVO.setPanNo(companyDTO.getPanNo());
//		companyVO.setEmail(companyDTO.getEmail());
//		companyVO.setWebSite(companyDTO.getWebSite());
//		companyVO.setNote(companyDTO.getNote());
//			companyVO.setEmployeeCode(companyDTO.getEmployeeCode());
//			companyVO.setEmployeeName(companyDTO.getEmployeeName());
//		companyVO.setCreatedBy(companyDTO.getCreatedBy());
		companyVO.setUpdatedBy(companyDTO.getCreatedBy());
		companyVO.setActive(companyDTO.isActive());
		companyVO.setCancel(companyDTO.isCancel());
//		companyVO.setRole(companyDTO.getRole());
		companyVO.setGstRegistered(companyDTO.isGstRegistered());
		companyVO.setCeo(companyDTO.getCeo());
		companyVO.setShiftIn(companyDTO.getShiftIn());
		companyVO.setShiftOut(companyDTO.getShiftOut());
		companyVO.setLatitude(companyDTO.getLatitude());
		companyVO.setLongitude(companyDTO.getLongitude());
		companyVO.setHybrid(companyDTO.isHybrid());
		companyVO.setLocationAddress(companyDTO.getLocationAddress());
		 if (companyDTO.getAttendanceMode() != null && !companyDTO.getAttendanceMode().isEmpty()) {
		        String modeString = String.join(",", companyDTO.getAttendanceMode());
		        companyVO.setAttendanceMode(modeString);
		    } else {
		        companyVO.setAttendanceMode(null); // or ""
		    }
		if (companyDTO.getCompanyWeekOffDTO() != null) {
			List<CompanyWeekOffVO> companyWeekOffVOList = new ArrayList<>();
			for (CompanyWeekOffDTO companyWeekOffDTO : companyDTO.getCompanyWeekOffDTO()) {
				CompanyWeekOffVO companyWeekOffVO = new CompanyWeekOffVO();
				companyWeekOffVO.setWeekOffDays(companyWeekOffDTO.getWeekOffDays());
				companyWeekOffVO.setWeekNumbers(companyWeekOffDTO.getWeekNumbers());
//				companyWeekOffVO.setOrgId(companyWeekOffDTO.getOrgId());
				companyWeekOffVO.setCompanyVO(companyVO);

				companyWeekOffVOList.add(companyWeekOffVO);
			}
			companyVO.setCompanyWeekOffVO(companyWeekOffVOList);
		}
		
	}

	@Override
	public void deleteCompany(Long companyid) {
		companyRepo.deleteById(companyid);
	}

//	@Override
//	public CompanyVO saveImage(MultipartFile file, @RequestParam Long id)
//			throws ApplicationException, java.io.IOException {
//
//		CompanyVO image = companyRepo.findById(id)
//				.orElseThrow(() -> new ApplicationException("Invalid company id" + id));
//
//		image.setImageName(file.getOriginalFilename());
//		image.setData(file.getBytes());
//		return companyRepo.save(image);
//
//	}
	
	
	
	@Override
	public CompanyVO uploadCompanyLogoInBloob(MultipartFile file, Long id) throws IOException, java.io.IOException {
		CompanyVO companyVO = companyRepo.findById(id).get();
		companyVO.setCompanyLogo(file.getBytes());
		return companyRepo.save(companyVO);
	}
	
	

	// FinScreen-----------------------------------------------------------------------------------
	@Override
	public List<ScreenNamesVO> getFinScreenById(Long id) {
		List<ScreenNamesVO> finScreenVO = new ArrayList<>();
		if (ObjectUtils.isNotEmpty(id)) {
			LOGGER.info("Successfully Received FinScreen BY Id : {}", id);
			finScreenVO = screenNamesRepo.findFinScreenById(id);
		} else {
			LOGGER.info("Successfully Received FinScreen For All Id.");
			finScreenVO = screenNamesRepo.findAll();
		}
		return finScreenVO;
	}

	@Override
	public ScreenNamesVO updateCreateFinScreen(@Valid FinScreenDTO finScreenDTO) throws ApplicationException {
		ScreenNamesVO finScreenVO = new ScreenNamesVO();
		boolean isUpdate = false;
		if (ObjectUtils.isNotEmpty(finScreenDTO.getId())) {
			isUpdate = true;
			finScreenVO = screenNamesRepo.findById(finScreenDTO.getId())
					.orElseThrow(() -> new ApplicationException("Invalid FinScreen Details"));
			finScreenVO.setUpdatedBy(finScreenDTO.getCreatedBy());

		} else {
			if (screenNamesRepo.existsByScreenName(finScreenDTO.getScreenName())) {
				throw new ApplicationException("The given Screen name already exists.");
			}
			if (screenNamesRepo.existsByScreenCode(finScreenDTO.getScreenCode())) {
				throw new ApplicationException("The given Screen code already exists.");
			}
			finScreenVO.setUpdatedBy(finScreenDTO.getCreatedBy());
			finScreenVO.setCreatedBy(finScreenDTO.getCreatedBy());
		}

		// update check
		if (isUpdate) {
			ScreenNamesVO finScreen = screenNamesRepo.findById(finScreenDTO.getId()).orElse(null);
			if (!finScreen.getScreenName().equalsIgnoreCase(finScreenDTO.getScreenName())) {
				if (screenNamesRepo.existsByScreenName(finScreenDTO.getScreenName())) {
					throw new ApplicationException("The given Screen name already exists.");
				}
			}
			if (!finScreen.getScreenCode().equals(finScreenDTO.getScreenCode())) {
				if (screenNamesRepo.existsByScreenCode(finScreenDTO.getScreenCode())) {
					throw new ApplicationException("The given Screen code already exists");
				}
			}
		}
		getFinScreenVOFromFinScreenDTO(finScreenDTO, finScreenVO);
		return screenNamesRepo.save(finScreenVO);
	}

	private void getFinScreenVOFromFinScreenDTO(@Valid FinScreenDTO finScreenDTO, ScreenNamesVO finScreenVO)
			throws ApplicationException {

		finScreenVO.setActive(finScreenDTO.isActive());
		finScreenVO.setScreenCode(finScreenDTO.getScreenCode());
		finScreenVO.setScreenName(finScreenDTO.getScreenName());
	}

	@Override
	public List<Map<String, Object>> getAllScreenCode(Long orgId) {
		Set<Object[]> getFinScreen = screenNamesRepo.findAllScreenCode(orgId);
		return getScreen(getFinScreen);
	}

	private List<Map<String, Object>> getScreen(Set<Object[]> getFinScreen) {
		List<Map<String, Object>> finScreenList = new ArrayList<>();

		for (Object[] finScreen : getFinScreen) {
			Map<String, Object> branchMap = new HashMap<>();
			branchMap.put("screenCode", finScreen[0] != null ? finScreen[0].toString() : "");
			branchMap.put("screenName", finScreen[1] != null ? finScreen[1].toString() : "");
			finScreenList.add(branchMap);
		}
		return finScreenList;
	}

	// Country

	@Override
	public List<CountryVO> getAllCountry(Long orgid) {
		return countryRepo.findAll(orgid);
	}

	@Override
	public Optional<CountryVO> getCountryById(Long countryid) {
		return countryRepo.findById(countryid);
	}

	@Override
	public Map<String, Object> createUpdateCountry(CountryDTO countryDTO) throws ApplicationException {

		CountryVO countryVO;
		String message = null;

		if (ObjectUtils.isEmpty(countryDTO.getId())) {
			if (countryRepo.existsByCountryNameAndCountryCodeAndOrgId(countryDTO.getCountryName(),
					countryDTO.getCountryCode(), countryDTO.getOrgId())) {
				String errorMessage = String.format(
						"The CountryName: %s and CountryCode: %s already exists This Organization.",
						countryDTO.getCountryName(), countryDTO.getCountryCode());
				throw new ApplicationException(errorMessage);
			}

			if (countryRepo.existsByCountryNameAndOrgId(countryDTO.getCountryName(), countryDTO.getOrgId())) {
				String errorMessage = String.format("The CountryName: %s already exists This Organization.",
						countryDTO.getCountryName());
				throw new ApplicationException(errorMessage);
			}

			if (countryRepo.existsByCountryCodeAndOrgId(countryDTO.getCountryCode(), countryDTO.getOrgId())) {
				String errorMessage = String.format("The CountryCode: %s already exists This Organization.",
						countryDTO.getCountryCode());
				throw new ApplicationException(errorMessage);
			}
			// Create new branch
			countryVO = new CountryVO();
			countryVO.setCreatedBy(countryDTO.getCreatedBy());
			countryVO.setUpdatedBy(countryDTO.getCreatedBy());
			message = "Country Creation SuccessFully";
		} else {
			// Update existing branch
			countryVO = countryRepo.findById(countryDTO.getId())
					.orElseThrow(() -> new ApplicationException("Branch not found with id: " + countryDTO.getId()));
			countryVO.setUpdatedBy(countryDTO.getCreatedBy());
			if (!countryVO.getCountryCode().equalsIgnoreCase(countryDTO.getCountryCode())) {
				if (countryRepo.existsByCountryCodeAndOrgId(countryDTO.getCountryCode(), countryDTO.getOrgId())) {
					String errorMessage = String.format("The CountryCode: %s already exists This Organization.",
							countryDTO.getCountryCode());
					throw new ApplicationException(errorMessage);
				}
				countryVO.setCountryCode(countryDTO.getCountryCode().toUpperCase());
			}
			if (!countryVO.getCountryName().equalsIgnoreCase(countryDTO.getCountryName())) {
				if (countryRepo.existsByCountryNameAndOrgId(countryDTO.getCountryName(), countryDTO.getOrgId())) {
					String errorMessage = String.format("The CountryName: %s already exists This Organization.",
							countryDTO.getCountryName());
					throw new ApplicationException(errorMessage);
				}
				countryVO.setCountryName(countryDTO.getCountryName().toUpperCase());

			}
			message = "Country Update Successfully";
		}

		getCountryVOFromCounytryDTO(countryVO, countryDTO);
		countryRepo.save(countryVO);
		Map<String, Object> response = new HashMap<String, Object>();
		response.put("message", message);
		response.put("countryVO", countryVO);
		return response;

	}

	private void getCountryVOFromCounytryDTO(CountryVO countryVO, CountryDTO countryDTO) {
		countryVO.setCountryName(countryDTO.getCountryName().toUpperCase());
		countryVO.setCountryCode(countryDTO.getCountryCode().toUpperCase());
		countryVO.setActive(countryDTO.isActive());
		countryVO.setOrgId(countryDTO.getOrgId());
		countryVO.setCancel(countryDTO.isCancel());

	}

	@Override
	public void deleteCountry(Long countryid) {

		countryRepo.deleteById(countryid);
	}

	// State

	@Override
	public List<StateVO> getAllgetAllStates(Long orgid) {
		return stateRepo.findAllByOrgId(orgid);
	}

	@Override
	public Optional<StateVO> getStateById(Long stateid) {
		return stateRepo.findById(stateid);
	}

	@Override
	public List<StateVO> getStatesByCountry(Long orgid, String country) {

		return stateRepo.findByCountry(orgid, country);
	}

	@Override
	@Transactional
	public Map<String, Object> createUpdateState(StateDTO stateDTO) throws ApplicationException {
		StateVO stateVO;
		String message = null;

		if (ObjectUtils.isEmpty(stateDTO.getId())) {
			// Check for existing state by state code, state number, and state name
			if (stateRepo.existsByStateCodeAndOrgId(stateDTO.getStateCode(), stateDTO.getOrgId())) {
				String errorMessage = String.format("The StateCode: %s already exists in This Organization.",
						stateDTO.getStateCode());
				throw new ApplicationException(errorMessage);
			}
			if (stateRepo.existsByStateNumberAndOrgId(stateDTO.getStateNumber(), stateDTO.getOrgId())) {
				String errorMessage = String.format("The StateNumber: %s already exists in This Organization.",
						stateDTO.getStateNumber());
				throw new ApplicationException(errorMessage);
			}
			if (stateRepo.existsByStateNameAndOrgId(stateDTO.getStateName(), stateDTO.getOrgId())) {
				String errorMessage = String.format("The StateName: %s already exists in This Organization.",
						stateDTO.getStateName());
				throw new ApplicationException(errorMessage);
			}

			// Create new state
			stateVO = new StateVO();
			stateVO.setCreatedBy(stateDTO.getCreatedBy());
			stateVO.setUpdatedBy(stateDTO.getCreatedBy());
			message = "State Creation Successfully";
		} else {
			// Update existing state
			stateVO = stateRepo.findById(stateDTO.getId())
					.orElseThrow(() -> new ApplicationException("State not found with id: " + stateDTO.getId()));

			stateVO.setUpdatedBy(stateDTO.getCreatedBy());

			if (!stateVO.getStateCode().equalsIgnoreCase(stateDTO.getStateCode())) {
				if (stateRepo.existsByStateCodeAndOrgId(stateDTO.getStateCode(), stateDTO.getOrgId())) {
					String errorMessage = String.format("The StateCode: %s already exists in This Organization.",
							stateDTO.getStateCode());
					throw new ApplicationException(errorMessage);
				}
				stateVO.setStateCode(stateDTO.getStateCode().toUpperCase());
			}

			if (!stateVO.getStateName().equalsIgnoreCase(stateDTO.getStateName())) {
				if (stateRepo.existsByStateNameAndOrgId(stateDTO.getStateName(), stateDTO.getOrgId())) {
					String errorMessage = String.format("The StateName: %s already exists in This Organization.",
							stateDTO.getStateName());
					throw new ApplicationException(errorMessage);
				}
				stateVO.setStateName(stateDTO.getStateName().toUpperCase());
			}

			if (!stateVO.getStateNumber().equalsIgnoreCase(stateDTO.getStateNumber())) {
				if (stateRepo.existsByStateNumberAndOrgId(stateDTO.getStateNumber(), stateDTO.getOrgId())) {
					String errorMessage = String.format("The StateNumber: %s already exists in This Organization.",
							stateDTO.getStateNumber());
					throw new ApplicationException(errorMessage);
				}
				stateVO.setStateNumber(stateDTO.getStateNumber().toUpperCase());
			}

			message = "State Update Successfully";
		}

		// Map the remaining fields
		getStateVOFromStateDTO(stateVO, stateDTO);

		// Save the entity
		stateRepo.save(stateVO);

		// Prepare the response
		Map<String, Object> response = new HashMap<>();
		response.put("message", message);
		response.put("stateVO", stateVO);

		return response;
	}

	private void getStateVOFromStateDTO(StateVO stateVO, StateDTO stateDTO) {
		stateVO.setStateCode(stateDTO.getStateCode().toUpperCase());
		stateVO.setStateName(stateDTO.getStateName().toUpperCase());
		stateVO.setStateNumber(stateDTO.getStateNumber().toUpperCase());
		stateVO.setCountry(stateDTO.getCountry().toUpperCase());
		stateVO.setRegion(stateDTO.getRegion().toUpperCase());
		stateVO.setActive(stateDTO.isActive());
		stateVO.setCancel(stateDTO.isCancel());
		stateVO.setOrgId(stateDTO.getOrgId());
		// stateVO.setDupchk(stateDTO.getOrgId() + stateDTO.getStateCode() +
		// stateDTO.getStateName());
	}

	@Override
	public void deleteState(Long countryid) {
		stateRepo.deleteById(countryid);
	}

	// City

	@Override
	public List<CityVO> getAllgetAllCities(Long orgid) {
		return cityRepo.findAll(orgid);
	}

	@Override
	public List<CityVO> getAllCitiesByState(Long orgid, String state) {

		return cityRepo.findAll(orgid, state);
	}

	@Override
	public Optional<CityVO> getCityById(Long cityid) {
		return cityRepo.findById(cityid);
	}

	@Override
	@Transactional
	public Map<String, Object> createUpdateCity(CityDTO cityDTO) throws ApplicationException {
		CityVO cityVO;
		String message;

		if (ObjectUtils.isEmpty(cityDTO.getId())) {
			if (cityRepo.existsByCityCodeAndOrgId(cityDTO.getCityCode(), cityDTO.getOrgId())) {
				String errorMessage = String.format("The CityCode: %s already exists in this organization.",
						cityDTO.getCityCode());
				throw new ApplicationException(errorMessage);
			}
			if (cityRepo.existsByCityNameAndOrgId(cityDTO.getCityName(), cityDTO.getOrgId())) {
				String errorMessage = String.format("The CityName: %s already exists in this organization.",
						cityDTO.getCityName());
				throw new ApplicationException(errorMessage);
			}
			// Create new city
			cityVO = new CityVO();
			cityVO.setCreatedBy(cityDTO.getCreatedBy());
			cityVO.setUpdatedBy(cityDTO.getCreatedBy());
			message = "City Created Successfully";
		} else {
			// Update existing city
			cityVO = cityRepo.findById(cityDTO.getId())
					.orElseThrow(() -> new ApplicationException("City not found with id: " + cityDTO.getId()));
			cityVO.setUpdatedBy(cityDTO.getCreatedBy());

			if (!cityVO.getCityCode().equalsIgnoreCase(cityDTO.getCityCode())) {
				if (cityRepo.existsByCityCodeAndOrgId(cityDTO.getCityCode(), cityDTO.getOrgId())) {
					String errorMessage = String.format("The CityCode: %s already exists in this organization.",
							cityDTO.getCityCode());
					throw new ApplicationException(errorMessage);
				}
				cityVO.setCityCode(cityDTO.getCityCode().toUpperCase());
			}

			if (!cityVO.getCityName().equalsIgnoreCase(cityDTO.getCityName())) {
				if (cityRepo.existsByCityNameAndOrgId(cityDTO.getCityName(), cityDTO.getOrgId())) {
					String errorMessage = String.format("The CityName: %s already exists in this organization.",
							cityDTO.getCityName());
					throw new ApplicationException(errorMessage);
				}
				cityVO.setCityName(cityDTO.getCityName().toUpperCase());
			}
			message = "City Updated Successfully";
		}

		getCityVOFromCityDTO(cityVO, cityDTO);
		cityRepo.save(cityVO);

		Map<String, Object> response = new HashMap<>();
		response.put("message", message);
		response.put("cityVO", cityVO);
		return response;
	}

	private void getCityVOFromCityDTO(CityVO cityVO, CityDTO cityDTO) {
		cityVO.setCityCode(cityDTO.getCityCode().toUpperCase());
		cityVO.setCityName(cityDTO.getCityName().toUpperCase());
		cityVO.setCountry(cityDTO.getCountry().toUpperCase());
		cityVO.setState(cityDTO.getState().toUpperCase());
		cityVO.setActive(cityDTO.isActive());
		cityVO.setOrgId(cityDTO.getOrgId());
		cityVO.setCancel(cityDTO.isCancel());
	}

	@Override
	public void deleteCity(Long cityid) {
		cityRepo.deleteById(cityid);
	}

	// Region

	@Override
	public List<RegionVO> getAllRegios() {

		return regionRepo.findAll();
	}

	@Override
	public List<RegionVO> getAllRegionsByOrgId(Long orgId) {
		return regionRepo.findAll(orgId);
	}

	@Override
	public Optional<RegionVO> getRegionById(Long Regionid) {
		return regionRepo.findById(Regionid);
	}

	@Override
	@Transactional
	public Map<String, Object> createUpdateRegion(RegionDTO regionDTO) throws ApplicationException {
		RegionVO regionVO;
		String message;

		if (ObjectUtils.isEmpty(regionDTO.getId())) {
			if (regionRepo.existsByRegionNameAndOrgId(regionDTO.getRegionName(), regionDTO.getOrgId())) {
				String errorMessage = String.format("This RegionName:%s Already Exists in This Organization",
						regionDTO.getRegionName().toUpperCase());
				throw new ApplicationException(errorMessage);
			}
			if (regionRepo.existsByRegionCodeAndOrgId(regionDTO.getRegionCode(), regionDTO.getOrgId())) {
				String errorMessage = String.format("This RegionCode:%s Already Exists in This Organization",
						regionDTO.getRegionCode().toUpperCase());
				throw new ApplicationException(errorMessage);
			}
			// Create new region
			regionVO = new RegionVO();
			regionVO.setCreatedBy(regionDTO.getCreatedBy());
			regionVO.setUpdatedBy(regionDTO.getCreatedBy());
			message = "Region Created Successfully";
		} else {
			// Update existing region
			regionVO = regionRepo.findById(regionDTO.getId()).orElseThrow(
					() -> new ApplicationException("This Id Is Not Found Any Information: " + regionDTO.getId()));
			regionVO.setUpdatedBy(regionDTO.getCreatedBy());

			if (!regionVO.getRegionName().equalsIgnoreCase(regionDTO.getRegionName())) {
				if (regionRepo.existsByRegionNameAndOrgId(regionDTO.getRegionName(), regionDTO.getOrgId())) {
					String errorMessage = String.format("This RegionName:%s Already Exists in This Organization",
							regionDTO.getRegionName());
					throw new ApplicationException(errorMessage);
				}
				regionVO.setRegionName(regionDTO.getRegionName().toUpperCase());
			}

			if (!regionVO.getRegionCode().equalsIgnoreCase(regionDTO.getRegionCode())) {
				if (regionRepo.existsByRegionCodeAndOrgId(regionDTO.getRegionCode(), regionDTO.getOrgId())) {
					String errorMessage = String.format("This RegionCode:%s Already Exists in This Organization",
							regionDTO.getRegionCode());
					throw new ApplicationException(errorMessage);
				}
				regionVO.setRegionCode(regionDTO.getRegionCode().toUpperCase());
			}
			message = "Region Updated Successfully";
		}

		getRegionVOFromRegionDTO(regionVO, regionDTO);
		regionRepo.save(regionVO);

		Map<String, Object> response = new HashMap<>();
		response.put("message", message);
		response.put("regionVO", regionVO);
		return response;
	}

	private void getRegionVOFromRegionDTO(RegionVO regionVO, RegionDTO regionDTO) {
		regionVO.setActive(regionDTO.isActive());
		regionVO.setOrgId(regionDTO.getOrgId());
		regionVO.setCancel(regionDTO.isCancel());
		regionVO.setRegionCode(regionDTO.getRegionCode().toUpperCase());
		regionVO.setRegionName(regionDTO.getRegionName().toUpperCase());
	}

	@Override
	public void deleteRegion(Long regionid) {
		regionRepo.deleteById(regionid);
	}

	// Currency
	@Override
	public List<CurrencyVO> getAllCurrency(Long orgid) {

		return currencyRepo.findAll(orgid);
	}

	@Override
	public Optional<CurrencyVO> getCurrencyById(Long currencyid) {

		return currencyRepo.findById(currencyid);
	}

	@Override
	@Transactional
	public Map<String, Object> createUpdateCurrency(CurrencyDTO currencyDTO) throws ApplicationException {

		CurrencyVO currencyVO;
		String message = null;

		if (ObjectUtils.isEmpty(currencyDTO.getId())) {
			if (currencyRepo.existsByOrgIdAndCountryAndCurrencyIgnoreCase(currencyDTO.getOrgId(),
					currencyDTO.getCountry(), currencyDTO.getCurrency())) {
				String errorMessage = String.format("This Currency:%s Already Exists in This Organization.",
						currencyDTO.getCurrency());
				throw new ApplicationException(errorMessage);
			}
			if (currencyRepo.existsByOrgIdAndCountryAndCurrencyDescriptionIgnoreCase(currencyDTO.getOrgId(),
					currencyDTO.getCountry(), currencyDTO.getCurrencyDescription())) {
				String errorMessage = String.format("This CurrencyDescription:%s Already Exists in This Organization.",
						currencyDTO.getCurrencyDescription());
				throw new ApplicationException(errorMessage);
			}
			if (currencyRepo.existsByOrgIdAndCountryAndSubCurrencyIgnoreCase(currencyDTO.getOrgId(),
					currencyDTO.getCountry(), currencyDTO.getSubCurrency())) {
				String errorMessage = String.format("This SubCurrency:%s Already Exists in This Organization.",
						currencyDTO.getSubCurrency());
				throw new ApplicationException(errorMessage);
			}

			// Create new currency
			currencyVO = new CurrencyVO();
			currencyVO.setCreatedBy(currencyDTO.getCreatedBy());
			currencyVO.setUpdatedBy(currencyDTO.getCreatedBy());
			message = "Currency Created Successfully";
		} else {
			// Update existing currency
			currencyVO = currencyRepo.findById(currencyDTO.getId()).orElseThrow(
					() -> new ApplicationException("This Id Is Not Found Any Information: " + currencyDTO.getId()));
			currencyVO.setUpdatedBy(currencyDTO.getCreatedBy());

			if (!currencyVO.getCurrency().equalsIgnoreCase(currencyDTO.getCurrency())) {
				if (currencyRepo.existsByOrgIdAndCountryAndCurrencyIgnoreCase(currencyDTO.getOrgId(),
						currencyDTO.getCountry(), currencyDTO.getCurrency())) {
					String errorMessage = String.format("This Currency:%s Already Exists in This Organization.",
							currencyDTO.getCurrency());
					throw new ApplicationException(errorMessage);
				}
				currencyVO.setCurrency(currencyDTO.getCurrency().toUpperCase());
			}
			if (!currencyVO.getSubCurrency().equalsIgnoreCase(currencyDTO.getSubCurrency())) {
				if (currencyRepo.existsByOrgIdAndCountryAndSubCurrencyIgnoreCase(currencyDTO.getOrgId(),
						currencyDTO.getCountry(), currencyDTO.getSubCurrency())) {
					String errorMessage = String.format("This SubCurrency:%s Already Exists in This Organization.",
							currencyDTO.getSubCurrency());
					throw new ApplicationException(errorMessage);
				}
				if (currencyDTO.getSubCurrency() != null) {
					currencyVO.setSubCurrency(currencyDTO.getSubCurrency().toUpperCase());
				}
			}
			if (!currencyVO.getCurrencyDescription().equalsIgnoreCase(currencyDTO.getCurrencyDescription())) {
				if (currencyRepo.existsByOrgIdAndCountryAndCurrencyDescriptionIgnoreCase(currencyDTO.getOrgId(),
						currencyDTO.getCountry(), currencyDTO.getCurrencyDescription())) {
					String errorMessage = String.format(
							"This CurrencyDescription:%s Already Exists in This Organization.",
							currencyDTO.getCurrencyDescription());
					throw new ApplicationException(errorMessage);
				}
				if (currencyDTO.getCurrencyDescription() != null) {
					currencyVO.setCurrencyDescription(currencyDTO.getCurrencyDescription().toUpperCase());
				}
			}
			message = "Currency Updated Successfully";
		}

		getCurrencyVOFromCurrencyDTO(currencyVO, currencyDTO);
		currencyRepo.save(currencyVO);

		Map<String, Object> response = new HashMap<>();
		response.put("message", message);
		response.put("currencyVO", currencyVO);
		return response;
	}

	private void getCurrencyVOFromCurrencyDTO(CurrencyVO currencyVO, CurrencyDTO currencyDTO) {
		currencyVO.setCurrency(currencyDTO.getCurrency().toUpperCase());
		if (currencyDTO.getSubCurrency() != null) {
			currencyVO.setSubCurrency(currencyDTO.getSubCurrency().toUpperCase());
		}
		if (currencyDTO.getCurrencyDescription() != null) {
			currencyVO.setCurrencyDescription(currencyDTO.getCurrencyDescription().toUpperCase());
		}
		currencyVO.setActive(currencyDTO.isActive());
		currencyVO.setCountry(currencyDTO.getCountry().toUpperCase());
		currencyVO.setOrgId(currencyDTO.getOrgId());
	}

	@Override
	public void deleteCurrency(Long currencyid) {
		currencyRepo.deleteById(currencyid);

	}

	@Override
	public Map<String, Object> createUpdateScreenNames(ScreenNamesDTO screenNamesDTO) throws ApplicationException {
		ScreenNamesVO screenNamesVO = new ScreenNamesVO();
		String message = null;

		if (ObjectUtils.isEmpty(screenNamesDTO.getId())) {

			// Validate if responsibility already exists by responsibility name
			if (screenNamesRepo.existsByScreenName(screenNamesDTO.getScreenName())) {
				throw new ApplicationException("Screen Name already exists");
			}
			if (screenNamesRepo.existsByScreenCode(screenNamesDTO.getScreenCode())) {
				throw new ApplicationException("Screen Code already exists");
			}

			screenNamesVO.setCreatedBy(screenNamesDTO.getCreatedBy());
			screenNamesVO.setUpdatedBy(screenNamesDTO.getCreatedBy());
			screenNamesVO.setActive(screenNamesDTO.isActive());
			screenNamesVO.setScreenCode(screenNamesDTO.getScreenCode());
			screenNamesVO.setScreenName(screenNamesDTO.getScreenName());
			// Set the values from screenNamesDTO to responsibilityVO
			message = "ScreenName Created successfully";

		} else {

			// Retrieve the existing ResponsibilityVO from the repository
			screenNamesVO = screenNamesRepo.findById(screenNamesDTO.getId())
					.orElseThrow(() -> new ApplicationException("Screen Name not found"));

			// Validate and update unique fields if changed
			if (!screenNamesVO.getScreenName().equalsIgnoreCase(screenNamesDTO.getScreenName())) {
				if (screenNamesRepo.existsByScreenName(screenNamesDTO.getScreenName())) {
					throw new ApplicationException("Screen Name already exists");
				}
				screenNamesVO.setScreenName(screenNamesDTO.getScreenName());
			}
			if (!screenNamesVO.getScreenCode().equalsIgnoreCase(screenNamesDTO.getScreenCode())) {
				if (screenNamesRepo.existsByScreenCode(screenNamesDTO.getScreenCode())) {
					throw new ApplicationException("Screen Code already exists");
				}
				screenNamesVO.setScreenCode(screenNamesDTO.getScreenCode());
			}
			screenNamesVO.setActive(screenNamesDTO.isActive());
			screenNamesVO.setUpdatedBy(screenNamesDTO.getCreatedBy());
			// Update the remaining fields from screenNamesDTO to responsibilityVO
			message = "ScreenName Updated successfully";
		}

		screenNamesRepo.save(screenNamesVO);
		Map<String, Object> response = new HashMap<>();
		response.put("screenNamesVO", screenNamesVO);
		response.put("message", message);
		return response;
	}

	@Override
	public List<ScreenNamesVO> getAllScreenNames() {

		return screenNamesRepo.findAll();
	}

	@Override
	public ScreenNamesVO getScreenNamesById(Long id) throws ApplicationException {

		if (ObjectUtils.isEmpty(id)) {
			throw new ApplicationException("Invalid Id");
		}

		ScreenNamesVO screenNamesVO = screenNamesRepo.findById(id)
				.orElseThrow(() -> new ApplicationException("Screen Name not found for Id: " + id));

		return screenNamesVO;
	}

	// Financila Year
	@Override
	public Map<String, Object> createUpdateFinYear(FinancialYearDTO financialYearDTO) throws ApplicationException {
		FinancialYearVO financialYearVO = null;
		String message;

		if (ObjectUtils.isEmpty(financialYearDTO.getId())) {
			if (financialYearRepo.existsByFinYearAndOrgId(financialYearDTO.getFinYear(), financialYearDTO.getOrgId())) {
				String errorMessage = String.format("ThiS finyear:%s Already Exists This Organization .",
						financialYearDTO.getFinYear());
				throw new ApplicationException(errorMessage);
			}

			if (financialYearRepo.existsByFinYearIdentifierAndOrgId(financialYearDTO.getFinYearIdentifier(),
					financialYearDTO.getOrgId())) {
				String errorMessage = String.format("ThiS finyearidentifier:%s Already Exists This Organization .",
						financialYearDTO.getFinYearIdentifier());
				throw new ApplicationException(errorMessage);
			}

			if (financialYearRepo.existsByFinYearIdAndOrgId(financialYearDTO.getFinYearId(),
					financialYearDTO.getOrgId())) {
				String errorMessage = String.format("ThiS finyearid:%s Already Exists This Organization .",
						financialYearDTO.getFinYearId());
				throw new ApplicationException(errorMessage);
			}

			financialYearVO = new FinancialYearVO();
			financialYearVO.setCreatedBy(financialYearDTO.getCreatedBy());
			financialYearVO.setUpdatedBy(financialYearDTO.getCreatedBy());
			message = "Financial Year Creation Successfully";

		} else {
			financialYearVO = financialYearRepo.findById(financialYearDTO.getId())
					.orElseThrow(() -> new ApplicationException(String
							.format("This Id Is Not Found Any Information, Invalid Id: %s", financialYearDTO.getId())));

			if (financialYearVO.getFinYear() != financialYearDTO.getFinYear()) {
				if (financialYearRepo.existsByFinYearAndOrgId(financialYearDTO.getFinYear(),
						financialYearDTO.getOrgId())) {
					String errorMessage = String.format("This finyear: %s already exists for this organization.",
							financialYearDTO.getFinYear());
					throw new ApplicationException(errorMessage);
				}
				financialYearVO.setFinYear(financialYearDTO.getFinYear());
			}

			if (!financialYearVO.getFinYearIdentifier().equals(financialYearDTO.getFinYearIdentifier())) {
				if (financialYearRepo.existsByFinYearIdentifierAndOrgId(financialYearDTO.getFinYearIdentifier(),
						financialYearDTO.getOrgId())) {
					String errorMessage = String.format(
							"This finyearIdentifier: %s already exists for this organization.",
							financialYearDTO.getFinYearIdentifier());
					throw new ApplicationException(errorMessage);
				}
				financialYearVO.setFinYearIdentifier(financialYearDTO.getFinYearIdentifier());
			}

			if (financialYearVO.getFinYearId() != financialYearDTO.getFinYearId()) {
				if (financialYearRepo.existsByFinYearIdAndOrgId(financialYearDTO.getFinYearId(),
						financialYearDTO.getOrgId())) {
					String errorMessage = String.format("This finyearId: %s already exists for this organization.",
							financialYearDTO.getFinYearId());
					throw new ApplicationException(errorMessage);
				}
				financialYearVO.setFinYearId(financialYearDTO.getFinYearId());
			}

			financialYearVO.setUpdatedBy(financialYearDTO.getCreatedBy());
			message = "Financial Year Updation Successfully";

		}
		getFinancialYearVOFromFinancialYearDTO(financialYearVO, financialYearDTO);
		financialYearRepo.save(financialYearVO);
		Map<String, Object> response = new HashMap<String, Object>();
		response.put("financialYearVO", financialYearVO);
		response.put("message", response);
		return response;

	}

	private void getFinancialYearVOFromFinancialYearDTO(FinancialYearVO financialYearVO,
			FinancialYearDTO financialYearDTO) {
		financialYearVO.setFinYear(financialYearDTO.getFinYear());
		financialYearVO.setFinYearId(financialYearDTO.getFinYearId());
		financialYearVO.setFinYearIdentifier(financialYearDTO.getFinYearIdentifier());
		financialYearVO.setStartDate(financialYearDTO.getStartDate());
		financialYearVO.setEndDate(financialYearDTO.getEndDate());
		financialYearVO.setCurrentFinYear(financialYearDTO.isCurrentFinYear());
		financialYearVO.setClosed(financialYearDTO.isClosed());
		financialYearVO.setOrgId(financialYearDTO.getOrgId());
		financialYearVO.setActive(financialYearDTO.isActive());
	}

	@Override
	public List<FinancialYearVO> getAllActiveFInYear(Long orgId) {
		return financialYearRepo.findAllActiveFinYear(orgId);
	}

	@Override
	public List<FinancialYearVO> getAllFInYearByOrgId(Long orgId) {
		return financialYearRepo.findFinancialYearByOrgId(orgId);
	}

	@Override
	public Optional<FinancialYearVO> getAllFInYearById(Long id) {
		return financialYearRepo.findById(id);
	}

	@Override
	public List<Map<String, Object>> getAllCurrencyForExRate(Long orgId) {
		Set<Object[]> getFullGridCurrency = currencyRepo.findCurrencyForFullGrid(orgId);
		return getCurrency(getFullGridCurrency); // Returning a list of Map<String, Object>
	}

	private List<Map<String, Object>> getCurrency(Set<Object[]> getFullGridCurrency) {
		List<Map<String, Object>> currencyList = new ArrayList<>(); // Correct variable name

		for (Object[] currency : getFullGridCurrency) { // Iterating over getFullGridCurrency
			Map<String, Object> currencyMap = new HashMap<>();
			currencyMap.put("id", currency[0] != null ? Integer.parseInt(currency[0].toString()) : 0);
			currencyMap.put("currency", currency[1] != null ? currency[1].toString() : "");
			currencyMap.put("currencyDescription", currency[2] != null ? currency[2].toString() : "");

			currencyList.add(currencyMap); // Add the Map to the list
		}
		return currencyList;
	}

	// Department

	@Override
	public List<DepartmentVO> getDepartmentByOrgId(Long orgId) {
		return departmentRepo.findDepartmentByOrgId(orgId);
	}

	@Override
	public Optional<DepartmentVO> getDepartmentById(Long id) {
		return departmentRepo.findById(id);
	}

	@Override
	public Map<String, Object> createUpdateDepartment(DepartmentDTO departmentDTO) throws ApplicationException {

		DepartmentVO departmentVO;
		String message = null;

		if (ObjectUtils.isEmpty(departmentDTO.getId())) {
			if (departmentRepo.existsByDepartmentNameAndDepartmentCodeAndOrgId(departmentDTO.getDepartmentName(),
					departmentDTO.getDepartmentCode(), departmentDTO.getOrgId())) {
				String errorMessage = String.format(
						"The DepartmentName: %s and DepartmentCode: %s already exists This Organization.",
						departmentDTO.getDepartmentName(), departmentDTO.getDepartmentCode());
				throw new ApplicationException(errorMessage);
			}

			if (departmentRepo.existsByDepartmentNameAndOrgId(departmentDTO.getDepartmentName(),
					departmentDTO.getOrgId())) {
				String errorMessage = String.format("The DepartmentName: %s already exists This Organization.",
						departmentDTO.getDepartmentName());
				throw new ApplicationException(errorMessage);
			}

			if (departmentRepo.existsByDepartmentCodeAndOrgId(departmentDTO.getDepartmentCode(),
					departmentDTO.getOrgId())) {
				String errorMessage = String.format("The DepartmentCode: %s already exists This Organization.",
						departmentDTO.getDepartmentCode());
				throw new ApplicationException(errorMessage);
			}
			// Create new branch
			departmentVO = new DepartmentVO();
			departmentVO.setCreatedBy(departmentDTO.getCreatedBy());
			departmentVO.setUpdatedBy(departmentDTO.getCreatedBy());
			message = "Department Creation SuccessFully";
		} else {
			// Update existing branch
			departmentVO = departmentRepo.findById(departmentDTO.getId()).orElseThrow(
					() -> new ApplicationException("Department not found with id: " + departmentDTO.getId()));
			departmentVO.setUpdatedBy(departmentDTO.getCreatedBy());
			if (!departmentVO.getDepartmentName().equalsIgnoreCase(departmentDTO.getDepartmentName())) {
				if (departmentRepo.existsByDepartmentNameAndOrgId(departmentDTO.getDepartmentName(),
						departmentDTO.getOrgId())) {
					String errorMessage = String.format("The DepartmentName: %s already exists This Organization.",
							departmentDTO.getDepartmentName());
					throw new ApplicationException(errorMessage);
				}
				departmentVO.setDepartmentName(departmentDTO.getDepartmentName().toUpperCase());
			}
			if (!departmentVO.getDepartmentCode().equalsIgnoreCase(departmentDTO.getDepartmentCode())) {
				if (departmentRepo.existsByDepartmentCodeAndOrgId(departmentDTO.getDepartmentCode(),
						departmentDTO.getOrgId())) {
					String errorMessage = String.format("The DepartmentCode: %s already exists This Organization.",
							departmentDTO.getDepartmentCode());
					throw new ApplicationException(errorMessage);
				}
				departmentVO.setDepartmentCode(departmentDTO.getDepartmentCode().toUpperCase());

			}
			message = "Department Update Successfully";
		}

		getDepartmentVOFromDepartmentDTO(departmentVO, departmentDTO);
		departmentRepo.save(departmentVO);
		Map<String, Object> response = new HashMap<String, Object>();
		response.put("message", message);
		response.put("departmentVO", departmentVO);
		return response;

	}

	private void getDepartmentVOFromDepartmentDTO(DepartmentVO departmentVO, DepartmentDTO departmentDTO) {
		departmentVO.setDepartmentName(departmentDTO.getDepartmentName().toUpperCase());
		departmentVO.setDepartmentCode(departmentDTO.getDepartmentCode().toUpperCase());
		departmentVO.setActive(departmentDTO.isActive());
		departmentVO.setOrgId(departmentDTO.getOrgId());

	}

	@Override
	public Map<String, Object> createUpdateDesignation(DesignationDTO designationDTO) throws ApplicationException {
		DesignationVO designationVO;
		String message = null;

		if (ObjectUtils.isEmpty(designationDTO.getId())) {

			if (designationRepo.existsByDesignationNameAndDesignationCodeAndOrgId(designationDTO.getDesignationName(),
					designationDTO.getDesignationCode(), designationDTO.getOrgId())) {
				String errorMessage = String.format(
						"The DesignationName: %s and DesignationCode: %s already exists This Organization.",
						designationDTO.getDesignationName(), designationDTO.getDesignationCode());
				throw new ApplicationException(errorMessage);
			}

			if (designationRepo.existsByDesignationNameAndOrgId(designationDTO.getDesignationName(),
					designationDTO.getOrgId())) {
				String errorMessage = String.format("The DesignationName: %s already exists This Organization.",
						designationDTO.getDesignationName());
				throw new ApplicationException(errorMessage);
			}

			if (designationRepo.existsByDesignationCodeAndOrgId(designationDTO.getDesignationCode(),
					designationDTO.getOrgId())) {
				String errorMessage = String.format("The DesignationCode: %s already exists This Organization.",
						designationDTO.getDesignationCode());
				throw new ApplicationException(errorMessage);
			}
			// Create new branch
			designationVO = new DesignationVO();
			designationVO.setCreatedBy(designationDTO.getCreatedBy());
			designationVO.setUpdatedBy(designationDTO.getCreatedBy());
			message = "Designation Creation SuccessFully";
		} else {
			// Update existing branch
			designationVO = designationRepo.findById(designationDTO.getId()).orElseThrow(
					() -> new ApplicationException("Designation not found with id: " + designationDTO.getId()));
			designationVO.setUpdatedBy(designationDTO.getCreatedBy());

			if (!designationVO.getDesignationName().equalsIgnoreCase(designationDTO.getDesignationName())) {
				if (departmentRepo.existsByDepartmentNameAndOrgId(designationDTO.getDesignationName(),
						designationDTO.getOrgId())) {
					String errorMessage = String.format("The DesignationName: %s already exists This Organization.",
							designationDTO.getDesignationName());
					throw new ApplicationException(errorMessage);
				}
				designationVO.setDesignationName(designationDTO.getDesignationName().toUpperCase());
			}
			if (!designationVO.getDesignationCode().equalsIgnoreCase(designationDTO.getDesignationCode())) {
				if (departmentRepo.existsByDepartmentCodeAndOrgId(designationDTO.getDesignationCode(),
						designationDTO.getOrgId())) {
					String errorMessage = String.format("The DesignationCode: %s already exists This Organization.",
							designationDTO.getDesignationCode());
					throw new ApplicationException(errorMessage);
				}
				designationVO.setDesignationCode(designationDTO.getDesignationCode().toUpperCase());

			}

			message = "Designation Update Successfully";
		}

		getDesignationVOFromDesignationDTO(designationVO, designationDTO);
		designationRepo.save(designationVO);
		Map<String, Object> response = new HashMap<String, Object>();
		response.put("message", message);
		response.put("designationVO", designationVO);
		return response;

	}

	private void getDesignationVOFromDesignationDTO(DesignationVO designationVO, DesignationDTO designationDTO) {
		designationVO.setDesignationName(designationDTO.getDesignationName().toUpperCase());
		designationVO.setDesignationCode(designationDTO.getDesignationCode().toUpperCase());
		designationVO.setActive(designationDTO.isActive());
		designationVO.setOrgId(designationDTO.getOrgId());

	}

	@Override
	public Optional<DesignationVO> getDesignationById(Long id) {
		// TODO Auto-generated method stub
		return designationRepo.findById(id);
	}

	@Override
	public List<DesignationVO> getDesignationByOrgId(Long orgId) {
		// TODO Auto-generated method stub
		return designationRepo.findDesignationByOrgId(orgId);
	}

	// Roles

	@Override
	public List<Map<String, Object>> getAllActiveRolesByOrgId(Long orgId) {
		Set<Object[]> chType = rolesRepo.getAllActiveRolesByOrgId(orgId);
		return getAllActiveRoles(chType);
	}

	private List<Map<String, Object>> getAllActiveRoles(Set<Object[]> chType) {
		List<Map<String, Object>> List1 = new ArrayList<>();
		for (Object[] ch : chType) {
			Map<String, Object> map = new HashMap<>();
			map.put("role", ch[0] != null ? ch[0].toString() : "");
			map.put("active", ch[1] != null ? ch[1].toString() : "");
			List1.add(map);
		}
		return List1;
	}

	@Override
	public Map<String, Object> createUpdateRoles(RolesDTO rolesDTO) throws ApplicationException {

		RolesVO rolesVO = new RolesVO();
		String message;
		String screenCode = "DEPT";
		if (ObjectUtils.isNotEmpty(rolesDTO.getId())) {
			rolesVO = rolesRepo.findById(rolesDTO.getId())
					.orElseThrow(() -> new ApplicationException("Invalid Roles details"));

			rolesVO.setUpdatedBy(rolesDTO.getCreatedBy());
			if (!rolesVO.getRole().equalsIgnoreCase(rolesDTO.getRole())) {
				if (rolesRepo.existsByRoleAndOrgId(rolesDTO.getRole(), rolesDTO.getOrgId())) {
					String errorMessage = String.format("The Role: %s already exists in This Organization.",
							rolesDTO.getRole());
					throw new ApplicationException(errorMessage);
				}
				rolesVO.setRole(rolesDTO.getRole().toUpperCase());
			}

			message = "Role Updated Successfully";
		} else {

			if (rolesRepo.existsByRoleAndOrgId(rolesDTO.getRole(), rolesDTO.getOrgId())) {
				String errorMessage = String.format("The DepartmentName : %s already exists in This Organization.",
						rolesDTO.getRole());
				throw new ApplicationException(errorMessage);
			}

			rolesVO.setCreatedBy(rolesDTO.getCreatedBy());
			rolesVO.setUpdatedBy(rolesDTO.getCreatedBy());
			message = "Department Created Successfully";
		}

		createUpdateRolesVOByRolesDTO(rolesDTO, rolesVO);
		rolesRepo.save(rolesVO);
		Map<String, Object> response = new HashMap<>();
		response.put("rolesVO", rolesVO);
		response.put("message", message);
		return response;
	}

	private void createUpdateRolesVOByRolesDTO(RolesDTO rolesDTO, RolesVO rolesVO) {
		rolesVO.setRole(rolesDTO.getRole().toUpperCase());
		rolesVO.setCreatedBy(rolesDTO.getCreatedBy().toUpperCase());
		rolesVO.setOrgId(rolesDTO.getOrgId());
		rolesVO.setUpdatedBy(rolesDTO.getUpdatedBy());
		rolesVO.setActive(rolesDTO.isActive());

	}

	@Override
	public RolesVO getRolesById(Long id) {

		return rolesRepo.getRolesById(id);
	}

	@Override
	public List<RolesVO> getRolesByOrgId(Long orgId) {
		// TODO Auto-generated method stub
		return rolesRepo.findRolesByOrgId(orgId);
	}

	

}
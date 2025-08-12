package com.efit.hrms.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.validation.Valid;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.efit.hrms.common.CommonConstant;
import com.efit.hrms.common.UserConstants;
import com.efit.hrms.dto.CityDTO;
import com.efit.hrms.dto.CompanyDTO;
import com.efit.hrms.dto.CountryDTO;
import com.efit.hrms.dto.CurrencyDTO;
import com.efit.hrms.dto.DepartmentDTO;
import com.efit.hrms.dto.DesignationDTO;
import com.efit.hrms.dto.FinScreenDTO;
import com.efit.hrms.dto.FinancialYearDTO;
import com.efit.hrms.dto.RegionDTO;
import com.efit.hrms.dto.ResponseDTO;
import com.efit.hrms.dto.RolesDTO;
import com.efit.hrms.dto.ScreenNamesDTO;
import com.efit.hrms.dto.StateDTO;
import com.efit.hrms.entity.CityVO;
import com.efit.hrms.entity.CompanyVO;
import com.efit.hrms.entity.CountryVO;
import com.efit.hrms.entity.CurrencyVO;
import com.efit.hrms.entity.DepartmentVO;
import com.efit.hrms.entity.DesignationVO;
import com.efit.hrms.entity.FinancialYearVO;
import com.efit.hrms.entity.RegionVO;
import com.efit.hrms.entity.RolesVO;
import com.efit.hrms.entity.ScreenNamesVO;
import com.efit.hrms.entity.StateVO;
import com.efit.hrms.service.CommonMasterService;

@CrossOrigin
@RestController
@RequestMapping("/api/commonmaster")
public class CommonMasterController extends BaseController {

	@Autowired
	CommonMasterService commonMasterService;

	public static final Logger LOGGER = LoggerFactory.getLogger(CommonMasterController.class);

	@GetMapping("/country")
	public ResponseEntity<ResponseDTO> getAllcountries(@RequestParam Long orgid) {
		String methodName = "getAllCountry()";
		LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
		String errorMsg = null;
		Map<String, Object> responseObjectsMap = new HashMap<>();
		ResponseDTO responseDTO = null;
		List<CountryVO> countryVO = new ArrayList<>();
		try {
			countryVO = commonMasterService.getAllCountry(orgid);
		} catch (Exception e) {
			errorMsg = e.getMessage();
			LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
		}
		if (StringUtils.isBlank(errorMsg)) {
			responseObjectsMap.put(CommonConstant.STRING_MESSAGE, "countries information get successfully");
			responseObjectsMap.put("countryVO", countryVO);
			responseDTO = createServiceResponse(responseObjectsMap);
		} else {
			responseDTO = createServiceResponseError(responseObjectsMap, "countries information receive failed",
					errorMsg);
		}
		LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
		return ResponseEntity.ok().body(responseDTO);
	}

	@GetMapping("/country/{countryid}")
	public ResponseEntity<ResponseDTO> getCountryById(@PathVariable Long countryid) {
		String methodName = "getCountryById()";
		LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
		String errorMsg = null;
		Map<String, Object> responseObjectsMap = new HashMap<>();
		ResponseDTO responseDTO = null;
		CountryVO CountryVO = null;
		try {
			CountryVO = commonMasterService.getCountryById(countryid).orElse(null);
		} catch (Exception e) {
			errorMsg = e.getMessage();
			LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
		}
		if (StringUtils.isEmpty(errorMsg)) {
			responseObjectsMap.put(CommonConstant.STRING_MESSAGE, "Country found by ID");
			responseObjectsMap.put("Country", CountryVO);
			responseDTO = createServiceResponse(responseObjectsMap);
		} else {
			errorMsg = "Country not found for ID: " + countryid;
			responseDTO = createServiceResponseError(responseObjectsMap, "Country not found", errorMsg);
		}
		LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
		return ResponseEntity.ok().body(responseDTO);
	}

	@PostMapping("/createUpdateCountry")
	public ResponseEntity<ResponseDTO> createUpdateCountry(@RequestBody CountryDTO countryDTO) {
		String methodName = "createCountry()";
		LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
		Map<String, Object> responseObjectsMap = new HashMap<String, Object>();
		String errorMsg = null;
		ResponseDTO responseDTO = null;
		try {
			Map<String, Object> createdCountryVO = commonMasterService.createUpdateCountry(countryDTO);
			responseObjectsMap.put(CommonConstant.STRING_MESSAGE, createdCountryVO.get("message"));
			responseObjectsMap.put("countryVO", createdCountryVO.get("countryVO"));
			responseDTO = createServiceResponse(responseObjectsMap);
		} catch (Exception e) {
			errorMsg = e.getMessage();
			LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
			responseDTO = createServiceResponseError(responseObjectsMap, errorMsg, errorMsg);
		}
		LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
		return ResponseEntity.ok().body(responseDTO);
	}

	// State

	@GetMapping("/state")
	public ResponseEntity<ResponseDTO> getAllStates(@RequestParam Long orgid) {
		String methodName = "getAllStates()";
		LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
		String errorMsg = null;
		Map<String, Object> responseObjectsMap = new HashMap<>();
		ResponseDTO responseDTO = null;
		List<StateVO> stateVO = new ArrayList<>();
		try {
			stateVO = commonMasterService.getAllgetAllStates(orgid);
		} catch (Exception e) {
			errorMsg = e.getMessage();
			LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
		}
		if (StringUtils.isBlank(errorMsg)) {
			responseObjectsMap.put(CommonConstant.STRING_MESSAGE, "State information get successfully");
			responseObjectsMap.put("stateVO", stateVO);
			responseDTO = createServiceResponse(responseObjectsMap);
		} else {
			responseDTO = createServiceResponseError(responseObjectsMap, "States information receive failed", errorMsg);
		}
		LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
		return ResponseEntity.ok().body(responseDTO);
	}

	// getStateByStateId

	@GetMapping("/state/{stateid}")
	public ResponseEntity<ResponseDTO> getStateById(@PathVariable Long stateid) {
		String methodName = "getStateById()";
		LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
		String errorMsg = null;
		Map<String, Object> responseObjectsMap = new HashMap<>();
		ResponseDTO responseDTO = null;
		StateVO stateVO = null;
		try {
			stateVO = commonMasterService.getStateById(stateid).orElse(null);
		} catch (Exception e) {
			errorMsg = e.getMessage();
			LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
		}

		if (StringUtils.isEmpty(errorMsg)) {
			responseObjectsMap.put(CommonConstant.STRING_MESSAGE, "States found by State ID");
			responseObjectsMap.put("stateVO", stateVO);
			responseDTO = createServiceResponse(responseObjectsMap);
		} else {
			errorMsg = "State not found for State ID: " + stateid;
			responseDTO = createServiceResponseError(responseObjectsMap, "States not found", errorMsg);
		}
		LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
		return ResponseEntity.ok().body(responseDTO);
	}

	@GetMapping("/state/country")
	public ResponseEntity<ResponseDTO> getStateByCountry(@RequestParam Long orgid, @RequestParam String country) {
		String methodName = "getStateByCountry()";
		LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
		String errorMsg = null;
		Map<String, Object> responseObjectsMap = new HashMap<>();
		ResponseDTO responseDTO = null;
		List<StateVO> stateVO = null;
		try {
			stateVO = commonMasterService.getStatesByCountry(orgid, country);
		} catch (Exception e) {
			errorMsg = e.getMessage();
			LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
		}

		if (StringUtils.isEmpty(errorMsg)) {
			responseObjectsMap.put(CommonConstant.STRING_MESSAGE, "States found by Country");
			responseObjectsMap.put("stateVO", stateVO);
			responseDTO = createServiceResponse(responseObjectsMap);
		} else {
			errorMsg = "State not found for country: " + country;
			responseDTO = createServiceResponseError(responseObjectsMap, "States not found", errorMsg);
		}
		LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
		return ResponseEntity.ok().body(responseDTO);
	}

	@PostMapping("/state")
	public ResponseEntity<ResponseDTO> createUpdateState(@RequestBody StateDTO stateDTO) {
		String methodName = "createUpdateState()";
		LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
		String errorMsg = null;
		Map<String, Object> responseObjectsMap = new HashMap<>();
		ResponseDTO responseDTO = null;
		try {
			Map<String, Object> stateVO = commonMasterService.createUpdateState(stateDTO);
			responseObjectsMap.put(CommonConstant.STRING_MESSAGE, stateVO.get("message"));
			responseObjectsMap.put("stateVO", stateVO.get("stateVO"));
			responseDTO = createServiceResponse(responseObjectsMap);
		} catch (Exception e) {
			errorMsg = e.getMessage();
			LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
			responseDTO = createServiceResponseError(responseObjectsMap, errorMsg, errorMsg);
		}
		LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
		return ResponseEntity.ok().body(responseDTO);
	}
	// city

	@GetMapping("/city")
	public ResponseEntity<ResponseDTO> getAllCities(@RequestParam Long orgid) {
		String methodName = "getAllCities()";
		LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
		String errorMsg = null;
		Map<String, Object> responseObjectsMap = new HashMap<>();
		ResponseDTO responseDTO = null;
		List<CityVO> cityVO = new ArrayList<>();
		try {
			cityVO = commonMasterService.getAllgetAllCities(orgid);
		} catch (Exception e) {
			errorMsg = e.getMessage();
			LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
		}

		if (StringUtils.isBlank(errorMsg)) {
			responseObjectsMap.put(CommonConstant.STRING_MESSAGE, "city information get successfully");
			responseObjectsMap.put("cityVO", cityVO);
			responseDTO = createServiceResponse(responseObjectsMap);
		} else {
			responseDTO = createServiceResponseError(responseObjectsMap, "city information receive failed", errorMsg);
		}
		LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
		return ResponseEntity.ok().body(responseDTO);
	}

	@GetMapping("/city/state")
	public ResponseEntity<ResponseDTO> getAllCitiesByState(@RequestParam Long orgid, @RequestParam String state) {
		String methodName = "getAllCitiesByState()";
		LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
		String errorMsg = null;
		Map<String, Object> responseObjectsMap = new HashMap<>();
		ResponseDTO responseDTO = null;
		List<CityVO> cityVO = new ArrayList<>();
		try {
			cityVO = commonMasterService.getAllCitiesByState(orgid, state);
		} catch (Exception e) {
			errorMsg = e.getMessage();
			LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
		}

		if (StringUtils.isBlank(errorMsg)) {
			responseObjectsMap.put(CommonConstant.STRING_MESSAGE, "city information get successfully");
			responseObjectsMap.put("cityVO", cityVO);
			responseDTO = createServiceResponse(responseObjectsMap);
		} else {
			responseDTO = createServiceResponseError(responseObjectsMap, "city information receive failed", errorMsg);
		}
		LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
		return ResponseEntity.ok().body(responseDTO);
	}

	@GetMapping("/city/{cityid}")
	public ResponseEntity<ResponseDTO> getCityById(@PathVariable Long cityid) {
		String methodName = "getCityById()";
		LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
		String errorMsg = null;
		Map<String, Object> responseObjectsMap = new HashMap<>();
		ResponseDTO responseDTO = null;
		CityVO cityVO = null;
		try {
			cityVO = commonMasterService.getCityById(cityid).orElse(null);
		} catch (Exception e) {
			errorMsg = e.getMessage();
			LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
		}

		if (StringUtils.isEmpty(errorMsg)) {
			responseObjectsMap.put(CommonConstant.STRING_MESSAGE, "City found by City ID");
			responseObjectsMap.put("cityVO", cityVO);
			responseDTO = createServiceResponse(responseObjectsMap);
		} else {
			errorMsg = "City not found for City ID: " + cityid;
			responseDTO = createServiceResponseError(responseObjectsMap, "City not found", errorMsg);
		}
		LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
		return ResponseEntity.ok().body(responseDTO);
	}

	@PostMapping("/createUpdateCity")
	public ResponseEntity<ResponseDTO> createUpdateCity(@RequestBody CityDTO cityDTO) {
		String methodName = "createCity()";
		LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
		String errorMsg = null;
		Map<String, Object> responseObjectsMap = new HashMap<>();
		ResponseDTO responseDTO = null;
		try {
			Map<String, Object> createdCityVO = commonMasterService.createUpdateCity(cityDTO);
			responseObjectsMap.put(CommonConstant.STRING_MESSAGE, createdCityVO.get("message"));
			responseObjectsMap.put("cityVO", createdCityVO.get("cityVO"));
			responseDTO = createServiceResponse(responseObjectsMap);
		} catch (Exception e) {
			errorMsg = e.getMessage();
			LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
			responseDTO = createServiceResponseError(responseObjectsMap, errorMsg, errorMsg);
		}
		LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
		return ResponseEntity.ok().body(responseDTO);
	}

	// Region

	@GetMapping("/getAllRegion")
	public ResponseEntity<ResponseDTO> getAllRegion() {
		String methodName = "getAllRegios()";
		LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
		String errorMsg = null;
		Map<String, Object> responseObjectsMap = new HashMap<>();
		ResponseDTO responseDTO = null;
		List<RegionVO> regionVO = new ArrayList<>();
		try {
			regionVO = commonMasterService.getAllRegios();
		} catch (Exception e) {
			errorMsg = e.getMessage();
			LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
		}
		if (StringUtils.isBlank(errorMsg)) {
			responseObjectsMap.put(CommonConstant.STRING_MESSAGE, "Region information get successfully");
			responseObjectsMap.put("regionVO", regionVO);
			responseDTO = createServiceResponse(responseObjectsMap);
		} else {
			responseDTO = createServiceResponseError(responseObjectsMap, "Region information receive failed", errorMsg);
		}
		LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
		return ResponseEntity.ok().body(responseDTO);
	}

	@GetMapping("/getAllRegionsByOrgId")
	public ResponseEntity<ResponseDTO> getAllRegionsByOrgId(@RequestParam Long orgId) {
		String methodName = "getAllRegionsByOrgId()";
		LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
		String errorMsg = null;
		Map<String, Object> responseObjectsMap = new HashMap<>();
		ResponseDTO responseDTO = null;
		List<RegionVO> regionVO = new ArrayList<>();
		try {
			regionVO = commonMasterService.getAllRegionsByOrgId(orgId);
		} catch (Exception e) {
			errorMsg = e.getMessage();
			LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
		}
		if (StringUtils.isBlank(errorMsg)) {
			responseObjectsMap.put(CommonConstant.STRING_MESSAGE, "Region information get successfully");
			responseObjectsMap.put("regionVO", regionVO);
			responseDTO = createServiceResponse(responseObjectsMap);
		} else {
			responseDTO = createServiceResponseError(responseObjectsMap, "Region information receive failed", errorMsg);
		}
		LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
		return ResponseEntity.ok().body(responseDTO);
	}

	@GetMapping("/region/{regionid}")
	public ResponseEntity<ResponseDTO> getRegionById(@PathVariable Long regionid) {
		String methodName = "getRegionById()";
		LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
		String errorMsg = null;
		Map<String, Object> responseObjectsMap = new HashMap<>();
		ResponseDTO responseDTO = null;
		RegionVO regionVO = null;
		try {
			regionVO = commonMasterService.getRegionById(regionid).orElse(null);
		} catch (Exception e) {
			errorMsg = e.getMessage();
			LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
		}

		if (StringUtils.isEmpty(errorMsg)) {
			responseObjectsMap.put(CommonConstant.STRING_MESSAGE, "Region found by Region ID");
			responseObjectsMap.put("regionVO", regionVO);
			responseDTO = createServiceResponse(responseObjectsMap);
		} else {
			errorMsg = "Region not found for Region ID: " + regionid;
			responseDTO = createServiceResponseError(responseObjectsMap, "Region not found", errorMsg);
		}
		LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
		return ResponseEntity.ok().body(responseDTO);
	}

	@PutMapping("/createUpdateRegion")
	public ResponseEntity<ResponseDTO> createUpdateRegion(@RequestBody RegionDTO regionDTO) {
		String methodName = "createUpdateRegion()";
		LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
		String errorMsg = null;
		Map<String, Object> responseObjectsMap = new HashMap<>();
		ResponseDTO responseDTO = null;
		try {
			Map<String, Object> regionvo = commonMasterService.createUpdateRegion(regionDTO);
			responseObjectsMap.put(CommonConstant.STRING_MESSAGE, regionvo.get("message"));
			responseObjectsMap.put("regionvo", regionvo.get("regionVO"));
			responseDTO = createServiceResponse(responseObjectsMap);
		} catch (Exception e) {
			errorMsg = e.getMessage();
			LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
			responseDTO = createServiceResponseError(responseObjectsMap, errorMsg, errorMsg);
		}
		LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
		return ResponseEntity.ok().body(responseDTO);
	}

	// Currency

	@GetMapping("/currency")
	public ResponseEntity<ResponseDTO> getAllCurrency(@RequestParam Long orgid) {
		String methodName = "getAllCurrency()";
		LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
		String errorMsg = null;
		Map<String, Object> responseObjectsMap = new HashMap<>();
		ResponseDTO responseDTO = null;
		List<CurrencyVO> currencyVO = new ArrayList<>();
		try {
			currencyVO = commonMasterService.getAllCurrency(orgid);
		} catch (Exception e) {
			errorMsg = e.getMessage();
			LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
		}
		if (StringUtils.isBlank(errorMsg)) {
			responseObjectsMap.put(CommonConstant.STRING_MESSAGE, "Currency" + " information get successfully");
			responseObjectsMap.put("currencyVO", currencyVO);
			responseDTO = createServiceResponse(responseObjectsMap);
		} else {
			responseDTO = createServiceResponseError(responseObjectsMap, "Currency information receive failed",
					errorMsg);
		}
		LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
		return ResponseEntity.ok().body(responseDTO);
	}

	// getCUrrencyById

	@GetMapping("/currency/{currencyid}")
	public ResponseEntity<ResponseDTO> getCurrencyById(@PathVariable Long currencyid) {
		String methodName = "getCurrencyById()";
		LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
		String errorMsg = null;
		Map<String, Object> responseObjectsMap = new HashMap<>();
		ResponseDTO responseDTO = null;
		CurrencyVO currencyVO = null;
		try {
			currencyVO = commonMasterService.getCurrencyById(currencyid).orElse(null);
		} catch (Exception e) {
			errorMsg = e.getMessage();
			LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
		}

		if (StringUtils.isEmpty(errorMsg)) {
			responseObjectsMap.put(CommonConstant.STRING_MESSAGE, "Currency found by Currency ID");
			responseObjectsMap.put("currencyVO", currencyVO);
			responseDTO = createServiceResponse(responseObjectsMap);
		} else {
			errorMsg = "Currency not found for Currency ID: " + currencyid;
			responseDTO = createServiceResponseError(responseObjectsMap, "Currency not found", errorMsg);
		}
		LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
		return ResponseEntity.ok().body(responseDTO);
	}

	@PutMapping("/createUpdateCurrency")
	public ResponseEntity<ResponseDTO> createUpdateCurrency(@RequestBody CurrencyDTO currencyDTO) {
		String methodName = "createUpdateCurrency()";
		LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
		String errorMsg = null;
		Map<String, Object> responseObjectsMap = new HashMap<>();
		ResponseDTO responseDTO = null;
		try {
			Map<String, Object> currency = commonMasterService.createUpdateCurrency(currencyDTO);
			responseObjectsMap.put(CommonConstant.STRING_MESSAGE, currency.get("message"));
			responseObjectsMap.put("currency", currency.get("currencyVO"));
			responseDTO = createServiceResponse(responseObjectsMap);
		} catch (Exception e) {
			errorMsg = e.getMessage();
			LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
			responseDTO = createServiceResponseError(responseObjectsMap, errorMsg, errorMsg);
		}
		LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
		return ResponseEntity.ok().body(responseDTO);
	}
	
	@GetMapping("/getAllCurrencyForExRate")
	public ResponseEntity<ResponseDTO> getAllCurrencyForExRate(@RequestParam Long orgId) {
		String methodName = "getAllCurrencyForExRate()";
		LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
		String errorMsg = null;
		Map<String, Object> responseObjectsMap = new HashMap<>();
		ResponseDTO responseDTO = null;
		List<Map<String, Object>> currencyVO = new ArrayList<>();
		try {
			currencyVO = commonMasterService.getAllCurrencyForExRate(orgId);
		} catch (Exception e) {
			errorMsg = e.getMessage();
			LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
		}
		if (StringUtils.isBlank(errorMsg)) {
			responseObjectsMap.put(CommonConstant.STRING_MESSAGE, "Currency" + " information get successfully");
			responseObjectsMap.put("currencyVO", currencyVO);
			responseDTO = createServiceResponse(responseObjectsMap);
		} else {
			responseDTO = createServiceResponseError(responseObjectsMap, "Currency information receive failed",
					errorMsg);
		}
		LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
		return ResponseEntity.ok().body(responseDTO);
	}

	// Company

	@GetMapping("/company")
	public ResponseEntity<ResponseDTO> getAllCompany() {
		String methodName = "getAllCompany()";
		LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
		String errorMsg = null;
		Map<String, Object> responseObjectsMap = new HashMap<>();
		ResponseDTO responseDTO = null;
		List<CompanyVO> companyVO = new ArrayList<>();
		try {
			companyVO = commonMasterService.getAllCompany();
		} catch (Exception e) {
			errorMsg = e.getMessage();
			LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
		}
		if (StringUtils.isBlank(errorMsg)) {
			responseObjectsMap.put(CommonConstant.STRING_MESSAGE, "Company information get successfully");
			responseObjectsMap.put("companyVO", companyVO);
			responseDTO = createServiceResponse(responseObjectsMap);
		} else {
			responseDTO = createServiceResponseError(responseObjectsMap, "Company information receive failed",
					errorMsg);
		}
		LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
		return ResponseEntity.ok().body(responseDTO);
	}

	@GetMapping("/company/{companyid}")
	public ResponseEntity<ResponseDTO> getcompanyById(@PathVariable Long companyid) {
		String methodName = "getCompanyById()";
		LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
		String errorMsg = null;
		Map<String, Object> responseObjectsMap = new HashMap<>();
		ResponseDTO responseDTO = null;
		List<CompanyVO> companyVO = new ArrayList<CompanyVO>();
		try {
			companyVO = commonMasterService.getCompanyById(companyid);
		} catch (Exception e) {
			errorMsg = e.getMessage();
			LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
		}

		if (StringUtils.isEmpty(errorMsg)) {
			responseObjectsMap.put(CommonConstant.STRING_MESSAGE, "Company found by Company ID");
			responseObjectsMap.put("companyVO", companyVO);
			responseDTO = createServiceResponse(responseObjectsMap);
		} else {
			errorMsg = "company not found for companyID: " + companyid;
			responseDTO = createServiceResponseError(responseObjectsMap, "company not found", errorMsg);
		}
		LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
		return ResponseEntity.ok().body(responseDTO);
	}

	@PostMapping("/company")
	public ResponseEntity<ResponseDTO> createCompany(@RequestBody CompanyDTO companyDTO) {
		String methodName = "createCompany()";
		LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
		String errorMsg = null;
		Map<String, Object> responseObjectsMap = new HashMap<>();
		ResponseDTO responseDTO = null;
		try {
			CompanyVO createdCompanyVO = commonMasterService.createCompany(companyDTO);
			responseObjectsMap.put(CommonConstant.STRING_MESSAGE, "Company created successfully");
			responseObjectsMap.put("createdCompanyVO", createdCompanyVO);
			responseDTO = createServiceResponse(responseObjectsMap);
		} catch (Exception e) {
			errorMsg = e.getMessage();
			LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
			responseDTO = createServiceResponseError(responseObjectsMap, "Company creation failed", errorMsg);
		}
		LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
		return ResponseEntity.ok().body(responseDTO);
	}

	@PutMapping("/updateCompany")
	public ResponseEntity<ResponseDTO> updateCompany(@RequestBody CompanyDTO companyDTO) {
		String methodName = "updateCompany()";
		LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
		String errorMsg = null;
		Map<String, Object> responseObjectsMap = new HashMap<>();
		ResponseDTO responseDTO = null;
		try {
			CompanyVO updatedCompanyVO = commonMasterService.updateCompany(companyDTO);
			if (updatedCompanyVO != null) {
				responseObjectsMap.put(CommonConstant.STRING_MESSAGE, "Customers updated successfully");
				responseObjectsMap.put("CompanyVO", updatedCompanyVO);
				responseDTO = createServiceResponse(responseObjectsMap);
			} else {
				errorMsg = "Organization not found for ID: " + companyDTO.getId();
				responseDTO = createServiceResponseError(responseObjectsMap, errorMsg, errorMsg);
			}
		} catch (Exception e) {
			errorMsg = e.getMessage();
			LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
			responseDTO = createServiceResponseError(responseObjectsMap, errorMsg, errorMsg);
		}
		LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
		return ResponseEntity.ok().body(responseDTO);
	}
	
	@PostMapping("/uploadCompanyLogoInBloob")
	public ResponseEntity<ResponseDTO> uploadCompanyLogoInBloob(@RequestParam("file") MultipartFile file,
			@RequestParam Long id) {
		String methodName = "uploadCompanyLogoInBloob()";
		LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
		String errorMsg = null;
		Map<String, Object> responseObjectsMap = new HashMap<>();
		ResponseDTO responseDTO = null;
		CompanyVO companyVO = null;
		try {
			companyVO = commonMasterService.uploadCompanyLogoInBloob(file, id);
		} catch (Exception e) {
			errorMsg = e.getMessage();
			LOGGER.error("Unable To Upload PartImage", methodName, errorMsg);
		}
		if (StringUtils.isBlank(errorMsg)) {
			responseObjectsMap.put(CommonConstant.STRING_MESSAGE, "Company Logo Successfully Upload");
			responseObjectsMap.put("companyVO", companyVO);
			responseDTO = createServiceResponse(responseObjectsMap);
		} else {
			responseDTO = createServiceResponseError(responseObjectsMap, "Company Logo Upload Failed", errorMsg);
		}
		LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
		return ResponseEntity.ok().body(responseDTO);
	}

	// FINANCIAL YEAR

	@PutMapping("/createUpdateFinYear")
	public ResponseEntity<ResponseDTO> createUpdateFinYear(@RequestBody FinancialYearDTO financialYearDTO) {
		String methodName = "createUpdateFinYear()";
		LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
		String errorMsg = null;
		Map<String, Object> responseObjectsMap = new HashMap<>();
		ResponseDTO responseDTO = null;
		try {
			Map<String, Object> finYearVO = commonMasterService.createUpdateFinYear(financialYearDTO);
			responseObjectsMap.put(CommonConstant.STRING_MESSAGE, finYearVO.get("messages"));
			responseObjectsMap.put("finYearVO", finYearVO.get("financialYearVO"));
			responseDTO = createServiceResponse(responseObjectsMap);
		} catch (Exception e) {
			errorMsg = e.getMessage();
			LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
			responseDTO = createServiceResponseError(responseObjectsMap, errorMsg, errorMsg);
		}
		LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
		return ResponseEntity.ok().body(responseDTO);
	}

	@GetMapping("/getAllAciveFInYear")
	public ResponseEntity<ResponseDTO> getAllFInYear(@RequestParam Long orgId) {
		String methodName = "getAllFInYear()";
		LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
		String errorMsg = null;
		Map<String, Object> responseObjectsMap = new HashMap<>();
		ResponseDTO responseDTO = null;
		List<FinancialYearVO> financialYearVOs = new ArrayList<FinancialYearVO>();
		try {
			financialYearVOs = commonMasterService.getAllActiveFInYear(orgId);
		} catch (Exception e) {
			errorMsg = e.getMessage();
			LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
		}
		if (StringUtils.isBlank(errorMsg)) {
			responseObjectsMap.put(CommonConstant.STRING_MESSAGE, "FInYear information get successfully");
			responseObjectsMap.put("financialYearVOs", financialYearVOs);
			responseDTO = createServiceResponse(responseObjectsMap);
		} else {
			responseDTO = createServiceResponseError(responseObjectsMap, "FInYear information receive failed",
					errorMsg);
		}
		LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
		return ResponseEntity.ok().body(responseDTO);
	}

	@GetMapping("/getAllFInYearByOrgId")
	public ResponseEntity<ResponseDTO> getAllFInYearByOrgId(Long orgId) {
		String methodName = "getAllFInYearByOrgId()";
		LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
		String errorMsg = null;
		Map<String, Object> responseObjectsMap = new HashMap<>();
		ResponseDTO responseDTO = null;
		List<FinancialYearVO> financialYearVOs = new ArrayList<FinancialYearVO>();
		try {
			financialYearVOs = commonMasterService.getAllFInYearByOrgId(orgId);
		} catch (Exception e) {
			errorMsg = e.getMessage();
			LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
		}
		if (StringUtils.isBlank(errorMsg)) {
			responseObjectsMap.put(CommonConstant.STRING_MESSAGE, "FInYear information get successfully By OrgId");
			responseObjectsMap.put("financialYearVOs", financialYearVOs);
			responseDTO = createServiceResponse(responseObjectsMap);
		} else {
			responseDTO = createServiceResponseError(responseObjectsMap, "FInYear information receive failed",
					errorMsg);
		}
		LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
		return ResponseEntity.ok().body(responseDTO);
	}

	@GetMapping("/getAllFInYearById")
	public ResponseEntity<ResponseDTO> getAllFInYearById(Long id) {
		String methodName = "getAllFInYearById()";
		LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
		String errorMsg = null;
		Map<String, Object> responseObjectsMap = new HashMap<>();
		ResponseDTO responseDTO = null;
		FinancialYearVO financialYearVOs = null;
		try {
			financialYearVOs = commonMasterService.getAllFInYearById(id).orElse(null);
		} catch (Exception e) {
			errorMsg = e.getMessage();
			LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
		}
		if (StringUtils.isBlank(errorMsg)) {
			responseObjectsMap.put(CommonConstant.STRING_MESSAGE, "FInYear information get successfully By Id");
			responseObjectsMap.put("financialYearVOs", financialYearVOs);
			responseDTO = createServiceResponse(responseObjectsMap);
		} else {
			responseDTO = createServiceResponseError(responseObjectsMap, "FInYear information receive failed",
					errorMsg);
		}
		LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
		return ResponseEntity.ok().body(responseDTO);
	}

//	 FinScreen
	@GetMapping("/getFinScreenById")
	public ResponseEntity<ResponseDTO> getFinScreenById(@RequestParam(required = false) Long id) {
		String methodName = "getFinScreenById()";
		LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
		String errorMsg = null;
		Map<String, Object> responseObjectsMap = new HashMap<>();
		ResponseDTO responseDTO = null;
		List<ScreenNamesVO> finScreenVO = new ArrayList<>();
		try {
			finScreenVO = commonMasterService.getFinScreenById(id);
		} catch (Exception e) {
			errorMsg = e.getMessage();
			LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
		}
		if (StringUtils.isBlank(errorMsg)) {
			responseObjectsMap.put(CommonConstant.STRING_MESSAGE, "FinScreen information get successfully By Id");
			responseObjectsMap.put("finScreenVO", finScreenVO);
			responseDTO = createServiceResponse(responseObjectsMap);
		} else {
			responseDTO = createServiceResponseError(responseObjectsMap, "FinScreen information receive failed By Id",
					errorMsg);
		}
		LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
		return ResponseEntity.ok().body(responseDTO);
	}

	@PutMapping("/updateFinScreen")
	public ResponseEntity<ResponseDTO> updateFinScreen(@Valid @RequestBody FinScreenDTO finScreenDTO) {
		String methodName = "updateCreateFinScreen()";
		LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
		String errorMsg = null;
		Map<String, Object> responseObjectsMap = new HashMap<>();
		ResponseDTO responseDTO = null;
		try {
			ScreenNamesVO finScreenVO = commonMasterService.updateCreateFinScreen(finScreenDTO);
			if (finScreenVO != null) {
				boolean isUpdate = finScreenDTO.getId() != null;
				responseObjectsMap.put(CommonConstant.STRING_MESSAGE,
						isUpdate ? "FinScreen updated successfully" : "FinScreen created successfully");
				responseObjectsMap.put("finScreenVO", finScreenVO);
				responseDTO = createServiceResponse(responseObjectsMap);
			} else {
				boolean isUpdate = finScreenDTO.getId() != null;
				errorMsg = isUpdate ? "FinScreen not found for ID: " + finScreenDTO.getId()
						: "FinScreen creation failed";
				responseDTO = createServiceResponseError(responseObjectsMap,
						isUpdate ? "FinScreen update failed" : "FinScreen creation failed", errorMsg);
			}
		} catch (Exception e) {
			errorMsg = e.getMessage();
			boolean isUpdate = finScreenDTO.getId() != null;
			LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
			responseDTO = createServiceResponseError(responseObjectsMap,
					isUpdate ? "FinScreen update failed" : "FinScreen creation failed", errorMsg);
		}
		LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
		return ResponseEntity.ok().body(responseDTO);
	}

	@GetMapping("/getAllScreenCode")
	public ResponseEntity<ResponseDTO> getAllScreenCode(@RequestParam Long orgId) {
		String methodName = "getAllScreenCode()";
		LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
		String errorMsg = null;
		Map<String, Object> responseObjectsMap = new HashMap<>();
		ResponseDTO responseDTO = null;
		List<Map<String, Object>> finScreen = new ArrayList<>();
		try {
			finScreen = commonMasterService.getAllScreenCode(orgId);
		} catch (Exception e) {
			errorMsg = e.getMessage();
			LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
		}
		if (StringUtils.isBlank(errorMsg)) {
			responseObjectsMap.put(CommonConstant.STRING_MESSAGE, "Fin Screen information get successfully By userId");
			responseObjectsMap.put("finScreen", finScreen);
			responseDTO = createServiceResponse(responseObjectsMap);
		} else {
			responseDTO = createServiceResponseError(responseObjectsMap,
					"Fin Screen information receive failed By userId", errorMsg);
		}
		LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
		return ResponseEntity.ok().body(responseDTO);
	}

	// Screen Names
	@PutMapping("/createUpdateScreenNames")
	public ResponseEntity<ResponseDTO> createUpdateScreenNames(@RequestBody ScreenNamesDTO screenNamesDTO) {
		String methodName = "createUpdateScreenNames()";
		LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
		String errorMsg = null;
		Map<String, Object> responseObjectsMap = new HashMap<>();
		ResponseDTO responseDTO = null;
		try {
			Map<String, Object> screenNamesVO = commonMasterService.createUpdateScreenNames(screenNamesDTO);
			responseObjectsMap.put(CommonConstant.STRING_MESSAGE, screenNamesVO.get("message"));
			responseObjectsMap.put("screenNamesVO", screenNamesVO.get("screenNamesVO"));
			responseDTO = createServiceResponse(responseObjectsMap);
		} catch (Exception e) {
			errorMsg = e.getMessage();
			LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
			responseDTO = createServiceResponseError(responseObjectsMap, errorMsg, errorMsg);
		}
		LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
		return ResponseEntity.ok().body(responseDTO);
	}

	@GetMapping("/getAllScreenNames")
	public ResponseEntity<ResponseDTO> getAllScreenNames() {
		String methodName = "getAllScreenNames()";
		LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
		String errorMsg = null;
		Map<String, Object> responseObjectsMap = new HashMap<>();
		ResponseDTO responseDTO = null;
		List<ScreenNamesVO> screenNamesVO = new ArrayList<>();
		try {
			screenNamesVO = commonMasterService.getAllScreenNames();
		} catch (Exception e) {
			errorMsg = e.getMessage();
			LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
		}
		if (StringUtils.isBlank(errorMsg)) {
			responseObjectsMap.put(CommonConstant.STRING_MESSAGE, "screenNames information get successfully");
			responseObjectsMap.put("screenNamesVO", screenNamesVO);
			responseDTO = createServiceResponse(responseObjectsMap);
		} else {
			responseDTO = createServiceResponseError(responseObjectsMap, "screenNames information receive failed",
					errorMsg);
		}
		LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
		return ResponseEntity.ok().body(responseDTO);
	}

	@GetMapping("/screenNamesById")
	public ResponseEntity<ResponseDTO> getScreenNamesById(@RequestParam Long id) {
		String methodName = "getScreenNamesById()";
		LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
		String errorMsg = null;
		Map<String, Object> responseObjectsMap = new HashMap<>();
		ResponseDTO responseDTO = null;
		ScreenNamesVO screenNamesVO = new ScreenNamesVO();
		try {
			screenNamesVO = commonMasterService.getScreenNamesById(id);
		} catch (Exception e) {
			errorMsg = e.getMessage();
			LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
		}
		if (StringUtils.isBlank(errorMsg)) {
			responseObjectsMap.put(CommonConstant.STRING_MESSAGE, "screenNames information get successfully");
			responseObjectsMap.put("screenNamesVO", screenNamesVO);
			responseDTO = createServiceResponse(responseObjectsMap);
		} else {
			responseDTO = createServiceResponseError(responseObjectsMap, "screenNames information receive failed",
					errorMsg);
		}
		LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
		return ResponseEntity.ok().body(responseDTO);
	}
	
	//Department
	
		@GetMapping("/getDepartmentByOrgId")
		public ResponseEntity<ResponseDTO> getDepartmentByOrgId(@RequestParam Long orgid) {
			String methodName = "getDepartmentByOrgId()";
			LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
			String errorMsg = null;
			Map<String, Object> responseObjectsMap = new HashMap<>();
			ResponseDTO responseDTO = null;
			List<DepartmentVO> departmentVO = new ArrayList<>();
			try {
				departmentVO = commonMasterService.getDepartmentByOrgId(orgid);
			} catch (Exception e) {
				errorMsg = e.getMessage();
				LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
			}
			if (StringUtils.isBlank(errorMsg)) {
				responseObjectsMap.put(CommonConstant.STRING_MESSAGE, "Department information get successfully");
				responseObjectsMap.put("departmentVO", departmentVO);
				responseDTO = createServiceResponse(responseObjectsMap);
			} else {
				responseDTO = createServiceResponseError(responseObjectsMap, "Department information receive failed",
						errorMsg);
			}
			LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
			return ResponseEntity.ok().body(responseDTO);
		}

		@GetMapping("getDepartmentById")
		public ResponseEntity<ResponseDTO> getDepartmentById(@RequestParam Long id) {
			String methodName = "getDepartmentById()";
			LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
			String errorMsg = null;
			Map<String, Object> responseObjectsMap = new HashMap<>();
			ResponseDTO responseDTO = null;
			Optional<DepartmentVO>  departmentVO = null;
			try {
				departmentVO = commonMasterService.getDepartmentById(id);
			} catch (Exception e) {
				errorMsg = e.getMessage();
				LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
			}
			if (StringUtils.isEmpty(errorMsg)) {
				responseObjectsMap.put(CommonConstant.STRING_MESSAGE, "Department found by ID");
				responseObjectsMap.put("departmentVO", departmentVO);
				responseDTO = createServiceResponse(responseObjectsMap);
			} else {
				errorMsg = "Department not found for ID: " + id;
				responseDTO = createServiceResponseError(responseObjectsMap, "Department not found", errorMsg);
			}
			LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
			return ResponseEntity.ok().body(responseDTO);
		}

		@PostMapping("/createUpdateDepartment")
		public ResponseEntity<ResponseDTO> createUpdateDepartment(@RequestBody DepartmentDTO departmentDTO) {
			String methodName = "createUpdateDepartment()";
			LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
			Map<String, Object> responseObjectsMap = new HashMap<String, Object>();
			String errorMsg = null;
			ResponseDTO responseDTO = null;
			try {
				Map<String, Object> departmentVO = commonMasterService.createUpdateDepartment(departmentDTO);
				responseObjectsMap.put(CommonConstant.STRING_MESSAGE, departmentVO.get("message"));
				responseObjectsMap.put("departmentVO", departmentVO.get("departmentVO"));
				responseDTO = createServiceResponse(responseObjectsMap);
			} catch (Exception e) {
				errorMsg = e.getMessage();
				LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
				responseDTO = createServiceResponseError(responseObjectsMap, errorMsg, errorMsg);
			}
			LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
			return ResponseEntity.ok().body(responseDTO);
		}
		
		
		@PostMapping("/uploadDepartment")
		public ResponseEntity<ResponseDTO> uploadDepartment(@RequestParam("files") MultipartFile file,
				@RequestParam("orgId") Long orgId, @RequestParam("createdBy") String createdBy) {
			String methodName = "uploadDepartment()";
			Map<String, Object> responseObjectsMap = new HashMap<>();
			ResponseDTO responseDTO;

			try {
				// Call service method to process Excel upload
				commonMasterService.uploadDepartment(file, orgId, createdBy);

				responseObjectsMap.put(CommonConstant.STRING_MESSAGE, "UploadDepartment data uploaded successfully");
				responseDTO = createServiceResponse(responseObjectsMap);

			} catch (Exception e) {
				String errorMsg = e.getMessage();
				LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
				responseDTO = createServiceResponseError(responseObjectsMap, errorMsg, errorMsg);
			}

			LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
			return ResponseEntity.ok().body(responseDTO);

		}

		//Designation
		
		@PostMapping("/uploadDesignation")
		public ResponseEntity<ResponseDTO> uploadDesignation(@RequestParam("files") MultipartFile file,
				@RequestParam("orgId") Long orgId, @RequestParam("createdBy") String createdBy) {
			String methodName = "uploadDesignation()";
			Map<String, Object> responseObjectsMap = new HashMap<>();
			ResponseDTO responseDTO;

			try {
				// Call service method to process Excel upload
				commonMasterService.uploadDesignation(file, orgId, createdBy);

				responseObjectsMap.put(CommonConstant.STRING_MESSAGE, "uploadDesignation data uploaded successfully");
				responseDTO = createServiceResponse(responseObjectsMap);

			} catch (Exception e) {
				String errorMsg = e.getMessage();
				LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
				responseDTO = createServiceResponseError(responseObjectsMap, errorMsg, errorMsg);
			}

			LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
			return ResponseEntity.ok().body(responseDTO);

		}

		
		@GetMapping("/getDesignationByOrgId")
		public ResponseEntity<ResponseDTO> getDesignationByOrgId(@RequestParam Long orgid) {
			String methodName = "getDesignationByOrgId()";
			LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
			String errorMsg = null;
			Map<String, Object> responseObjectsMap = new HashMap<>();
			ResponseDTO responseDTO = null;
			List<DesignationVO> designationVO = new ArrayList<>();
			try {
				designationVO = commonMasterService.getDesignationByOrgId(orgid);
			} catch (Exception e) {
				errorMsg = e.getMessage();
				LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
			}
			if (StringUtils.isBlank(errorMsg)) {
				responseObjectsMap.put(CommonConstant.STRING_MESSAGE, "Designation information get successfully");
				responseObjectsMap.put("designationVO", designationVO);
				responseDTO = createServiceResponse(responseObjectsMap);
			} else {
				responseDTO = createServiceResponseError(responseObjectsMap, "Designation information receive failed",
						errorMsg);
			}
			LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
			return ResponseEntity.ok().body(responseDTO);
		}

		@GetMapping("getDesignationById")
		public ResponseEntity<ResponseDTO> getDesignationById(@RequestParam Long id) {
			String methodName = "getDesignationById()";
			LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
			String errorMsg = null;
			Map<String, Object> responseObjectsMap = new HashMap<>();
			ResponseDTO responseDTO = null;
			Optional<DesignationVO> designationVO = null;
			try {
				designationVO = commonMasterService.getDesignationById(id);
			} catch (Exception e) {
				errorMsg = e.getMessage();
				LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
			}
			if (StringUtils.isEmpty(errorMsg)) {
				responseObjectsMap.put(CommonConstant.STRING_MESSAGE, "Designation found by ID");
				responseObjectsMap.put("designationVO", designationVO);
				responseDTO = createServiceResponse(responseObjectsMap);
			} else {
				errorMsg = "Designation not found for ID: " + id;
				responseDTO = createServiceResponseError(responseObjectsMap, "Designation not found", errorMsg);
			}
			LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
			return ResponseEntity.ok().body(responseDTO);
		}

		@PostMapping("/createUpdateDesignation")
		public ResponseEntity<ResponseDTO> createUpdateDesignation(@RequestBody DesignationDTO designationDTO) {
			String methodName = "createUpdateDesignation()";
			LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
			Map<String, Object> responseObjectsMap = new HashMap<String, Object>();
			String errorMsg = null;
			ResponseDTO responseDTO = null;
			try {
				Map<String, Object> designationVO = commonMasterService.createUpdateDesignation(designationDTO);
				responseObjectsMap.put(CommonConstant.STRING_MESSAGE, designationVO.get("message"));
				responseObjectsMap.put("designationVO", designationVO.get("designationVO"));
				responseDTO = createServiceResponse(responseObjectsMap);
			} catch (Exception e) {
				errorMsg = e.getMessage();
				LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
				responseDTO = createServiceResponseError(responseObjectsMap, errorMsg, errorMsg);
			}
			LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
			return ResponseEntity.ok().body(responseDTO);
		}
		
		
		//Roles
		
		@GetMapping("/getActiveRoles")
		public ResponseEntity<ResponseDTO> getAllActiveRolesByOrgId(@RequestParam Long orgId) {
			String methodName = "getAllActiveRolesByOrgId()";
			LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
			String errorMsg = null;
			Map<String, Object> responseObjectsMap = new HashMap<>();
			ResponseDTO responseDTO = null;
			List<Map<String, Object>> mapp = new ArrayList<>();

			try {
				mapp = commonMasterService.getAllActiveRolesByOrgId(orgId);
			} catch (Exception e) {
				errorMsg = e.getMessage();
				LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
			}

			if (StringUtils.isBlank(errorMsg)) {
				responseObjectsMap.put(CommonConstant.STRING_MESSAGE, "Active Roles Details retrieved successfully");
				responseObjectsMap.put("rolesVO", mapp);
				responseDTO = createServiceResponse(responseObjectsMap);
			} else {
				responseDTO = createServiceResponseError(responseObjectsMap, "Failed to retrieve Active Roles Details", errorMsg);
			}

			LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
			return ResponseEntity.ok().body(responseDTO);
		}

		@PutMapping("/createUpdateRoles")
		public ResponseEntity<ResponseDTO> createUpdateRoles(@Valid @RequestBody RolesDTO rolesDTO) {
			String methodName = "createUpdateRoles()";
			LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
			String errorMsg = null;
			Map<String, Object> responseObjectsMap = new HashMap<>();
			ResponseDTO responseDTO = null;
			try {
				Map<String, Object> rolesVO = commonMasterService.createUpdateRoles(rolesDTO);
				responseObjectsMap.put(CommonConstant.STRING_MESSAGE, rolesVO.get("message"));
				responseObjectsMap.put("rolesVO", rolesVO.get("rolesVO"));
				responseDTO = createServiceResponse(responseObjectsMap);
			} catch (Exception e) {
				errorMsg = e.getMessage();
				LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
				responseDTO = createServiceResponseError(responseObjectsMap, errorMsg, errorMsg);
			}
			LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
			return ResponseEntity.ok().body(responseDTO);
		}
		
		@GetMapping("getRolesById")
		public ResponseEntity<ResponseDTO> getRolesById(@RequestParam Long id) {
			String methodName = "getRolesById()";
			LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
			String errorMsg = null;
			Map<String, Object> responseObjectsMap = new HashMap<>();
			ResponseDTO responseDTO = null;
			RolesVO rolesVO = null;
			try {
				rolesVO = commonMasterService.getRolesById(id);
			} catch (Exception e) {
				errorMsg = e.getMessage();
				LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
			}
			if (StringUtils.isEmpty(errorMsg)) {
				responseObjectsMap.put(CommonConstant.STRING_MESSAGE, "Roles found by ID");
				responseObjectsMap.put("rolesVO", rolesVO);
				responseDTO = createServiceResponse(responseObjectsMap);
			} else {
				errorMsg = "Roles not found for ID: " + id;
				responseDTO = createServiceResponseError(responseObjectsMap, "Roles not found", errorMsg);
			}
			LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
			return ResponseEntity.ok().body(responseDTO);
		}

		@GetMapping("getRolesByOrgId")
		public ResponseEntity<ResponseDTO> getRolesByOrgId(@RequestParam Long OrgId) {
			String methodName = "getRolesByOrgId()";
			LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
			String errorMsg = null;
			Map<String, Object> responseObjectsMap = new HashMap<>();
			ResponseDTO responseDTO = null;
			List<RolesVO>  rolesVO = null;
			try {
				rolesVO = commonMasterService.getRolesByOrgId(OrgId);
			} catch (Exception e) {
				errorMsg = e.getMessage();
				LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
			}
			if (StringUtils.isEmpty(errorMsg)) {
				responseObjectsMap.put(CommonConstant.STRING_MESSAGE, "Roles found by ORGID");
				responseObjectsMap.put("rolesVO", rolesVO);
				responseDTO = createServiceResponse(responseObjectsMap);
			} else {
				errorMsg = "Roles not found for ID: " + OrgId;
				responseDTO = createServiceResponseError(responseObjectsMap, "Roles not found", errorMsg);
			}
			LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
			return ResponseEntity.ok().body(responseDTO);
		}
		
		
		
		
		
}
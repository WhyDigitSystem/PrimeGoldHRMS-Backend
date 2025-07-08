package com.efit.hrms.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.efit.hrms.entity.TokenVO;

public interface TokenRepo extends JpaRepository<TokenVO, String>{

}

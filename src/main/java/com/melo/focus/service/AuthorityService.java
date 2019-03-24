package com.melo.focus.service;

import java.util.List;
import java.util.UUID;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.melo.focus.domain.basic.Authority;
import com.melo.focus.domain.extend.AuthorityController;
import com.melo.focus.domain.vm.AuthoritySaveReq;
import com.melo.focus.domain.vm.AuthorityUpdateReq;
import com.melo.focus.domain.vm.AuthorityReqs;
import com.melo.focus.mapper.basic.AuthorityMapper;
import com.melo.focus.mapper.extend.AuthorityExtendMapper;
import com.melo.focus.util.Asserts;
import com.melo.focus.util.EntityUtils;

@Service
@Transactional
public class AuthorityService {

	@Autowired
	AuthorityExtendMapper authorityExtendMapper;
	
	@Autowired
	AuthorityMapper authorityMapper;
	
	@Autowired
	ResourceAuthorityService resourceAuthorityService;
	
	
	/**
	 * 根据 权限id 查权限对象集
	 */
	public List<Authority> getAuthorityByIds(List<String> ids){
		if(CollectionUtils.isEmpty(ids)){return null;}
		return authorityExtendMapper.getAuthorityByIds(ids);
	}


	/**
	 * 按controller分组，得到authority列表
	 * @return
	 */
	public List<AuthorityReqs> getAuthority() {
		List<AuthorityController> authorityList = authorityExtendMapper.getAuthority();
		return EntityUtils.entity2VMList(authorityList, AuthorityReqs.class);
	}


	public List<Authority> getLikeAuthority(String method, String word) {
		Asserts.notEmpty(method);
		return authorityExtendMapper.getLikeAuthority(method, word);
	}


	public void saveAuthority(AuthoritySaveReq authoritySaveReq) {
		Asserts.validate(authoritySaveReq,"authoritySaveReq");
		Authority authority = EntityUtils.entity2VM(authoritySaveReq, Authority.class);
		authority.setId(UUID.randomUUID().toString());
		authorityMapper.insert(authority);
	}


	public void updateAuhtority(AuthorityUpdateReq authorityUpdateReq) {
		Asserts.validate(authorityUpdateReq, "authorityUpdateReq");
		//校验id是否存在
		
		Authority authority = EntityUtils.vm2Entity(authorityUpdateReq, Authority.class);
		authorityMapper.updateByPrimaryKey(authority);
	}


	public void deleteAuthority(String id) {
		Asserts.notEmpty(id);
		
		//删除资源权限关系
		resourceAuthorityService.deleteResourceAuthority(null,id);
		
		//删除权限
		authorityMapper.deleteByPrimaryKey(id);
	}



	
}

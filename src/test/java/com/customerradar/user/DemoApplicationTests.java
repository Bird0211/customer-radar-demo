package com.customerradar.user;

import com.customerradar.user.enums.StatusCode;
import com.customerradar.user.vo.Result;
import com.customerradar.user.vo.UserVo;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Assert;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class DemoApplicationTests {

	@Autowired
	private TestRestTemplate testRestTemplate;

	@Test
	void contextLoads() {
	}

	/**
	 * Create User Test
	 * 1、Get New User
	 * 2、Create User with Put Method
	 * 3、Get User By Phone
	 */
	@Test
	void createUser() {
		UserVo userVo = getNewUser();
		Result<UserVo> user = createUser(userVo);
		if(user == null || user.getStatusCode() != StatusCode.SUCCESS.getCode()) {
			Assert.fail();
		}

		UserVo uVo = user.getData();
		getUserByPhone(uVo.getPhone());
	}

	Result<UserVo> createUser(UserVo userVo) {
		ObjectMapper mapper = new ObjectMapper();
		Result<UserVo> result = null;
		try {
			String json = mapper.writeValueAsString(userVo);
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_JSON);
	
			HttpEntity<String> entity = new HttpEntity<String>(json,headers);

			ParameterizedTypeReference<Result<UserVo>> typeRef = new ParameterizedTypeReference<Result<UserVo>>() {};
			result = testRestTemplate.exchange("/api/user/add", HttpMethod.PUT, entity, typeRef).getBody();
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}

		return result;
	}

	/**
	 * Check Phone Param Error
	 * 1、Get New User
	 * 2、Set Wrong Phone Number
	 * 3、Create User
	 */
	@Test
	void createUserWithParamError() {
		UserVo userVo = getNewUser();
		userVo.setPhone("ABCS");

		Result<UserVo> result = createUser(userVo);
		Assert.assertEquals(result.getStatusCode(), StatusCode.PARAM_ERROR.getCode());

	}

	/**
	 * Update User
	 * 1、Create User
	 * 2、Change User Value
	 * 3、Update User
	 */
	@Test
	void updateUser() {
		UserVo user = getNewUser();
		Result<UserVo> result = createUser(user);	

		UserVo userVo = result.getData();
		userVo.setPhone("99999999");

		ObjectMapper mapper = new ObjectMapper();
		try {
			String json = mapper.writeValueAsString(userVo);
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_JSON);
	
			HttpEntity<String> entity = new HttpEntity<String>(json,headers);

			ParameterizedTypeReference<Result<UserVo>> typeRef = new ParameterizedTypeReference<Result<UserVo>>() {};
			Result<UserVo> newResult = testRestTemplate.exchange("/api/user/update", HttpMethod.POST, entity, typeRef).getBody();

			Assert.assertEquals(newResult.getStatusCode(), StatusCode.SUCCESS.getCode());
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}

	}

	/**
	 * Create User
	 * Delete By Id
	 */
	@Test
	void delUserById() {
		UserVo userVo = getNewUser();
		
		Result<UserVo> result = createUser(userVo);
		
		UserVo user = result.getData();
		testRestTemplate.delete("/api/user/del/{id}", user.getId());

		getUserByPhoneWithError(userVo.getPhone(), StatusCode.USER_NOT_EXIST);

	}


	/**
	 * Get User By Phone
	 * 1、CreateUser
	 * 2、Get User By Phone
	 */
	@Test
	void getUserByPhone() {
		UserVo userVo = getNewUser();
		createUser(userVo);
		getUserByPhone(userVo.getPhone());
	}

	/**
	 * Get User By Id
	 * 1、Create User
	 * 2、Get User By Id
	 */
	@Test
	void getUserById() {
		UserVo userVo = getNewUser();
		Result<UserVo> result = createUser(userVo);
		if(result != null && result.getStatusCode() == 0){
			Result<UserVo> uResult = getUserById(result.getData().getId());
			Assert.assertEquals(uResult.getStatusCode(),0);
		}
	}

	@Test
	void getUserByIdNotExist() {
		Long id = 1111111L;
		Result<UserVo> result = getUserById(id);
		Assert.assertEquals(result.getStatusCode(), StatusCode.USER_NOT_EXIST.getCode());
	}

	void getUserByPhone(String phone) {
		getUserByPhoneWithError(phone, StatusCode.SUCCESS);
	}

	void getUserByPhoneWithError(String phone, StatusCode statusCode) {
		if(phone == null) {
			phone = "123456789";
		}
		Result<UserVo> result = getUserDataByPhone(phone);
		Assert.assertEquals(result.getStatusCode(),statusCode.getCode());
	}

	Result<UserVo> getUserById(Long id) {
		ParameterizedTypeReference<Result<UserVo>> typeRef = new ParameterizedTypeReference<Result<UserVo>>() {};
		Result<UserVo> result = testRestTemplate.exchange("/api/user/{id}", HttpMethod.GET, null, typeRef,id).getBody();
		return result;
	}

	Result<UserVo> getUserDataByPhone(String phone) {

		ParameterizedTypeReference<Result<UserVo>> typeRef = new ParameterizedTypeReference<Result<UserVo>>() {};
		Result<UserVo> result = testRestTemplate.exchange("/api/user/phone/{phone}", HttpMethod.GET, null, typeRef, phone).getBody();

		return result;
	}

	UserVo getNewUser() {
		UserVo user = new UserVo();
		user.setName(RandomStringUtils.random(6,true,false));
		user.setPhone(RandomStringUtils.random(11, false, true));
		user.setAddress(RandomStringUtils.random(50, true, true));

		return user;
	}

}

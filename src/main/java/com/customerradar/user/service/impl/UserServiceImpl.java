package com.customerradar.user.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import com.customerradar.user.enums.StatusCode;
import com.customerradar.user.exception.CustomerRadarException;
import com.customerradar.user.po.QUser;
import com.customerradar.user.po.User;
import com.customerradar.user.repository.UserRepository;
import com.customerradar.user.service.IUserService;
import com.customerradar.user.vo.PageResult;
import com.customerradar.user.vo.UserVo;
import com.querydsl.core.QueryResults;
import com.querydsl.jpa.impl.JPAQueryFactory;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * User Service
 * 
 */
@Service
@Transactional
public class UserServiceImpl implements IUserService {

    private static final Logger logger = LoggerFactory.getLogger(IUserService.class);

    @Autowired
    @PersistenceContext
    private EntityManager entityManager;

    private JPAQueryFactory jpaQueryFactory;

    @Autowired
    private UserRepository userRepository;

    @PostConstruct
    public void init() {
        jpaQueryFactory = new JPAQueryFactory(entityManager);
    }

    @Override
    public UserVo getUserByPhone(String phone) throws CustomerRadarException {
        if (StringUtils.isEmpty(phone))
            throw new CustomerRadarException(StatusCode.PARAM_ERROR);

        User user = getUserPoByPhone(phone);
        if (user == null) {
            throw new CustomerRadarException(StatusCode.USER_NOT_EXIST);
        }

        UserVo uVo = new UserVo(user);
        return uVo;
    }

    private User getUserPoByPhone(String phone) {
        QUser qUser = QUser.user;
        User user = jpaQueryFactory.selectFrom(qUser).where(qUser.phone.eq(phone)).fetchOne();

        return user;
    }

    @Override
    public UserVo createUser(UserVo user) throws CustomerRadarException {
        if (user == null || StringUtils.isEmpty(user.getName()) || StringUtils.isEmpty(user.getPhone())) {
            throw new CustomerRadarException(StatusCode.PARAM_ERROR);
        }
        UserVo userVo = null;

        User userPo = getUserPoByPhone(user.getPhone());
        if (userPo == null) {
            User u = new User();
            u.setAddress(user.getAddress());
            u.setName(user.getName());
            u.setPhone(user.getPhone());
            User result = userRepository.save(u);
            if (result == null) {
                logger.info("Create User Save Error");
                throw new CustomerRadarException(StatusCode.DB_ERROR);
            }
            userVo = new UserVo(result);
        } else {
            user.setId(userPo.getId());
            if (updateUser(user)) {
                userVo = user;
            } else {
                throw new CustomerRadarException(StatusCode.DB_ERROR);
            }
        }

        return userVo;
    }

    @Override
    public boolean updateUser(UserVo user) throws CustomerRadarException {
        if (user == null || user.isEmpty())
            throw new CustomerRadarException(StatusCode.PARAM_ERROR);

        QUser qUser = QUser.user;
        long result = jpaQueryFactory.update(qUser).where(qUser.id.eq(user.getId())).set(qUser.name, user.getName())
                .set(qUser.address, user.getAddress()).set(qUser.phone, user.getPhone()).execute();

        logger.info("update row = {}", result);
        return result > 0 ? true : false;
    }

    @Override
    public boolean delUser(Long id) throws CustomerRadarException {
        if (id == null || id <= 0) {
            throw new CustomerRadarException(StatusCode.PARAM_ERROR);
        }

        QUser qUser = QUser.user;
        long result = jpaQueryFactory.delete(qUser).where(qUser.id.eq(id)).execute();

        logger.info("delete row = {}", result);
        return result > 0 ? true : false;
    }

    @Override
    public UserVo getUserById(Long userId) throws CustomerRadarException {
        if (userId == null || userId <= 0)
            throw new CustomerRadarException(StatusCode.PARAM_ERROR);

        QUser qUser = QUser.user;
        User user = jpaQueryFactory.selectFrom(qUser).where(qUser.id.eq(userId)).fetchOne();
        if (user == null) {
            throw new CustomerRadarException(StatusCode.USER_NOT_EXIST);
        }

        UserVo uVo = new UserVo(user);
        return uVo;
    }

    @Override
    public PageResult<UserVo> getUsers(long pageIndex, long pageSize) throws CustomerRadarException {
        long pageNumber = (pageIndex-1) * pageSize;

        QUser qUser = QUser.user;
        QueryResults<User> userQueryResults =
            jpaQueryFactory.selectFrom(qUser).orderBy(qUser.id.desc()).offset(pageNumber).limit(pageSize).fetchResults();

        List<User> users = userQueryResults.getResults();
        long total = userQueryResults.getTotal();
        List<UserVo> userVos = null;
        if(users != null) {
            userVos = users.stream().map(item -> new UserVo(item)).collect(Collectors.toList());
        }

        PageResult<UserVo> result = new PageResult<UserVo>();
        result.setData(userVos);
        result.setPageIndex(pageIndex);
        result.setPageSize(pageSize);
        result.setTotal(total);

        return result;
    }
    
}

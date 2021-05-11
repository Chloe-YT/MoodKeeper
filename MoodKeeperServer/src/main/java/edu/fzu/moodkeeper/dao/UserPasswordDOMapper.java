package edu.fzu.moodkeeper.dao;

import edu.fzu.moodkeeper.dataobject.UserPasswordDO;

public interface UserPasswordDOMapper {
    int deleteByPrimaryKey(Integer id);
    int insert(UserPasswordDO record);
    int insertSelective(UserPasswordDO record);
    UserPasswordDO selectByPrimaryKey(Integer id);
    UserPasswordDO selectByUserId(Integer UserId);
    int updateByPrimaryKeySelective(UserPasswordDO record);
    int updateByPrimaryKey(UserPasswordDO record);
}
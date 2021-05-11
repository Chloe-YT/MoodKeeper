package edu.fzu.moodkeeper.dao;

import edu.fzu.moodkeeper.dataobject.UserDO;

public interface UserDOMapper {
    int deleteByPrimaryKey(Integer id);
    int insert(UserDO record);
    int insertSelective(UserDO record);
    UserDO selectByPrimaryKey(Integer id);
    UserDO selectByTelephone(String telphone);
    int updateByPrimaryKeySelective(UserDO record);
    int updateByPrimaryKey(UserDO record);
}
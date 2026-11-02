package com.cykj.mapper;

import com.cykj.enity.DO.AdminDO;
import com.cykj.enity.vo.AdminVO;
import com.cykj.enity.vo.ConsultantVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface AdminMapper {
    AdminVO login(@Param("adminVO") AdminVO adminVO);

    List<AdminVO> selectAllAdmin(@Param("adminDO") AdminDO adminDO);

    Integer insertAdmin(@Param("adminVO") AdminVO adminVO);

    Integer insertConsultant(@Param("consultantVO") ConsultantVO consultantVO);

    Integer reviewConsultant(@Param("adminId")Integer adminId);

    AdminVO selectAdminById(@Param("adminId") Integer adminId);

    Integer deleteAdmin(@Param("adminId") Integer adminId);

    Integer updateAdmin(@Param("adminVO") AdminVO adminVO);

}

package com.cykj.mapper;

import com.cykj.enity.vo.AiDifyVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.sql.Timestamp;
import java.util.List;

@Mapper
public interface AiMapper {
    AiDifyVO selectAiByStatus();

    List<AiDifyVO> selectAi(AiDifyVO aiDifyVO);

    Integer insertAi(AiDifyVO aiDifyVO);

    Integer updateAi(AiDifyVO aiDifyVO);

    Integer enableAi(@Param("id") Integer id, @Param("status") Integer status);

    Integer deleteAi(@Param("id") Integer id, @Param("ut") Timestamp ut);
}

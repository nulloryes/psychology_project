package com.cykj.service;

import com.cykj.enity.vo.AiDifyVO;

import java.util.List;

public interface AiService {

    List<AiDifyVO> selectAi(AiDifyVO aiDifyVO);

    AiDifyVO selectAiByStatus();

    Integer insertAi(AiDifyVO aiDifyVO);

    Integer updateAi(AiDifyVO aiDifyVO);

    Integer enableAi(Integer id);

    Integer deleteAi(Integer id);
}

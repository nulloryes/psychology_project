package com.cykj.service.impl;

import com.cykj.enity.vo.AiDifyVO;
import com.cykj.mapper.AiMapper;
import com.cykj.service.AiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class AiServiceImpl implements AiService {

    @Autowired
    private AiMapper aiMapper;

    @Override
    public List<AiDifyVO> selectAi(AiDifyVO aiDifyVO) {
        return aiMapper.selectAi(aiDifyVO);
    }

    @Override
    public AiDifyVO selectAiByStatus() {
        return aiMapper.selectAiByStatus();
    }

    @Override
    public Integer insertAi(AiDifyVO aiDifyVO) {
        LocalDateTime now = LocalDateTime.now();
        aiDifyVO.setCreateTime(Timestamp.valueOf(now));
        return aiMapper.insertAi(aiDifyVO);
    }

    @Override
    public Integer updateAi(AiDifyVO aiDifyVO) {
        LocalDateTime now = LocalDateTime.now();
        aiDifyVO.setUpdateTime(Timestamp.valueOf(now));
        return aiMapper.updateAi(aiDifyVO);
    }

    @Override
    public Integer enableAi(Integer id) {
        AiDifyVO lastAi = aiMapper.selectAiByStatus();
        if(lastAi != null) {
            aiMapper.enableAi(lastAi.getAiId(), 0);
        }
        return aiMapper.enableAi(id,2);
    }

    @Override
    public Integer deleteAi(Integer id) {
        Timestamp now = Timestamp.valueOf(LocalDateTime.now());
        return aiMapper.deleteAi(id,now);
    }

}

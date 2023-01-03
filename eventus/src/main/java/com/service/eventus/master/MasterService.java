package com.service.eventus.master;

import com.service.eventus.mappers.MasterDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MasterService {

    @Autowired
    private MasterDao masterDao;

    public List<MasterVo> getListMemberApp(String user_id) {
        return masterDao.getListMemberApp(user_id);
    }

    public List<MasterVo> getListUserApp(String user_id) {
        return masterDao.getListUserApp(user_id);
    }
}

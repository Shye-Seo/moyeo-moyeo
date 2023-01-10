package com.service.eventus.master;

import com.service.eventus.mappers.MasterDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;


import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.List;
import java.util.Map;

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

    public MasterVo getEventInfo(int id) {
        return masterDao.getEventInfo(id);
    }

    public void insert_contract(MasterVo masterVo) {
        masterDao.insert_contract(masterVo);
    }

    public int checkStaffPasser(MasterVo masterVo) {
        return masterDao.checkStaffPasser(masterVo);
    }

    public int checkContractFile(MasterVo masterVo) {
        return masterDao.checkContractFile(masterVo);
    }

}

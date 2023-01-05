package com.service.eventus.master;

import com.lowagie.text.DocumentException;
import com.service.eventus.mappers.MasterDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.xhtmlrenderer.pdf.ITextRenderer;

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

    // pdf 다운로드
    private Logger logger = LoggerFactory.getLogger(MasterService.class);

    @Autowired
    private TemplateEngine templateEngine;

    @Value("${pdf.directory}")
    private String pdfDirectory;

}

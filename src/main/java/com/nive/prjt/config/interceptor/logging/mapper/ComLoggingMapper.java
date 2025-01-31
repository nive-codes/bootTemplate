package com.nive.prjt.config.interceptor.logging.mapper;

import com.nive.prjt.config.interceptor.logging.domain.ComLoggingDomain;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ComLoggingMapper {

    void insertLog(ComLoggingDomain comLoggingDomain);
}

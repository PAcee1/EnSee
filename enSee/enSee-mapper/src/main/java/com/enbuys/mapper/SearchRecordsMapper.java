package com.enbuys.mapper;

import com.enbuys.pojo.SearchRecords;
import com.enbuys.utils.MyMapper;

import java.util.List;

public interface SearchRecordsMapper extends MyMapper<SearchRecords> {

    List<String> queryHotRecords();
}
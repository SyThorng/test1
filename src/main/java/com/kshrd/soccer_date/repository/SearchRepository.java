package com.kshrd.soccer_date.repository;

import com.kshrd.soccer_date.model.response.SearchResultResponse;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface SearchRepository {

    @Select("""
            SELECT * FROM app_user WHERE first_name ILIKE '%#{keyword}%'
            """)
    SearchResultResponse search(String keyword);
}

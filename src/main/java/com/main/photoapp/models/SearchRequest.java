package com.main.photoapp.models;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.relational.core.sql.In;

import java.util.List;

public class SearchRequest {
    @Getter @Setter
    private List<Integer> tagIds;

    @Getter @Setter
    private int page;

    @Getter @Setter
    private int pageSize;
}

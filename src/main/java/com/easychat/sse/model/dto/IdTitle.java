package com.easychat.sse.model.dto;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

@Getter
@Setter
public class IdTitle implements Serializable {
    private String id;
    private String title;
    private List<IdTitle> children;
}

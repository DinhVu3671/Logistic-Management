package com.vrp.demo.models.search;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class BaseSearch {

    private Long id;
    private Long fromCreatedAt;
    private Long toCreatedAt;
    private Long fromUpdatedAt;
    private Long toUpdatedAt;
    private Sort sort;
    private Integer page;
    private Integer pageSize;
    private boolean paged = true;
    private Boolean isAdmin = false;
    private List<Long> ids;

    public Pageable getPageable() {
        if (page == null || pageSize == null)
            return null;
        if (sort == null)
            return PageRequest.of(page - 1, pageSize);
        else
            return PageRequest.of(page - 1, pageSize, sort);

    }

    public boolean isAdmin(){
        return getIsAdmin();
    }
}

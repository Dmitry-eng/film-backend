package org.film.util;

import org.film.dto.request.filter.FilterContext;
import org.film.dto.request.filter.PageRequestDto;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

public class PageableHelper {

    public static Pageable createPageable(FilterContext context) {
        if(context == null) {
            return null;
        }
        return createPageable(context.getPageable());
    }

    public static Pageable createPageable(PageRequestDto dto) {

        if (dto == null) {
            return null;
        }

        return PageRequest.of(dto.getPage(), dto.getSize());
    }
}
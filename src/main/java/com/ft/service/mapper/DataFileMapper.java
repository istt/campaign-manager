package com.ft.service.mapper;

import com.ft.domain.*;
import com.ft.service.dto.DataFileDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity DataFile and its DTO DataFileDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface DataFileMapper extends EntityMapper<DataFileDTO, DataFile> {


}

package com.ft.service.mapper;

import com.ft.domain.*;
import com.ft.service.dto.VasCloudConfigurationDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity VasCloudConfiguration and its DTO VasCloudConfigurationDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface VasCloudConfigurationMapper extends EntityMapper<VasCloudConfigurationDTO, VasCloudConfiguration> {


}

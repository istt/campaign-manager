package com.ft.service.mapper;

import com.ft.domain.*;
import com.ft.service.dto.SmsDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity Sms and its DTO SmsDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface SmsMapper extends EntityMapper<SmsDTO, Sms> {


}

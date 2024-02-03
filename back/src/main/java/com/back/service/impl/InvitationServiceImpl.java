package com.back.service.impl;

import com.back.entity.relationalEntity.InvitationEntity;
import com.back.mapper.InvitationMapper;
import com.back.service.InvitationService;
import com.back.util.invitaionCode.InvitationCodeStatusEnum;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class InvitationServiceImpl extends ServiceImpl<InvitationMapper, InvitationEntity> implements InvitationService {
    private final InvitationMapper invitationMapper;

    public InvitationServiceImpl(InvitationMapper invitationMapper) {
        this.invitationMapper = invitationMapper;
    }

    @Override
    public Boolean updateStatusAndInviteeIdByInvitationCode(String invitationCode, String codeStatus, Long userId) {
        return 1==invitationMapper.updateStatusAndInviteeIdByInvitationCode(invitationCode,codeStatus,userId);
    }

    @Override
    public Boolean verify(String invitationCode) {
        InvitationEntity invitationCodeEntity = invitationMapper.selectByInvitationCode(invitationCode);
        System.out.println(invitationCodeEntity);
        return invitationCodeEntity != null && Objects.equals(invitationCodeEntity.getCodeStatus(), InvitationCodeStatusEnum.ISSUED.status);
    }
}

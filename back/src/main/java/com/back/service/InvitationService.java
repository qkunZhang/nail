package com.back.service;

import com.back.entity.relationalEntity.InvitationEntity;
import com.baomidou.mybatisplus.extension.service.IService;

public interface InvitationService extends IService<InvitationEntity> {
    public Boolean verify(String invitationCode);
    public Boolean updateStatusAndInviteeIdByInvitationCode(String invitationCode, String codeStatus, Long userId);
}

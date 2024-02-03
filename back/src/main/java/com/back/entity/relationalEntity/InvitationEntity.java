package com.back.entity.relationalEntity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("invitation_code_tbl")
public class InvitationEntity {
    @TableId(value = "invitation_id",type = IdType.ASSIGN_ID)
    private Long invitationId;

    @TableField(value = "invitation_code")
    private String invitationCode;

    @TableField(value = "code_status")
    private String codeStatus;

    @TableField(value = "inviter_id")
    private Long inviterId;

    @TableField(value = "invitee_id")
    private Long inviteeId;
}

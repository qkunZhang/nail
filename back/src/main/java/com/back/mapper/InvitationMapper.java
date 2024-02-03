package com.back.mapper;

import com.back.entity.relationalEntity.InvitationEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface InvitationMapper extends BaseMapper<InvitationEntity> {
    @Select("select * from invitation_tbl where invitation_code = #{invitationCode}")
    public InvitationEntity selectByInvitationCode(@Param("invitationCode") String invitationCode);

    @Update("update invitation_tbl set code_status=#{CodeStatus}, invitee_id=#{inviteeId} where invitation_code = #{invitationCode}")
    public Integer updateStatusAndInviteeIdByInvitationCode(@Param("invitationCode") String invitationCode, @Param("CodeStatus") String CodeStatus, @Param("inviteeId") long inviteeId);

}

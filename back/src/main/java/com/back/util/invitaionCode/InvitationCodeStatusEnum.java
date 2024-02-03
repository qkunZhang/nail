package com.back.util.invitaionCode;

public enum InvitationCodeStatusEnum {
    UNISSUED("unissued"),
    ISSUED("issued"),
    USED("used");
    public final String status;

    InvitationCodeStatusEnum(String status){
        this.status = status;
    }
}

package com.bitdubai.fermat_cbp_api.all_definition.enums;

import com.bitdubai.fermat_api.layer.all_definition.enums.interfaces.FermatEnum;
import com.bitdubai.fermat_api.layer.all_definition.exceptions.InvalidParameterException;

/**
 * Created by jorge on 12-10-2015.
 */
public enum ClauseStatus implements FermatEnum {
    DRAFT("DRA"),
    SENT_TO_BROKER("STB"),
    WAITING_FOR_BROKER("WFB"),
    SENT_TO_CUSTOMER("STC"),
    WAITING_FOR_CUSTOMER("WFC"),
    AGREED("AGR"),
    REJECTED("REJ"),
    ACCEPTED("ACCEPT"),
    CHANGED("CHANGE"),
    CONFIRM("CONFIR");

    private String code;

    ClauseStatus(String code) {
        this.code = code;
    }

    @Override
    public String getCode() {
        return this.code;
    }

    public static ClauseStatus getByCode(String code) throws InvalidParameterException {
        switch (code) {
            case "DRA": return DRAFT;
            case "AGR": return AGREED;
            case "ACCEPT": return ACCEPTED;
            case "CHANGE":  return CHANGED;
            case "CONFIR":  return CONFIRM;

            default: throw new InvalidParameterException(InvalidParameterException.DEFAULT_MESSAGE, null, "Code Received: " + code, "This Code Is Not Valid for the ClauseStatus enum");
        }
    }
}

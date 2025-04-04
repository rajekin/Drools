private static OfficerCodes setOfficerCodes(String code1, String code2, String code3, DareRestrictRequest inputRest) {
    OfficerCodes officerCodes = new OfficerCodes();

    if (code1 != null) {
        if (inputRest.getAccountDetails().getOfficerCodes().getCode1().equals(code1)) {
            officerCodes.setCode1(null);
        } else {
            officerCodes.setCode1(code1);
        }
    }

    if (code2 != null) {
        if (inputRest.getAccountDetails().getOfficerCodes().getCode2().equals(code2)) {
            officerCodes.setCode2(null);
        } else {
            officerCodes.setCode2(code2);
        }
    }

    if (code3 != null) {
        if (inputRest.getAccountDetails().getOfficerCodes().getCode3().equals(code3)) {
            officerCodes.setCode3(null);
        } else {
            officerCodes.setCode3(code3);
        }
    }

    return officerCodes;
}

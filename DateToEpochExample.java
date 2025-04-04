private static StatementFields setStatementFields(String field1, String field3, String field4,
                                                  DareRestrictRequest inputRest, StatementFields statementFields) {
    if (field1 != null) {
        if (inputRest.getAccountDetails().getStatementConfiguration().getStatementFields().getField1().equals(field1)) {
            statementFields.setField1(null);
        } else {
            statementFields.setField1(field1);
        }
    }

    if (field3 != null) {
        if (inputRest.getAccountDetails().getStatementConfiguration().getStatementFields().getField3().equals(field3)) {
            statementFields.setField3(null);
        } else {
            statementFields.setField3(field3);
        }
    }

    if (field4 != null) {
        if (inputRest.getAccountDetails().getStatementConfiguration().getStatementFields().getField4().equals(field4)) {
            statementFields.setField4(null);
        } else {
            statementFields.setField4(field4);
        }
    }

    return statementFields;
}

let jsonData = pm.response.json();

const expectedDepositRecomm = [
    {
        seqNR: 1,
        hold: 1,
        businessHoldDays: 1,
        amount: 8000,
        triggerEvent: "ITEMPROCESSING",
        holdReasonCode: "RSN"
    },
    {
        seqNR: 2,
        hold: 0,
        businessHoldDays: 0,
        amount: 0,
        triggerEvent: "ITEMPROCESSING",
        holdReasonCode: "RSN"
    },
    {
        seqNR: 3,
        hold: 0,
        businessHoldDays: 0,
        amount: 0,
        triggerEvent: "ITEMPROCESSING",
        holdReasonCode: "RSN"
    }
];

pm.test("depositRecomm array matches expected items", function () {
    pm.expect(jsonData.depositRecomm.length).to.eql(expectedDepositRecomm.length);

    jsonData.depositRecomm.forEach((item, index) => {
        const expected = expectedDepositRecomm[index];

        pm.expect(item.seqNR).to.eql(expected.seqNR);
        pm.expect(item.hold).to.eql(expected.hold);
        pm.expect(item.businessHoldDays).to.eql(expected.businessHoldDays);
        pm.expect(Number(item.amount)).to.eql(Number(expected.amount));  // numeric comparison
        pm.expect(item.triggerEvent).to.eql(expected.triggerEvent);
        pm.expect(item.holdReasonCode).to.eql(expected.holdReasonCode);
    });
});

// Parse the response body
let jsonData = pm.response.json();

// Define the expected depositRecomm array
const expectedDepositRecomm = [
    {
        "seqNR": 1,
        "hold": 1,
        "businessHoldDays": 1,
        "amount": 8000.0,
        "triggerEvent": "ITEMPROCESSING",
        "holdReasonCode": "RSN"
    },
    {
        "seqNR": 2,
        "hold": 0,
        "businessHoldDays": 0,
        "amount": 0.0,
        "triggerEvent": "ITEMPROCESSING",
        "holdReasonCode": "RSN"
    },
    {
        "seqNR": 3,
        "hold": 0,
        "businessHoldDays": 0,
        "amount": 0.0,
        "triggerEvent": "ITEMPROCESSING",
        "holdReasonCode": "RSN"
    }
];

// Assert that depositRecomm is exactly as expected
pm.test("depositRecomm matches expected data", function () {
    pm.expect(jsonData.depositRecomm).to.eql(expectedDepositRecomm);
});

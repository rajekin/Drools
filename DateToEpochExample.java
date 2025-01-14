pm.test("Validate entire criteriaElementAssociations list (order-independent)", function () {
    let response = pm.response.json();

    const expectedList = [
        {
            id: "9901125",
            criteriaElementType: "transactionCodes",
            assessedValue: "0030",
            status: "NOT_MET"
        },
        {
            id: "9901126",
            criteriaElementType: "transactionCodes",
            assessedValue: "0040",
            status: "NOT_MET"
        }
    ];

    const actualList = response.response.incentiveAssociation.stipulationAssociations[0].criteriaElementAssociations;

    // Sort lists before comparison
    const sortById = (a, b) => a.id.localeCompare(b.id);
    actualList.sort(sortById);
    expectedList.sort(sortById);

    pm.expect(actualList).to.deep.equal(expectedList);
});

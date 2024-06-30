public enum FundingSource {
    EXTERNAL_FUNDING_SOURCE_NON_CC("External Funding Source NON CC"),
    EXTERNAL_FUNDING_SOURCE_CC("External Funding Source CC"),
    NOT_A_FUNDING_SOURCE("Not a Funding Source"),
    INTERNAL_FUNDING_SOURCE_CC("Internal Funding Source CC"),
    INTERNAL_FUNDING_SOURCE_NON_CC("Internal Funding Source NON CC");

    private final String description;

    FundingSource(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public String toString() {
        return description;
    }
}

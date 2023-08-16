package com.example.Dashboard;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Person {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String decisionService; // Added Decision Service attribute
    /**
	 * @return the decisionService
	 */
	public String getDecisionService() {
		return decisionService;
	}

	/**
	 * @param decisionService the decisionService to set
	 */
	public void setDecisionService(String decisionService) {
		this.decisionService = decisionService;
	}

	private String version;
    private String typeChange;
    @Column(name = "`group`") // Use backticks to escape the reserved keyword
    private String group;
    private String description;
    private String stage;
    private String status;
    private String environment;
    private String estimatedDueDate;
    private String decisionAutomated;
    private String decisionModel;
    private String brmsEpic;
    private String extEpic;
    private String dmnMaturityLevel;
    private String projectFundingName;
    private String isFunded;
    

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

   
    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getTypeChange() {
        return typeChange;
    }

    public void setTypeChange(String typeChange) {
        this.typeChange = typeChange;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getStage() {
        return stage;
    }

    public void setStage(String stage) {
        this.stage = stage;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getEnvironment() {
        return environment;
    }

    public void setEnvironment(String environment) {
        this.environment = environment;
    }

    public String getEstimatedDueDate() {
        return estimatedDueDate;
    }

    public void setEstimatedDueDate(String estimatedDueDate) {
        this.estimatedDueDate = estimatedDueDate;
    }

    public String getDecisionAutomated() {
        return decisionAutomated;
    }

    public void setDecisionAutomated(String decisionAutomated) {
        this.decisionAutomated = decisionAutomated;
    }

    public String getDecisionModel() {
        return decisionModel;
    }

    public void setDecisionModel(String decisionModel) {
        this.decisionModel = decisionModel;
    }

    public String getBrmsEpic() {
        return brmsEpic;
    }

    public void setBrmsEpic(String brmsEpic) {
        this.brmsEpic = brmsEpic;
    }

    public String getExtEpic() {
        return extEpic;
    }

    public void setExtEpic(String extEpic) {
        this.extEpic = extEpic;
    }

    public String getDmnMaturityLevel() {
        return dmnMaturityLevel;
    }

    public void setDmnMaturityLevel(String dmnMaturityLevel) {
        this.dmnMaturityLevel = dmnMaturityLevel;
    }

    public String getProjectFundingName() {
        return projectFundingName;
    }

    public void setProjectFundingName(String projectFundingName) {
        this.projectFundingName = projectFundingName;
    }

    public String getIsFunded() {
        return isFunded;
    }

    public void setIsFunded(String isFunded) {
        this.isFunded = isFunded;
    }

   }

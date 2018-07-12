package com.nonobank.group.entity.remote;

/**
 * Created by tangrubei on 2018/3/14.
 */
public class TestCase {
    Integer id;
    String name;
    boolean caseType;
    String description;
    boolean checked;
    boolean divisionType;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    public boolean isCaseType() {
        return caseType;
    }

    public void setCaseType(boolean caseType) {
        this.caseType = caseType;
    }

	public boolean isDivisionType() {
		return divisionType;
	}

	public void setDivisionType(boolean divisionType) {
		this.divisionType = divisionType;
	}
}

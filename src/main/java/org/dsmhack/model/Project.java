/*
 * Here to Help
 * This is a restful web service used to log hours for non-profits to submit for money grants.
 *
 * OpenAPI spec version: 1.0.0
 *
 *
 * NOTE: This class is auto generated by the swagger code generator program.
 * https://github.com/swagger-api/swagger-codegen.git
 * Do not edit the class manually.
 */


package org.dsmhack.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.sql.Time;
import java.sql.Timestamp;
import java.time.OffsetDateTime;
import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name = "project")
public class Project {
    @Id
    @Column(name = "proj_guid")
    private String projGuid;

    @NotNull(message = "Organization guid is required.")
    @Size(min = 1, max = 36, message = "Organization guid must be between 1 and 36 characters.")
    @Column(name = "org_guid")
    private String orgGuid;

    @NotNull(message = "Name is required.")

    @Size(min = 1, max = 50, message = "Name must be between 1 and 50 characters.")
    @Column(name = "name")
    private String name;

    @NotNull(message = "Description is required.")

    @Size(min = 1, max = 50, message = "Description must be between 1 and 50 characters.")
    @Column(name = "description")
    private String description;

    @Column(name = "start_dt")
    private Timestamp startDate;

    @Column(name = "end_dt")
    private Timestamp endDate;

    public Project() {
    }

    public String getProjGuid() {
        return projGuid;
    }

    public Project setProjGuid(String projGuid) {
        this.projGuid = projGuid;
        return this;
    }

    public String getOrgGuid() {
        return orgGuid;
    }

    public Project setOrgGuid(String orgGuid) {
        this.orgGuid = orgGuid;
        return this;
    }

    public String getName() {
        return name;
    }

    public Project setName(String name) {
        this.name = name;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public Project setDescription(String description) {
        this.description = description;
        return this;
    }

    public Timestamp getStartDate() {
        return startDate;
    }

    public Project setStartDate(Timestamp startDate) {
        this.startDate = startDate;
        return this;
    }

    public Timestamp getEndDate() {
        return endDate;
    }

    public Project setEndDate(Timestamp endDate) {
        this.endDate = endDate;
        return this;
    }

    public String toJson() {
        return new Gson().toJson(this);
    }
}


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

import com.google.gson.Gson;
import org.hibernate.validator.constraints.Email;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table
public class User {
  @Id
  @Column
  private String userGuid;

  @NotNull
  @Size(min = 1, max = 50)
  @Column
  private String firstName;

  @NotNull
  @Size(min = 1, max = 50)
  @Column
  private String lastName;

  @NotNull
  @Size(min = 1, max = 50)
  @Email
  @Column
  private String email;

  @NotNull
  @Size(min = 1, max = 50)
  @Pattern(regexp = "ROLE_DSMHACK_ADMINISTRATOR|ROLE_ORGANIZATION_ADMINISTRATOR|ROLE_VOLUNTEER")
  @Column
  private String role;

  @Transient
  private List<Project> projectList = new ArrayList<>();

  public User() {
  }

  public String getUserGuid() {
    return userGuid;
  }

  public User setUserGuid(String userGuid) {
    this.userGuid = userGuid;
    return this;
  }

  public String getFirstName() {
    return firstName;
  }

  public User setFirstName(String firstName) {
    this.firstName = firstName;
    return this;
  }

  public String getLastName() {
    return lastName;
  }

  public User setLastName(String lastName) {
    this.lastName = lastName;
    return this;
  }

  public String getEmail() {
    return email;
  }

  public User setEmail(String email) {
    this.email = email;
    return this;
  }

  public String getRole() {
    return role;
  }

  public User setRole(String role) {
    this.role = role;
    return this;
  }

  public List<Project> getProjectList() {
    return projectList;
  }

  public User setProjectList(List<Project> projectList) {
    this.projectList = projectList;
    return this;
  }

  public String toJson() {
    return new Gson().toJson(this);
  }
}


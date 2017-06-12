package models;

import java.util.Date;

public class Method {

  private String thirdFunction;
  private String secondFunction;
  private String function;
  private String method;
  private String userId;
  private String id;
  private String date;

  public Method(String function, String method, String userId, String date) {
    this.function = function;
    this.method = method;
    this.userId = userId;
    this.date = date;
  }

  public Method(String function, String secondFunction, String method, String userId, String date) {
    this.function = function;
    this.secondFunction = secondFunction;
    this.method = method;
    this.userId = userId;
    this.date = date;
  }

  public Method(String function, String secondFunction, String thirdFunction, String method, String userId, String date) {
    this.function = function;
    this.secondFunction = secondFunction;
    this.thirdFunction = thirdFunction;
    this.method = method;
    this.userId = userId;
    this.date = date;
  }

  /**
   * @return The userId
   */
  public String getUserId() {
    return userId;
  }

  /**
   * @param userId The userId
   */
  public void setUserId(String userId) {
    this.userId = userId;
  }

  /**
   * @return The function
   */
  public String getFunction() {
    return function;
  }

  /**
   * @param function The function
   */
  public void setFunction(String function) {
    this.function = function;
  }

  /**
   * @return The id
   */
  public String getId() {
    return id;
  }

  /**
   * @param id The id
   */
  public void setId(String id) {
    this.id = id;
  }

  /**
   * @return The method
   */
  public String getMethod() {
    return method;
  }

  /**
   * @param method The method
   */
  public void setMethod(String method) {
    this.method = method;
  }

  public String getDate() {
    return date;
  }

  public void setDate(String date) {
    this.date = date;
  }

  public String getThirdFunction() {
    return thirdFunction;
  }

  public void setThirdFunction(String thirdFunction) {
    this.thirdFunction = thirdFunction;
  }

  public String getSecondFunction() {
    return secondFunction;
  }

  public void setSecondFunction(String secondFunction) {
    this.secondFunction = secondFunction;
  }
}

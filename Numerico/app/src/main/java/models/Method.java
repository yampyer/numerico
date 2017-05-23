package models;

public class Method {
    private String function;
    private String method;
    private String userId;
    private String id;

    public Method(String function, String method, String userId) {
        this.function = function;
        this.method = method;
        this.userId = userId;
    }

    /**
     *
     * @return
     * The userId
     */
    public String getUserId() {
        return userId;
    }

    /**
     *
     * @param userId
     * The userId
     */
    public void setUserId(String userId) {
        this.userId = userId;
    }

    /**
     *
     * @return
     * The function
     */
    public String getFunction() {
        return function;
    }

    /**
     *
     * @param function
     * The function
     */
    public void setFunction(String function) {
        this.function = function;
    }

    /**
     *
     * @return
     * The id
     */
    public String getId() {
        return id;
    }

    /**
     *
     * @param id
     * The id
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     *
     * @return
     * The method
     */
    public String getMethod() {
        return method;
    }

    /**
     *
     * @param method
     * The method
     */
    public void setMethod(String method) {
        this.method = method;
    }
}

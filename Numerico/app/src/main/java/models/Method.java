package models;

public class Method {
    private String function;
    private Double [] params;
    private String userId;
    private String id;

    public Method(String function, Double [] params, String userId) {
        this.function = function;
        this.params = params;
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
     * The params
     */
    public Double [] getParams() {
        return params;
    }

    /**
     *
     * @param params
     * The params
     */
    public void setParams(Double [] params) {
        this.params = params;
    }
}

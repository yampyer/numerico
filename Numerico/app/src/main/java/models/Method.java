package models;

public class Method {
    private String function;
    private Double [] params;
    private User user;
    private String id;

    public Method(String function, Double [] params, User user) {
        this.function = function;
        this.params = params;
        this.user = user;
    }

    /**
     *
     * @return
     * The user
     */
    public User getUser() {
        return user;
    }

    /**
     *
     * @param user
     * The user
     */
    public void setUser(User user) {
        this.user = user;
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

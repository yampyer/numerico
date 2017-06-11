package API;

import java.util.List;

import models.Method;
import models.User;
import models.Token;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface NumericoAPI {
    @POST("Usuarios/login")
    Call<Token> login(@Body User user);

    @GET("Usuarios/{id}")
    Call<User> getUser(
            @Path("id") String id,
            @Query("filter") String includeLiteral
    );

    @POST("Usuarios")
    Call<User> newUser(@Body User user);

    @GET("Usuarios")
    Call<List<User>> getAllUsers(
            @Query("filter") String includeLiteral
    );

    @PUT("Usuarios/{id}")
    Call<User> updateUser(
            @Path("id") String id,
            @Body User user
    );

    @POST("Metodos")
    Call<Method> newMethod(@Body Method method);

    @GET("Metodos")
    Call<List<Method>> getMethods(
            @Query("filter") String includeLiteral
    );
}
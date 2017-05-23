package API;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ServerAPI {
    private static NumericoAPI instance = null;
    private static final String BASE_URL = "https://numerico.herokuapp.com/api/";

    public static NumericoAPI getInstance() {
        if(instance == null) {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            instance = retrofit.create(NumericoAPI.class);
        }
        return instance;
    }
}
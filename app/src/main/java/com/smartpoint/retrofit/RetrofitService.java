package com.smartpoint.retrofit;



import com.smartpoint.entity.Movie;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface RetrofitService {
    @GET("top250")
    Observable<Movie> getMovieInfo(@Query("start") int start, @Query("count") int count);
}

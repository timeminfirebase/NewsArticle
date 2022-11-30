package com.news.newsarticle.utils

import com.news.newsarticle.entity.Story
import kotlinx.coroutines.Deferred
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface WebService {

    companion object {
        const val BASE_URL = "https://hacker-news.firebaseio.com/v0/"
    }

    @GET("topstories.json?print=pretty")
    fun getStories(): Deferred<Response<List<Long>>>

    @GET("newstories.json?print=pretty")
    fun getNewStories(): Deferred<Response<List<Long>>>

    @GET("beststories.json?print=pretty")
    fun getBestStories(): Deferred<Response<List<Long>>>

    @GET("askstories.json?print=pretty")
    fun getAskStories(): Deferred<Response<List<Long>>>

    @GET("showstories.json?print=pretty")
    fun getShowStories(): Deferred<Response<List<Long>>>

    @GET("jobstories.json?print=pretty")
    fun getJobStories(): Deferred<Response<List<Long>>>

    @GET("item/{story}.json?print=pretty")
    fun getStory(@Path("story") id: Long): Deferred<Response<Story>>

}
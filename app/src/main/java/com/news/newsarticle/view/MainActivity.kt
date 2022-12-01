package com.news.newsarticle.view

import android.content.Context
import android.content.SharedPreferences
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.os.Bundle
import android.preference.PreferenceManager
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.LinearLayout
import android.widget.Toast


import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.PopupMenu
import androidx.appcompat.widget.SearchView
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.news.newsarticle.R
import com.news.newsarticle.entity.Story
import com.news.newsarticle.utils.WebService

import kotlinx.coroutines.*

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.lang.reflect.Type
import java.util.*
import kotlin.collections.ArrayList

class MainActivity : AppCompatActivity() {

    private lateinit var webService: WebService
    private lateinit var recyclerView: RecyclerView
    lateinit var swipeRefreshLayout: SwipeRefreshLayout
    lateinit var storiesAdapter: StoriesAdapter
    lateinit var topStories: MutableList<Story>

    private val job = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + job)
    lateinit var stories: List<Long>

    private var ll_progress: LinearLayout? = null
    public var toolbar: Toolbar? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        try {
            ll_progress = findViewById<LinearLayout>(R.id.ll_progress) as LinearLayout


            recyclerView = findViewById(R.id.idRVCourses)
            swipeRefreshLayout = findViewById(R.id.container)
            recyclerView.hasFixedSize()
            recyclerView.layoutManager = LinearLayoutManager(this)

            topStories = ArrayList()

            swipeRefreshLayout.setOnRefreshListener {

                swipeRefreshLayout.isRefreshing = false
                Collections.shuffle(topStories, Random(System.currentTimeMillis()))
                storiesAdapter.notifyDataSetChanged()
            }


            loadData("top");

        } catch (ex: Exception) {
            ex.printStackTrace()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.search_menu, menu)

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.getItemId()
        when (id) {
            R.id.actionSearch -> {

                val searchView: SearchView = item.getActionView() as SearchView
                searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener,
                    android.widget.SearchView.OnQueryTextListener {
                    override fun onQueryTextSubmit(p0: String?): Boolean {
                        return false
                    }

                    override fun onQueryTextChange(msg: String): Boolean {
                        filter(msg)
                        return false
                    }
                })

            }
            R.id.actionfiltter -> {
                val vItem = this@MainActivity.findViewById<View>(R.id.actionfiltter)
                var popup: PopupMenu? = PopupMenu(this!!, vItem)
                popup?.menuInflater?.inflate(R.menu.menu_main, popup.menu)
                popup?.show()
                popup?.setOnMenuItemClickListener { item: MenuItem? ->
                    when (item!!.itemId) {
                        R.id.action_TopStory ->

                            loadData("top")
                        R.id.action_NewStory ->

                            loadData("new")
                      R.id . action_BestStory ->
                          loadData("best")
                        R.id.action_askStory ->
                            loadData("ask")
                        R.id.action_ShowStory ->
                            loadData("show")
                        R.id.action_JobStory ->
                            loadData("job")

                    }
                    true
                }
            }
            else -> super.onOptionsItemSelected(item)
        }

        return true
    }

    private fun loadData(s: String) {
        if (checkForInternet(this)) {

        ll_progress!!.visibility = View.VISIBLE
        val retrofit = Retrofit.Builder()
            .baseUrl(WebService.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(CoroutineCallAdapterFactory())
            .build()

        webService = retrofit.create(WebService::class.java)

        uiScope.launch {
            try {
                if (s == "top") {
                    val storiesResponse = webService.getStories().await()
                    stories = storiesResponse.body() as List<Long>
                    Log.d("top story", stories.toString())
                    toolbar?.title = "Top Stories"


                }else if (s.equals("new")) {
                    val storiesResponse = webService.getNewStories().await()
                    stories = storiesResponse.body() as List<Long>
                    Log.d("new story", stories.toString())
                    toolbar?.title = "New Stories"

                }else if (s.equals("best")) {
                    val storiesResponse = webService.getBestStories().await()
                    stories = storiesResponse.body() as List<Long>
                    Log.d("best story", stories.toString())
                    toolbar?.title = "Best Stories"
                }else if (s.equals("ask")) {
                    val storiesResponse = webService.getAskStories().await()
                    stories = storiesResponse.body() as List<Long>
                    Log.d("ask story", stories.toString())
                    toolbar?.title = "Ask Stories"
                }else if (s.equals("show")) {
                    val storiesResponse = webService.getShowStories().await()
                    stories = storiesResponse.body() as List<Long>
                    Log.d("show story", stories.toString())
                    toolbar?.title = "View Stories"
                }else if (s.equals("job")) {
                    val storiesResponse = webService.getJobStories().await()
                    stories = storiesResponse.body() as List<Long>
                    Log.d("job story", stories.toString())
                    toolbar?.title = "Job Stories"
                }


                //val topStories: MutableList<Story> = ArrayList()

                withContext(Dispatchers.IO) {
                    stories.subList(0, 19).forEach {
                        topStories.add(getStory(it))
                    }
                }

                Log.d("story", topStories.toString())
                storiesAdapter = StoriesAdapter(this@MainActivity, topStories)
                recyclerView.adapter = storiesAdapter
                storiesAdapter.notifyDataSetChanged()
                saveArrayList(topStories,"story")
                ll_progress!!.visibility = View.GONE
                recyclerView.visibility = View.VISIBLE


            } catch (ex: Exception) {
                ex.printStackTrace()
            }
        }

        } else {

            topStories=getArrayList("story")
            Log.d("story",topStories.toString())

            storiesAdapter = StoriesAdapter(this@MainActivity, topStories)
            recyclerView.adapter = storiesAdapter
            storiesAdapter.notifyDataSetChanged()
            saveArrayList(topStories,"story")
            ll_progress!!.visibility = View.GONE
            recyclerView.visibility = View.VISIBLE

            Toast.makeText(this, "No Internet Connection", Toast.LENGTH_SHORT).show()
        }
    }

        private suspend fun getStory(id: Long): Story {
        val storyResponse = webService.getStory(id).await()
        return storyResponse.body() as Story
    }

    private fun filter(text: String) {
        try {
            val filteredlist: ArrayList<Story> = ArrayList()

            for (item in topStories) {
                if (item.title.toLowerCase().contains(text.toLowerCase())) {
                    filteredlist.add(item)
                }
            }
            if (filteredlist.isEmpty()) {
                Toast.makeText(this, "No Data Found..", Toast.LENGTH_SHORT).show()
            } else {
                storiesAdapter.filterList(filteredlist)
            }
        } catch (ex: Exception) {
            ex.printStackTrace()
        }

    }

    private fun checkForInternet(context: Context): Boolean {

        // register activity with the connectivity manager service
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        // if the android version is equal to M
        // or greater we need to use the
        // NetworkCapabilities to check what type of
        // network has the internet connection
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            // Returns a Network object corresponding to
            // the currently active default data network.
            val network = connectivityManager.activeNetwork ?: return false

            // Representation of the capabilities of an active network.
            val activeNetwork = connectivityManager.getNetworkCapabilities(network) ?: return false

            return when {
                // Indicates this network uses a Wi-Fi transport,
                // or WiFi has network connectivity
                activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true

                // Indicates this network uses a Cellular transport. or
                // Cellular has network connectivity
                activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true

                // else return false
                else -> false
            }
        } else {
            // if the android version is below M
            @Suppress("DEPRECATION") val networkInfo =
                connectivityManager.activeNetworkInfo ?: return false
            @Suppress("DEPRECATION")
            return networkInfo.isConnected
        }
    }
    private fun saveArrayList(list: MutableList<Story>, key: String?) {
        val prefs: SharedPreferences = PreferenceManager.getDefaultSharedPreferences(this@MainActivity)
        val editor: SharedPreferences.Editor = prefs.edit()
        val gson = Gson()
        val json: String = gson.toJson(list)
        editor.putString(key, json)
        editor.apply()
        Log.d("story", "savevd")

    }

    private fun getArrayList(key: String?): MutableList<Story> {
        val prefs: SharedPreferences = PreferenceManager.getDefaultSharedPreferences(this@MainActivity)
        val gson = Gson()
        val json: String = prefs.getString(key, null)!!
        val type: Type = object : TypeToken<MutableList<Story?>?>() {}.getType()
        Log.d("story", "get")
        return gson.fromJson(json, type)
    }

}
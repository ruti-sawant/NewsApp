package com.example.news

import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.browser.customtabs.CustomTabsIntent
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), NewsItemClicked {
    private lateinit var mAdapter: NewsListAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        recyclerView.layoutManager=LinearLayoutManager(this)
        fetchData()
        mAdapter=NewsListAdapter(this)
        recyclerView.adapter=mAdapter
    }
    private fun fetchData(){
        val url="https://saurav.tech/NewsAPI/top-headlines/category/health/in.json"
//        val url="http://newsapi.org/v2/top-headlines?country=in&category=business&apiKey=e2f6b3b5dae2400291629f50c9dc401c"
        val jsonObjectRequest=JsonObjectRequest(Request.Method.GET,url,null,
            {
            Log.d("Error","Here Line 26")
            val newsJsonArray=it.getJSONArray("articles")
            val newsArray=ArrayList<News>()
            for(i in 0 until newsJsonArray.length()){
                val newsJsonObject=newsJsonArray.getJSONObject(i)
                val news=News(newsJsonObject.getString("title"),
                    newsJsonObject.getString("author"),
                    newsJsonObject.getString("url"),
                    newsJsonObject.getString("urlToImage")
                )

                newsArray.add(news)
            }
            mAdapter.updateNews(newsArray)
        }, {
            Log.d("Error","Line 40")
            Toast.makeText(this,it.stackTraceToString(),Toast.LENGTH_LONG).show()
        }
        )
        MySingleton.getInstance(this).addToRequestQueue(jsonObjectRequest)
    }

    override fun onItemClicked(item: News) {
        val builder=CustomTabsIntent.Builder()
        val customTabsIntent=builder.build()
        customTabsIntent.launchUrl(this, Uri.parse(item.url))
    }
}
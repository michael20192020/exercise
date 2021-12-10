package com.hansoft.exercise
/**
 * created by qi zhu 10/12/2011
 * Create MainActivity class to show the data
 */
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.util.Log
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout.OnRefreshListener
import butterknife.BindView
import org.json.JSONException
import org.json.JSONObject
import java.util.ArrayList

class MainActivity : AppCompatActivity() {


    var firstRecyclerView: RecyclerView? = null

    private val canadas = ArrayList<Canada>()
    var data: String? = null
    var title: String? = null
    var finishDownload = false
    val GET_DATA_SUCCESS = 1
    val NETWORK_ERROR = 2
    val SERVER_ERROR = 3


    private var swipeRefreshLayout: SwipeRefreshLayout? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        bindview()

        // load Json file
        loadJsonFile("https://dl.dropboxusercontent.com/s/2iodh4vg0eortkl/facts.json")
    }

    fun bindview()
    {
        // user swiperefreshlayout to load the images lazily
        swipeRefreshLayout = findViewById(R.id.swipeRefresh)
        swipeRefreshLayout!!.setOnRefreshListener(OnRefreshListener {
            refresh()
            swipeRefreshLayout!!.setRefreshing(false)
        })
        firstRecyclerView = findViewById(R.id.recycler_view)

    }

    fun initList() {


        firstRecyclerView!!.setHasFixedSize(true)
        firstRecyclerView!!.setLayoutManager(LinearLayoutManager(this))
        firstRecyclerView!!.setAdapter(
                ListAdapter(this,
                        canadas,
                        object : ListAdapter.OnItemClickListener {
                            override fun onItemClick(position: Int) {
                                // do something when customer click the item
                            }
                        })
        )
    }


    private val handler: Handler = Handler(Looper.getMainLooper(), Handler.Callback {
            when (it.what) {
                GET_DATA_SUCCESS -> {
                    //set the title of the ActionBar
                    setTitle(title)
                    initList()

                }
                NETWORK_ERROR -> Toast.makeText(
                        this,
                        "Newwork is not available",
                        Toast.LENGTH_SHORT
                ).show()
                SERVER_ERROR -> Toast.makeText(
                        this,
                        "Server does not work",
                        Toast.LENGTH_SHORT
                ).show()
            }
        true

    })

    private fun downloadJsonFile(url: String): String? {
        // use okhttp3 to download json file
        try {
            data = NetworkService.INSTANCE.getString(url)
            finishDownload = true
        } catch (e: Exception) {
            Log.d("aaa", "downloadJsonFile: Exception = " + e.localizedMessage)
        }
        return data
    }

    private fun loadJsonFile(url: String) {

        // create new thread to loading the data , not block UI
        object : Thread() {
            override fun run() {
                val result = downloadJsonFile(url)
                if (result == null) {
                    // can not get json file
                    handler.sendEmptyMessage(SERVER_ERROR)
                } else {
                    try {
                        canadas.clear()
                        val root = JSONObject(result)


                        title = root.getString("title")
                        val array = root.getJSONArray("rows")
                        for (i in 0 until array.length()) {
                            val lan = array.getJSONObject(i)
                            val canada = Canada()

                            canada.title = lan.getString("title")
                            canada.description = lan.getString("description")
                            canada.imageHref = lan.getString("imageHref")

                            canadas.add(canada)
                        }

                        handler.sendEmptyMessage(GET_DATA_SUCCESS)
                    } catch (e: JSONException) {
                        e.printStackTrace()
                    }
                }
            }
        }.start()
    }

    // pull down the screen to refresh data
    private fun refresh() {
        loadJsonFile("https://dl.dropboxusercontent.com/s/2iodh4vg0eortkl/facts.json")
    }
}
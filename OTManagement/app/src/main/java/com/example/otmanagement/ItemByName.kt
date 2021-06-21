package com.example.otmanagement

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.otmanagement.adapter.ProductListAdapter
import com.example.otmanagement.data.Product
import com.example.otmanagement.databinding.ActivityItemByNameBinding
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.json.JSONObject
import java.io.*
import java.net.HttpURLConnection
import java.net.MalformedURLException
import java.net.URL

class ItemByName : AppCompatActivity() {
    private val TAG = "ItemByName"

    private lateinit var binding: ActivityItemByNameBinding
    lateinit var productList: ArrayList<Product>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_item_by_name)

        binding.ivBack.setOnClickListener {
            onBackPressed()
        }

        val name = intent.getStringExtra(EXTRA_QUERY)
        binding.svName.setQuery(name, true)
        getProducts(name!!)

        val lm = LinearLayoutManager(this)
        binding.rvByName.layoutManager = lm
        binding.rvByName.setHasFixedSize(true)

        binding.svName.setOnQueryTextListener(object : SearchView.OnQueryTextListener {

            override fun onQueryTextChange(newText: String): Boolean {
                return false
            }

            override fun onQueryTextSubmit(query: String): Boolean {
                getProducts(query)
                return false
            }

        })
    }

    fun refresh() {
        runOnUiThread {
            val mAdapter = ProductListAdapter(applicationContext, productList)
            binding.rvByName.adapter = mAdapter
        }
    }

    fun getProducts(name: String) {
        val url = "/searchByName"
        val data = JSONObject()
        data.accumulate("name", name)

        var status = false

        productList = arrayListOf<Product>()

        GlobalScope.launch {
            var con: HttpURLConnection? = null
            var reader: BufferedReader? = null
            try {
                val url = URL(serverAddr + url)
                con = url.openConnection() as HttpURLConnection?
                con!!.requestMethod = "POST"
                con.setRequestProperty("Cache-Control", "no-cache")
                con.setRequestProperty("content-Type", "application/json")
                con.setRequestProperty("Accept", "text/html")
                con.doOutput = true
                con.doInput = true
                con!!.connect()

                val outStream: OutputStream = con.outputStream
                val writer: BufferedWriter = BufferedWriter(OutputStreamWriter(outStream))
                writer.write(data.toString())
                writer.flush()
                writer.close()

                val stream: InputStream = con.inputStream
                reader = BufferedReader(InputStreamReader(stream))
                val buffer = StringBuffer()

                var line: String? = reader.readLine()
                while (line != null) {
                    buffer.append(line)
                    line = reader.readLine()
                }

                Log.d(TAG, "Response: $buffer")
                val arr = buffer.split("~")
                for (i: String in arr) {
                    Log.d(TAG, "record: $i")
                    val field = i.split('^')
                    if(field.size == 4) {
                        Log.d(TAG, "field: ${field[0]} // ${field[1]} // ${field[2]} // ${field[3]}")
                        productList.add(Product(field[0], field[1], field[2].toFloat(), field[3].toFloat()))
                    }
                }
                status = true
                refresh()

                return@launch
            } catch (e: MalformedURLException) {
                e.printStackTrace()
            } catch (e: IOException) {
                e.printStackTrace()
            } finally {
                if (con != null) {
                    con.disconnect()
                }
                try {
                    if (reader != null) {
                        reader.close()
                    }

                    if (status) {

                    } else {
                        Log.e(TAG, "품목 조회 에러")
                    }
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
        }
    }
}
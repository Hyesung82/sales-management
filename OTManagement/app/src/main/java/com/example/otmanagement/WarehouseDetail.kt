package com.example.otmanagement

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.otmanagement.adapter.InventoryListAdapter
import com.example.otmanagement.data.Inventory
import com.example.otmanagement.databinding.ActivityWarehouseDetailBinding
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.json.JSONObject
import java.io.*
import java.net.HttpURLConnection
import java.net.MalformedURLException
import java.net.URL

class WarehouseDetail : AppCompatActivity() {
    lateinit var binding: ActivityWarehouseDetailBinding
    val TAG = "WarehouseDetail"

    var inventories = arrayListOf<Inventory>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_warehouse_detail)

        binding.ivBack.setOnClickListener {
            onBackPressed()
        }

        Log.d(TAG, "extra_warehouse: ${intent.getStringExtra(EXTRA_WAREHOUSE)}")
        val warehouse = intent.getStringExtra(EXTRA_WAREHOUSE)
        binding.tvWarehouseTitle.text = warehouse
        getWarehouse(warehouse!!)

        val lm = LinearLayoutManager(this)
        binding.rvWarehouse.layoutManager = lm
        binding.rvWarehouse.setHasFixedSize(true)
    }

    fun refresh() {
        runOnUiThread {
            val mAdapter = InventoryListAdapter(this, inventories)
            binding.rvWarehouse.adapter = mAdapter
        }
    }

    fun getWarehouse(name: String) {
        val url = "/warehouse"
        val data = JSONObject()
        data.accumulate("name", name)

        var status = false

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
                    if (field.size == 2) {
                        Log.d(TAG, "field: ${field[0]} // ${field[1]}")
                        inventories.add(Inventory(field[0], field[1].toInt()))
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
                        Log.e(TAG, "재고 조회 에러")
                    }
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
        }
    }
}
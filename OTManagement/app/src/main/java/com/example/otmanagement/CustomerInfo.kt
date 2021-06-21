package com.example.otmanagement

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.otmanagement.adapter.CustomerListAdapter
import com.example.otmanagement.databinding.ActivityCustomerInfoBinding
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.json.JSONObject
import java.io.*
import java.net.HttpURLConnection
import java.net.MalformedURLException
import java.net.URL

const val EXTRA_CUSTOMER = "customer"

class CustomerInfo : AppCompatActivity() {
    private val TAG = "CustomerInfo"

    private lateinit var binding: ActivityCustomerInfoBinding
    var customerList = arrayListOf<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_customer_info)

        binding.ivBack.setOnClickListener {
            onBackPressed()
        }

        binding.svCustomer.setOnQueryTextListener(object : SearchView.OnQueryTextListener {

            override fun onQueryTextChange(newText: String): Boolean {
                return false
            }

            override fun onQueryTextSubmit(query: String): Boolean {
                getCustomer(query)
                return false
            }

        })

        getCustomer("")

        val lm = LinearLayoutManager(this)
        binding.rvCustomer.layoutManager = lm
        binding.rvCustomer.setHasFixedSize(true)
    }

    fun refresh() {
        runOnUiThread {
            val mAdapter = CustomerListAdapter(this, customerList) {
                val intent = Intent(this, CustomerDetail::class.java).apply {
                    putExtra(EXTRA_CUSTOMER, it)
                }
                startActivity(Intent(intent))
            }
            binding.rvCustomer.adapter = mAdapter
        }
    }

    fun getCustomer(name: String) {
        val url = "/allCustomers"
        val data = JSONObject()
        data.accumulate("name", name)

        var status = false

        customerList = arrayListOf<String>()

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
                    customerList.add(i)
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
                        Log.e(TAG, "고객 조회 에러")
                    }
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
        }
    }
}
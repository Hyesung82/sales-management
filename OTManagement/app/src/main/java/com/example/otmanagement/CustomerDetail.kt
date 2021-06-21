package com.example.otmanagement

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.otmanagement.adapter.OrderListAdapter
import com.example.otmanagement.data.Order
import com.example.otmanagement.databinding.ActivityCustomerDetailBinding
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.json.JSONObject
import java.io.*
import java.net.HttpURLConnection
import java.net.MalformedURLException
import java.net.URL

class CustomerDetail : AppCompatActivity() {
    private lateinit var binding: ActivityCustomerDetailBinding
    private val TAG = "CustomerDetail"

    var customerInfo = arrayListOf<String>() // address, website, credit limit
    var orders = arrayListOf<Order>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_customer_detail)

        binding.ivBack.setOnClickListener {
            onBackPressed()
        }

        binding.tvWebsite.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(binding.tvWebsite.text.toString()))
            startActivity(intent)
        }

        val curCustomer = intent.getStringExtra(EXTRA_CUSTOMER)
        binding.tvCustomerTitle.text = curCustomer
        getCustomer(curCustomer!!)

        val lm = LinearLayoutManager(this)
        binding.rvOrder.layoutManager = lm
        binding.rvOrder.setHasFixedSize(true)
    }

    fun refresh() {
        runOnUiThread {
            binding.tvAddress.text = customerInfo[1]
            binding.tvCreditLimit.text = customerInfo[2]
            binding.tvWebsite.text = customerInfo[3]

            val mAdapter = OrderListAdapter(this, orders) {
                // TODO: 주문 상세 정보 팝업
            }
            binding.rvOrder.adapter = mAdapter
        }
    }

    fun getCustomer(name: String) {
        val url = "/customer"
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

//                Log.d(TAG, "record: $i")
                val field1 = arr[0].split('^')
                    Log.d(TAG, "field: ${field1[0]} // ${field1[1]} // ${field1[2]} // ${field1[3]}")
                    customerInfo.add(field1[0])
                    customerInfo.add(field1[1])
                    customerInfo.add(field1[2])
                    customerInfo.add(field1[3])

                Log.d(TAG, "field2: ${arr[1]}")
                val field2 = arr[1].split('!')
                for (i: String in field2) {
                    val o = i.split('^')
                    if (o.size == 4) {
                        orders.add(Order(o[0].toInt(), o[1], o[2], o[3]))
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
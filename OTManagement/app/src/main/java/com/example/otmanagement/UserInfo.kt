package com.example.otmanagement

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.example.otmanagement.databinding.ActivityUserInfoBinding
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.json.JSONObject
import java.io.*
import java.net.HttpURLConnection
import java.net.MalformedURLException
import java.net.URL

class UserInfo : AppCompatActivity() {
    private val TAG = "UserInfo"
    private lateinit var binding: ActivityUserInfoBinding

    var info = arrayListOf<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_user_info)

        val sharedPref =  getSharedPreferences(
            getString(R.string.preference_file_key), Context.MODE_PRIVATE)
        val id = sharedPref.getString(getString(R.string.user_id), "")

        binding.ivBack.setOnClickListener {
            onBackPressed()
        }

        binding.bUpdate.setOnClickListener {
            binding.etEmail.visibility = View.VISIBLE
            binding.etPhone.visibility = View.VISIBLE
            binding.tvEmployeeMail.visibility = View.INVISIBLE
            binding.tvEmployeePhone.visibility = View.INVISIBLE

            binding.bUpdate.visibility = View.INVISIBLE
            binding.bSubmit.visibility = View.VISIBLE
        }

        binding.bSubmit.setOnClickListener {
            val newEmail = binding.etEmail.text.toString()
            val newPhone = binding.etPhone.text.toString()

            update(id!!, newEmail, newPhone)

            binding.etEmail.visibility = View.INVISIBLE
            binding.etPhone.visibility = View.INVISIBLE
            binding.tvEmployeeMail.visibility = View.VISIBLE
            binding.tvEmployeePhone.visibility = View.VISIBLE

            binding.bUpdate.visibility = View.VISIBLE
            binding.bSubmit.visibility = View.INVISIBLE
        }

        binding.bLogout.setOnClickListener {
            startActivity(Intent(this, Login::class.java))

            Toast.makeText(applicationContext, "로그아웃", Toast.LENGTH_LONG).show()

            finish()
        }

        binding.etEmail.visibility = View.INVISIBLE
        binding.etPhone.visibility = View.INVISIBLE
        binding.bSubmit.visibility = View.INVISIBLE


        getInfo(id!!)
    }

    fun refresh() {
        runOnUiThread {
            binding.tvEmployeeName.text = info[0]
            binding.tvEmployeeMail.text = info[1]
            binding.tvEmployeePhone.text = info[2]
            binding.tvEmployeeHiredate.text = info[3]
            binding.tvEmployeeTitle.text = info[4]
            binding.etEmail.setText(info[1])
            binding.etPhone.setText(info[2])
        }
    }

    fun getInfo(id: String) {
        val url = "/myInfo"
        val data = JSONObject()
        data.accumulate("user_id", id)

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
                val arr = buffer.split("^")
                for (i: String in arr) {
                    Log.d(TAG, "record: $i")
                    info.add(i)
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
                        Log.e(TAG, "정보 조회 에러")
                    }
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
        }
    }

    fun update(id: String, email: String = binding.tvEmployeeMail.text.toString(),
               phone: String = binding.tvEmployeePhone.text.toString()) {
        val url = "/update"
        val data = JSONObject()
        data.accumulate("user_id", id)
        data.accumulate("email", email)
        data.accumulate("phone", phone)

        var status = false

        GlobalScope.launch {
            var con: HttpURLConnection? = null
            var reader: BufferedReader? = null
            try {
                val url = URL(serverAddr + url)
                con = url.openConnection() as HttpURLConnection?
                con!!.requestMethod = "GET"
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
                        Log.e(TAG, "정보 변경 에러")
                    }
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
        }
    }
}
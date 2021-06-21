package com.example.otmanagement

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.example.otmanagement.databinding.ActivityLoginBinding
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.json.JSONObject
import java.io.*
import java.net.HttpURLConnection
import java.net.MalformedURLException
import java.net.URL

const val serverAddr = "http://42be392063f2.ngrok.io"

class Login : AppCompatActivity() {
    private val TAG = "Login"
    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login)

        binding.bLogin.setOnClickListener {
            val id = binding.tietId.text.toString()
            val password = binding.tietPassword.text.toString()

            login(id, password)
//            startActivity(Intent(this, MainActivity::class.java))

            val sharedPref = getSharedPreferences(
                getString(R.string.preference_file_key), Context.MODE_PRIVATE)
            val editor = sharedPref.edit()
            editor.putString(getString(R.string.user_id), id)
            editor.commit()
        }
    }

    fun login(id: String, pw: String) {
        val url = "/login"
        val data = JSONObject()
        data.accumulate("user_id", id)
        data.accumulate("user_pw", pw)

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

                Log.d(TAG, "Login Response: $buffer")
                if (buffer.toString() == "1") {
                    status = true
                }

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
                        runOnUiThread {

                            Toast.makeText(applicationContext, "로그인 성공", Toast.LENGTH_SHORT).show()
                        }

                        val intent = Intent(applicationContext, MainActivity::class.java)
                        startActivity(intent)
                    } else {
//                        val dialog = LoginFragment()
//                        dialog.show(supportFragmentManager, "login error")
                        runOnUiThread {
                            Toast.makeText(applicationContext, "로그인 실패", Toast.LENGTH_SHORT).show()
                        }
                        Log.e(TAG, "로그인 에러")
                    }
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
        }
    }
}
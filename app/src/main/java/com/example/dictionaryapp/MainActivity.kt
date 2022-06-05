package com.example.dictionaryapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.toolbox.HttpResponse
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.dictionaryapp.databinding.ActivityMainBinding
import com.example.dictionaryapp.databinding.SearchresulltBinding
import org.json.JSONArray
import java.lang.Exception

class MainActivity : AppCompatActivity() {
    private val KEY = "WORD DEFINITION"
    lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        val queue = Volley.newRequestQueue(this)
        binding.btnFind.setOnClickListener {
            val url = getUrl()
            val stringRequest = StringRequest(Request.Method.GET,url,
                { response ->
                    try{
                        extractDefinitionFromJson(response)
                    }catch (exception: Exception){
                        exception.printStackTrace()
                    }
                },
                {
                    error ->
                    Toast.makeText(this,error.message,Toast.LENGTH_LONG).show()

                }
                )
            queue.add(stringRequest)
        }

    }
    private fun getUrl(): String{
        val word = binding.edtSerach.text
        val apikey = "adda4761-5519-4d1d-a554-bc647e0e6572"
        val url =
            "https://www.dictionaryapi.com/api/v3/references/learners/json/$word?key=$apikey"
        return url
    }
    private fun extractDefinitionFromJson(response: String) {
        val jsonArray = JSONArray(response)
        val firstIndex = jsonArray.getJSONObject(0)
        val getShortDefinition = firstIndex.getJSONArray("shortdef")
        val firstshortdefinition = getShortDefinition.get(0)

      val intent = Intent(this,SearchresulltBinding::class.java)
        intent.putExtra(KEY,firstshortdefinition.toString())
        startActivity(intent)
    }
}
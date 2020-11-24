package com.example.kotlin_homework4_2

import android.annotation.SuppressLint
import android.os.AsyncTask
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.util.Log
import android.widget.Button
import android.widget.SeekBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.*

class MainActivity : AppCompatActivity() {
    private var rabprogress=0
    private var torprogress=0
    val scope = CoroutineScope(Dispatchers.Default)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val btn_start = findViewById<Button>(R.id.btn_start)
        val seekBar = findViewById<SeekBar>(R.id.seekBar)
        val seekBar2 = findViewById<SeekBar>(R.id.seekBar2)
        btn_start.setOnClickListener {
            btn_start.isEnabled=false
            seekBar.progress=0
            seekBar2.progress=0
            rabprogress=0
            torprogress=0

            scope.launch{
                myCoroutineTask()
            }

            runThread()
        }
    }
    private fun runThread(){
        object:Thread(){
            override fun run(){
                while(rabprogress<=100 && torprogress<100){
                    try{
                        Thread.sleep(100)
                    }catch(e: InterruptedException){
                        e.printStackTrace()
                    }
                    rabprogress+=(Math.random()*3).toInt()
                    val msg= Message()
                    msg.what=1 //訊息代號
                    mHandler.sendMessage(msg) //傳送
                }
            }
        }.start()
    }
    private val mHandler = Handler(Handler.Callback { msg ->
        when (msg.what) {
            1 -> {
                val seekBar = findViewById<SeekBar>(R.id.seekBar)
                val btn_start = findViewById<Button>(R.id.btn_start)
                seekBar.progress = rabprogress
                if (rabprogress >= 100 && torprogress < 100) {
                    Toast.makeText(this, "兔子勝利", Toast.LENGTH_SHORT).show()
                    //啟動Button
                    btn_start.isEnabled = true
                }
            }
        }
        true
    })



    private suspend fun myCoroutineTask(){

        try{

            val seekBar2 = findViewById<SeekBar>(R.id.seekBar2)
            val btn_start = findViewById<Button>(R.id.btn_start)
            while (torprogress<=100 && rabprogress<100)
            {
                try{
                    delay(100)
                }catch(e:InterruptedException){
                    e.printStackTrace()
                }
                torprogress+= (Math.random()*3).toInt()
                // onProgressUpdate
                seekBar2.progress=torprogress
            }

            withContext(Dispatchers.Main) {
                if (rabprogress < 100 && torprogress >= 100) {
                    Toast.makeText(this@MainActivity, "烏龜勝利", Toast.LENGTH_SHORT).show()
                    btn_start.isEnabled=true
                }
            }
        }catch (e:Exception){   //如果發生錯誤則報錯
            Log.e(localClassName,"Cancel",e)
        }
    }
}
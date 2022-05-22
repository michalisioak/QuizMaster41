package com.michalisioak.quizmaster41

import android.content.Context
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Button
import android.widget.TextView
import androidx.core.content.ContextCompat
import org.json.JSONObject
import java.net.URL
import android.os.StrictMode
import android.os.StrictMode.ThreadPolicy
import android.text.Html
import androidx.core.text.HtmlCompat
import org.json.JSONArray


class MainActivity : AppCompatActivity() {



    private var correctAnswer = -1

    override fun onCreate(savedInstanceState: Bundle?) {


        val policy = ThreadPolicy.Builder().permitAll().build()
        StrictMode.setThreadPolicy(policy)

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        nextQuestion(this)

        findViewById<Button>(R.id.a).setOnClickListener {
            nextQuestion(this)
        }
        findViewById<Button>(R.id.b).setOnClickListener {
            nextQuestion(this)
        }
        findViewById<Button>(R.id.c).setOnClickListener {
            nextQuestion(this)
        }
        findViewById<Button>(R.id.d).setOnClickListener {
            nextQuestion(this)
        }
    }


    private fun nextQuestion(context: Context) {
        val a = findViewById<Button>(R.id.a)
        val b = findViewById<Button>(R.id.b)
        val c = findViewById<Button>(R.id.c)
        val d = findViewById<Button>(R.id.d)
        val buttons = listOf(a,b,c,d)
        if (correctAnswer != -1) {
            buttons.forEachIndexed { index, button ->
                if (index == correctAnswer) {
                    button.setBackgroundColor(Color.GREEN)
                } else {
                    button.setBackgroundColor(Color.RED)
                }
            }
        }
        Handler(Looper.getMainLooper()).postDelayed(
            {
                buttons.forEach { it.setBackgroundColor(ContextCompat.getColor(context, R.color.design_default_color_primary)) }
                val json = JSONObject( URL("https://opentdb.com/api.php?amount=1&difficulty=hard&type=multiple").readText()).getJSONArray("results")[0] as JSONObject
                findViewById<TextView>(R.id.question).text = Html.fromHtml(json.getString("question"), HtmlCompat.FROM_HTML_MODE_LEGACY)
                val answers = json.getJSONArray("incorrect_answers").toMutableList()
                correctAnswer = (0..3).random()
                answers.add(correctAnswer,json.getString("correct_answer"))
                val text = answers.map {
                    Html.fromHtml(it, HtmlCompat.FROM_HTML_MODE_LEGACY)
                }
                a.text = text[0]
                b.text = text[1]
                c.text = text[2]
                d.text = text[3]
            },

            if (correctAnswer == -1) 0 else 1000
        )
    }
}


fun JSONArray.toMutableList(): MutableList<String> = MutableList(length(),  this::getString)

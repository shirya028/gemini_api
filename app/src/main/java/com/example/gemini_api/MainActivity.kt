package com.example.gemini_api

import android.app.Dialog
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.ai.client.generativeai.GenerativeModel
import com.google.ai.client.generativeai.type.content
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import android.view.Window
import android.view.WindowManager

class MainActivity : AppCompatActivity() {
    lateinit var entry : EditText;
    lateinit var send : ImageButton;
    lateinit var pickImg : ImageButton;
    lateinit var list : MutableList<chatData>;
    lateinit var lv : RecyclerView;
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        entry=findViewById(R.id.edit)
        send=findViewById(R.id.btn)
        pickImg=findViewById(R.id.pickImage)

        list= mutableListOf()
        lv=findViewById(R.id.rv)


        val generativeModel = GenerativeModel(
            modelName = "gemini-pro",
            apiKey = "" // Add your API KEY here
        )

        send.setOnClickListener {
            val prompt = entry.text.toString()
            addIntoView(prompt, true,null)
            addIntoView("● ● ●",false,null)
            CoroutineScope(Dispatchers.Main).launch {
                val response = generativeModel.generateContent(prompt)
                val ans: String? = response.text
                list.removeAt(list.size-1)
                addIntoView(ans ?: "", false,null)
            }
            entry.setText("")
        }
        pickImg.setOnClickListener {
            var i1= Intent(Intent.ACTION_GET_CONTENT)
            i1.type="image/*"
            startActivityForResult(i1,0);
        }

        val myadapter=ListAdapter(list);
        lv.adapter=myadapter
        lv.layoutManager= LinearLayoutManager(this)
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data1: Intent?) {
        super.onActivityResult(requestCode, resultCode, data1)

        var uri=data1?.data;
        if(uri==null)
        {
            Toast.makeText(this,"please select an image",Toast.LENGTH_LONG).show();
        }
        else {
            openDialog(uri)
        }

    }
    fun addIntoView(text : String, bool : Boolean , uri: Uri?) {

        list.add(chatData(text,bool,uri))
        lv.adapter?.notifyDataSetChanged();
        lv.adapter?.itemCount?.let { itemCount ->
            if (itemCount > 0) {
                lv.smoothScrollToPosition(itemCount - 1)
            }
        }
    }

    fun openDialog(uri : Uri) {
        var d= Dialog(this)
        d.setContentView(R.layout.custom_dialog)


        var imgView : ImageView =d.findViewById<ImageView>(R.id.dialog_img)
        var close : ImageButton =d.findViewById<ImageButton>(R.id.close_dialog);
        var editTxt : EditText =d.findViewById<EditText>(R.id.dialog_editTxt);
        var send : ImageButton =d.findViewById<ImageButton>(R.id.dialog_send);
        imgView.setImageURI(uri)
        close.setOnClickListener {
                d.dismiss()
        }
        send.setOnClickListener {
            var prompt : String = editTxt.text.toString();
            addIntoView(prompt,true,uri)
            d.dismiss()
            val generativeModel = GenerativeModel(
                modelName = "gemini-pro-vision",
                apiKey = "" // Add your API KEY here
            )
            val drawable: Drawable? = imgView.drawable
            val bitmap: Bitmap? = (drawable as? BitmapDrawable)?.bitmap
            if(bitmap!=null) {
                val image1: Bitmap = bitmap

                val inputContent = content {
                    image(image1)
                    text(prompt)
                }

                CoroutineScope(Dispatchers.Main).launch {
                    val response = generativeModel.generateContent(inputContent)
                    val ans: String? = response.text
                    addIntoView(ans ?: "", false,null)
                }
            }

        }

        d.show()

    }


}
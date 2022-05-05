package codes.drinky.testapp

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import codes.drinky.testapp.client.ImgurClient
import codes.drinky.testapp.model.Upload
import kotlinx.coroutines.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList
import androidx.core.content.ContextCompat.getExternalFilesDirs
import com.google.android.material.floatingactionbutton.FloatingActionButton

class UploadAdapter(private val context: Context, private val items: ArrayList<Upload>) :
    RecyclerView.Adapter<UploadAdapter.ViewHolder>() {

    private val defaultBrowser: Intent = Intent.makeMainSelectorActivity(Intent.ACTION_MAIN, Intent.CATEGORY_APP_BROWSER)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        //tekitame ilusa graafilisise listi piltidest ja nende informatsioonist
        return ViewHolder(
            LayoutInflater.from(context).inflate(
                R.layout.item_upload_layout, parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val item = items[position]
        holder.date.text = epochToDate(item.uploadDate)
        holder.url.text = item.url

        CoroutineScope(Dispatchers.IO).launch {
            val image: Bitmap? = ImgurClient().fetchImage(item.url)
            withContext(Dispatchers.Main) {
                holder.image.setImageBitmap(image)
            }
        }
    }

    override fun getItemCount(): Int {
        //p2rime mitu pilti on yles laetud
        return items.size
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        //defineerime nupud ja informatsiooni yhele pildi objektile
        val date: TextView = view.findViewById(R.id.tv_upload_date)
        val url: TextView = view.findViewById(R.id.tv_url)
        val image: ImageView = view.findViewById(R.id.imageView)
        private val copy: ImageView = view.findViewById(R.id.copy)
        private val share: ImageView = view.findViewById(R.id.share)

        init {
            view.setOnLongClickListener {
                val position: Int = adapterPosition
                (context as MainActivity).remove(items[position].url)
                Toast.makeText(context, "Upload deleted", Toast.LENGTH_SHORT).show()
                true
            }

            copy.setOnClickListener {
                val position: Int = adapterPosition
                (context as MainActivity).copyToClipboard(items[position].url)
            }

            image.setOnClickListener {
                val position: Int = adapterPosition
                defaultBrowser.data = Uri.parse(items[position].url)
                (context as MainActivity).startActivity(defaultBrowser)
            }
            //val shareIntent: Intent = Intent().apply {
            //    action = Intent.ACTION_SEND
            //    putExtra(Intent.EXTRA_TEXT, "This is my text to send.")
            //    type = "text/plain" --sellega tootab
            //}



            share.setOnClickListener {
                val position: Int = adapterPosition
                val shareIntent: Intent = Intent().apply {
                    action = Intent.ACTION_SEND
                    putExtra(Intent.EXTRA_STREAM, Uri.parse(items[position].url))
                    type = "image/jpeg"

                }
                shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                shareIntent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION)
                shareIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                (context as MainActivity).startActivity(Intent.createChooser(shareIntent, "SHARE WITH"))
            }
        }
    }

    private fun epochToDate(epoch: Long): String {
        val sdf = SimpleDateFormat("dd/MM/yyyy 'at' HH:mm")
        return sdf.format(Date(epoch))
    }
}
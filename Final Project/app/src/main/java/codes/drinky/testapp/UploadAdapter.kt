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

class UploadAdapter(private val context: Context, private val items: ArrayList<Upload>) :
    RecyclerView.Adapter<UploadAdapter.ViewHolder>() {

    private val defaultBrowser: Intent = Intent.makeMainSelectorActivity(Intent.ACTION_MAIN, Intent.CATEGORY_APP_BROWSER)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
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
        return items.size
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val date: TextView = view.findViewById(R.id.tv_upload_date)
        val url: TextView = view.findViewById(R.id.tv_url)
        val image: ImageView = view.findViewById(R.id.imageView)
        private val copy: ImageView = view.findViewById(R.id.copy)

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
        }
    }

    private fun epochToDate(epoch: Long): String {
        val sdf = SimpleDateFormat("dd/MM/yyyy 'at' HH:mm")
        return sdf.format(Date(epoch))
    }
}